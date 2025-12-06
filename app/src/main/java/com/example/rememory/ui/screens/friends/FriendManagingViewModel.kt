package com.example.rememory.ui.screens.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rememory.data.remote.ApiObject
import com.example.rememory.data.repository.FriendRepositoryImpl
import com.example.rememory.data.repository.UserRepositoryImpl
import com.example.rememory.domain.model.FriendItemDomainModel
import com.example.rememory.domain.model.UserSearchDomainModel
import com.example.rememory.domain.repository.FriendRepository
import com.example.rememory.domain.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
// ----------------------------------------------------
// 1. UI 상태 정의
// ----------------------------------------------------

data class FriendManagingState(
    val friendList: List<FriendItemDomainModel> = emptyList(),
    val requestList: List<FriendItemDomainModel> = emptyList(),
    val isLoading: Boolean = false,
    val isListSelected: Boolean = true, // true: 친구 목록, false: 요청 목록
    val errorMessage: String? = null,
    //친구 검색
    val searchQuery: String = "",
    val searchResults: List<UserSearchDomainModel> = emptyList(),
    val isSearching: Boolean = false
)

// ----------------------------------------------------
// 2. ViewModel 구현
// ----------------------------------------------------

class FriendManagingViewModel() : ViewModel() {
    private val repository: FriendRepository = FriendRepositoryImpl(
        //ApiObject.friendService
    )
    private val userRepository: UserRepository = UserRepositoryImpl()

    private val _state = MutableStateFlow(FriendManagingState())
    val state: StateFlow<FriendManagingState> = _state
    private var searchJob: Job? = null

    init {
        // ViewModel 초기화 시 두 목록을 동시에 로드합니다.
        loadFriendData()
        startSearchObserver()
    }

    /**
     * BigSwitch 토글 처리 (UI 이벤트)
     */
    fun onTabToggle(isListSelected: Boolean) {
        if (_state.value.isListSelected == isListSelected) return
        _state.update { it.copy(isListSelected = isListSelected) }
    }

    /**
     * UI 이벤트: AppTextField의 값이 변경될 때 호출
     */
    fun onSearchQueryChange(newQuery: String) {
        _state.update { it.copy(searchQuery = newQuery) }
    }

    /**
     * 검색어 Flow를 관찰하여 Debounce 후 검색 API 호출
     */
    private fun startSearchObserver() {
        // Debounce: 사용자가 입력을 멈춘 후 300ms 후에만 검색 시작 (서버 부하 감소)
        _state.asStateFlow()
            .debounce(300L)
            .onEach { state ->
                val query = state.searchQuery
                if (query.isBlank()) {
                    _state.update { it.copy(searchResults = emptyList(), isSearching = false) }
                    return@onEach
                }

                searchJob?.cancel() // 이전 검색 작업 취소

                searchJob = viewModelScope.launch {
                    _state.update { it.copy(isSearching = true) }
                    try {
                        val results = userRepository.searchAllUsers(query) // 🎯 검색 API 호출
                        _state.update { it.copy(searchResults = results, isSearching = false) }
                    } catch (e: Exception) {
                        // 검색 에러 처리 (로그 출력 등)
                        _state.update { it.copy(searchResults = emptyList(), isSearching = false) }
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    /**
     * 비즈니스 로직: 특정 친구를 삭제하고 목록을 갱신합니다.
     */
    fun deleteFriend(friendshipId: Int) {
        viewModelScope.launch {
            // UI에 로딩 상태를 표시할 수도 있습니다. (_state.update { it.copy(isLoading = true) })

            try {
                // 1. Repository를 통해 삭제 API 호출
                repository.deleteFriend(friendshipId)

                // 2. 삭제 성공 후, 친구 목록 데이터를 갱신 (전체 목록 재로드)
                loadFriendData()

            } catch (e: Exception) {
                _state.update {
                    it.copy(errorMessage = "친구 삭제 실패: ${e.message}")
                }
            }
        }
    }

    /**
     * API 호출: 친구 목록과 요청 목록을 비동기로 동시에 로드합니다.
     */
    private fun loadFriendData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                // 병렬 비동기 호출 (실제 앱에서는 async/await로 처리하여 성능 최적화)
                val friends = repository.getFriendList()
                val requests = repository.getFriendRequests()

                _state.update {
                    it.copy(
                        isLoading = false,
                        friendList = friends,
                        requestList = requests
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "데이터 로드 실패: ${e.message}",
                        friendList = emptyList(),
                        requestList = emptyList()
                    )
                }
            }
        }
    }
}