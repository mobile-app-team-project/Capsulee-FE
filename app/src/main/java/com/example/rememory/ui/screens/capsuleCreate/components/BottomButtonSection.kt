package com.example.rememory.ui.screens.capsuleCreate.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rememory.ui.components.GrayButton
import com.example.rememory.ui.components.PrimaryButton

@Composable
fun BottomButtons(
    showPrevious: Boolean = true,
    previousText: String = "PREVIOUS",
    nextText: String = "NEXT",
    onPrevious: (() -> Unit)? = null,
    onNext: () -> Unit,
    isSingleButton: Boolean = false,
    rightEnabled: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (!isSingleButton && showPrevious && onPrevious != null) {
            GrayButton(
                text = previousText,
                onClick = onPrevious,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
        }

        PrimaryButton(
            text = nextText,
            onClick = onNext,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            enabled = rightEnabled
        )
    }
}