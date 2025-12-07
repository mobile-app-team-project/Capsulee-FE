package com.example.rememory.ui.screens.capsuleCreate

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.example.rememory.domain.model.ConditionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.time.LocalDate
import java.time.LocalTime

class CreateCapsuleViewModel(application: Application) : AndroidViewModel(application) {

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
        val context = getApplication<Application>().applicationContext
        // contentResolver를 사용해 Uri로부터 InputStream을 얻음
        val inputStream = context.contentResolver.openInputStream(uri)
        // 앱의 캐시 디렉토리에 임시 파일 생성
        val file = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")

        // InputStream의 내용을 파일에 복사
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        _imageFile.value = file // 파일 상태 업데이트
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
}