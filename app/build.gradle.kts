plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // 1. Retrofit (HTTP Client)
    implementation("com.squareup.retrofit2:retrofit:3.0.0")

    // 2. Retrofit Converter: JSON 데이터를 Kotlin 객체로 변환하기 위해 필요합니다.
    //    (Kotlinx Serialization 또는 Gson 중 하나를 선택합니다. 여기서는 Gson 예시)
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")

    // 3. Kotlin Coroutines 지원 (Retrofit에서 suspend 함수를 사용할 수 있게 함)
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization:3.0.0") // 만약 Kotlinx Serialization을 쓴다면

    // 4. (선택) OkHttp 로깅 인터셉터 (API 요청/응답 디버깅 시 유용)
    implementation("com.squareup.okhttp3:logging-interceptor:5.3.2")

}