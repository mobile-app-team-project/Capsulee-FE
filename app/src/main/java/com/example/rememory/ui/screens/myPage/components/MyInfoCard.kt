package com.example.rememory.ui.screens.myPage.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rememory.R // 🚨 리소스 ID는 프로젝트에 맞게 수정 필요
import com.example.rememory.domain.model.MyInfoDomainModel
import com.example.rememory.ui.theme.GrayText
import com.example.rememory.ui.theme.PurplePrimary // (예시 색상)
import com.example.rememory.ui.theme.PurpleLight // (예시 색상: #E6E0F8 톤)

@Composable
fun MyInfoCard(
    myInfo: MyInfoDomainModel,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // 1. 프로필 정보 (닉네임, ID, 수정 버튼)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 아바타
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color.Transparent, CircleShape)
                    // 🚨 TODO: 실제 아바타 이미지로 대체 필요
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_capsulee_main), // 임시 이미지
                        contentDescription = "My Avatar",
                        modifier = Modifier.size(60.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // 닉네임 & ID
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = myInfo.nickname,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "@${myInfo.loginId}",
                        fontSize = 16.sp,
                        color = GrayText
                    )
                }

                // 수정 버튼
                Text(
                    text = "edit",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PurplePrimary,
                    modifier = Modifier.clickable(onClick = onEditClick)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. 통계 정보 (total joined, unlocked, friends)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    title = "total joined",
                    count = myInfo.totalCapsules
                )
                StatItem(
                    title = "unlocked",
                    count = myInfo.openedCapsules
                )
                StatItem(
                    title = "friends",
                    count = myInfo.totalFriends
                )
            }
        }
    }
}

@Composable
private fun RowScope.StatItem(title: String, count: Int) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = GrayText
        )
        Spacer(modifier = Modifier.height(8.dp))

        // 데이터 블록
        Box(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .background(PurpleLight.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = count.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PurplePrimary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMyInfoCard() {
    val mockInfo = MyInfoDomainModel(
        userId = 1,
        loginId = "my_id",
        nickname = "my_nickname",
        totalCapsules = 12,
        openedCapsules = 24,
        totalFriends = 36,
        isAlarmOn = true
    )
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFF0F0F0))
        .padding(top = 40.dp)
    ) {
        MyInfoCard(myInfo = mockInfo, onEditClick = {})
    }
}