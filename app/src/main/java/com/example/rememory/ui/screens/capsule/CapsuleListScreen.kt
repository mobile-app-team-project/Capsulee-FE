package com.example.rememory.ui.screens.capsule

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.rememory.R

data class conditionInfo (
    val type: String,
    val value: String
)
data class CapsuleItemInfo (
    val title: String,
    val fromOrTo: String,
    val opened: Boolean,
    val conditionSummaries: List<conditionInfo>,
)


@Composable
private fun CapsuleListItemCard(capsuleInfo: CapsuleItemInfo){
    val iconRes =
        if(capsuleInfo.opened){
            R.drawable.ic_lock_opened
        }else{
            R.drawable.ic_lock_locked
        }
    Card {
        Row {
            Column {
                Text(capsuleInfo.title)
                Text(capsuleInfo.fromOrTo)
            }
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
        Column {
            capsuleInfo.conditionSummaries.forEach { item ->
                Row {
                    //타입에 따른 이미지 넣고
                    Text(item.value)
                }
            }

        }
    }
}