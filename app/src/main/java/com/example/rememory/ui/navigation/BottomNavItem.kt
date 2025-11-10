package com.example.rememory.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.rememory.R

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val iconSelected: Int,
    val iconUnselected: Int
) {
    object Home : BottomNavItem(
        route = "home",
        title = "home",
        iconSelected = R.drawable.ic_home_filled,
        iconUnselected = R.drawable.ic_home_outline
    )

    object Capsule : BottomNavItem(
        route = "capsule",
        title = "capsule",
        iconSelected = R.drawable.ic_capsule_filled,
        iconUnselected = R.drawable.ic_capsule_outline
    )

    object Friends : BottomNavItem(
        route = "friends",
        title = "friends",
        iconSelected = R.drawable.ic_friend_filled,
        iconUnselected = R.drawable.ic_friend_outline
    )

    object MyPage : BottomNavItem(
        route = "my_page",
        title = "my page",
        iconSelected = R.drawable.ic_mypage_filled,
        iconUnselected = R.drawable.ic_mypage_outline
    )
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Capsule,
    BottomNavItem.Friends,
    BottomNavItem.MyPage
)