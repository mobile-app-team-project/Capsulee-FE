package com.example.rememory.ui.screens.myPage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rememory.ui.components.AppHeader
import com.example.rememory.ui.components.TitleLogoStyle
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rememory.ui.screens.myPage.components.MyInfoCard

@Composable
fun MyPageScreen(
    navController: NavController,
    viewModel: MyPageViewModel = viewModel()
){
    val uiState by viewModel.state.collectAsState()
    
    Scaffold (
        topBar = {
            AppHeader(
                title = "Re:Memory",
                titleStyle = TitleLogoStyle,
                onPlusClick = {},
            )
        }
    ){ innerPadding ->
        Column (
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ){
            MyInfoCard(
                myInfo = uiState.myInfo,
                onEditClick = {/*수정 페이지로 이동*/}
            )
            SettingBox(
                "Managing friends",
                {/*친구 페이지로 이동*/}
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

    val horizontalPadding = 20.dp // 일반적인 좌우 패딩을 유지하거나, 0.dp로 설정
    val customContentPadding = PaddingValues(horizontal = horizontalPadding)

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .alpha(buttonAlpha),

        // 버튼의 기본 내부 패딩을 재정의하여 콘텐츠를 왼쪽 끝으로 밀기 쉽게 만듭니다.
        contentPadding = customContentPadding,

        // 버튼 색상은 White 또는 Transparent를 사용하여 배경색을 드러나게 할 수 있습니다.
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