package com.example.rememory.ui.screens.capsuleList.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rememory.domain.model.CapsuleStatsDomainModel
import com.example.rememory.ui.theme.PurpleExtraLight // 사용하시는 테마 색상으로 대체
import com.example.rememory.ui.theme.PurplePrimary // 사용하시는 테마 색상으로 대체

// 캡슐 통계 카드의 각 항목을 나타내는 컴포넌트
@Composable
fun StatsItem(
    value: String,
    label: String,
    valueColor: Color,
    labelColor: Color = Color.Gray,
    isHighlighted: Boolean = false // 첫 번째 'total' 항목에만 사용
) {
    // isHighlighted가 true일 경우 왼쪽 항목에만 배경색을 추가합니다.
    val backgroundModifier = if (isHighlighted) {
        Modifier.background(PurpleExtraLight, shape = RoundedCornerShape(12.dp))
    } else {
        Modifier.background(Color.White, shape = RoundedCornerShape(12.dp))
    }

    // 아이템 Column
    Column(
        modifier = backgroundModifier
            .height(70.dp)
            .width(77.dp), // Box의 width를 내용물에 맞춤
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = valueColor,
        )
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = labelColor,
        )
    }
}

// 캡슐 대시보드 전체 카드 컴포넌트
@Composable
fun CapsuleStatsCard(stats: CapsuleStatsDomainModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Capsule Dashboard",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 13.dp)
            )

            // 통계 항목이 들어갈 Row (높이를 IntrinsicSize로 설정하여 Column Height를 맞춥니다)
            Row(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min), // 높이를 가장 큰 자식에 맞춤
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Total (강조된 항목)
                StatsItem(
                    value = stats.total.toString(),
                    label = "total",
                    valueColor = PurplePrimary,
                    labelColor = Color.Gray,
                    isHighlighted = true
                )

                // 2. Can Open (구분선 + 항목)
                Divider(Modifier
                    .padding(horizontal = 4.dp)
                    .fillMaxHeight()
                )
                StatsItem(
                    value = stats.canOpen.toString(),
                    label = "Can Open",
                    valueColor = Color(0xFF007bff) // 임시 Blue
                )

                // 3. Locked (구분선 + 항목)
                Divider(Modifier
                    .padding(horizontal = 4.dp)
                    .fillMaxHeight()
                )
                StatsItem(
                        value = stats.locked.toString(),
                        label = "Locked",
                        valueColor = Color(0xFFFFA500) // 임시 Orange
                    )

            }
        }
    }
}

@Composable
fun Divider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxHeight(0.8f) // Box의 높이를 Row 높이의 80%로 설정
            .width(1.dp)
            .background(Color.LightGray)
    )
}

// Preview 예시
@Preview
@Composable
fun CapsuleStatsCardPreview() {
    val mockStats = CapsuleStatsDomainModel(total = 12, canOpen = 3, locked = 8)
    Column(modifier = Modifier.background(PurpleExtraLight).padding(20.dp)) {
        CapsuleStatsCard(stats = mockStats)
    }
}