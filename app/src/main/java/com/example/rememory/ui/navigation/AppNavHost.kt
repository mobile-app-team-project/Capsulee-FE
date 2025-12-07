package com.example.rememory.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material3.Scaffold // Scaffold import
import com.example.rememory.ui.components.BottomNavigationBar
import com.example.rememory.ui.screens.capsuleList.CapsuleListScreen
import com.example.rememory.ui.screens.friends.FriendManagingScreen
import com.example.rememory.ui.screens.myPage.MyPageScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // 앱의 최상위 Scaffold
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->

        // 실제 화면 전환 담당
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Capsule.route, // 캡슐 목록을 시작 화면으로 설정
            modifier = modifier.padding(innerPadding)
        ) {
            // ----------------------------------------------------
            // 캡슐 목록 화면 정의
            // ----------------------------------------------------
            composable(BottomNavItem.Capsule.route) {
                CapsuleListScreen(
                    navController = navController // 화면에 NavController 전달 (네비게이션 액션 사용 가능)
                )
            }
            composable(BottomNavItem.Home.route) {
                // HomeScreen(navController = navController)
            }
            composable(BottomNavItem.Friends.route) {
                FriendManagingScreen()
            }
            composable(BottomNavItem.MyPage.route) {
                MyPageScreen(navController = navController)
            }
        }
    }
}