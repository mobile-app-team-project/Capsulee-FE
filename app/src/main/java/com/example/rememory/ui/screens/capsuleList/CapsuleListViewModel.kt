package com.example.rememory.ui.screens.capsuleList
import com.example.rememory.domain.repository.CapsuleRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rememory.data.remote.api.MockCapsuleService
import com.example.rememory.data.remote.mock.CapsuleMockData
import com.example.rememory.data.repository.CapsuleRepositoryImpl
import com.example.rememory.domain.model.CapsuleDomainModel
import com.example.rememory.domain.model.CapsuleStatsDomainModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ----------------------------------------------------
// 1. UI 상태 정의 (CapsuleListScreen이 관찰할 데이터)
// ----------------------------------------------------

data class CapsuleListState(
    val capsules: List<CapsuleDomainModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSentSelected: Boolean = true,  // 현재 선택된 탭: true=Sent, false=Received
    val stats: CapsuleStatsDomainModel = CapsuleStatsDomainModel(0, 0, 0)
)

// ----------------------------------------------------
// 2. ViewModel 구현
// ----------------------------------------------------

class CapsuleListViewModel() : ViewModel() {
    private val capsuleRepository: CapsuleRepository = CapsuleRepositoryImpl(
        apiService = MockCapsuleService()
    )

    private val _state = MutableStateFlow(CapsuleListState())
    val state: StateFlow<CapsuleListState> = _state

    init {
        // ViewModel이 생성될 때 초기 데이터 로드
        loadCapsuleList()
    }

    //BigSwitch 토글 시 호출
    fun onTabToggle(isSent: Boolean) {
        if (_state.value.isSentSelected == isSent) return
        _state.update { it.copy(isSentSelected = isSent) }
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
                val domainData = capsuleRepository.getCapsuleList(isSent)
                _state.update {
                    it.copy(
                        isLoading = false,
                        capsules = domainData.capsules, // Domain Model 사용
                        stats = domainData.stats
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