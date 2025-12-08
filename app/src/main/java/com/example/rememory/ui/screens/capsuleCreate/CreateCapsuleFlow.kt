package com.example.rememory.ui.screens.capsuleCreate

import CreateCapsuleScreen
import Step1BasicInfo
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.rememory.domain.model.ActionCondition
import com.example.rememory.domain.model.ConditionType
import com.example.rememory.domain.model.WeatherCondition
import com.example.rememory.ui.screens.capsuleCreate.components.ActionScreen
import com.example.rememory.ui.screens.capsuleCreate.components.CreateFinalConfirm
import com.example.rememory.ui.screens.capsuleCreate.components.LocationScreen
import com.example.rememory.ui.screens.capsuleCreate.components.Step2UnlockTime
import com.example.rememory.ui.screens.capsuleCreate.components.Step3SelectConditions
import com.example.rememory.ui.screens.capsuleCreate.components.Step4RecipientSelection
import com.example.rememory.ui.screens.capsuleCreate.components.WeatherScreen

// 다음 단계 버튼 활성화 조건
fun isNextEnabled(
    step: Int,
    capsuleTitle: String,
    capsuleMessage: String,
    selectedConditions: List<ConditionType>,
    selectedWeather: WeatherCondition? = null,
    selectedAction: ActionCondition? = null,
    currentCondition: ConditionType? = null,
    selectedRecipientsCount: Int = 0,
    conditionOrder: List<ConditionType> = emptyList()
): Boolean {
    val recipientStep = 4 + conditionOrder.size

    if (step == recipientStep) {
        return selectedRecipientsCount > 0
    }

    return when (step) {
        1 -> capsuleTitle.isNotBlank() && capsuleMessage.isNotBlank()
        in 4 until recipientStep -> {
            when (currentCondition) {
                ConditionType.WEATHER -> selectedWeather != null
                ConditionType.ACTION -> selectedAction != null
                else -> true // LOCATION이나 기타 조건은 항상 true
            }
        }

        else -> true
    }
}

// 단계별 title
fun getStepTitle(step: Int, conditionOrder: List<ConditionType>): String =
    when (step) {
        1 -> "Create Your Memory"
        2 -> "When will this Memory Unlock?"
        3 -> "Choose Your Special Keys"
        in 4 until 4 + conditionOrder.size -> when (conditionOrder[step - 4]) {
            ConditionType.LOCATION -> "Tie this Memory to a Place"
            ConditionType.WEATHER -> "Set the Weather Condition"
            ConditionType.ACTION -> "Add a Action Key"
            else -> ""
        }
        4 + conditionOrder.size -> "Who is This Moment for?"
        else -> "Ready to Seal?"
    }

// 단계별 subtitle
fun getStepSubtitle(step: Int, conditionOrder: List<ConditionType>): String =
    when (step) {
        1 -> "Tell us about your memory"
        2 -> "Select the exact moment this capsule will open"
        3 -> "Mix and match to create a unique key"
        in 4 until 4 + conditionOrder.size -> when (conditionOrder[step - 4]) {
            ConditionType.LOCATION -> "Search for a place or pin a location on the map"
            ConditionType.WEATHER -> "Unlocks when this real-world event occurs"
            ConditionType.ACTION -> "This gesture will be the final key to unlock this"
            else -> ""
        }
        4 + conditionOrder.size -> "Send it just for you, or invite friends\n" +
                "to unlock it together"
        else -> "Please review your capsule before\n" + "it's sealed forever"
    }

// Next 버튼 텍스트 변경
fun getNextButtonText(step: Int, conditionOrder: List<ConditionType>): String {
    val reviewStep = 3 + conditionOrder.size + 2
    return if (step == reviewStep) "SEAL" else "NEXT"
}

// 다음 단계 계산
fun goToNextStep(step: Int, conditionOrder: List<ConditionType>): Int =
    step + 1


@Composable
fun CreateCapsuleFlow(
    navController: NavController,
    viewModel: CreateCapsuleViewModel = hiltViewModel()
) {
    var step by remember { mutableStateOf(1) }

    // Step 1 상태
    val capsuleTitle by viewModel.title.collectAsState()
    val capsuleMessage by viewModel.message.collectAsState()
    val imageFile by viewModel.imageFile.collectAsState()
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.onImageSelected(it) // ViewModel에 이미지 Uri 전달
        }
    }

    // Step 2 상태
    val selectedDate by viewModel.selectedDate.collectAsState()
    val selectedTime by viewModel.selectedTime.collectAsState()

    // Step 3 상태
    val selectedConditions by viewModel.selectedConditions.collectAsState()

    // 조건 순서 정렬 (고정 우선순위)
    val orderedConditionSteps = selectedConditions.sortedWith(compareBy {
        when (it) {
            ConditionType.LOCATION -> 1
            ConditionType.WEATHER -> 2
            ConditionType.ACTION -> 3
            else -> Int.MAX_VALUE
        }
    })

    // 총 단계 수 계산
    val totalSteps = 3 + orderedConditionSteps.size + 2
    // 1: 기본정보, 2: 날짜, 3: 조건선택, 4~n: 조건 상세, n+1: 수신자 선택, n+2: 확인 및 최종 제출

    val currentCondition: ConditionType? =
        if (step in 4 until 4 + orderedConditionSteps.size)
            orderedConditionSteps[step - 4]
        else null

    val selectedRecipientsCount =
        viewModel.recipients.collectAsState().value.count { it.selected }

    CreateCapsuleScreen(
        title = getStepTitle(step, orderedConditionSteps),
        subtitle = getStepSubtitle(step, orderedConditionSteps),
        progress = step.toFloat() / totalSteps,
        showPrevious = true,
        previousText = "PREVIOUS",
        nextText = getNextButtonText(step, orderedConditionSteps),
        rightEnabled = isNextEnabled(
            step = step,
            capsuleTitle = capsuleTitle,
            capsuleMessage = capsuleMessage,
            selectedConditions = selectedConditions,
            selectedWeather = viewModel.selectedWeather.collectAsState().value,
            selectedAction = viewModel.selectedAction.collectAsState().value,
            currentCondition = currentCondition,
            selectedRecipientsCount = selectedRecipientsCount,   // ⬅ 추가
            conditionOrder = orderedConditionSteps
        ),
        isSingleButton = false,
        onPrevious = {
            if (step > 1) {
                step--
            } else {
                navController.popBackStack() // step == 1일 때 뒤로 가기
            }
        },
        showBottomBar = currentCondition != ConditionType.LOCATION,
        onNext = {
            val reviewStep = 3 + orderedConditionSteps.size + 2
            if (step == reviewStep) {
                // 마지막 단계에서 submit 호출
                viewModel.submitCapsule(
                    onSuccess = {
                        // 예: 캡슐 목록으로 이동
                        navController.navigate("capsule_create_complete") {
                            popUpTo("create_capsule_flow") { inclusive = true }
                        }
                    },
                    onFailure = { error ->
                        // 예: 에러 메시지 출력 (임시용)
                        Log.e("CreateCapsule", "Failed to create capsule", error)
                    }
                )
            } else {
                step = goToNextStep(step, orderedConditionSteps)
            }
        }
    ) {

        when (step) {

            1 -> Step1BasicInfo(
                title = capsuleTitle,
                message = capsuleMessage,
                imageFile = imageFile,
                onTitleChange = { newTitle -> viewModel.onTitleChange(newTitle) },
                onMessageChange = { newMessage -> viewModel.onMessageChange(newMessage) },
                onImageSelected = { imagePickerLauncher.launch("image/*") }
            )

            2 -> Step2UnlockTime(
                selectedDate = selectedDate,
                selectedTime = selectedTime,
                onDateChange = { viewModel.onDateSelected(it) },
                onTimeChange = { viewModel.onTimeSelected(it) }
            )

            3 -> Step3SelectConditions(
                selectedConditions = selectedConditions,
                onToggle = { condition -> viewModel.toggleCondition(condition) }
            )

            in 4 until 4 + orderedConditionSteps.size -> {
                val currentCondition = orderedConditionSteps[step - 4]

                when (currentCondition) {
                    ConditionType.LOCATION -> LocationScreen(
                        onNextClicked = { step = goToNextStep(step, orderedConditionSteps) },
                        onPreviousClicked = { step-- },
                        onUseCurrentLocation = { latLng ->
                            // 지도 위치 이동 처리에만 사용됨
                        },
                        onLocationSelected = { selectedLocation ->
                            viewModel.setSelectedLocation(selectedLocation)
                        }
                    )
                    ConditionType.WEATHER -> WeatherScreen(
                        selectedWeather = viewModel.selectedWeather.collectAsState().value,
                        onWeatherSelected = { viewModel.setSelectedWeather(it) }
                    )

                    ConditionType.ACTION -> ActionScreen(
                        selectedAction = viewModel.selectedAction.collectAsState().value,
                        onActionSelected = { viewModel.setSelectedAction(it) }
                    )
                    else -> {}
                }
            }

            // 수신자 선택 단계
            4 + orderedConditionSteps.size -> Step4RecipientSelection(viewModel = viewModel)

            // 마지막 확인 단계
            else -> CreateFinalConfirm(viewModel)
        }
    }
}