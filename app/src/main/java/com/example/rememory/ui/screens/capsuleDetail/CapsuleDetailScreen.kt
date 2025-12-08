package com.example.rememory.ui.screens.capsuleDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.rememory.R
import com.example.rememory.domain.model.CapsuleCondition
import com.example.rememory.domain.model.CapsuleDetailData
import com.example.rememory.domain.model.CapsuleDetailStatus
import com.example.rememory.domain.model.CapsuleParticipant
import com.example.rememory.ui.components.*
import com.example.rememory.ui.theme.*

@Composable
fun CapsuleDetailScreen(
    navController: NavController,
    capsuleId: Int,
    viewModel: CapsuleDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(capsuleId) {
        viewModel.loadCapsuleDetail(capsuleId)
    }

    Scaffold(
        topBar = {
            // ✅ [수정] 홈 화면처럼 상태바 패딩을 추가하여 헤더 위치와 크기감을 맞춤
            Column(
                modifier = Modifier.background(BackgroundLight)
            ) {
                Spacer(modifier = Modifier.statusBarsPadding())
                AppHeader(
                    title = "Capsule Details",
                    titleStyle = TitlePageStyle,
                    onBackClick = { navController.popBackStack() },
                    onPlusClick = { },
                    onBellClick = { }
                )
            }
        },
        containerColor = BackgroundLight
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = PurplePrimary
                )
            } else if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage!!,
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Red
                )
            } else {
                uiState.data?.let { data ->
                    // Page 6: 오픈 로딩 상태 (Ready 완료 후 오픈 버튼 클릭 시)
                    if (uiState.isOpening) {
                        OpeningLoadingContent()
                    } else {
                        // 상태별 분기
                        when (data.status) {
                            CapsuleDetailStatus.LOCKED ->
                                LockedStateContent(
                                    data = data,
                                    remainingDays = uiState.remainingDays,
                                    remainingTime = uiState.remainingTime,
                                    remainingSeconds = uiState.remainingSeconds
                                )

                            CapsuleDetailStatus.WAITING,
                            CapsuleDetailStatus.READY ->
                                WaitingAndReadyStateContent(
                                    data = data,
                                    onReadyClick = { viewModel.onReadyClick() },
                                    onOpenClick = { viewModel.onOpenCapsuleClick() }
                                )

                            CapsuleDetailStatus.OPENED ->
                                OpenedStateContent(data)
                        }
                    }
                }
            }
        }
    }
}

// ------------------------------------------------------------------------
// Page 1: LOCKED (오픈 전)
// ------------------------------------------------------------------------
@Composable
private fun LockedStateContent(
    data: CapsuleDetailData,
    remainingDays: Int,
    remainingTime: String,
    remainingSeconds: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // 진행률 표시 카드
        ProcessCard(processPercent = data.capsuleInfo.processPercent ?: 0)

        Spacer(modifier = Modifier.height(16.dp))

        // 캡슐 이미지 + 타이머
        CapsuleTimerSection(
            remainingDays = remainingDays,
            remainingTime = remainingTime,
            remainingSeconds = remainingSeconds
        )

        Spacer(modifier = Modifier.height(16.dp))

        CapsuleInfoCard(
            title = data.capsuleInfo.title,
            from = data.capsuleInfo.from,
            openTime = data.capsuleInfo.openTime
        )

        Spacer(modifier = Modifier.height(16.dp))

//        // Participants 카드
//        val participants = data.participants.map {
//            ParticipantInfo(nickname = it.userName, isReady = false)
//        }
        ParticipantsCard(participants = data.participants)

        Spacer(modifier = Modifier.height(16.dp))

        // Capsule Details 카드
        CapsuleDetailsCard(conditions = data.conditions)

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// 진행률 카드
@Composable
private fun ProcessCard(processPercent: Int) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$processPercent%",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MontserratFontFamily,
                color = PurplePrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            ProgressBar(
                progress = processPercent / 100f,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// 캡슐 + 타이머 섹션
@Composable
private fun CapsuleTimerSection(
    remainingDays: Int,
    remainingTime: String,
    remainingSeconds: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // 캡슐 이미지 (HomeScreen과 동일)
        Box(
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_capsulee_main),
                contentDescription = "Capsule",
                modifier = Modifier.size(280.dp)
            )

            // 타이머 오버레이
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.offset(y = 60.dp)
            ) {
                Text(
                    text = "$remainingDays Days",
                    fontSize = 28.sp,
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "$remainingTime:${String.format("%02d", remainingSeconds)}",
                    fontSize = 24.sp,
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }


    }
}

@Composable
private fun CapsuleInfoCard(
    title: String,
    from: String,
    openTime: String
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MontserratFontFamily,
                color = BlackText,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "From $from",
                fontSize = 14.sp,
                fontFamily = MontserratFontFamily,
                color = GrayText,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = openTime,
                fontSize = 14.sp,
                fontFamily = MontserratFontFamily,
                color = GrayText,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ✅ Participants 카드 (가로 배치, 상태 없음)
@Composable
private fun ParticipantsCard(participants: List<CapsuleParticipant>) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_friend_outline),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Participants",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = MontserratFontFamily,
                    color = BlackText
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ 가로 스크롤 레이아웃
            if (participants.size <= 4) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly  // ✅ 균등 분배
                ) {
                    participants.forEach { participant ->
                        ParticipantItem(participant.userName)
                    }
                }
            } else {
                // 참여자가 많으면 스크롤
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(participants) { participant ->
                        ParticipantItem(participant.userName)
                    }
                }
            }
        }
    }
}

// ✅ 참여자 아이템 (세로 배치: 아이콘 + 이름)
@Composable
private fun ParticipantItem(userName: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 프로필 아이콘
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.White, CircleShape)
                .border(1.dp, GrayBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_capsulee_main),
                contentDescription = "profile",
                modifier = Modifier.size(30.dp)
            )
        }

        // 이름
        Text(
            text = userName,
            fontSize = 14.sp,
            fontFamily = MontserratFontFamily,
            color = BlackText,
            textAlign = TextAlign.Center
        )
    }
}

// Capsule Details 카드
@Composable
private fun CapsuleDetailsCard(conditions: List<CapsuleCondition>) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(26.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "#",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = MontserratFontFamily,
                    color = BlackText
                )
                Text(
                    text = "Capsule Details",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = MontserratFontFamily,
                    color = BlackText
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            conditions.forEach { condition ->
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = "${condition.type} Lock:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = MontserratFontFamily,
                        color = BlackText
                    )
                    Text(
                        text = condition.value,
                        fontSize = 14.sp,
                        fontFamily = MontserratFontFamily,
                        color = GrayText
                    )

                    if (condition != conditions.last()) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun WaitingAndReadyStateContent(
    data: CapsuleDetailData,
    onReadyClick: () -> Unit,
    onOpenClick: () -> Unit
) {
    val isAllConditionsMet = data.conditions.all { it.isUnlocked }
    val isUserReady = data.status == CapsuleDetailStatus.READY
    val isAllParticipantsReady = data.participants.all { it.isReady }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // 캡슐 정보 카드 (공통)
        CapsuleInfoCardWithIcon(
            title = data.capsuleInfo.title,
            from = data.capsuleInfo.from,
            openTime = data.capsuleInfo.openTime
        )

        Spacer(modifier = Modifier.height(16.dp))

        // === WAITING 상태 (Ready 버튼 누르기 전) ===
        if (!isUserReady) {
            // Ready 버튼 (모든 조건 충족 시에만 표시)
            if (isAllConditionsMet) {
                PrimaryButton(
                    text = "I'm Ready!",
                    onClick = onReadyClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 조건 카드들
            data.conditions.forEach { condition ->
                ConditionCard(
                    cardInfo = ConditionInfo(
                        type = when (condition.type.uppercase()) {
                            "LOCATION" -> ConditionType.LOCATION
                            "WEATHER" -> ConditionType.WEATHER
                            else -> ConditionType.ACTION
                        },
                        isUnlocked = condition.isUnlocked,
                        items = getConditionItems(condition)
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // === READY 상태 (Ready 버튼 누른 후) ===
        if (isUserReady) {
            // Waiting 또는 All Ready 카드
            if (!isAllParticipantsReady) {
                WaitingCard()
            } else {
                AllReadyCard(onOpenClick = onOpenClick)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Ready Progress 바
            ReadyProgressCard(
                readyCount = data.readyCount,
                totalCount = data.totalCount
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Participants 카드 (공통)
        val participants = data.participants.map {
            ParticipantInfo(nickname = it.userName, isReady = it.isReady)
        }
        ParticipantCard(participants = participants)

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun CapsuleInfoCardWithIcon(
    title: String,
    from: String,
    openTime: String
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 원형 배경 + 캡슐 아이콘
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(PurpleExtraLight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_capsulee_main),
                    contentDescription = "Capsule",
                    modifier = Modifier.size(80.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = MontserratFontFamily,
                color = BlackText,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "From $from",
                fontSize = 14.sp,
                fontFamily = MontserratFontFamily,
                color = GrayText,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = openTime,
                fontSize = 14.sp,
                fontFamily = MontserratFontFamily,
                color = GrayText,
                textAlign = TextAlign.Center
            )
        }
    }
}

// 조건별 상세 정보 생성 함수
@Composable
private fun getConditionItems(condition: CapsuleCondition): List<ConditionItem> {
    return when (condition.type.uppercase()) {
        "LOCATION" -> {
            // Location은 여러 정보를 보여줄 수 있음
            // 실제 구현에서는 서버에서 받은 데이터를 파싱해야 함
            listOf(
                ConditionItem("Current Location", "Seoul Station"),
                ConditionItem("Target Location", condition.value),
                ConditionItem("Distance", "2.3km")
            )
        }
        "WEATHER" -> {
            listOf(
                ConditionItem("Weather Lock", condition.value)
            )
        }
        else -> { // ACTION
            listOf(
                ConditionItem("Action Lock", condition.value)
            )
        }
    }
}

// ------------------------------------------------------------------------
// Page 6: Loading (캡슐 열리는 중)
// ------------------------------------------------------------------------
@Composable
private fun OpeningLoadingContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = PurplePrimary,
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Unlocking Memory...",
            fontSize = 18.sp,
            color = PurplePrimary,
            fontFamily = MontserratFontFamily
        )
    }
}

// ------------------------------------------------------------------------
// Page 7: OPENED (내용 확인)
// ------------------------------------------------------------------------
@Composable
private fun OpenedStateContent(data: CapsuleDetailData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // 캡슐 정보 카드 (아이콘 포함)
        CapsuleInfoCardWithIcon(
            title = data.capsuleInfo.title,
            from = data.capsuleInfo.from,
            openTime = data.capsuleInfo.openTime
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Opened Together (Participants)
        OpenedTogetherCard(participants = data.participants)

        Spacer(modifier = Modifier.height(16.dp))

        // Message 카드
        MessageCard(content = data.capsuleInfo.content ?: "No Content")

        Spacer(modifier = Modifier.height(16.dp))

        // Capsule Details 카드 (기존 것 재사용)
        CapsuleDetailsCard(conditions = data.conditions)

        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
fun ParticipantStatusRow(participant: CapsuleParticipant) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = participant.userName,
            fontSize = 16.sp,
            fontFamily = MontserratFontFamily,
            color = BlackText
        )
        if (participant.isReady) {
            Text(
                text = "READY",
                color = PurplePrimary,
                fontWeight = FontWeight.Bold,
                fontFamily = MontserratFontFamily
            )
        } else {
            Text(
                text = "...",
                color = GrayText,
                fontFamily = MontserratFontFamily
            )
        }
    }
}

@Composable
private fun WaitingCard() {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xBFAEEBD6)), // 연한 민트색
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Waiting...",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = MontserratFontFamily,
                color = BlackText // 진한 민트
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Waiting for everyone to get ready...",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = MontserratFontFamily,
                color = BlackText,
                textAlign = TextAlign.Center
            )
        }
    }
}

// --- All Ready 카드 ---
@Composable
private fun AllReadyCard(onOpenClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xBFF7B8CD)), // 연한 핑크
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "All Ready",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = MontserratFontFamily,
                color = BlackText
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Press the open button!!!",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = MontserratFontFamily,
                color = BlackText,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))

            PrimaryButton(
                text = "Capsule Open",
                onClick = onOpenClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
            )
        }
    }
}

// --- Ready Progress 카드 ---
@Composable
private fun ReadyProgressCard(readyCount: Int, totalCount: Int) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ready Progress",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = MontserratFontFamily,
                    color = BlackText
                )
                Text(
                    text = "$readyCount/$totalCount",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = MontserratFontFamily,
                    color = PurplePrimary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            ProgressBar(
                progress = if (totalCount > 0) readyCount.toFloat() / totalCount else 0f,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun OpenedTogetherCard(participants: List<CapsuleParticipant>) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_friend_outline),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Opened Together",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = MontserratFontFamily,
                    color = BlackText
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 가로 스크롤 레이아웃
            if (participants.size <= 4) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    participants.forEach { participant ->
                        ParticipantItem(participant.userName)
                    }
                }
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(participants) { participant ->
                        ParticipantItem(participant.userName)
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageCard(content: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_message), // 문서 아이콘
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = BlackText
                )
                Text(
                    text = "Message",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = MontserratFontFamily,
                    color = BlackText
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = content,
                fontSize = 14.sp,
                fontFamily = MontserratFontFamily,
                color = BlackText,
                lineHeight = 20.sp
            )
        }
    }
}