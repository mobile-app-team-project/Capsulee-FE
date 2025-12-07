package com.example.rememory.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material3.Scaffold // Scaffold import
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.rememory.ui.components.BottomNavigationBar
import com.example.rememory.ui.screens.auth.LoginScreen
import com.example.rememory.ui.screens.auth.SignUpScreen
import com.example.rememory.ui.screens.capsuleList.CapsuleListScreen
import com.example.rememory.ui.screens.capsuleList.CapsuleListViewModel
import com.example.rememory.ui.screens.onboarding.OnboardingScreen
import com.example.rememory.ui.screens.friends.FriendManagingScreen
import com.example.rememory.ui.screens.friends.FriendManagingViewModel
import com.example.rememory.ui.screens.home.HomeScreen
import com.example.rememory.ui.screens.myPage.MyPageScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Onboarding.route, // 캡슐 목록을 시작 화면으로 설정
        modifier = modifier
    ) {
        // 온보딩 화면 (BottomBar 없음)
        composable(Screen.Onboarding.route) {
            OnboardingScreen(navController = navController)
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(navController = navController)
        }

        composable(Screen.Login.route) {
             LoginScreen(navController = navController)
        }

        // 메인 앱 화면들 (BottomBar 포함)
        composable(Screen.Home.route) {
            MainScaffold(navController = navController)
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
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Capsule.route) {
                val viewModel: CapsuleListViewModel = hiltViewModel()

                CapsuleListScreen(
                    navController = mainNavController,
                    viewModel = viewModel
                )
            }

            composable(BottomNavItem.Home.route) {
                HomeScreen(navController = mainNavController)
            }
            composable(BottomNavItem.Friends.route) {
                val viewModel: FriendManagingViewModel = hiltViewModel()
                FriendManagingScreen(
                    viewModel = viewModel
                )
            }
            composable(BottomNavItem.MyPage.route) {
                MyPageScreen(navController = mainNavController)
            }
        }
    }
}