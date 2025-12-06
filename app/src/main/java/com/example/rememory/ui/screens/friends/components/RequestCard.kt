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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rememory.R
import com.example.rememory.domain.model.FriendItemDomainModel
import com.example.rememory.ui.components.GrayButton
import com.example.rememory.ui.components.PrimaryButton
import com.example.rememory.ui.theme.GrayText

@Composable
fun RequestCard(
    requestInfo : FriendItemDomainModel
){
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 18.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ){
        Column (
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.Start
        ){
            Row (
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    painter = painterResource(id = R.drawable.ic_capsulee_main),
                    contentDescription = null,
                    modifier = Modifier.size(45.dp)
                )
                Column {
                    Text(
                        text = requestInfo.userLoginId,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF603886)
                    )
                    Text(
                        text = requestInfo.username,
                        fontSize = 14.sp,
                        color = GrayText
                    )
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
}

@Preview
@Composable
fun requestCardPreview(){
    RequestCard(
        FriendItemDomainModel(2, 2, "user_y", "윤우"),
    )
}