import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.rememory.ui.theme.BlackText
import com.example.rememory.ui.theme.PurplePrimary
import java.time.LocalTime

@Composable
fun TimeSelector(
    initialTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit
) {
    val hours = (1..12).toList()
    val minutes = (0..59).toList()
    val amPmOptions = listOf("AM", "PM")

    var selectedHour by remember { mutableStateOf(initialTime.hour % 12) }
    var selectedMinute by remember { mutableStateOf(initialTime.minute) }
    var isAm by remember { mutableStateOf(initialTime.hour < 12) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ScrollPicker(
            items = hours,
            selectedItem = if (selectedHour == 0) 12 else selectedHour,
            onItemSelected = {
                selectedHour = it
                onTimeSelected(
                    LocalTime.of(
                        if (isAm) it % 12 else (it % 12) + 12,
                        selectedMinute
                    )
                )
            }
        )

        ScrollPicker(
            items = minutes,
            selectedItem = selectedMinute,
            onItemSelected = {
                selectedMinute = it
                onTimeSelected(
                    LocalTime.of(
                        if (isAm) selectedHour % 12 else (selectedHour % 12) + 12,
                        it
                    )
                )
            }
        )

        ScrollPicker(
            items = amPmOptions,
            selectedItem = if (isAm) "AM" else "PM",
            onItemSelected = {
                isAm = it == "AM"
                onTimeSelected(
                    LocalTime.of(
                        if (isAm) selectedHour % 12 else (selectedHour % 12) + 12,
                        selectedMinute
                    )
                )
            }
        )
    }
}

@Composable
fun <T> ScrollPicker(
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .height(100.dp)
            .width(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(items) { _, item ->
            val isSelected = item == selectedItem
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(Color.Transparent)
                    .clickable { onItemSelected(item) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.toString().padStart(2, '0'),
                    fontSize = if (isSelected) 20.sp else 16.sp,
                    color = if (isSelected) PurplePrimary else BlackText,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}