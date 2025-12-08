package com.example.rememory.ui.screens.capsuleCreate.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.rememory.domain.model.Recipient
import com.example.rememory.ui.screens.capsuleCreate.CreateCapsuleViewModel
import com.example.rememory.R
import com.example.rememory.ui.theme.GrayText

@Composable
fun Step4RecipientSelection(viewModel: CreateCapsuleViewModel) {
    val recipients by viewModel.recipients.collectAsState()

    // 처음 진입 시 친구 목록 요청
    LaunchedEffect(Unit) {
        viewModel.fetchFriendList()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 카드 형태의 리스트
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            LazyColumn {
                items(recipients) { recipient ->
                    RecipientItem(recipient = recipient, onToggle = {
                        viewModel.toggleRecipient(recipient.id)
                    })
                }
            }
        }
    }
}

@Composable
fun RecipientItem(
    recipient: Recipient,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar 아이콘
        Icon(
            painter = painterResource(id = R.drawable.ic_capsulee_main),
            contentDescription = "Avatar",
            modifier = Modifier.size(36.dp),
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = recipient.username, style = MaterialTheme.typography.bodyLarge)
            }
            Text(
                text = "@${recipient.loginId}",
                style = MaterialTheme.typography.labelSmall,
                color = GrayText
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_check_circle),
            contentDescription = "Selected",
            tint = if (recipient.selected) Color(0xFF9333EA) else Color.LightGray,
            modifier = Modifier.size(24.dp)
        )
    }
}