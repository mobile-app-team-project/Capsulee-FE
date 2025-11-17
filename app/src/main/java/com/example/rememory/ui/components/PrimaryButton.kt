package com.example.rememory.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rememory.ui.theme.MontserratFontFamily
import com.example.rememory.ui.theme.PurplePrimary
import com.example.rememory.ui.theme.ReMemoryTheme

@Composable
fun PrimaryButton(
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
            containerColor = PurplePrimary,
            contentColor = Color.White
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
fun ButtonDefaultPreview() {
    ReMemoryTheme {
        PrimaryButton(
            text = "Button_Default",
            onClick = {},
            modifier = Modifier
                .width(324.dp)
                .height(45.dp),
        )
    }
}

@Preview
@Composable
fun Button54Preview() {
    ReMemoryTheme {
        PrimaryButton(
            text = "Button_54",
            onClick = {},
            modifier = Modifier
                .width(314.dp)
                .height(54.dp),
            shape = RoundedCornerShape(15.dp),
            textStyle = TextStyle(
                fontFamily = MontserratFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp
            )
        )
    }
}

@Preview
@Composable
fun PrimarySealButtonPreview() {
    ReMemoryTheme {
        PrimaryButton(
            text = "Rounded_58",
            onClick = {},
            modifier = Modifier
                .width(216.dp)
                .height(58.dp),
            shape = RoundedCornerShape(28),
            textStyle = TextStyle(
                fontFamily = MontserratFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp
            )
        )
    }
}