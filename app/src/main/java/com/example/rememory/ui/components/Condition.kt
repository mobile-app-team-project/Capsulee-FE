package com.example.rememory.ui.components

data class ConditionItem(
    val label: String,
    val value: String? = null
)

enum class ConditionType {
    WEATHER,
    LOCATION,
    ACTION
}

data class ConditionInfo(
    val type: ConditionType,
    val isUnlocked: Boolean,
    val items: List<ConditionItem>
)