package com.example.rememory.ui.screens.friends

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rememory.R
import com.example.rememory.ui.components.AppHeader
import com.example.rememory.ui.components.AppTextField
import com.example.rememory.ui.components.BigSwitch
import com.example.rememory.ui.components.TitleLogoStyle

@Composable
fun FriendManagingScreen(
){
    val listnum = 3
    val requestnum = 4

    Scaffold (
        topBar = {
            AppHeader(
                title = "Re:Memory",
                titleStyle = TitleLogoStyle,
                onBackClick = null,
                onPlusClick = {},
                onBellClick = {}
            )
        }
    ){ innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            AppTextField(
                value = "",
                onValueChange = {/* 검색할 경우 */},
                modifier = Modifier.width(325.dp),
                textFieldModifier = Modifier.height(40.dp),
                label = null,
                placeholderText = "Search friends",
                leadingIcon = R.drawable.ic_search
            )
            BigSwitch(
                leftText = "list ($listnum) ",
                rightText = "request ($requestnum)",
                isLeftSelected = true,
            ) {
                //토글시, 바뀌면서 해당 api를 받아와야함
            }
            //여기에 친구 목록 또는 요청 목록을 띄워줘야함
        }
    }
}