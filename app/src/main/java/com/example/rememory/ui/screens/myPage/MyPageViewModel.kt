package com.example.rememory.ui.screens.myPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rememory.domain.model.MyInfoDomainModel
import com.example.rememory.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyPageState(
    val myInfo: MyInfoDomainModel? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MyPageState())
    val state: StateFlow<MyPageState> = _state

    init {
        loadMyInfo()
    }

    private fun loadMyInfo() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val info = userRepository.getMyInfo()
                _state.update {
                    it.copy(
                        myInfo = info,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "내 정보 로드 실패: ${e.message}"
                    )
                }
            }
        }
    }

    // TODO: 프로필 수정, 로그아웃 등 다른 기능 추가 예정
}