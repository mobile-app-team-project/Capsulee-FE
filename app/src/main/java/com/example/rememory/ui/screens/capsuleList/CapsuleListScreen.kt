package com.example.rememory.ui.screens.capsuleList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rememory.R
import com.example.rememory.domain.model.CapsuleDomainModel
import com.example.rememory.domain.model.ConditionType
import com.example.rememory.ui.components.AppHeader
import com.example.rememory.ui.components.BigSwitch
import com.example.rememory.ui.components.TitleLogoStyle
import com.example.rememory.ui.screens.capsuleList.components.CapsuleStatsCard
import com.example.rememory.ui.theme.GrayBorder
import com.example.rememory.ui.theme.GrayText
import com.example.rememory.ui.theme.PurpleExtraLight

@Composable
fun CapsuleListScreen(
    navController: NavController,
    viewModel: CapsuleListViewModel = viewModel()
){
    val uiState by viewModel.state.collectAsState()

    Scaffold (
        topBar = {
            AppHeader(
                title = "Re:Memory",
                titleStyle = TitleLogoStyle,
                onBackClick = null,
                onPlusClick = null,
                onBellClick = {},
            )
        }
    ){ innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            CapsuleStatsCard(stats = uiState.stats)

            BigSwitch(
                leftText = "sent",
                rightText = "received",
                isLeftSelected = uiState.isSentSelected
            ) { isSent ->
                // 토글 시, ViewModel의 onTabToggle 함수 호출
                viewModel.onTabToggle(isSent)
            }

            if (uiState.isLoading) {
                // 로딩 중일 때
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Text("Loading capsules...")
                }
            } else if (uiState.errorMessage != null) {
                // 에러 발생 시
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Error: ${uiState.errorMessage}", color = Color.Red)
                }
            } else {
                // 데이터 로드 완료 시 (LazyColumn으로 변경)
                LazyColumn (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PurpleExtraLight),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.capsules) { capsule ->
                        CapsuleListItemCard(capsule)
                    }
                }
            }
        }

    }
}

@Composable
private fun CapsuleListItemCard(capsuleInfo: CapsuleDomainModel){
    val iconRes =
        if(capsuleInfo.isOpened){
            R.drawable.ic_lock_opened
        }else{
            R.drawable.ic_lock_locked
        }

    //띄울 캡슐 개수
    val maxDisplayCount = 3
    val totalCount = capsuleInfo.conditionSummary.size
    val hiddenCount = (totalCount - maxDisplayCount).coerceAtLeast(0) // 0 미만 방지

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp, top = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column (
                modifier = Modifier.widthIn(max = 200.dp)
            ){
                Text(
                    text = capsuleInfo.title,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF603886),
                    fontSize = 15.sp
                )
                Text(
                    text = capsuleInfo.relationText,
                    color = GrayText,
                    fontSize = 13.sp
                )
            }
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp, bottom = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ){
            Column {
                capsuleInfo.conditionSummary.take(maxDisplayCount).forEach { item ->
                    val iconRes =
                        when (item.type) {
                            ConditionType.TIME -> {
                                R.drawable.ic_calrender_purple
                            }
                            ConditionType.GEO -> {
                                R.drawable.ic_map_pin_heart_purple
                            }
                            else -> {
                                R.drawable.ic_action_purple
                            }
                        }
                    Row (
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Image(
                            painter = painterResource(id = iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(text = item.value, fontSize = 13.sp)
                    }
                }
            }
            if (hiddenCount > 0) {
                Text(
                    text = "$hiddenCount more",
                    color = GrayBorder,
                    fontSize = 12.sp
                )
            }
        }
    }
}
