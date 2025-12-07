package com.example.rememory.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rememory.R
import com.example.rememory.ui.theme.BackgroundLight
import com.example.rememory.ui.theme.GrayText
import com.example.rememory.ui.theme.MontserratFontFamily
import com.example.rememory.ui.theme.ReMemoryTheme

@Composable
fun SimpleHeader(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(BackgroundLight),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_back),
            contentDescription = "뒤로가기",
            tint = GrayText,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 18.dp)
                .size(22.dp)
                .clickable { onBackClick() }
        )

        Text(
            text = title,
            fontSize = 22.sp,
            fontFamily = MontserratFontFamily,
            fontWeight = FontWeight.SemiBold,
            color = GrayText,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun SimpleHeaderPreview() {
    ReMemoryTheme {
        SimpleHeader(
            title = "SIGN UP",
            onBackClick = {}
        )
    }
}