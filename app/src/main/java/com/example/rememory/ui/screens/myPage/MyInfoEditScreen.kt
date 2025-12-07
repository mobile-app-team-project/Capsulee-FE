package com.example.rememory.ui.screens.myPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.rememory.R
import com.example.rememory.ui.components.AppHeader
import com.example.rememory.ui.components.AppTextField
import com.example.rememory.ui.components.PrimaryButton
import com.example.rememory.ui.components.TitleLogoStyle
import com.example.rememory.ui.navigation.BottomNavItem
import com.example.rememory.ui.theme.GrayBorder
import com.example.rememory.ui.theme.GrayText


@Composable
fun MyInfoEditScreen(
    navController: NavController,
    viewModel: MyPageViewModel = viewModel()
) {
    val uiState by viewModel.state.collectAsState()

    val initialNickname = uiState.myInfo?.nickname
    val initialLoginId = uiState.myInfo?.loginId

    Scaffold (
        topBar = {
            AppHeader(
                title = "",
                titleStyle = TitleLogoStyle,
                onBackClick = {
                    navController.navigate(BottomNavItem.MyPage.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onPlusClick = {},
            )
        }
    ){ innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .padding(18.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ){
            Column (
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
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
                            text = uiState.myInfo?.nickname ?: "",
                            fontSize = 15.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "@${uiState.myInfo?.loginId}",
                            fontSize = 13.sp,
                            color = GrayText
                        )
                    }
                }
                //nick name input with label
                AppTextField(
                    value = uiState.editingNickname,
                    onValueChange = viewModel::onNicknameChange,
                    modifier = Modifier.fillMaxWidth(),
                    textFieldModifier = Modifier.height(45.dp),
                    label = "nick name",
                    placeholderText = initialNickname ?: "Enter new nickname"
                )

                AppTextField(
                    value = uiState.editingLoginId,
                    onValueChange = viewModel::onLoginIdChange,
                    modifier = Modifier.fillMaxWidth(),
                    textFieldModifier = Modifier.height(45.dp),
                    label = "Id",
                    placeholderText = initialLoginId ?: "Enter new Id"
                )

                // 에러 메시지 표시
                if (uiState.errorMessage != null) {
                    Text(
                        text = uiState.errorMessage!!,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            // SAVE 버튼
            PrimaryButton(
                text = if (uiState.isLoading) "SAVING..." else "SAVE",
                onClick = {
                    if (!uiState.isLoading) {
                        viewModel.saveMyInfo {
                            navController.navigate(BottomNavItem.MyPage)
                        }
                    }
                },
                // 로딩 중일 때는 버튼 비활성화
                //enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth().height(50.dp)
            )


        }
    }

}