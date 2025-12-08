package com.example.rememory.ui.screens.capsuleCreate.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.rememory.R
import com.example.rememory.ui.screens.capsuleCreate.CreateCapsuleViewModel
import com.example.rememory.ui.theme.GrayText
import java.time.format.DateTimeFormatter

@Composable
fun CreateFinalConfirm(viewModel: CreateCapsuleViewModel) {
    val title = viewModel.title.collectAsState().value
    val message = viewModel.message.collectAsState().value
    val imageFile = viewModel.imageFile.collectAsState().value
    val date = viewModel.selectedDate.collectAsState().value
    val time = viewModel.selectedTime.collectAsState().value
    val location = viewModel.selectedLocation.collectAsState().value
    val weather = viewModel.selectedWeather.collectAsState().value
    val action = viewModel.selectedAction.collectAsState().value
    val recipients = viewModel.recipients.collectAsState().value.filter { it.selected }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp) // 카드 간 간격
    ) {

        // 이미지 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 이미지만 가운데 정렬
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (imageFile != null) {
                        Image(
                            painter = rememberAsyncImagePainter(imageFile),
                            contentDescription = "Capsule Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.ic_capsulee_main),
                            contentDescription = "Default Icon",
                            modifier = Modifier.size(120.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Start
                    )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = message,
                    color = GrayText,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Start
                )
            }
        }

        // 날짜 & 시간 요약
        InfoSection(title = "Date & Time") {
            Text(
                text = "${date.format(DateTimeFormatter.ISO_DATE)} ${time?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: ""}",
                color = GrayText
            )
        }

        // 조건 요약 (선택된 순서대로)
        InfoSection(title = "Conditions Summary") {
            location?.let { Text(text = "Location: ${it.name}", color = GrayText) }
            weather?.let { Text(text = "Weather: ${it.name}", color = GrayText) }
            action?.let { Text(text = "Action: ${action.label}", color = GrayText) }
        }

        // 수신자 요약
        InfoSection(title = "Receiver") {
            recipients.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(Color(0xFFE5DFF8), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = it.username.firstOrNull()?.uppercase() ?: "?",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF6B21A8)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = it.username, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        // 안내 텍스트
        Text(
            text = "Once sealed, this capsule\ncannot be edited. It will only unlock\nwhen all your conditions are met.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun InfoSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            content()
        }
    }
}