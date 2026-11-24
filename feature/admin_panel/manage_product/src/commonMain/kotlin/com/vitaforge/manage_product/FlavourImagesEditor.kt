package com.vitaforge.manage_product

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.IconPrimary
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.SurfaceLighter
import com.tylerdev.vitaforge.shared.TextMuted
import com.tylerdev.vitaforge.shared.TextPrimary
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.manage_product_flavour_image_add
import vitaforge.shared.generated.resources.manage_product_flavour_image_description
import vitaforge.shared.generated.resources.manage_product_flavour_image_remove
import vitaforge.shared.generated.resources.manage_product_section_flavour_images

private val SlotSize = 56.dp
private val SlotShape = RoundedCornerShape(8.dp)
private val SlotIconSize = 20.dp
private val RemoveIconSize = 18.dp
private val RowSpacing = 12.dp

@Composable
internal fun FlavourImagesEditor(
    flavours: List<String>,
    images: Map<String, String>,
    uploadingFlavour: String?,
    onPickImage: (String) -> Unit,
    onRemoveImage: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(RowSpacing)) {
        FormSectionLabel(stringResource(Res.string.manage_product_section_flavour_images))
        flavours.forEach { flavour ->
            FlavourImageRow(
                flavour = flavour,
                imageUrl = images[flavour],
                isUploading = uploadingFlavour == flavour,
                onPickImage = { onPickImage(flavour) },
                onRemoveImage = { onRemoveImage(flavour) },
            )
        }
    }
}

@Composable
private fun FlavourImageRow(
    flavour: String,
    imageUrl: String?,
    isUploading: Boolean,
    onPickImage: () -> Unit,
    onRemoveImage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = SlotSize)
                .clickable(enabled = !isUploading, onClick = onPickImage),
        horizontalArrangement = Arrangement.spacedBy(RowSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FlavourImageSlot(flavour = flavour, imageUrl = imageUrl, isUploading = isUploading)
        Column(modifier = Modifier.weight(1f)) {
            Text(text = flavour, fontSize = FontSize.REGULAR, color = TextPrimary)
            if (imageUrl == null && !isUploading) {
                Text(
                    text = stringResource(Res.string.manage_product_flavour_image_add),
                    fontSize = FontSize.SMALL,
                    color = TextMuted,
                )
            }
        }
        if (imageUrl != null && !isUploading) {
            IconButton(onClick = onRemoveImage) {
                Icon(
                    modifier = Modifier.size(RemoveIconSize),
                    painter = painterResource(Resources.Icon.TrashCan),
                    contentDescription = stringResource(Res.string.manage_product_flavour_image_remove, flavour),
                    tint = IconPrimary,
                )
            }
        }
    }
}

@Composable
private fun FlavourImageSlot(
    flavour: String,
    imageUrl: String?,
    isUploading: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.size(SlotSize).clip(SlotShape).background(SurfaceLighter),
        contentAlignment = Alignment.Center,
    ) {
        when {
            isUploading -> CircularProgressIndicator(modifier = Modifier.size(SlotIconSize), strokeWidth = 2.dp)
            imageUrl != null ->
                AsyncImage(
                    modifier = Modifier.size(SlotSize),
                    model = ImageRequest.Builder(LocalPlatformContext.current).data(imageUrl).crossfade(true).build(),
                    contentDescription = stringResource(Res.string.manage_product_flavour_image_description, flavour),
                    contentScale = ContentScale.Fit,
                )
            else ->
                Icon(
                    modifier = Modifier.size(SlotIconSize),
                    painter = painterResource(Resources.Icon.Plus),
                    contentDescription = null,
                    tint = IconPrimary,
                )
        }
    }
}
