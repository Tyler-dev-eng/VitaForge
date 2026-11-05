package com.vitaforge.manage_product

import kotlin.test.Test
import kotlin.test.assertEquals

class BulletFormatterTest {
    @Test
    fun formatBullets_enterOnBulletLine_continuesBullet() {
        val actualText = formatBullets(previous = "• One", updated = "• One\n")

        assertEquals("• One\n• ", actualText)
    }

    @Test
    fun formatBullets_enterOnEmptyBullet_endsList() {
        val actualText = formatBullets(previous = "• One\n• ", updated = "• One\n• \n")

        assertEquals("• One\n", actualText)
    }

    @Test
    fun formatBullets_dashAtLineStart_becomesBullet() {
        val actualText = formatBullets(previous = "-", updated = "- ")

        assertEquals("• ", actualText)
    }

    @Test
    fun formatBullets_dashMidLine_isUnchanged() {
        val actualText = formatBullets(previous = "a -", updated = "a - ")

        assertEquals("a - ", actualText)
    }

    @Test
    fun appendBullet_afterText_startsNewBulletLine() {
        val actualText = appendBullet("Intro")

        assertEquals("Intro\n• ", actualText)
    }
}
