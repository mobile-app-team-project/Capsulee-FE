package com.example.rememory.ui.screens.capsuleCreate.components

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rememory.R
import com.example.rememory.domain.model.SelectedLocation
import com.example.rememory.ui.theme.GrayText
import com.example.rememory.ui.theme.PurplePrimary
import com.example.rememory.util.getAddressFromLatLng
import com.example.rememory.util.getPlaceNameFromLatLng
import com.example.rememory.util.getPreciseLocation
import com.example.rememory.util.searchLocationByAddress
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch

@Composable
fun LocationScreen(
    onNextClicked: () -> Unit,
    onPreviousClicked: () -> Unit,
    onUseCurrentLocation: (LatLng) -> Unit,
    onLocationSelected: (SelectedLocation) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<Pair<String, LatLng>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState()
    var selectedLatLng by remember { mutableStateOf<LatLng?>(null) }
    var selectedName by remember { mutableStateOf<String?>(null) }
    var selectedAddress by remember { mutableStateOf<String?>(null) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            coroutineScope.launch {
                isLoading = true
                val location = getPreciseLocation(context)
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)

                    val address = getAddressFromLatLng(context, latLng)
                    val placeName = getPlaceNameFromLatLng(context, latLng)

                    selectedName = placeName
                    selectedAddress = address
                    selectedLatLng = latLng

                    cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
                    onUseCurrentLocation(latLng)
                }
                isLoading = false
            }
        } else {
            Toast.makeText(context, "위치 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // 검색창
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                coroutineScope.launch {
                    searchResults = searchLocationByAddress(context, searchQuery)
                }
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search Icon"
                )
            },
            placeholder = { Text("Search by a place or address") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    coroutineScope.launch {
                        searchResults = searchLocationByAddress(context, searchQuery)
                    }
                }
            ),
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        )

        // 검색 결과 리스트
        if (searchResults.isNotEmpty()) {
            Column {
                searchResults.take(5).forEach { (name, latLng) ->
                    ListItem(
                        headlineContent = { Text(name) },
                        supportingContent = { Text(getAddressFromLatLng(context, latLng) ?: "") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val addr = getAddressFromLatLng(context, latLng)
                                val placeName = name.ifBlank { getPlaceNameFromLatLng(context, latLng) }

                                selectedLatLng = latLng
                                selectedName = placeName
                                selectedAddress = addr

                                coroutineScope.launch {
                                    cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
                                }

                                searchResults = emptyList()
                            }
                    )
                    Divider()
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 현재 위치 버튼
        Button(
            onClick = {
                locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            },
            enabled = !isLoading,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE5E7EB),
                contentColor = GrayText
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_my_location),
                contentDescription = "Current Location",
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text="Use current location",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 지도
        Box(modifier = Modifier.weight(1f)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(zoomControlsEnabled = false)
            ) {
                selectedLatLng?.let {
                    Marker(
                        state = MarkerState(position = it),
                        title = selectedName ?: "Selected Location"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 하단 정보 카드
        selectedLatLng?.let {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = selectedName ?: "",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = selectedAddress ?: "",
                    color = GrayText,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Please confirm the pin and address are correct",
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFDECEA), RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    color = Color(0xFFB91C1C)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        selectedLatLng?.let {
                            onLocationSelected(
                                SelectedLocation(
                                    name = selectedName ?: "",
                                    address = selectedAddress ?: "",
                                    lat = it.latitude,
                                    long = it.longitude
                                )
                            )
                            onNextClicked() // 다음 단계로 이동
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text="Set this Location",
                        style=MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}