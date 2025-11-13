package com.example.rememory.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rememory.R
import com.example.rememory.ui.theme.PurpleExtraLight

@Composable
fun ConditionCard(
    cardInfo: ConditionInfo, modifier: Modifier = Modifier
){
    val iconRes = when(cardInfo.type){
        ConditionType.WEATHER -> R.drawable.ic_clear_day_black
        ConditionType.ACTION -> R.drawable.ic_action_black
        ConditionType.LOCATION -> R.drawable.ic_map_pin_heart_black
    }
    Card (
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(15.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(start = 5.dp)
                )
                Text(
                    text = cardInfo.type.name.lowercase().replaceFirstChar { it.uppercase() },
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }

            // 오른쪽: 태그
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(if (cardInfo.isUnlocked) Color(0xFFB6F3C6) else Color(0xFFFFB8B8))
                    .padding(horizontal = 16.dp, vertical = 5.dp)
            ) {
                Text(
                    text = if (cardInfo.isUnlocked) "unlocked" else "locked",
                    color = Color.Black,
                    fontSize = 14.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(11.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)) // 둥근 모서리
                .background(PurpleExtraLight)
                .padding(16.dp),
            Arrangement.spacedBy(11.dp)
        ) {
            cardInfo.items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                    Text(text = item.label)
                    if (item.value != null) {
                        Text(text = item.value, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ConditionCardPreview(){
    val weatherCard = ConditionInfo(
        type = ConditionType.WEATHER,
        isUnlocked = true,
        items = listOf(
            ConditionItem("Weather Lock", "snow")
        )
    )

    val actionCard = ConditionInfo(
        type = ConditionType.ACTION,
        isUnlocked = false,
        items = listOf(
            ConditionItem("Action Lock", "Shaking 3 times")
        )
    )

    val locationCard = ConditionInfo(
        type = ConditionType.LOCATION,
        isUnlocked = false,
        items = listOf(
            ConditionItem("Current Location", "Seoul Station"),
            ConditionItem("Target Location", "Chung-Ang University"),
            ConditionItem("Distance", "2.3km")
        )
    )

    Column(modifier = Modifier.padding(16.dp)) {
        ConditionCard(weatherCard)
        Spacer(modifier = Modifier.height(16.dp))
        ConditionCard(actionCard)
        Spacer(modifier = Modifier.height(16.dp))
        ConditionCard(locationCard)
    }
}
