package com.example.rememory.ui.screens.friends

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rememory.R
import com.example.rememory.ui.components.AppHeader
import com.example.rememory.ui.components.AppTextField
import com.example.rememory.ui.components.BigSwitch
import com.example.rememory.ui.components.TitleLogoStyle
import com.example.rememory.ui.screens.friends.components.FriendActionCallbacks
import com.example.rememory.ui.screens.friends.components.FriendCard
import com.example.rememory.ui.screens.friends.components.RequestCard
import com.example.rememory.ui.screens.friends.components.SearchActionCallbacks
import com.example.rememory.ui.screens.friends.components.SearchCard

@Composable
fun FriendManagingScreen(
    viewModel: FriendManagingViewModel = viewModel()
){
    val uiState by viewModel.state.collectAsState()

    // 1. 목록 개수 계산
    val listnum = uiState.friendList.size
    val requestnum = uiState.requestList.size

    //  화면 이탈 시 검색 상태 초기화
    DisposableEffect(key1 = Unit) {
        onDispose{
            // 이 화면(Screen) 컴포저블이 화면에서 제거(Dispose)될 때 호출됩니다.
            viewModel.resetSearchState()
        }
    }

    Scaffold (
        topBar = {
            AppHeader(
                title = "Re:Memory",
                titleStyle = TitleLogoStyle,
                onBackClick = null,
                onPlusClick = {},
                onBellClick = {}
            )
        }
    ){ innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            AppTextField(
                value = uiState.searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                modifier = Modifier.width(325.dp),
                textFieldModifier = Modifier.height(40.dp),
                label = null,
                placeholderText = "Search friends",
                leadingIcon = R.drawable.ic_search
            )
            BigSwitch(
                leftText = "list ($listnum) ",
                rightText = "request ($requestnum)",
                isLeftSelected = uiState.isListSelected,
            ) { isListSelected ->
                viewModel.resetSearchState()
                viewModel.onTabToggle(isListSelected)
            }

            if (uiState.searchQuery.isNotEmpty()) {
                // 검색어가 있을 경우: 검색 결과 표시
                SearchContent(uiState = uiState, viewModel)
            } else {
                // 검색어가 없을 경우: 친구 목록/요청 목록 표시 (기존 로직)
                FriendListContent(uiState = uiState, viewModel)
            }

        }
    }
}

// 목록 표시 전용 컴포넌트
@Composable
private fun FriendListContent(uiState: FriendManagingState, viewModel: FriendManagingViewModel) {
    if (uiState.isLoading) {
        // 로딩 중일 때
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Text("데이터 로딩 중...")
        }
    } else if (uiState.errorMessage != null) {
        // 에러 발생 시
        Text("Error: ${uiState.errorMessage}", color = Color.Red)
    } else {
        // 데이터 로드 완료 시 (선택된 탭에 따른 목록 표시)
        val isFriendList = uiState.isListSelected
        val currentList = if (uiState.isListSelected) uiState.friendList else uiState.requestList
        val listTitle = if (uiState.isListSelected) "Friend list" else "Friend request"

        if (currentList.isEmpty()) {
            Text(" $listTitle is empty", color = Color.Gray)
        } else {
            LazyColumn (
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(currentList) { item ->
                    if (isFriendList) {
                        // 친구 목록 탭이 선택된 경우
                        FriendCard(
                            friendInfo = item, // item은 FriendItemDomainModel 타입
                            callbacks = FriendActionCallbacks(
                                onDelete = viewModel::deleteFriend
                            )
                        )
                    } else {
                        // 친구 요청 탭이 선택된 경우
                        RequestCard(
                            requestInfo = item, // item은 FriendItemDomainModel 타입
                            onAccept = { viewModel.processFriendRequest(item.userLoginId, true) },
                            onDecline = { viewModel.processFriendRequest(item.userLoginId, false) }

                        )
                    }
                }
            }
        }
    }
}

//검색 표시 전용 컴포넌트
@Composable
private fun SearchContent(uiState: FriendManagingState, viewModel: FriendManagingViewModel) {
    if (uiState.isSearching) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Text("searching...")
        }
    } else if (uiState.searchResults.isEmpty() && uiState.searchQuery.isNotEmpty()) {
        Text("There are no results", color = Color.Gray)
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.searchResults) { user ->
                SearchCard(
                    userInfo = user,
                    callbacks = SearchActionCallbacks(
                        onRequestFriend = {
                            // 요청 대상의 Login ID를 전달해야 하므로, user.loginId를 사용합니다.
                            viewModel.requestFriend(user.userLoginId)
                        }
                    )
                )
            }
        }
    }
}