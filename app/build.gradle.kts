plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("kotlin-kapt") // Kapt 플러그인 추가 (Hilt 프로세서 사용)
    id("com.google.dagger.hilt.android") // Hilt 플러그인 적용
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
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation("androidx.navigation:navigation-compose:2.9.6")
    implementation(libs.androidx.compose.ui.text)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Retrofit (HTTP Client)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Retrofit Converter: JSON 데이터를 Kotlin 객체로 변환하기 위해 필요합니다.
    //    (Kotlinx Serialization 또는 Gson 중 하나를 선택합니다. 여기서는 Gson 예시)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Kotlin Coroutines 지원 (Retrofit에서 suspend 함수를 사용할 수 있게 함)
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Hilt Dependencies
    implementation("com.google.dagger:hilt-android:2.51")
    kapt("com.google.dagger:hilt-compiler:2.51") // ✅ Annotation Processor

    // Compose Navigation과의 통합을 위해 필요
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0") // ✅ Hilt Compose

    // ViewModel에 Hilt를 사용하기 위해 필요
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")
    kapt("androidx.hilt:hilt-compiler:1.2.0") // ✅ Hilt 컴파일러
}