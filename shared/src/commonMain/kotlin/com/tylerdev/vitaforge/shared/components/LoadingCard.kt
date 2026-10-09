package com.tylerdev.vitaforge.shared.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.IconSecondary
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.loading_card_description

private val IndicatorSize = 40.dp

@Composable
fun LoadingCard(modifier: Modifier = Modifier) {
    val description = stringResource(Res.string.loading_card_description)
    Box(
        modifier = modifier.fillMaxSize().semantics { contentDescription = description },
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(IndicatorSize),
            color = IconSecondary,
        )
    }
}
