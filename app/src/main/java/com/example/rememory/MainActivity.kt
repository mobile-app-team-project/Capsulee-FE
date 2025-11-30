package com.example.rememory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.rememory.ui.navigation.AppNavHost
import com.example.rememory.ui.screens.capsuleList.CapsuleListScreen
import com.example.rememory.ui.theme.ReMemoryTheme

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
