package com.example.rememory.ui.screens.capsule

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rememory.R
import com.example.rememory.ui.components.AppHeader
import com.example.rememory.ui.components.BigSwitch
import com.example.rememory.ui.components.TitleLogoStyle
import com.example.rememory.ui.theme.GrayBorder
import com.example.rememory.ui.theme.GrayText
import com.example.rememory.ui.theme.PurpleExtraLight
import com.example.rememory.ui.theme.PurplePrimary

//서버 응답 데이터 클래스
data class CapsuleListResponse(
    val stats: CapsuleStats,
    val capsules: List<CapsuleItemInfo>
)

data class CapsuleStats(
    val total: Int,
    val canOpen: Int,
    val locked: Int
)
//캡슐 카드 데이터 클래스
data class ConditionInfo (
    val type: String,
    val value: String
)
data class CapsuleItemInfo (
    val capsuleId: Int,
    val title: String,
    val fromOrTo: String,
    val opened: Boolean,
    val conditionSummaries: List<ConditionInfo>,
)

//Mock DATA
val mockCapsuleListResponse = CapsuleListResponse(
    stats = CapsuleStats(
        total = 12,
        canOpen = 3,
        locked = 8
    ),
    capsules = listOf(
        CapsuleItemInfo(
            capsuleId = 1,
            title = "title",
            fromOrTo = "To Jisoo",
            opened = true,
            conditionSummaries = listOf(
                ConditionInfo(type = "TIME", value = "2025-12-25, 09:00 AM"),
                ConditionInfo(type = "RECIPIENTS", value = "Suginnn, Bonnie, Nicolas"),
                ConditionInfo(type = "GEO", value = "Chung-And Univ. Main Gate")
            )
        ),
        CapsuleItemInfo(
            capsuleId = 2,
            title = "22222",
            fromOrTo = "From Jisoo",
            opened = false,
            conditionSummaries = listOf(
                ConditionInfo(type = "TIME", value = "2025-12-25, 09:00 AM"),
                ConditionInfo(type = "GEO", value = "Chung-And Univ. Main Gate")
            )
        )
    )
)



@Composable
fun CapsuleListScreen(){
    //캡슐 리스트 응답 목데이터
    val mockData = mockCapsuleListResponse

    Scaffold (
        topBar = {
            AppHeader(
                title = "Re:Memory",
                titleStyle = TitleLogoStyle,
                onBackClick = null,
                onPlusClick = null,
                onBellClick = {}
            )
        }
    ){ innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .background(PurpleExtraLight),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            BigSwitch(
                leftText = "sent", rightText = "received",
                isLeftSelected = false
            ) { }

            mockData.capsules.forEach { capsule ->
                CapsuleListItemCard(capsule)
            }
        }

    }
}

@Composable
private fun CapsuleListItemCard(capsuleInfo: CapsuleItemInfo){
    val iconRes =
        if(capsuleInfo.opened){
            R.drawable.ic_lock_opened
        }else{
            R.drawable.ic_lock_locked
        }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp, top = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column (
                modifier = Modifier.widthIn(max = 200.dp)
            ){
                Text(
                    text = capsuleInfo.title,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF603886),
                    fontSize = 15.sp
                )
                Text(
                    text = capsuleInfo.fromOrTo,
                    color = GrayText,
                    fontSize = 13.sp
                )
            }
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ){
            Column {
                capsuleInfo.conditionSummaries.forEach { item ->
                    val iconRes =
                        if (item.type === "TIME") {
                            R.drawable.ic_calrender_purple
                        } else if (item.type === "GEO") {
                            R.drawable.ic_map_pin_heart_purple
                        } else {
                            R.drawable.ic_action_purple
                        }
                    Row (
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Image(
                            painter = painterResource(id = iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(text = item.value, fontSize = 13.sp)
                    }
                }
            }
            Text(text = "3 more", color = GrayText)
        }
    }
}

@Preview
@Composable
fun CapsuleListScreenPreview(){
    CapsuleListScreen()
}