package com.example.rememory.ui.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Home : Screen("home")
    object CapsuleCreate: Screen("capsule_create")

    object CapsuleDetail : Screen("capsule_detail/{capsuleId}") {
        fun createRoute(capsuleId: Int) = "capsule_detail/$capsuleId"
    }
}