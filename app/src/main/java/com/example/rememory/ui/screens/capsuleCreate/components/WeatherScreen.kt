package com.example.rememory.ui.screens.capsuleCreate.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.rememory.R
import com.example.rememory.domain.model.WeatherCondition
import com.example.rememory.ui.theme.PurplePrimary
import androidx.compose.material3.Icon

@Composable
fun WeatherScreen(
    selectedWeather: WeatherCondition?,
    onWeatherSelected: (WeatherCondition) -> Unit
) {
    val weatherOptions = listOf(
        WeatherCondition.CLEAR,
        WeatherCondition.RAINY,
        WeatherCondition.SNOWY,
        WeatherCondition.CLOUD
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(weatherOptions.size) { index ->
            val condition = weatherOptions[index]
            WeatherCard(
                condition = condition,
                isSelected = condition == selectedWeather,
                onClick = { onWeatherSelected(condition) }
            )
        }
    }
}

@Composable
fun WeatherCard(
    condition: WeatherCondition,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) PurplePrimary.copy(alpha = 0.15f) else Color.White
    val borderColor = if (isSelected) PurplePrimary else Color.LightGray
    val icon = when (condition) {
        WeatherCondition.CLEAR -> R.drawable.ic_clear_day
        WeatherCondition.RAINY -> R.drawable.ic_rainy_day
        WeatherCondition.SNOWY -> R.drawable.ic_snowy_day
        WeatherCondition.CLOUD -> R.drawable.ic_cloud_day
    }
    val label = when (condition) {
        WeatherCondition.CLEAR -> "Clear Day"
        WeatherCondition.RAINY -> "Rainy Day"
        WeatherCondition.SNOWY -> "Snowy Day"
        WeatherCondition.CLOUD -> "Cloudy Day"
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        onClick = onClick,
        modifier = Modifier
            .size(153.dp, 180.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(painterResource(id = icon), contentDescription = label, tint = Color.Unspecified)
            Spacer(modifier = Modifier.height(25.dp))
            Text(label)
        }
    }
}