package com.example.rememory.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rememory.R
import com.example.rememory.ui.theme.BlackText
import com.example.rememory.ui.theme.GrayText
import com.example.rememory.ui.theme.MontserratFontFamily
import com.example.rememory.ui.theme.PurplePrimary
import com.example.rememory.ui.theme.ReMemoryTheme


@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier,
    label: String? = null,
    labelStyle: TextStyle = TextStyle(
        fontFamily = MontserratFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        color = BlackText
    ),
    counterText: String? = null,
    counterStyle: TextStyle = TextStyle(
        fontFamily = MontserratFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        color = GrayText
    ),
    placeholderText: String = "",
    placeholderStyle: TextStyle = TextStyle(
        fontFamily = MontserratFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        color = GrayText
    ),
    textStyle: TextStyle = placeholderStyle.copy(color = BlackText),
    leadingIcon: Int? = null,
    isSingleLine: Boolean = true,
    defaultBorderColor: Color = Color(0xFFE5E7EB),
    shape: Shape = RoundedCornerShape(10.dp)
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        if (label != null || counterText != null) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                if (label != null) {
                    Text(
                        text = label,
                        style = labelStyle,
                        modifier = Modifier.padding(start = 5.dp)
                    )
                }
                Spacer(Modifier.weight(1f))
                if (counterText != null) {
                    Text(
                        text = counterText,
                        style = counterStyle,
                        modifier = Modifier.padding(end = 5.dp)
                    )
                }
            }
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = isSingleLine,
            textStyle = textStyle,
            cursorBrush = SolidColor(PurplePrimary),
            modifier = Modifier.onFocusChanged { isFocused = it.isFocused },
            decorationBox = { innerTextField ->
                Row(
                    modifier = textFieldModifier
                        .fillMaxWidth()
                        .background(Color.White, shape)
                        .border(
                            width = 1.dp,
                            color = if (isFocused) PurplePrimary else defaultBorderColor,
                            shape = shape
                        )
                        .padding(top = if (!isSingleLine) 15.dp else 0.dp),
                    verticalAlignment = if (isSingleLine) Alignment.CenterVertically else Alignment.Top
                ) {
                    if (leadingIcon != null) {
                        Icon(
                            painter = painterResource(id = leadingIcon),
                            contentDescription = "Icon",
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .size(22.dp),
                            tint = GrayText
                        )
                        Spacer(Modifier.width(5.dp))
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = if (leadingIcon == null) 16.dp else 0.dp)
                            .padding(end = 16.dp)
                            .padding(vertical = if (isSingleLine) 12.dp else 0.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isEmpty()) {
                            Text(text = placeholderText, style = placeholderStyle)
                        }
                        innerTextField()
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppTextFieldPreview1() {
    ReMemoryTheme {
        AppTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.width(325.dp),
            textFieldModifier = Modifier.height(45.dp),
            label = "Capsule Title",
            placeholderText = "ex. Our Graduation Day"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppTextFieldPreview2() {
    ReMemoryTheme {
        AppTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.width(325.dp),
            textFieldModifier = Modifier.height(155.dp),
            label = "Your Message",
            counterText = "0/500",
            placeholderText = "Remember this moment...",
            isSingleLine = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppTextFieldPreview3() {
    ReMemoryTheme {
        AppTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.width(325.dp),
            textFieldModifier = Modifier.height(40.dp),
            label = null,
            placeholderText = "Search by a place or address",
            leadingIcon = R.drawable.ic_search
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppTextFieldPreview4() {
    ReMemoryTheme {
        AppTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.width(314.dp),
            textFieldModifier = Modifier.height(54.dp),
            shape = RoundedCornerShape(15.dp),
            label = "Username",
            placeholderText = "This will be shown to others",
            defaultBorderColor = PurplePrimary
        )
    }
}