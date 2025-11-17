package com.example.rememory.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rememory.R
import com.example.rememory.ui.theme.BackgroundLight
import com.example.rememory.ui.theme.IndieFlowerFontFamily
import com.example.rememory.ui.theme.MontserratFontFamily
import com.example.rememory.ui.theme.ReMemoryTheme

@Composable
fun AppHeader(
    modifier: Modifier = Modifier,
    title: String,
    titleStyle: TextStyle,
    onBackClick: (() -> Unit)? = null,
    onPlusClick: (() -> Unit)? = null,
    onBellClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(BackgroundLight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBackClick != null) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "뒤로가기",
                    modifier = Modifier
                        .padding(start = 14.dp)
                        .size(22.dp)
                        .clickable { onBackClick() }
                )
                Spacer(Modifier.width(3.dp))
            }

            Text(
                text = title,
                style = titleStyle,
                modifier = Modifier
                    .padding(start = if (onBackClick == null) 18.dp else 0.dp)
            )
        }

        Spacer(Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onPlusClick != null) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_plus),
                    contentDescription = "추가하기",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { onPlusClick() }
                )
                Spacer(Modifier.width(3.dp))
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_bell),
                contentDescription = "알림",
                modifier = Modifier
                    .padding(end = 14.dp)
                    .size(30.dp)
                    .clickable { onBellClick() }
            )
        }
    }
}

val TitleLogoStyle = TextStyle(
    fontSize = 24.sp,
    fontFamily = IndieFlowerFontFamily,
    fontWeight = FontWeight(400),
    color = Color(0xFF000000)
)

val TitlePageStyle = TextStyle(
    fontSize = 16.sp,
    fontFamily = MontserratFontFamily,
    fontWeight = FontWeight(500),
    color = Color(0xFF202020)
)


@Preview
@Composable
fun AppHeaderPreview_Type1() {
    ReMemoryTheme {
        AppHeader(
            title = "Re:Memory",
            titleStyle = TitleLogoStyle,
            onBackClick = null,
            onPlusClick = {},
            onBellClick = {}
        )
    }
}

@Preview
@Composable
fun AppHeaderPreview_Type2() {
    ReMemoryTheme {
        AppHeader(
            title = "Re:Memory",
            titleStyle = TitleLogoStyle,
            onBackClick = null,
            onPlusClick = null,
            onBellClick = {}
        )
    }
}

@Preview
@Composable
fun AppHeaderPreview_Type3() {
    ReMemoryTheme {
        AppHeader(
            title = "Capsule Details",
            titleStyle = TitlePageStyle,
            onBackClick = {},
            onPlusClick = {},
            onBellClick = {}
        )
    }
}