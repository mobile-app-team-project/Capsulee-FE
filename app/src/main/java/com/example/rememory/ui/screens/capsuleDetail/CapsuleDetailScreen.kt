package com.example.rememory.ui.screens.capsuleDetail

import android.Manifest
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaRecorder
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.rememory.R
import com.example.rememory.domain.model.CapsuleCondition
import com.example.rememory.domain.model.CapsuleDetailData
import com.example.rememory.domain.model.CapsuleDetailStatus
import com.example.rememory.domain.model.CapsuleParticipant
import com.example.rememory.ui.components.*
import com.example.rememory.ui.theme.*
import com.example.rememory.util.getPreciseLocation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sqrt

@Composable
fun CapsuleDetailScreen(
    navController: NavController,
    capsuleId: Int,
    viewModel: CapsuleDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // 위치 권한 요청 런처
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            coroutineScope.launch {
//                val location = getPreciseLocation(context)
//                location?.let {
//                    viewModel.checkLocationCondition(it.latitude, it.longitude)
//                }
                viewModel.checkLocationCondition(37.50508, 126.95706)
            }
        }
    }

    // 마이크 권한 요청 런처 (소리 감지용)
    val audioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // 소리 감지 시작
            startSoundDetection(context) { detected ->
                if (detected) {
                    viewModel.checkActionCondition("SOUND")
                }
            }
        }
    }

    LaunchedEffect(capsuleId) {
        viewModel.loadCapsuleDetail(capsuleId)
    }

    Scaffold(
        topBar = {
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
                    if (uiState.isOpening) {
                        OpeningLoadingContent()
                    } else {
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
                                    onOpenClick = { viewModel.onOpenCapsuleClick() },
                                    onConditionClick = { condition ->
                                        if (!condition.isUnlocked) {
                                            when (condition.type.uppercase()) {
                                                "LOCATION", "WEATHER" -> {
                                                    // 위치 권한 확인 후 실제 위치 사용
//                                                    locationPermissionLauncher.launch(
//                                                        Manifest.permission.ACCESS_FINE_LOCATION
//                                                    )
                                                    viewModel.checkLocationCondition(37.50508, 126.95706)
                                                }
                                                "ACTION" -> {
                                                    // 행동 조건 값 파싱
                                                    val actionValue = condition.value.uppercase()
                                                    when {
                                                        actionValue.contains("SHAKE") || actionValue.contains("SHAKING") -> {
                                                            // 흔들기 감지 시작
                                                            startShakeDetection(context, viewModel)
                                                        }
                                                        actionValue.contains("SOUND") -> {
                                                            // 소리 감지 (마이크 권한 필요)
                                                            audioPermissionLauncher.launch(
                                                                Manifest.permission.RECORD_AUDIO
                                                            )
                                                        }
                                                        actionValue.contains("TAP") -> {
                                                            // 탭 감지는 화면에서 직접 처리
                                                            // (아래 TapDetectionOverlay 사용)
                                                        }
                                                        actionValue.contains("COMPASS") || actionValue.contains("NORTH") -> {
                                                            // 방향 감지
                                                            startCompassDetection(context, viewModel)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    },
                                    context = context,
                                    viewModel = viewModel
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

// 흔들기 감지
@Composable
fun rememberShakeDetection(
    context: Context,
    onShakeDetected: () -> Unit
) {
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    val accelerometer = remember { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }

    val shakeListener = remember {
        object : SensorEventListener {
            private var lastUpdate: Long = 0
            private var lastX = 0f
            private var lastY = 0f
            private var lastZ = 0f
            private var shakeCount = 0
            private val SHAKE_THRESHOLD = 15f
            private val TIME_THRESHOLD = 500L

            override fun onSensorChanged(event: SensorEvent) {
                val currentTime = System.currentTimeMillis()

                if (currentTime - lastUpdate > 100) {
                    val diffTime = currentTime - lastUpdate
                    lastUpdate = currentTime

                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]

                    val speed = sqrt(
                        ((x - lastX) * (x - lastX) +
                                (y - lastY) * (y - lastY) +
                                (z - lastZ) * (z - lastZ)).toDouble()
                    ) / diffTime * 10000

                    if (speed > SHAKE_THRESHOLD) {
                        shakeCount++
                        if (shakeCount >= 3) {
                            onShakeDetected()
                            shakeCount = 0
                        }
                    }

                    lastX = x
                    lastY = y
                    lastZ = z
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    return DisposableEffect(Unit) {
        accelerometer?.let {
            sensorManager.registerListener(
                shakeListener,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        onDispose {
            sensorManager.unregisterListener(shakeListener)
        }
    }
}

fun startShakeDetection(context: Context, viewModel: CapsuleDetailViewModel) {
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    var lastUpdate: Long = 0
    var lastX = 0f
    var lastY = 0f
    var lastZ = 0f
    var shakeCount = 0

    val shakeListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val currentTime = System.currentTimeMillis()

            if (currentTime - lastUpdate > 100) {
                val diffTime = currentTime - lastUpdate
                lastUpdate = currentTime

                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val speed = sqrt(
                    ((x - lastX) * (x - lastX) +
                            (y - lastY) * (y - lastY) +
                            (z - lastZ) * (z - lastZ)).toDouble()
                ) / diffTime * 10000

                if (speed > 15f) {
                    shakeCount++
                    if (shakeCount >= 3) {
                        viewModel.checkActionCondition("SHAKE")
                        sensorManager.unregisterListener(this)
                        shakeCount = 0
                    }
                }

                lastX = x
                lastY = y
                lastZ = z
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    accelerometer?.let {
        sensorManager.registerListener(
            shakeListener,
            it,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }
}

// 소리 감지
fun startSoundDetection(context: Context, onSoundDetected: (Boolean) -> Unit) {
    try {
        val mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile("${context.cacheDir}/temp_audio.3gp")
            prepare()
            start()
        }

        // 2초 후 진폭 측정
        Thread {
            Thread.sleep(2000)
            val amplitude = mediaRecorder.maxAmplitude
            mediaRecorder.stop()
            mediaRecorder.release()

            // 진폭이 일정 수준 이상이면 소리 감지
            onSoundDetected(amplitude > 5000)
        }.start()

    } catch (e: Exception) {
        e.printStackTrace()
        onSoundDetected(false)
    }
}

// 나침반 (방향) 감지
fun startCompassDetection(context: Context, viewModel: CapsuleDetailViewModel) {
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    val gravity = FloatArray(3)
    val geomagnetic = FloatArray(3)

    val compassListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            when (event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    System.arraycopy(event.values, 0, gravity, 0, event.values.size)
                }
                Sensor.TYPE_MAGNETIC_FIELD -> {
                    System.arraycopy(event.values, 0, geomagnetic, 0, event.values.size)
                }
            }

            val R = FloatArray(9)
            val I = FloatArray(9)

            if (SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(R, orientation)

                // 방위각 계산 (라디안을 각도로 변환)
                val azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()

                // 북쪽을 향하면 (-10도 ~ +10도 범위)
                if (azimuth in -10f..10f || azimuth in 350f..360f) {
                    viewModel.checkActionCondition("COMPASS")
                    sensorManager.unregisterListener(this)
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    magnetometer?.let {
        sensorManager.registerListener(compassListener, it, SensorManager.SENSOR_DELAY_NORMAL)
    }
    accelerometer?.let {
        sensorManager.registerListener(compassListener, it, SensorManager.SENSOR_DELAY_NORMAL)
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

        ProcessCard(processPercent = data.capsuleInfo.processPercent ?: 0)

        Spacer(modifier = Modifier.height(16.dp))

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

        ParticipantsCard(participants = data.participants)

        Spacer(modifier = Modifier.height(16.dp))

        CapsuleDetailsCard(conditions = data.conditions)

        Spacer(modifier = Modifier.height(16.dp))
    }
}

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
        Box(
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_capsulee_main),
                contentDescription = "Capsule",
                modifier = Modifier.size(280.dp)
            )

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
private fun ParticipantItem(userName: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
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

        Text(
            text = userName,
            fontSize = 14.sp,
            fontFamily = MontserratFontFamily,
            color = BlackText,
            textAlign = TextAlign.Center
        )
    }
}

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

            conditions
                .filterNot { condition -> condition.type == "TIME" }
                .forEach { condition ->
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

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

        }
    }
}

@Composable
private fun WaitingAndReadyStateContent(
    data: CapsuleDetailData,
    onReadyClick: () -> Unit,
    onOpenClick: () -> Unit,
    onConditionClick: (CapsuleCondition) -> Unit,
    context: Context,
    viewModel: CapsuleDetailViewModel
) {
    val isAllConditionsMet = data.conditions.isEmpty() || data.conditions.all { it.isUnlocked }
    val isUserReady = data.status == CapsuleDetailStatus.READY
    val isAllParticipantsReady = data.participants.all { it.isReady }
    val isSolo = data.participants.size == 1

    // TAP 조건 감지
    var tapCount by remember { mutableStateOf(0) }
    var tapCondition by remember { mutableStateOf<CapsuleCondition?>(null) }

    LaunchedEffect(data.conditions) {
        tapCondition = data.conditions.find {
            it.type.uppercase() == "ACTION" &&
                    it.value.uppercase().contains("TAP") &&
                    !it.isUnlocked
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (tapCondition != null) {
                    Modifier.pointerInput(Unit) {
                        detectTapGestures {
                            tapCount++
                            if (tapCount >= 3) {
                                viewModel.checkActionCondition("TAP")
                                tapCount = 0
                            }
                        }
                    }
                } else Modifier
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            CapsuleInfoCardWithIcon(
                title = data.capsuleInfo.title,
                from = data.capsuleInfo.from,
                openTime = data.capsuleInfo.openTime,
                isOpened = data.status == CapsuleDetailStatus.OPENED,
                imageUrl = data.capsuleInfo.imageUrl
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isSolo && isAllConditionsMet && !isUserReady) {
                AllReadyCard(onOpenClick = onOpenClick)
                Spacer(modifier = Modifier.height(16.dp))
            }
            else {
                if (!isUserReady) {
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

                    if (data.conditions.isNotEmpty()) {
                        data.conditions.forEach { condition ->
                            ConditionCard(
                                cardInfo = ConditionInfo(
                                    type = when (condition.type.uppercase()) {
                                        "LOCATION" -> ConditionType.LOCATION
                                        "WEATHER" -> ConditionType.WEATHER
                                        "ACTION" -> ConditionType.ACTION
                                        "TIME" -> return@forEach // TIME은 건너뜀
                                        else -> return@forEach // 알 수 없는 타입도 건너뜀
                                    },
                                    isUnlocked = condition.isUnlocked,
                                    items = getConditionItems(condition)
                                ),
                                onClick = { onConditionClick(condition) }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }

            if (isUserReady) {
                if (!isAllParticipantsReady) {
                    WaitingCard()
                } else {
                    AllReadyCard(onOpenClick = onOpenClick)
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (!isSolo) {
                    ReadyProgressCard(
                        readyCount = data.readyCount,
                        totalCount = data.totalCount
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (!isSolo) {
                val participants = data.participants.map {
                    ParticipantInfo(nickname = it.userName, isReady = it.isReady)
                }
                ParticipantCard(participants = participants)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // TAP 카운터 표시
        if (tapCondition != null && tapCount > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(PurplePrimary.copy(alpha = 0.9f), RoundedCornerShape(50))
                    .padding(24.dp)
            ) {
                Text(
                    text = "$tapCount / 3",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun CapsuleInfoCardWithIcon(
    title: String,
    from: String,
    openTime: String,
    isOpened: Boolean,
    imageUrl: String? = null,
    onImageClick: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        Log.d("CapsuleDetail", "imageUrl = ${imageUrl}")
    }
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
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(PurpleExtraLight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isOpened && !imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Capsule Photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .clickable { onImageClick() }
                            .background(Color.Gray) // 이미지 안 뜨는지 확인용
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_capsulee_main),
                        contentDescription = "Capsule",
                        modifier = Modifier.size(80.dp)
                    )
                }
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

@Composable
private fun getConditionItems(condition: CapsuleCondition): List<ConditionItem> {
    return when (condition.type.uppercase()) {
        "LOCATION" -> {
            listOf(
                ConditionItem("Target Location", condition.value)
            )
        }
        "WEATHER" -> {
            listOf(
                ConditionItem("Weather Lock", condition.value)
            )
        }
        else -> {
            listOf(
                ConditionItem("Action Lock", condition.value)
            )
        }
    }
}

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

@Composable
private fun OpenedStateContent(data: CapsuleDetailData) {
    var isImageViewerVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        CapsuleInfoCardWithIcon(
            title = data.capsuleInfo.title,
            from = data.capsuleInfo.from,
            openTime = data.capsuleInfo.openTime,
            isOpened = data.status == CapsuleDetailStatus.OPENED,
            imageUrl = data.capsuleInfo.imageUrl,
            onImageClick = { isImageViewerVisible = true }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OpenedTogetherCard(participants = data.participants)

        Spacer(modifier = Modifier.height(16.dp))

        MessageCard(content = data.capsuleInfo.content ?: "No Content")

        Spacer(modifier = Modifier.height(16.dp))

        CapsuleDetailsCard(conditions = data.conditions)

        Spacer(modifier = Modifier.height(16.dp))
    }

    if (isImageViewerVisible && !data.capsuleInfo.imageUrl.isNullOrBlank()) {
        FullscreenImageViewer(
            imagePath = data.capsuleInfo.imageUrl,
            onDismiss = { isImageViewerVisible = false }
        )
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
        colors = CardDefaults.cardColors(containerColor = Color(0xBFAEEBD6)),
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
                color = BlackText
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

@Composable
private fun AllReadyCard(onOpenClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xBFF7B8CD)),
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
                    painter = painterResource(id = R.drawable.ic_message),
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

@Composable
fun FullscreenImageViewer(
    imagePath: String,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberImagePainter(data = imagePath),
            contentDescription = "Fullscreen Image",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}