package com.example.rememory.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rememory.R
import com.example.rememory.ui.theme.GrayBorder
import com.example.rememory.ui.theme.GrayText
import com.example.rememory.ui.theme.PurplePrimary

@Composable
fun ParticipantCard(
    participants: List<ParticipantInfo>
){
    Card (
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(1.dp, GrayBorder, RoundedCornerShape(12.dp))
            .padding(15.dp)

    )
    {
        Column (

        ){
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ){
                Image(
                    painter = painterResource(id = R.drawable.ic_friend_outline),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(start = 5.dp)
                )
                Text(
                    text = "Participants",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium)
            }

            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(260.dp, 260.dp)
            ){
                items(participants.size){ index ->
                    val participant = participants[index]

                    Row (
                        Modifier
                            .padding(top = 15.dp),
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ){
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(Color.White)
                                .border(
                                    width = 1.dp,
                                    color = GrayBorder,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ){
                            Image(
                                painter = painterResource(id = R.drawable.ic_capsulee_main),
                                contentDescription = "profile",
                                modifier = Modifier
                                    .size(30.dp)
                            )
                        }
                        Column (
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = participant.nickname,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp
                            )
                            Text(
                                text = if (participant.isReady) "Ready" else "Waiting...",
                                color = if (participant.isReady) PurplePrimary else GrayText,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ParticipantCardPreview(){
    val dummyParticipants = listOf(
        ParticipantInfo("Bonnie", true),
        ParticipantInfo("Yeonu",false),
        ParticipantInfo("Jiyo", true),
        ParticipantInfo("Yeonu",false),
        ParticipantInfo("Jiyo", true),
        ParticipantInfo("Yeonu",false),
        ParticipantInfo("Jiyo", true),
        ParticipantInfo("YK", false)
    )
    Box(
        modifier = Modifier
            .padding(16.dp)
    ){
        ParticipantCard(dummyParticipants)
    }
}