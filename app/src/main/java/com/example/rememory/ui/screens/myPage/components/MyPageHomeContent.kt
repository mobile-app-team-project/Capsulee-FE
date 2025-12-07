package com.example.rememory.ui.screens.myPage.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rememory.ui.components.AppHeader
import com.example.rememory.ui.components.TitleLogoStyle
import com.example.rememory.ui.screens.myPage.MyPageViewModel


@Composable
fun MyPageHomeContent(
    viewModel: MyPageViewModel,
    onEditNavigate: () -> Unit, // 수정 화면 이동 콜백
    onFriendNavigate: () -> Unit
) {
    val uiState by viewModel.state.collectAsState()
    Scaffold (
        topBar = {
            AppHeader(
                title = "Re:Memory",
                titleStyle = TitleLogoStyle,
                onPlusClick = {},
            )
        }
    ){innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(start = 18.dp, end = 18.dp, top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MyInfoCard(
                myInfo = uiState.myInfo,
                onEditClick = onEditNavigate
            )
            SettingBox(
                "Managing friends",
                onFriendNavigate
            )
            SettingBox(
                "Managing groups",
                {}
            )
            SettingBox(
                "Log out",
                {/*토큰 제거 로직*/},
                isLogout = true
            )

        }
    }

}


@Composable
fun SettingBox(
    text: String,
    onClick: () -> Unit,
    isLogout: Boolean? = false
){
    val buttonAlpha = if ( isLogout == true) 0.5f else 1.0f

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .alpha(buttonAlpha),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 25.dp, vertical = 10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black // 텍스트 색상
        ),
        elevation = ButtonDefaults.buttonElevation(0.dp) // 그림자 제거
    ) {
        // 3. 콘텐츠 왼쪽 정렬
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = 13.sp
            )
        }
    }
}