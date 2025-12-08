package com.example.rememory.ui.screens.capsuleCreate.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rememory.R
import com.example.rememory.domain.model.ConditionType

@Composable
fun Step3SelectConditions(
    selectedConditions: List<ConditionType>,
    onToggle: (ConditionType) -> Unit
) {
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            ConditionCard(
                iconResId = R.drawable.ic_map_pin_heart_black,
                title = "Geo-Lock (By Location)",
                subtitle = "Unlock at a place",
                isSelected = selectedConditions.contains(ConditionType.LOCATION),
                onClick = { onToggle(ConditionType.LOCATION) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ConditionCard(
                iconResId = R.drawable.ic_clear_day_black,
                title = "Event-Lock (By Weather)",
                subtitle = "Unlock on a weather event",
                isSelected = selectedConditions.contains(ConditionType.WEATHER),
                onClick = { onToggle(ConditionType.WEATHER) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ConditionCard(
                iconResId = R.drawable.ic_action_black,
                title = "Action-Lock (By User Action)",
                subtitle = "Unlock after an action",
                isSelected = selectedConditions.contains(ConditionType.ACTION),
                onClick = { onToggle(ConditionType.ACTION) }
            )
        }
    }
}

