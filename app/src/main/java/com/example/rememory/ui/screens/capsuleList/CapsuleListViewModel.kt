package com.example.rememory.ui.screens.capsule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
// Domain Repository import
// import com.example.rememory.domain.repository.CapsuleRepository

// ----------------------------------------------------
// 1. UI 상태 정의 (CapsuleListScreen이 관찰할 데이터)
// ----------------------------------------------------

data class CapsuleListState(
    // 캡슐 목록 데이터 (실제 Domain Model로 대체 필요)
    val capsules: List<CapsuleItemInfo> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    // 현재 선택된 탭: true=Sent, false=Received
    val isSentSelected: Boolean = true
)

// ----------------------------------------------------
// 2. ViewModel 구현
// ----------------------------------------------------

// TODO: @HiltViewModel, @Inject 등을 사용하여 Repository를 주입받아야 합니다.
class CapsuleListViewModel(
    // private val capsuleRepository: CapsuleRepository // 🎯 Repository 주입
) : ViewModel() {

    // 외부에 노출되는 상태 (Read-only)
    private val _state = MutableStateFlow(CapsuleListState())
    val state: StateFlow<CapsuleListState> = _state

    init {
        // ViewModel이 생성될 때 초기 데이터 로드
        loadCapsuleList()
    }

    /**
     * UI 이벤트: BigSwitch 토글 시 호출되어 상태를 변경하고 새 목록을 로드합니다.
     */
    fun onTabToggle(isSent: Boolean) {
        // 이미 같은 탭이 선택된 경우 중복 호출 방지
        if (_state.value.isSentSelected == isSent) return

        _state.update {
            it.copy(isSentSelected = isSent)
        }
        loadCapsuleList()
    }

    /**
     * 비즈니스 로직: 현재 탭 상태에 따라 Repository를 호출하여 데이터를 가져옵니다.
     */
    private fun loadCapsuleList() {
        val isSent = _state.value.isSentSelected

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                // 🎯 Repository 호출
                // val resultCapsules = capsuleRepository.getCapsules(isSent)

                // ⚠️ Mock 데이터 사용 예시 (실제 구현 시 위 Repository 호출로 대체)
                delay(500)
                val mockCapsules = mockCapsuleListResponse.capsules
                val resultCapsules = if (isSent) {
                    mockCapsules.filter { it.fromOrTo.startsWith("To") }
                } else {
                    mockCapsules.filter { it.fromOrTo.startsWith("From") }
                }
                // ⚠️ Mock 데이터 끝

                _state.update {
                    it.copy(
                        isLoading = false,
                        capsules = resultCapsules // 🎯 받아온 데이터로 업데이트
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "데이터 로드 실패: ${e.message}"
                    )
                }
            }
        }
    }

    // TODO: 캡슐 클릭 등의 다른 사용자 이벤트 처리 함수를 여기에 추가합니다.
}