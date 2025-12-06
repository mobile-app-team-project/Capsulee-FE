package com.example.rememory.ui.screens.friends.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rememory.R
import com.example.rememory.domain.model.FriendItemDomainModel
import com.example.rememory.ui.theme.GrayText
import com.example.rememory.ui.theme.PurplePrimary

// FriendCard가 받을 콜백 함수 정의 (ViewModel로 전달될 이벤트)
data class FriendActionCallbacks(
    val onBlock: (Int) -> Unit = {},
    val onDelete: (Int) -> Unit = {}
)

@Composable
fun FriendCard(
    friendInfo: FriendItemDomainModel,
    callbacks: FriendActionCallbacks = FriendActionCallbacks()
) {
    // 메뉴가 열렸는지 닫혔는지 상태를 관리
    var expanded by remember { mutableStateOf(false) }

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
                        text = friendInfo.userLoginId, // loginID를 id로 표시
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF603886)
                    )
                    Text(
                        text = friendInfo.username, // username을 닉네임으로 표시
                        fontSize = 14.sp,
                        color = GrayText
                    )
                }
            }

            // 오른쪽 영역 (메뉴 버튼 및 DropdownMenu)
            Box(
                modifier = Modifier.wrapContentSize(Alignment.TopEnd)
            ) {
                // 3. 점 3개 메뉴 버튼
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = GrayText
                    )
                }

                // 4. 드롭다운 메뉴 (열리고 닫힘)
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }, // 다른 곳 클릭 시 닫힘
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .wrapContentWidth(),

                ) {
                    // 삭제 (delete) 항목
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "delete",
                                color = Color(0xFF603886),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        },
                        onClick = {
                            expanded = false
                            callbacks.onDelete(friendInfo.friendShipId) // 삭제 이벤트 호출
                        },
                        modifier = Modifier
                            .background(Color.White)
                            .height(35.dp)
                            .width(70.dp)
                            .padding( horizontal = 4.dp)
                    )
                }
            }
        }
    }
}

// Preview를 위한 Mock 데이터
@Preview(showBackground = true)
@Composable
fun PreviewFriendCard() {
    val mockFriend = FriendItemDomainModel(
        friendShipId = 1,
        userId = 10,
        userLoginId = "ji_soo_login",
        username = "지수 닉네임"
    )
    Column(modifier = Modifier.padding(16.dp).background(Color(0xFFF5F5F5))) {
        FriendCard(friendInfo = mockFriend)
    }
}