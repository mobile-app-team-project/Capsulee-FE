package com.example.rememory.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rememory.data.repository.HomeRepositoryImpl
import com.example.rememory.domain.model.HomeScreenData
import com.example.rememory.domain.repository.HomeRepository
import com.example.rememory.util.TimeUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val homeData: HomeScreenData? = null,
    val remainingDays: Int = 0,
    val remainingTime: String = "00:00",
    val remainingSeconds: Int = 0,
    val isLoading: Boolean = true
)

class HomeViewModel : ViewModel() {
    private val repository: HomeRepository = HomeRepositoryImpl()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadData()
        startTimeUpdater()
    }

    private fun loadData() {
        viewModelScope.launch {
            val data = repository.getHomeData()

            _uiState.update {
                it.copy(
                    homeData = data,
                    isLoading = false
                )
            }

            updateRemainingTime()
        }
    }

    private fun startTimeUpdater() {
        viewModelScope.launch {
            while (true) {
                updateRemainingTime()
                delay(1000) // 1초마다 업데이트
            }
        }
    }

    private fun updateRemainingTime() {
        val openTime = _uiState.value.homeData?.capsuleInfo?.openTime ?: return
        val (days, time, seconds) = TimeUtils.calculateTimeRemaining(openTime)

        _uiState.update {
            it.copy(
                remainingDays = days,
                remainingTime = time,
                remainingSeconds = seconds
            )
        }
    }

    fun onCreateCapsuleClick() {
        // TODO: 캡슐 생성 화면으로 이동
    }
}