package com.example.rememory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.rememory.ui.navigation.AppNavHost
import com.example.rememory.ui.theme.ReMemoryTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReMemoryTheme {
                val navController = rememberNavController()

                // 앱의 최상위 네비게이션 컴포넌트 호출
                AppNavHost(navController = navController)
            }
        }
    }
}
