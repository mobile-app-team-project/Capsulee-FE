package com.example.rememory.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.rememory.ui.theme.PurplePrimary

@Composable
fun ProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    activeColor: Color = PurplePrimary,
    trackColor: Color = Color(0xFFE5E7EB),
    height: Dp = 10.dp
) {
    val animatedProgress by animateFloatAsState(targetValue = progress)

    Box(modifier = modifier.fillMaxWidth()) {
        // Track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(height / 2))
                .background(trackColor)
        )

        // Active Indicator
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = animatedProgress)
                .height(height)
                .clip(RoundedCornerShape(height / 2))
                .background(activeColor)
        )
    }
}
@Preview(showBackground = true)
@Composable
fun ProgressBarPreview(){
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ){
        Text("Progress 70%")
        Spacer(modifier = Modifier.padding(16.dp))
        ProgressBar(progress = 0.7f)
    }
}