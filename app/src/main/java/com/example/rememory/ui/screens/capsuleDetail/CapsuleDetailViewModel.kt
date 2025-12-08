package com.example.rememory.ui.screens.capsuleDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rememory.data.remote.dto.ActionConditionRequestDto
import com.example.rememory.data.remote.dto.LocationConditionRequestDto
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
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error: ${e.message}") }
            }
        }
    }

    fun checkLocationCondition(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val requestDto = LocationConditionRequestDto(
                    capsuleId = currentCapsuleId,
                    latitude = latitude,
                    longitude = longitude
                )

                repository.checkLocationCondition(requestDto)

                loadCapsuleDetail(currentCapsuleId)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "위치 확인 실패: ${e.message}") }
            }
        }
    }

    fun checkActionCondition(actionType: String) {
        viewModelScope.launch {
            try {
                val requestDto = ActionConditionRequestDto(
                    capsuleId = currentCapsuleId,
                    matched = true
                )

                repository.checkActionCondition(requestDto)

                loadCapsuleDetail(currentCapsuleId)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "액션 확인 실패: ${e.message}") }
            }
        }
    }

    fun onReadyClick() {
        viewModelScope.launch {
            try {
                repository.setReady(currentCapsuleId, true)

                loadCapsuleDetail(currentCapsuleId)

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
                val data = repository.openCapsule(currentCapsuleId)
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