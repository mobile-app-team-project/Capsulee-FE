package com.example.rememory.ui.screens.friends.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rememory.R
import com.example.rememory.domain.model.FriendItemDomainModel
import com.example.rememory.ui.components.GrayButton
import com.example.rememory.ui.components.PrimaryButton

@Composable
fun RequestCard(
    requestInfo : FriendItemDomainModel
){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row (
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(id = R.drawable.ic_capsulee_main),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Column {
                Text(text = requestInfo.userLoginId)
                Text(text = requestInfo.username)
            }
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height(34.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)

        ){
            PrimaryButton("accept", onClick = {/*친구 승인 api 호출*/})
            GrayButton("decline", onClick = {/*친구 거절 api 호출*/})
        }
    }
}

@Preview
@Composable
fun requestCardPreview(){
    RequestCard(
        FriendItemDomainModel(2, 2, "user_y", "윤우"),
    )
}