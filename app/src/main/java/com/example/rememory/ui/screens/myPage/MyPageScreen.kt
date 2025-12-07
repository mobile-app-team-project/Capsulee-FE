package com.example.rememory.ui.screens.myPage

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.rememory.ui.navigation.AppDestinations
import com.example.rememory.ui.screens.myPage.components.MyPageHomeContent
import androidx.navigation.compose.composable
import com.example.rememory.ui.navigation.BottomNavItem

@Composable
fun MyPageScreen(
    navController: NavController,
    viewModel: MyPageViewModel = viewModel()
){
    val myPageNavController = rememberNavController()


        NavHost(
            navController = myPageNavController, 
            startDestination = AppDestinations.MY_PAGE_HOME_ROUTE,
            //modifier = Modifier.padding(innerPadding)
        ) {
            // A. 내 정보 카드 화면 (기본 화면)
            composable(AppDestinations.MY_PAGE_HOME_ROUTE) {
                MyPageHomeContent(
                    viewModel = viewModel,
                    onEditNavigate = {
                        myPageNavController.navigate(AppDestinations.MY_INFO_EDIT_ROUTE)
                    },
                    onFriendNavigate = {
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
            }

            // B. 내 정보 수정 화면
            composable(AppDestinations.MY_INFO_EDIT_ROUTE) {
                MyInfoEditScreen(
                    navController,
                    viewModel = viewModel
                )
            }
        }

}

