package com.example.rememory.ui.screens.capsuleCreate

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rememory.data.remote.dto.ConditionDto
import com.example.rememory.data.remote.dto.CreateCapsuleRequest
import com.example.rememory.domain.model.ActionCondition
import com.example.rememory.domain.model.ConditionType
import com.example.rememory.domain.model.Recipient
import com.example.rememory.domain.model.SelectedLocation
import com.example.rememory.domain.model.WeatherCondition
import com.example.rememory.domain.repository.CapsuleRepository
import com.example.rememory.domain.repository.FriendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@HiltViewModel
class CreateCapsuleViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val friendRepository: FriendRepository,
    private val capsuleRepository: CapsuleRepository
) : ViewModel() {

    // 캡슐 제목 상태 관리
    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    // 캡슐 메시지 상태 관리
    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()

    // 이미지 파일 상태 관리
    private val _imageFile = MutableStateFlow<File?>(null)
    val imageFile: StateFlow<File?> = _imageFile.asStateFlow()

    // UI에서 제목이 변경될 때 호출할 함수
    fun onTitleChange(newTitle: String) {
        _title.value = newTitle
    }

    // UI에서 메시지가 변경될 때 호출할 함수
    fun onMessageChange(newMessage: String) {
        _message.value = newMessage
    }

    // 이미지 선택이 완료되었을 때 Uri를 받아 File로 변환하고 상태를 업데이트하는 함수
    fun onImageSelected(uri: Uri) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
        inputStream?.use { input ->
            file.outputStream().use { output -> input.copyTo(output) }
        }
        _imageFile.value = file
    }

    // Step2
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    private val _selectedTime = MutableStateFlow<LocalTime?>(null)
    val selectedTime: StateFlow<LocalTime?> = _selectedTime

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }

    fun onTimeSelected(time: LocalTime?) {
        _selectedTime.value = time
    }

    // Step3 - 선택된 조건들
    private val _selectedConditions = MutableStateFlow<List<ConditionType>>(emptyList())
    val selectedConditions: StateFlow<List<ConditionType>> = _selectedConditions.asStateFlow()

    fun toggleCondition(condition: ConditionType) {
        _selectedConditions.value =
            if (_selectedConditions.value.contains(condition)) {
                _selectedConditions.value - condition
            } else {
                _selectedConditions.value + condition
            }
    }

    fun clearConditions() {
        _selectedConditions.value = emptyList()
    }

    // Step 4
    // 위치 조건
    private val _selectedLocation = MutableStateFlow<SelectedLocation?>(null)
    val selectedLocation: StateFlow<SelectedLocation?> = _selectedLocation

    fun setSelectedLocation(location: SelectedLocation) {
        _selectedLocation.value = location
    }

    // 날씨 조건
    private val _selectedWeather = MutableStateFlow<WeatherCondition?>(null)
    val selectedWeather: StateFlow<WeatherCondition?> = _selectedWeather

    fun setSelectedWeather(condition: WeatherCondition?) {
        _selectedWeather.value = condition
    }

    // 행동 조건
    private val _selectedAction = MutableStateFlow<ActionCondition?>(null)
    val selectedAction: StateFlow<ActionCondition?> = _selectedAction

    fun setSelectedAction(action: ActionCondition) {
        _selectedAction.value = action
    }

    // Step 4 - 수신자 선택
    private val _recipients = MutableStateFlow<List<Recipient>>(emptyList())
    val recipients: StateFlow<List<Recipient>> = _recipients.asStateFlow()

    fun setRecipients(list: List<Recipient>) {
        _recipients.value = list
    }

    fun toggleRecipient(id: Int) {
        _recipients.value = _recipients.value.map {
            if (it.id == id) it.copy(selected = !it.selected) else it
        }
    }

    fun getSelectedRecipientIds(): List<Int> {
        return _recipients.value.filter { it.selected }.map { it.id }
    }

    fun fetchFriendList() {
        viewModelScope.launch {
            try {
                val friends = friendRepository.getFriendList()
                val mapped = friends.map {
                    Recipient(
                        id = it.userId,
                        username = it.username,
                        loginId = it.userLoginId,
                        selected = false
                    )
                }
                _recipients.value = mapped
            } catch (e: Exception) {
                e.printStackTrace()
                // 에러 처리 (예: 메시지 표시, 로그 출력 등)
            }
        }
    }

    fun submitCapsule(
        onSuccess: () -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val title = _title.value
                val content = _message.value
                val openTime = LocalDateTime.of(
                    _selectedDate.value,
                    _selectedTime.value ?: LocalTime.MIDNIGHT
                ).toString()

                val recipientIds = _recipients.value.filter { it.selected }.map { it.id }

                val conditions = mutableListOf<ConditionDto>()

                _selectedLocation.value?.let {
                    conditions.add(
                        ConditionDto(
                            type = "LOCATION",
                            value = "${it.lat}, ${it.long}, ${it.name}"
                        )
                    )
                }

                _selectedWeather.value?.let {
                    conditions.add(
                        ConditionDto(
                            type = "WEATHER",
                            value = it.name
                        )
                    )
                }

                _selectedAction.value?.let {
                    conditions.add(
                        ConditionDto(
                            type = "ACTION",
                            value = it.label
                        )
                    )
                }

                val payload = mapOf(
                    "title" to title,
                    "content" to content,
                    "openTime" to openTime,
                    "recipientIds" to recipientIds,
                    "conditions" to conditions
                )

                val request = CreateCapsuleRequest(
                    title = title,
                    content = content,
                    openTime = openTime,
                    recipientIds = recipientIds,
                    conditions = conditions
                )

                capsuleRepository.createCapsule(request, _imageFile.value)
                onSuccess()
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }
}