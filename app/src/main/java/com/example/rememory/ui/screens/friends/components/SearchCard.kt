package com.example.rememory.ui.screens.friends.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rememory.R
import com.example.rememory.domain.model.UserSearchDomainModel
import com.example.rememory.domain.model.UserStatus
import com.example.rememory.ui.components.PrimaryButton
import com.example.rememory.ui.components.GrayButton
import com.example.rememory.ui.theme.GrayText

/**
 * 검색 결과 카드에서 발생하는 이벤트 콜백 정의
 */
data class SearchActionCallbacks(
    // userId: 요청을 보낼 대상 ID
    val onRequestFriend: (Int) -> Unit = {}
)

@Composable
fun SearchCard(
    userInfo: UserSearchDomainModel,
    callbacks: SearchActionCallbacks = SearchActionCallbacks()
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp, horizontal = 22.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 왼쪽 영역 (아바타, ID, 닉네임)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_capsulee_main),
                    contentDescription = null,
                    modifier = Modifier.size(45.dp)
                )
                Spacer(modifier = Modifier.width(7.dp))

                // 2. ID 및 닉네임 Column
                Column {
                    Text(
                        text = userInfo.userLoginId,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF603886) // #603886
                    )
                    Text(
                        text = userInfo.username,
                        fontSize = 14.sp,
                        color = GrayText
                    )
                }
            }

            // 3. 오른쪽 영역 (상태 버튼)
            Box(modifier = Modifier.wrapContentWidth()) {
                when (userInfo.status) {
                    // 1. 이미 친구인 경우 (ACCEPTED)나 대기중
                    UserStatus.ACCEPTED, UserStatus.PENDING -> {
                        GrayButton(
                            text = if (userInfo.status == UserStatus.PENDING) "pending.." else "connected",
                            onClick = {},
                            modifier = Modifier
                                .width(95.dp),
                            textStyle = TextStyle(fontSize = 14.sp),
                            contentPadding = PaddingValues(horizontal = 4.dp)
                        )
                    }

                    // 2. 친구가 아닌 경우
                    UserStatus.NONE, UserStatus.REJECTED -> {
                        PrimaryButton(
                            text = "request",
                            onClick = {
                                // 친구 요청 이벤트 호출
                                callbacks.onRequestFriend(userInfo.userId)
                            },
                            modifier = Modifier
                                .width(95.dp),
                            textStyle = TextStyle(fontSize = 14.sp),
                            contentPadding = PaddingValues(horizontal = 4.dp)
                        )
                    }

                }
            }
        }
    }
}

// ----------------------------------------------------
// Preview 예시
// ----------------------------------------------------

@Preview(showBackground = true)
@Composable
fun PreviewSearchCardConnected() {
    Column(modifier = Modifier
        .padding(16.dp)
        .background(Color(0xFFF5F5F5))) {
        SearchCard(
            userInfo = UserSearchDomainModel(
                userId = 1, userLoginId = "user_connected", username = "친구1", status = UserStatus.ACCEPTED
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        SearchCard(
            userInfo = UserSearchDomainModel(
                userId = 2, userLoginId = "user_new", username = "새로운 유저", status = UserStatus.NONE
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        SearchCard(
            userInfo = UserSearchDomainModel(
                userId = 3, userLoginId = "user_pending", username = "요청 대기", status = UserStatus.PENDING
            )
        )
    }
}