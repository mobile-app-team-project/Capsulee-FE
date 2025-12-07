package com.example.rememory.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material3.Scaffold // Scaffold import
import androidx.navigation.compose.rememberNavController
import com.example.rememory.ui.components.BottomNavigationBar
import com.example.rememory.ui.screens.capsuleCreate.CreateCapsuleFlow
import com.example.rememory.ui.screens.capsuleList.CapsuleListScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route, // 캡슐 목록을 시작 화면으로 설정
        modifier = modifier
    ) {
        // 온보딩 화면 (BottomBar 없음)
//        composable(Screen.Onboarding.route) {
//            OnboardingScreen(navController = navController)
//        }

        // 메인 앱 화면들 (BottomBar 포함)
        composable(Screen.Home.route) {
            MainScaffold(navController = navController)
        }

        composable(Screen.CapsuleCreate.route) {
            CreateCapsuleFlow(navController = navController)
        }
    }
}

@Composable
private fun MainScaffold(navController: NavHostController) {
    val mainNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = mainNavController)
        }
    ) { innerPadding ->
        NavHost(
            navController = mainNavController,
            startDestination = BottomNavItem.Capsule.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Capsule.route) {
                CapsuleListScreen(navController = mainNavController)
            }

            composable(BottomNavItem.Home.route) {
//                 HomeScreen(navController = mainNavController)
            }
            composable(BottomNavItem.Friends.route) {
                // FriendsScreen(navController = mainNavController)
            }
            composable(BottomNavItem.MyPage.route) {
                // MyPageScreen(navController = mainNavController)
            }
        }
    }
}