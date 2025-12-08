import java.util.Properties

// [1] local.properties에서 API 키 가져오기
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}
val mapsApiKey = localProperties.getProperty("MAPS_API_KEY") ?: ""

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("kotlin-kapt") // Kapt 플러그인 (Hilt/Room 사용 시 필수)
    id("com.google.dagger.hilt.android") // Hilt 플러그인
}

// [2] 중복 클래스 오류 해결 (intellij annotations 충돌 방지)
configurations.all {
    exclude(group = "com.intellij", module = "annotations")
}

android {
    namespace = "com.example.rememory"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.rememory"
        minSdk = 34
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // [3] API 키 설정: 코드(BuildConfig)와 매니페스트 양쪽에서 사용 가능하도록 설정
        buildConfigField("String", "MAPS_API_KEY", "\"$mapsApiKey\"")
        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true // BuildConfig 사용 활성화
    }

}

dependencies {
    // Android 기본 라이브러리
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation("androidx.navigation:navigation-compose:2.9.6")

    // [수정] Places 라이브러리 (중복 제거 후 버전 명시)
    implementation("com.google.android.libraries.places:places:3.3.0")
    implementation(libs.androidx.compose.foundation)

    // 테스트 관련
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Room 라이브러리
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // Retrofit (네트워크)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Coil (이미지 로딩)
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Hilt (의존성 주입)
    implementation("com.google.dagger:hilt-android:2.55")
    kapt("com.google.dagger:hilt-compiler:2.55")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // ViewModel용 컴파일러 추가
    kapt("androidx.hilt:hilt-compiler:1.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")

    // 구글 맵
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.maps.android:maps-compose:4.1.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
}