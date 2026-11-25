const {setGlobalOptions} = require("firebase-functions");
const {onDocumentCreated} = require("firebase-functions/v2/firestore");
const {defineSecret, defineString} = require("firebase-functions/params");
const logger = require("firebase-functions/logger");
const {initializeApp} = require("firebase-admin/app");
const {getFirestore} = require("firebase-admin/firestore");

initializeApp();

// For cost control, cap the number of containers running at the same time.
setGlobalOptions({maxInstances: 10});

// Ignores floating-point noise when deriving tax from total minus subtotal.
const TAX_EPSILON = 0.005;
const RESEND_API_URL = "https://api.resend.com/emails";
const resendApiKey = defineSecret("RESEND_API_KEY");
// Must be an address on a domain verified in Resend.
// "onboarding@resend.dev" only delivers to your own Resend account email.
const fromEmail = defineString("FROM_EMAIL", {
  default: "VitaForge <onboarding@resend.dev>",
});

const escapeHtml = (value) =>
  String(value ?? "")
      .replace(/&/g, "&amp;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;")
      .replace(/"/g, "&quot;");

const formatPrice = (amount) => `R${Number(amount).toFixed(2)}`;

// " (Vanilla)", " (Black)" or "" depending on what the customer picked.
const formatOptions = ({flavour, colour}) => {
  const options = [flavour, colour].filter(Boolean).map(escapeHtml);
  return options.length ? ` (${options.join(", ")})` : "";
};

async function loadLineItems(db, items) {
  return Promise.all(items.map(async (item) => {
    const snapshot = await db.collection("product").doc(item.productId).get();
    const product = snapshot.data() ?? {};
    return {
      title: product.title ?? "Unknown product",
      flavour: item.flavour ?? null,
      colour: item.colour ?? null,
      quantity: item.quantity,
      price: product.price ?? 0,
    };
  }));
}

function buildEmailHtml({firstName, orderId, lines, totalAmount}) {
  const rows = lines.map((line) => `
    <tr>
      <td>${escapeHtml(line.title)}${formatOptions(line)}</td>
      <td align="center">${line.quantity}</td>
      <td align="right">${formatPrice(line.price * line.quantity)}</td>
    </tr>`).join("");

  const subtotal = lines.reduce((sum, line) => sum + line.price * line.quantity, 0);
  const tax = totalAmount - subtotal;
  const summary = tax > TAX_EPSILON ?
    `<p style="text-align: right;">Subtotal: ${formatPrice(subtotal)}<br>Tax: ${formatPrice(tax)}</p>` : "";

  return `
    <div style="font-family: sans-serif; max-width: 480px; margin: 0 auto;">
      <h2>Thanks for your order, ${escapeHtml(firstName)}!</h2>
      <p>Order <strong>#${escapeHtml(orderId)}</strong> has been received.</p>
      <table width="100%" cellpadding="6" cellspacing="0">
        <thead><tr><th align="left">Item</th><th>Qty</th><th align="right">Price</th></tr></thead>
        <tbody>${rows}</tbody>
      </table>
      ${summary}
      <p style="text-align: right;"><strong>Total: ${formatPrice(totalAmount)}</strong></p>
    </div>`;
}

async function sendEmail({to, subject, html, orderId}) {
  const response = await fetch(RESEND_API_URL, {
    method: "POST",
    headers: {
      "Authorization": `Bearer ${resendApiKey.value()}`,
      "Content-Type": "application/json",
      // Prevents duplicate emails if the trigger is delivered more than once.
      "Idempotency-Key": `order-confirmation-${orderId}`,
    },
    body: JSON.stringify({from: fromEmail.value(), to: [to], subject, html}),
  });

  if (!response.ok) {
    throw new Error(`Resend responded ${response.status}: ${await response.text()}`);
  }
  return response.json();
}

exports.sendOrderConfirmationEmail = onDocumentCreated(
    {document: "order/{orderId}", secrets: [resendApiKey]},
    async (event) => {
      const order = event.data?.data();
      if (!order) {
        logger.warn("Order snapshot was empty", {orderId: event.params.orderId});
        return;
      }

      const db = getFirestore();
      const customerSnapshot = await db.collection("customer").doc(order.customerId).get();
      const customer = customerSnapshot.data();
      if (!customer?.email) {
        logger.error("Customer has no email, skipping", {orderId: order.id, customerId: order.customerId});
        return;
      }

      const lines = await loadLineItems(db, order.items ?? []);
      const result = await sendEmail({
        to: customer.email,
        subject: `Your VitaForge order #${order.id} is confirmed`,
        html: buildEmailHtml({
          firstName: customer.firstName,
          orderId: order.id,
          lines,
          totalAmount: order.totalAmount,
        }),
        orderId: order.id,
      });
      logger.info("Order confirmation sent", {orderId: order.id, emailId: result.id});
    },
);
