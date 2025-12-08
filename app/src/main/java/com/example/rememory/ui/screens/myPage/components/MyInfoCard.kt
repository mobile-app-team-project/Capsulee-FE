package com.example.rememory.ui.screens.myPage.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rememory.R
import com.example.rememory.domain.model.MyInfoDomainModel
import com.example.rememory.ui.theme.GrayBorder
import com.example.rememory.ui.theme.GrayText
import com.example.rememory.ui.theme.PurplePrimary // (예시 색상)
import com.example.rememory.ui.theme.PurpleLight // (예시 색상: #E6E0F8 톤)

@Composable
fun MyInfoCard(
    myInfo: MyInfoDomainModel?,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
                        .size(70.dp)
                        .clip(CircleShape)
                        .border(width = 1.dp, color = GrayBorder, shape = CircleShape)
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_capsulee_main), // 임시 이미지
                        contentDescription = "My Avatar",
                        modifier = Modifier.size(45.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // 닉네임 & ID
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = myInfo?.nickname ?: "",
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "@${myInfo?.loginId}",
                        fontSize = 13.sp,
                        color = GrayText
                    )
                }

                // 수정 버튼
                Text(
                    text = "edit",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    style = TextStyle(
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .clickable(onClick = onEditClick)
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
                    count = myInfo?.totalCapsules ?: 0
                )
                StatItem(
                    title = "unlocked",
                    count = myInfo?.openedCapsules ?: 0
                )
                StatItem(
                    title = "friends",
                    count = myInfo?.totalFriends ?: 0
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
            .padding(horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            color = GrayText,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))

        // 데이터 블록
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(PurpleLight.copy(alpha = 0.5f), RoundedCornerShape(15.dp))
                .padding(vertical = 14.dp, horizontal = 20.dp),
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