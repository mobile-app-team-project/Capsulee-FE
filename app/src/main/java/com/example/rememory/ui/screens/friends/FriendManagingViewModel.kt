package com.example.rememory.ui.screens.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rememory.domain.model.FriendItemDomainModel
import com.example.rememory.domain.model.UserSearchDomainModel
import com.example.rememory.domain.model.UserStatus
import com.example.rememory.domain.repository.FriendRepository
import com.example.rememory.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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

@HiltViewModel
class FriendManagingViewModel @Inject constructor(
    private val repository: FriendRepository,
    private val userRepository: UserRepository
) : ViewModel() {

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

            .distinctUntilChanged { oldState, newState ->
                // 검색어(searchQuery) 값이 이전과 같으면 true를 반환하여 이벤트를 무시합니다.
                oldState.searchQuery == newState.searchQuery
            }
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
                        val results = userRepository.getAllUsers(query) //  검색 API 호출
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
     * 검색 상태를 초기화하고 모든 검색 결과를 숨깁니다.
     */
    fun resetSearchState() {
        // searchQuery를 빈 문자열로 업데이트하고, searchResults도 비웁니다.
        _state.update {
            it.copy(
                searchQuery = "",
                searchResults = emptyList(),
                isSearching = false
            )
        }
        searchJob?.cancel()
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

    /**
     * 비즈니스 로직: 친구 요청을 보내고, UI 상태를 PENDING으로 갱신합니다.
     */
    fun requestFriend(userLoginId: String) {
        viewModelScope.launch {
            // _state.update { it.copy(isSearching = true) } // UI를 막는 경우 사용

            try {
                val success = repository.requestFriend(userLoginId)

                if (success) {
                    // 요청 성공 시: 검색 결과 목록에서 해당 사용자의 상태를 PENDING으로 즉시 업데이트
                    _state.update { currentState ->
                        val updatedResults = currentState.searchResults.map { user ->
                            if (user.userLoginId == userLoginId) {
                                // 상태를 PENDING으로 변경하여 UI에 반영
                                user.copy(status = UserStatus.PENDING)
                            } else {
                                user
                            }
                        }
                        currentState.copy(searchResults = updatedResults)
                    }
                } else {
                    // 요청 실패 처리
                    _state.update { it.copy(errorMessage = "친구 요청 실패") }
                }

            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = "친구 요청 중 오류 발생: ${e.message}") }
            }
        }
    }

    /**
     * 비즈니스 로직: 친구 요청을 처리하고 목록을 갱신합니다.
     */
    fun processFriendRequest(senderLoginId: String, isAccepted: Boolean) {
        viewModelScope.launch {
            val actionStatus = if (isAccepted) "ACCEPTED" else "REJECTED"

            try {
                // 1. Repository를 통해 요청 처리 API 호출
                val success = repository.processFriendRequest(senderLoginId, actionStatus)

                if (success) {
                    // 2. 요청 성공 시, 친구 목록 및 요청 목록 갱신 (전체 목록 재로드)
                    loadFriendData()
                } else {
                    _state.update { it.copy(errorMessage = "요청 처리 실패.") }
                }

            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = "요청 처리 중 오류 발생: ${e.message}") }
            }
        }
    }
}