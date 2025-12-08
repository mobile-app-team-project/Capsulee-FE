package com.example.rememory.ui.screens.capsuleCreate.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.rememory.R
import com.example.rememory.domain.model.ActionCondition
import com.example.rememory.ui.theme.PurplePrimary

@Composable
fun ActionScreen(
    selectedAction: ActionCondition?,
    onActionSelected: (ActionCondition) -> Unit
) {
    val actionOptions = listOf(
        ActionCondition.SHAKE,
        ActionCondition.SOUND,
        ActionCondition.TAP,
        ActionCondition.COMPASS
    )

    LazyVerticalGrid(
        columns =
            GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(actionOptions.size) { index ->
            val action = actionOptions[index]
            ActionCard(
                action = action,
                isSelected = action == selectedAction,
                onClick = { onActionSelected(action) }
            )
        }
    }
}

@Composable
fun ActionCard(
    action: ActionCondition,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) PurplePrimary.copy(alpha = 0.15f) else Color.White
    val borderColor = if (isSelected) PurplePrimary else Color.LightGray
    val icon = when (action) {
        ActionCondition.SHAKE -> R.drawable.ic_shake
        ActionCondition.SOUND -> R.drawable.ic_sound
        ActionCondition.TAP -> R.drawable.ic_tap
        ActionCondition.COMPASS -> R.drawable.ic_compass
    }
    val label = when (action) {
        ActionCondition.SHAKE -> "Shake 3 times"
        ActionCondition.SOUND -> "Sound"
        ActionCondition.TAP -> "Tap 3 times"
        ActionCondition.COMPASS -> "Point to Your\n“North”"
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        onClick = onClick,
        modifier = Modifier
            .size(153.dp, 180.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(painterResource(id = icon), contentDescription = label, tint = Color.Unspecified)
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = label,
                textAlign = TextAlign.Center,
            )
        }
    }
}