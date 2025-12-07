package com.example.rememory.ui.screens.myPage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.rememory.ui.components.AppHeader
import com.example.rememory.ui.components.TitleLogoStyle

@Composable
fun MyPageScreen(
    navController: NavController,
){
    Scaffold (
        topBar = {
            AppHeader(
                title = "Re:Memory",
                titleStyle = TitleLogoStyle,
                onPlusClick = {},
            )
        }
    ){ innerPadding ->
        Column (
            modifier = Modifier.padding(innerPadding)
        ){
            Card {
                Text("my nickname")
            }
        }

    }
}