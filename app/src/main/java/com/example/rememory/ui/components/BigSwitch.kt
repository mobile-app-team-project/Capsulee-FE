package com.example.rememory.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rememory.ui.theme.PurplePrimary

@Composable
fun BigSwitch(
    leftText: String,
    rightText: String,
    isLeftSelected: Boolean,
    onToggle: (Boolean) -> Unit
) {
    BoxWithConstraints (
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(Color.White),
        contentAlignment = Alignment.CenterStart
    ) {
        //선택된 박스 이동 로직
        val totalWidth = maxWidth
        val horizontalPadding = 4.dp
        val slotWidth = (totalWidth - horizontalPadding * 2) / 2     // 각 슬롯 너비: (총너비 - 좌우 패딩*2) / 2
        val targetOffset = if (isLeftSelected) horizontalPadding else horizontalPadding + slotWidth // 목표 offset
        val thumbOffset by animateDpAsState(targetValue = targetOffset)

        // 이동하는 선택 배경(thumb)
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .width(slotWidth)
                .height(50.dp - horizontalPadding * 2) // 내부 여백을 주려면 조정
                .align(Alignment.CenterStart)
                .clip(RoundedCornerShape(11.dp))
                .background(PurplePrimary)
        )

        //텍스트 박스
        // 각 슬롯 (텍스트 포함)
        Row(
            modifier = Modifier
                .matchParentSize()
                .padding(horizontal = horizontalPadding),
        ) {
            val leftInteractionSource = remember { MutableInteractionSource() }
            // 왼쪽 슬롯
            Box(
                modifier = Modifier
                    .width(slotWidth)
                    .fillMaxHeight()
                    .clickable(
                        interactionSource = leftInteractionSource,
                        indication = null // 눌림/포커스 효과 제거
                    ) { if (!isLeftSelected) onToggle(true) },
                contentAlignment = Alignment.Center // ✅ 중앙 정렬
            ) {
                Text(
                    text = leftText,
                    color = if (isLeftSelected) Color.White else PurplePrimary,
                    fontWeight = if (isLeftSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }

            // 오른쪽 슬롯
            val rightInteractionSource = remember { MutableInteractionSource() }
            Box(
                modifier = Modifier
                    .width(slotWidth)
                    .fillMaxHeight()
                    .clickable(
                        interactionSource = rightInteractionSource,
                        indication = null // 눌림/포커스 효과 제거
                    ) { if (isLeftSelected) onToggle(false) },
                contentAlignment = Alignment.Center // ✅ 중앙 정렬
            ) {
                Text(
                    text = rightText,
                    color = if (!isLeftSelected) Color.White else PurplePrimary,
                    fontWeight = if (!isLeftSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BigSwitchPreview(){
    var isLeftSelected by remember { mutableStateOf(true) }
    Box(modifier = Modifier.padding(18.dp))
    {
        BigSwitch(
            "list",
            "request",
            isLeftSelected,
            {
                newValue -> isLeftSelected = newValue
            }
        )
    }
}