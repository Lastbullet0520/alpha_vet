package com.example.alpha_vet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.kakao.vectormap.KakaoMapSdk

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // KakaoMapSdk 초기화
        KakaoMapSdk.init(this, "45acaf256ea22896ae5df77e36cebc2f") // 여기에 카카오 네이티브 앱 키를 넣어야 합니다.
        enableEdgeToEdge()
        setContent {
            MyApp {
                val hospitalLocations = remember {
                    listOf(
                        Location(
                            name = "서울병원",
                            latitude = 37.5665,
                            longitude = 126.9780,
                            imageUrl = "https://via.placeholder.com/400x300.png?text=Seoul+Hospital",
                            detailAddress = "서울특별시 중구 세종대로 110"
                        ),
                        Location(
                            name = "부산병원",
                            latitude = 35.1796,
                            longitude = 129.0756,
                            imageUrl = "https://via.placeholder.com/400x300.png?text=Busan+Hospital",
                            detailAddress = "부산광역시 중구 중앙대로 120번길 1"
                        ),
                        Location(
                            name = "대구병원",
                            latitude = 35.8714,
                            longitude = 128.6014,
                            imageUrl = "https://via.placeholder.com/400x300.png?text=Daegu+Hospital",
                            detailAddress = "대구광역시 중구 국채보상로 541"
                        )
                    )
                }
                // 지도 컴포저블을 호출하여 화면에 표시
                KakaoMapScreen(
                    modifier = Modifier.fillMaxSize(),
                    locations = hospitalLocations
                )
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}