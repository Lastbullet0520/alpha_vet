plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.alpha_vet"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.alpha_vet"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }


}

dependencies {
    // 프로젝트 복사 과정 나중에 삭제할 수도 있음
    implementation("com.google.android.material:material:1.10.0") // 최신 버전을 사용하세요
    implementation("androidx.appcompat:appcompat:1.6.1")

    implementation("com.kakao.sdk:v2-all:2.20.3") // 전체 모듈 설치, 2.11.0 버전부터 지원
    implementation("com.kakao.sdk:v2-user:2.20.3") // 카카오 로그인 API 모듈
    implementation("com.kakao.sdk:v2-share:2.20.3") // 카카오톡 공유 API 모듈
    implementation("com.kakao.sdk:v2-talk:2.20.3") // 카카오톡 채널, 카카오톡 소셜, 카카오톡 메시지 API 모듈
    implementation("com.kakao.sdk:v2-friend:2.20.3") // 피커 API 모듈
    implementation("com.kakao.sdk:v2-navi:2.20.3") // 카카오내비 API 모듈
    implementation("com.kakao.sdk:v2-cert:2.20.3") // 카카오톡 인증 서비스 API 모듈
    implementation("com.kakao.maps.open:android:2.11.9") // 카카오 지도 API 모듈

    // Coil의 기본 의존성
    implementation("io.coil-kt:coil:2.6.0")
    // 만약 Jetpack Compose를 사용하고 있다면 추가적으로 아래 의존성을 추가하세요.
    implementation("io.coil-kt:coil-compose:2.6.0")

    // OkHttp: HTTP 클라이언트 라이브러리, API 호출을 위해 사용
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    // Kotlin Coroutines Core: 코루틴을 사용하여 비동기 프로그래밍을 지원하는 라이브러리
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    // Kotlin Coroutines Android: Android에서 코루틴을 사용할 수 있게 해주는 라이브러리 (메인 스레드에서의 안전한 비동기 작업 지원)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")

    // Material Icons (최신 버전으로 대체)
    implementation("androidx.compose.material:material:1.6.8")

    // Other dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)

    // Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debug dependencies
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
