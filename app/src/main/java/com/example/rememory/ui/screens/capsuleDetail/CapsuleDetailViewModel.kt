package com.example.rememory.ui.screens.capsuleDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rememory.domain.model.CapsuleCondition
import com.example.rememory.domain.model.CapsuleDetailData
import com.example.rememory.domain.model.CapsuleDetailStatus
import com.example.rememory.domain.repository.CapsuleDetailRepository
import com.example.rememory.util.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CapsuleDetailUiState(
    val data: CapsuleDetailData? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isOpening: Boolean = false, // 캡슐 오픈 애니메이션 중인지
    // 타이머 관련
    val remainingDays: Int = 0,
    val remainingTime: String = "00:00",
    val remainingSeconds: Int = 0
)

@HiltViewModel
class CapsuleDetailViewModel @Inject constructor(
    private val repository: CapsuleDetailRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CapsuleDetailUiState())
    val uiState: StateFlow<CapsuleDetailUiState> = _uiState

    private var currentCapsuleId: Int = 0
    private var isTimerRunning = false

    fun loadCapsuleDetail(capsuleId: Int) {
        currentCapsuleId = capsuleId
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val data = repository.getCapsuleDetail(capsuleId)
                _uiState.update { it.copy(data = data, isLoading = false) }

                // LOCKED 상태면 타이머 시작
                if (data.status == CapsuleDetailStatus.LOCKED) {
                    startTimer(data.capsuleInfo.openTime)
                }

                if (data.status == CapsuleDetailStatus.WAITING) {
                    checkConditions(data.conditions)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error: ${e.message}") }
            }
        }
    }

    private suspend fun checkConditions(conditions: List<CapsuleCondition>) {
        conditions.forEach { condition ->
            when (condition.type.uppercase()) {
                "WEATHER" -> {
                    // 날씨는 자동으로 체크 (위치 정보 필요)
                    // TODO: 현재 위치 가져와서 날씨 체크
                }
                "LOCATION" -> {
                    // 위치는 사용자가 버튼 클릭 시 체크
                    // (자동으로 체크하지 않음)
                }
                "ACTION" -> {
                    // 액션은 사용자가 수행 시 체크
                    // (자동으로 체크하지 않음)
                }
            }
        }
    }

    fun checkLocationCondition(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val result = repository.checkLocationCondition(
                    currentCapsuleId,
                    latitude,
                    longitude
                )

                // 조건 충족 여부에 따라 UI 업데이트
                if (result.isReadyAvailable) {
                    // 모든 조건이 충족되었으면 데이터 새로고침
                    loadCapsuleDetail(currentCapsuleId)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "위치 확인 실패: ${e.message}") }
            }
        }
    }

    fun checkActionCondition(actionType: String) {
        viewModelScope.launch {
            try {
                val result = repository.checkActionCondition(currentCapsuleId, actionType)

                if (result.isReadyAvailable) {
                    loadCapsuleDetail(currentCapsuleId)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "액션 확인 실패: ${e.message}") }
            }
        }
    }

    fun onReadyClick() {
        viewModelScope.launch {
            try {
                repository.setReady(currentCapsuleId, true)

                val updatedData = repository.getCapsuleDetail(currentCapsuleId)
                _uiState.update { it.copy(data = updatedData) }

                startLobbyPolling()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Ready 처리 실패: ${e.message}") }
            }
        }
    }

    private fun startLobbyPolling() {
        viewModelScope.launch {
            while (true) {
                try {
                    val lobbyStatus = repository.checkLobbyStatus(currentCapsuleId)

                    if (lobbyStatus.opened) {
                        // 캡슐이 열렸으면 폴링 중지하고 데이터 새로고침
                        loadCapsuleDetail(currentCapsuleId)
                        break
                    }

                    // 참가자 상태 업데이트
                    _uiState.update { currentState ->
                        val updatedParticipants = currentState.data?.participants?.map { participant ->
                            val lobbyParticipant = lobbyStatus.participants.find { it.userId == participant.userId }
                            participant.copy(isReady = lobbyParticipant?.ready ?: false)
                        } ?: emptyList()

                        currentState.copy(
                            data = currentState.data?.copy(participants = updatedParticipants)
                        )
                    }

                    delay(2000) // 2초마다 폴링
                } catch (e: Exception) {
                    break
                }
            }
        }
    }

    fun onOpenCapsuleClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(isOpening = true) }
            delay(2500) // 캡슐 열리는 연출 시간 (2.5초)

            try {
                val data = repository.getCapsuleDetail(currentCapsuleId)
                _uiState.update { it.copy(data = data, isOpening = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isOpening = false, errorMessage = "캡슐 열기 실패: ${e.message}") }
            }
        }
    }

    private fun startTimer(openTimeStr: String) {
        if (isTimerRunning) return
        isTimerRunning = true
        viewModelScope.launch {
            while (true) {
                val (days, timeStr, seconds) = TimeUtils.calculateTimeRemaining(openTimeStr)

                _uiState.update {
                    it.copy(
                        remainingDays = days,
                        remainingTime = timeStr,
                        remainingSeconds = seconds
                    )
                }

                // 시간이 다 되면 루프 종료 및 데이터 새로고침
                if (days <= 0 && timeStr == "00:00" && seconds == 0) {
                    isTimerRunning = false
                    loadCapsuleDetail(currentCapsuleId)
                    break
                }
                delay(1000)
            }
        }
    }
}