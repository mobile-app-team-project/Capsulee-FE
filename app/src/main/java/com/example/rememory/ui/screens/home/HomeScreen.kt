package com.example.rememory.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rememory.R
import com.example.rememory.domain.model.CapsuleStatus
import com.example.rememory.ui.components.AppHeader
import com.example.rememory.ui.components.PrimaryButton
import com.example.rememory.ui.components.TitleLogoStyle
import com.example.rememory.ui.theme.*

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        Spacer(modifier = Modifier.statusBarsPadding())

        AppHeader(
            title = "Re:Memory",
            titleStyle = TitleLogoStyle,
            onBackClick = null,
            onPlusClick = null,
            onBellClick = {
                // TODO: 알림 화면으로 이동
            }
        )

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PurplePrimary)
            }
        } else {
            uiState.homeData?.let { data ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(70.dp))

                    Text(
                        text = data.capsuleInfo.title,
                        fontSize = 32.sp,
                        fontFamily = MontserratFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = PurpleText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 36.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 32.sp
                    )

                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(
                                id = if (data.status == CapsuleStatus.LOCKED) {
                                    R.drawable.ic_capsulee_main
                                } else {
                                    R.drawable.ic_capsulee_open
                                }
                            ),
                            contentDescription = "Capsule",
                            modifier = Modifier.size(320.dp)
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.offset(y = 70.dp)
                        ) {
                            Text(
                                text = "${uiState.remainingDays} Days",
                                fontSize = 32.sp,
                                fontFamily = MontserratFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "${uiState.remainingTime}:${String.format("%02d", uiState.remainingSeconds)}",
                                fontSize = 28.sp,
                                fontFamily = MontserratFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    PrimaryButton(
                        text = "Create Capsule",
                        onClick = {
                            viewModel.onCreateCapsuleClick()
                        },
                        modifier = Modifier
                            .width(260.dp)
                            .height(58.dp),
                        shape = RoundedCornerShape(28.dp),
                        textStyle = TextStyle(
                            fontFamily = MontserratFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 24.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun HomeScreenPreview() {
    ReMemoryTheme {
        HomeScreen(navController = rememberNavController())
    }
}