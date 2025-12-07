package com.example.rememory.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import android.Manifest
import android.app.Activity
import android.location.Geocoder
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withTimeoutOrNull
import java.util.Locale

@SuppressLint("MissingPermission")
suspend fun getPreciseLocation(context: Context): Location? {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    // 1. 5초 동안 위치 업데이트를 시도합니다. (시간 조절 가능)
    // withTimeoutOrNull을 사용하여 너무 오래 걸리면 null 반환 (무한 대기 방지)
    return withTimeoutOrNull(5000L) {
        callbackFlow {
            val locationRequest = com.google.android.gms.location.LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, // 우선순위
                1000L                            // 간격 (1초)
            ).build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.locations.lastOrNull()?.let { location ->
                        // 2. 정확도 필터링: 오차 범위가 30m 이내인 정확한 위치만 받음
                        // (실내 테스트라면 100m 정도로 늘려주세요)
                        if (location.accuracy != 0.0f && location.accuracy < 100f) { // 예: 100m 이내 정확도
                            trySend(location)
                        }
                    }
                }
            }

            // 위치 업데이트 시작
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

            // Flow가 닫힐 때 업데이트 제거 (메모리 누수 방지)
            awaitClose {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }.firstOrNull() // 가장 먼저 들어온 유효한 위치 하나만 받고 종료
    }
}

// 위치 권한 확인
fun hasLocationPermission(context: Context): Boolean {
    return ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

fun requestLocationPermission(activity: Activity) {
    ActivityCompat.requestPermissions(
        activity,
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
        1001
    )
}

suspend fun searchLocationByAddress(context: Context, address: String): List<Pair<String, LatLng>> {
    val geocoder = Geocoder(context)
    return try {
        geocoder.getFromLocationName(address, 5)?.map {
            val name = it.featureName ?: address
            val latLng = LatLng(it.latitude, it.longitude)
            name to latLng
        } ?: emptyList()
    } catch (e: Exception) {
        emptyList()
    }
}

fun getAddressFromLatLng(context: Context, latLng: LatLng): String? {
    return try {
        val geocoder = Geocoder(context)
        val addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        addressList?.firstOrNull()?.getAddressLine(0)
    } catch (e: Exception) {
        null
    }
}

fun getPlaceNameFromLatLng(context: Context, latLng: LatLng): String {
    return try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val results = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

        val addr = results?.firstOrNull()
        // featureName은 건물명, POI 이름, 주소 일부 등
        addr?.featureName ?: "Unknown place"
    } catch (e: Exception) {
        "Unknown place"
    }
}