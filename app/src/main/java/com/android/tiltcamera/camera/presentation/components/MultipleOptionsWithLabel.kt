package com.android.tiltcamera.camera.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MultipleOptionsWithLabel(
    modifier: Modifier = Modifier,
    label: String,
    options: List<OptionItem>,
    selectedOption: OptionItem?,
    onOptionSelected: (OptionItem) -> Unit,
    tooltipText: String? = null,
) {
    Row(modifier = modifier
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // ROW LABEL
        LabelWithTooltip(
            modifier = Modifier.weight(1f),
            label = label,
            tooltipText
        )

        // Icons
        Row(modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
        ) {
            options.forEach { option ->
                OptionItems(
                    optionItem = option,
                    isSelected = selectedOption?.id == option.id,
                    onClick = { item ->
                        onOptionSelected(item)
                    }
                )
            }
        }
    }
}

data class OptionItem(
    val id: Long,
    val icon: Int,
    val data: Any,
)

