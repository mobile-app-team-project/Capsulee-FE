package com.example.rememory.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rememory.ui.theme.BlackText
import com.example.rememory.ui.theme.GrayBorder
import com.example.rememory.ui.theme.MontserratFontFamily
import com.example.rememory.ui.theme.ReMemoryTheme

@Composable
fun GrayButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(10.dp),
    textStyle: TextStyle = TextStyle(
        fontFamily = MontserratFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = GrayBorder,
            contentColor = BlackText
        ),
    ) {
        Text(
            text = text,
            style = textStyle
        )
    }
}


@Preview
@Composable
fun GrayButtonPreview() {
    ReMemoryTheme {
        GrayButton(
            text = "Button_gray",
            onClick = {},
            modifier = Modifier
                .width(157.dp)
                .height(45.dp)
        )
    }
}