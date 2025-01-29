package com.android.tiltcamera.camera.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> DropDownMenuWithLabel(
    modifier: Modifier = Modifier,
    label: String,
    tooltipText: String? = null,
    enabled: Boolean = true,
    items: List<T>,
    selectedIndex: Int = -1,
    emptyPlaceholder: String = "",
    onItemSelected: (index: Int, item: T) -> Unit,
    selectedItemToString: (T) -> String = { it.toString() },
    notSetLabel: String? = null,
) {

    Row(modifier = modifier
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // LABEL
        LabelWithTooltip(
            modifier = Modifier.weight(1f),
            label = label,
            tooltipText
        )

        Spacer(modifier = Modifier.width(32.dp))

        LargeDropdownMenu(
            modifier = Modifier,
            enabled = enabled,
            items = items,
            emptyPlaceholder = emptyPlaceholder,
            notSetLabel = notSetLabel,
            selectedIndex = selectedIndex,
            onItemSelected = { index, item ->
                onItemSelected(index, item)
            },
            selectedItemToString = selectedItemToString
        )


    }
}