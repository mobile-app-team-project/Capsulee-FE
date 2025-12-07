package com.example.rememory.ui.screens.capsuleCreate.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rememory.R
import com.example.rememory.ui.theme.GrayBorder
import com.example.rememory.ui.theme.GrayText
import com.example.rememory.ui.theme.PurpleLight
import com.example.rememory.ui.theme.PurplePrimary

@Composable
fun ConditionCard(
    iconResId: Int,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) PurpleLight else Color.White
    val borderColor = if (isSelected) PurplePrimary else GrayBorder
    val checkIconTint = if (isSelected) PurplePrimary else GrayBorder

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // 아이콘
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = title,
                modifier = Modifier.size(24.dp),
                colorFilter = if (isSelected) null else ColorFilter.tint(Color.Black)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 텍스트 (제목 + 설명)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = GrayText
                )
            }

            // 체크 아이콘
            Image(
                painter = painterResource(id = R.drawable.ic_check_circle),
                contentDescription = "Selected",
                modifier = Modifier.size(22.dp),
                colorFilter = ColorFilter.tint(checkIconTint)
            )
        }
    }
}