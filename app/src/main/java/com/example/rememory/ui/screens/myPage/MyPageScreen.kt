package com.example.rememory.ui.screens.myPage

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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.rememory.ui.navigation.BottomNavItem
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
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            MyInfoCard(
                myInfo = uiState.myInfo,
                onEditClick = {/*수정 페이지로 이동*/}
            )
            SettingBox(
                "Managing friends",
                {
                    navController.navigate(BottomNavItem.Friends.route) {
                        //옵션 이용해서 이전 스택 제거하고 깔끔하게 이동
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true // 이전 탭의 상태 저장
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
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
        contentPadding = PaddingValues(horizontal = 25.dp, vertical = 8.dp),
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