package com.example.rememory.ui.screens.capsuleCreate.components

import TimeSelector
import android.view.ContextThemeWrapper
import android.widget.CalendarView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.rememory.R
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun LegacyCalendar(
    onDateChange: (LocalDate) -> Unit
) {
    val context = LocalContext.current
    val calendarStyle = remember {
        R.style.PurpleCalendar // styles.xml에서 정의한 스타일
    }

    AndroidView(
        factory = {
            val themedContext = ContextThemeWrapper(it, calendarStyle)
            CalendarView(themedContext).apply {
                setOnDateChangeListener { _, year, month, dayOfMonth ->
                    onDateChange(LocalDate.of(year, month + 1, dayOfMonth))
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)) // ✅ 둥근 테두리
    )
}

@Composable
fun Step2UnlockTime(
    selectedDate: LocalDate,
    selectedTime: LocalTime?,
    onDateChange: (LocalDate) -> Unit,
    onTimeChange: (LocalTime?) -> Unit
) {
    var isTimeEnabled by remember { mutableStateOf(selectedTime != null) }

    Column(modifier = Modifier.fillMaxSize()) {
        Text("Choose Date", style = MaterialTheme.typography.titleSmall)

        Spacer(modifier = Modifier.height(5.dp))

        // 날짜 선택
        LegacyCalendar(onDateChange)

        Spacer(modifier = Modifier.height(15.dp))

        // 시간 선택 스위치
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Choose Time", style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
            Switch(
                checked = isTimeEnabled,
                onCheckedChange = {
                    isTimeEnabled = it
                    if (!it) {
                        onTimeChange(null)
                    } else {
                        // 기본값 설정 (처음 토글 켰을 때)
                        onTimeChange(selectedTime ?: LocalTime.now())
                    }
                }
            )
        }

        // 시간 선택 (조건부 렌더링)
        if (isTimeEnabled) {
            Spacer(modifier = Modifier.height(20.dp))
            TimeSelector(
                initialTime = selectedTime ?: LocalTime.now(),
                onTimeSelected = { time -> onTimeChange(time) }
            )
        }
    }
}