package com.vitaforge.manage_product

internal const val BULLET_PREFIX = "• "
private const val MARKDOWN_PREFIX = "- "
private const val NEW_LINE = "\n"

/**
 * Applies bullet-list behaviour to a text edit:
 * - typing "- " at the start of a line becomes a bullet
 * - pressing enter on a bullet line starts a new bullet
 * - pressing enter on an empty bullet removes it (ends the list)
 */
internal fun formatBullets(
    previous: String,
    updated: String,
): String =
    when {
        updated.length > previous.length && updated.endsWith(NEW_LINE) -> continueBullet(updated)
        updated.length > previous.length && updated.endsWith(MARKDOWN_PREFIX) -> convertMarkdownBullet(updated)
        else -> updated
    }

/** Appends a bullet on a new line (or on the current line when it is empty). */
internal fun appendBullet(text: String): String =
    when {
        text.isEmpty() || text.endsWith(NEW_LINE) -> text + BULLET_PREFIX
        text.substringAfterLast(NEW_LINE) == BULLET_PREFIX -> text
        else -> text + NEW_LINE + BULLET_PREFIX
    }

private fun continueBullet(text: String): String {
    val body = text.dropLast(NEW_LINE.length)
    val lastLine = body.substringAfterLast(NEW_LINE)
    return when {
        lastLine == BULLET_PREFIX -> body.dropLast(BULLET_PREFIX.length)
        lastLine.startsWith(BULLET_PREFIX) -> text + BULLET_PREFIX
        else -> text
    }
}

private fun convertMarkdownBullet(text: String): String {
    val lineStart = text.length - MARKDOWN_PREFIX.length
    val isAtLineStart = lineStart == 0 || text[lineStart - 1] == NEW_LINE.single()
    return if (isAtLineStart) text.take(lineStart) + BULLET_PREFIX else text
}
