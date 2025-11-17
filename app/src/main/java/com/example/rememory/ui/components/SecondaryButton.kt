package com.example.rememory.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rememory.ui.theme.BackgroundLight
import com.example.rememory.ui.theme.MontserratFontFamily
import com.example.rememory.ui.theme.PurplePrimary
import com.example.rememory.ui.theme.ReMemoryTheme

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(28.dp)
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        border = BorderStroke(1.dp, PurplePrimary),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = BackgroundLight,
            contentColor = PurplePrimary
        ),
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontFamily = MontserratFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp
            )
        )
    }
}


@Preview
@Composable
fun SecondaryButtonPreview() {
    ReMemoryTheme {
        SecondaryButton(
            text = "outlined",
            onClick = {},
            modifier = Modifier
                .width(216.dp)
                .height(58.dp)
        )
    }
}