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
    val errorMessage: String? = null,
    val editingNickname: String = "",
    val editingLoginId: String = ""
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

    fun loadMyInfo() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val info = userRepository.getMyInfo()
                _state.update {
                    it.copy(
                        myInfo = info,
                        isLoading = false,
                        editingNickname = info.nickname,
                        editingLoginId = info.loginId
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

    fun onNicknameChange(newNickname: String) {
        _state.update { it.copy(editingNickname = newNickname) }
    }

    fun onLoginIdChange(newLoginId: String) {
        _state.update { it.copy(editingLoginId = newLoginId) }
    }

    fun saveMyInfo(onSuccess: () -> Unit) {
        val nickname = _state.value.editingNickname
        val loginId = _state.value.editingLoginId

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                // 1. 정보 업데이트 (PUT /users/me)
                userRepository.updateMyInfo(nickname, loginId)
                loadMyInfo()
                onSuccess()
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed editing my info"
                    )
                }
            }
        }
    }

    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                // 1. Repository를 통해 토큰 제거 (및 서버 로그아웃 호출)
                userRepository.logoutUser()

                // 2. ViewModel 상태 초기화 (필요시)
                _state.update { MyPageState() }

                // 3. 성공 콜백 호출 (UI 네비게이션 트리거)
                onLogoutSuccess()
            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = "Log out Failed") }
            }
        }
    }
}