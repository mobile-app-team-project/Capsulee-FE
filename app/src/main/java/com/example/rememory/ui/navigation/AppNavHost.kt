package com.example.rememory.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.rememory.ui.components.BottomNavigationBar
import com.example.rememory.ui.screens.capsuleCreate.CreateCapsuleFlow
import com.example.rememory.ui.screens.capsuleList.CapsuleListScreen
import com.example.rememory.ui.screens.capsuleList.CapsuleListViewModel
import com.example.rememory.ui.screens.friends.FriendManagingScreen
import com.example.rememory.ui.screens.friends.FriendManagingViewModel
import com.example.rememory.ui.screens.myPage.MyPageScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // 1. 현재 네비게이션 경로(route)를 실시간으로 추적합니다.
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 2. 하단 바를 보여줘야 하는 화면들의 경로 목록을 정의합니다.
    val bottomBarRoutes = listOf(
        BottomNavItem.Capsule.route,
        BottomNavItem.Home.route,
        BottomNavItem.Friends.route,
        BottomNavItem.MyPage.route
    )

    Scaffold(
        modifier = modifier,
        // 3. 현재 경로가 bottomBarRoutes 목록에 포함되어 있을 때만 BottomNavigationBar를 렌더링합니다.
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Capsule.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // --- 하단 바가 보이는 화면들 ---
            composable(BottomNavItem.Capsule.route) {
                val viewModel: CapsuleListViewModel = hiltViewModel()

                CapsuleListScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable(BottomNavItem.Home.route) {
                // HomeScreen(navController = navController)
            }
            composable(BottomNavItem.Friends.route) {
                val viewModel: FriendManagingViewModel = hiltViewModel()
                FriendManagingScreen(
                    viewModel = viewModel
                )
            }
            composable(BottomNavItem.MyPage.route) {
                MyPageScreen(navController = navController)
            }

            // --- 하단 바가 보이지 않는 화면 ---
            composable(Screen.CapsuleCreate.route) {
                // 이 화면으로 이동하면, 위의 조건문에 따라 bottomBar가 렌더링되지 않습니다.
                CreateCapsuleFlow(navController = navController)
            }

            // 다른 독립적인 화면이 있다면 여기에 추가
            // composable(Screen.Onboarding.route) {
            //     OnboardingScreen(navController = navController)
            // }
        }
    }
}
