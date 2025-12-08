package com.example.rememory.ui.screens.capsuleCreate

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.rememory.R
import com.example.rememory.ui.components.AppHeader
import com.example.rememory.ui.components.BottomNavigationBar
import com.example.rememory.ui.components.TitleLogoStyle
import com.example.rememory.ui.theme.BackgroundLight
import com.example.rememory.ui.theme.GrayText

@Composable
fun CapsuleCompleteScreen(navController: NavController) {
    Scaffold(
        topBar = {
            AppHeader(
                title = "Re:Memory",
                titleStyle = TitleLogoStyle,
                onBellClick = {} // 필요하면 알림 클릭 핸들러
            )
        },
        containerColor = BackgroundLight
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Image(
                painter = painterResource(R.drawable.ic_capsulee_main), // ✅ 너가 쓰는 기본 캡슐 이미지
                contentDescription = "Capsule Sealed",
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "🎉 Capsule is Sealed! 🎉",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "See you in the future",
                style = MaterialTheme.typography.bodyMedium,
                color = GrayText,
                textAlign = TextAlign.Center
            )
        }
    }
}