package com.example.alpha_vet

import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberAsyncImagePainter
import com.kakao.vectormap.*
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.*

// Location 데이터 클래스: 병원 위치 정보를 저장
data class Location(
    val name: String,          // 병원 이름
    val latitude: Double,      // 병원의 위도
    val longitude: Double,     // 병원의 경도
    val imageUrl: String,      // 병원 이미지를 표시할 URL
    val detailAddress: String  // 병원의 상세 주소
)

@Composable
fun KakaoMapScreen(
    modifier: Modifier = Modifier,
    locations: List<Location> // 병원 위치 목록을 전달받음
) {
    // Context와 MapView를 기억
    val localContext = LocalContext.current
    val mapView = remember { MapView(localContext) }

    // 현재 선택된 위치를 기억 (선택된 병원 위치 정보를 저장)
    var selectedLocation by remember { mutableStateOf<Location?>(null) }

    // 전체 화면을 차지하는 Box 레이아웃
    Box(modifier = modifier.fillMaxSize()) {
        // AndroidView: MapView를 Android 뷰로 삽입
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { _ ->
                mapView.apply {
                    // MapView 생명주기 콜백 설정
                    start(object : MapLifeCycleCallback() {
                        override fun onMapDestroy() {
                            // 지도 소멸 시 로그 출력
                            Log.d("KakaoMap", "Map destroyed")
                        }

                        override fun onMapError(exception: Exception?) {
                            // 지도 오류 발생 시 로그 출력
                            Log.e("KakaoMap", "Map error: ${exception?.message}")
                        }
                    }, object : KakaoMapReadyCallback() {
                        override fun onMapReady(kakaoMap: KakaoMap) {
                            // 지도 준비 완료 시 호출됨
                            Log.d("KakaoMap", "Map is ready")

                            // 지도 중앙을 설정하기 위해 병원 위치들의 평균 위도와 경도를 계산
                            val centerLat = locations.map { it.latitude }.average()
                            val centerLon = locations.map { it.longitude }.average()
                            val center = LatLng.from(centerLat, centerLon)

                            // 카메라를 병원 위치들의 중앙으로 이동 및 줌 레벨 설정
                            kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(center))
                            kakaoMap.moveCamera(CameraUpdateFactory.zoomTo(11))

                            // 라벨 스타일 설정 (텍스트 크기, 색상 등)
                            val labelStyle = LabelStyle.from(R.drawable.transparentmarkerresize)
                                .setTextStyles(32, Color.BLACK, 1, Color.GRAY)
                            val labelStyles = kakaoMap.labelManager!!.addLabelStyles(LabelStyles.from(labelStyle))

                            // 병원 위치마다 라벨을 지도에 추가
                            locations.forEach { location ->
                                val options = LabelOptions.from(LatLng.from(location.latitude, location.longitude))
                                    .setStyles(labelStyles)
                                    .setTexts(LabelTextBuilder().setTexts(location.name))
                                kakaoMap.labelManager?.layer?.addLabel(options)
                            }

                            // 라벨 클릭 이벤트 처리
                            kakaoMap.setOnLabelClickListener(object : KakaoMap.OnLabelClickListener {
                                override fun onLabelClicked(
                                    kakaoMap: KakaoMap,
                                    labelLayer: LabelLayer,
                                    label: Label
                                ): Boolean {
                                    // 클릭된 라벨에 해당하는 위치 정보를 찾아서 selectedLocation에 저장
                                    val clickedLocation = locations.find { it.name == label.texts?.get(0) }
                                    selectedLocation = clickedLocation
                                    return true
                                }
                            })
                        }
                    })
                }
            }
        )

        // 사용자가 특정 병원을 선택한 경우, 상세 정보 표시
        selectedLocation?.let { location ->
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter) // 화면 하단 중앙에 위치
                    .fillMaxWidth()
                    .background(androidx.compose.ui.graphics.Color.White) // 배경색을 흰색으로 설정
                    .padding(16.dp) // 여백 설정
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally // 내용들을 중앙 정렬
                ) {
                    // 병원 이미지를 표시
                    Image(
                        painter = rememberAsyncImagePainter(location.imageUrl),
                        contentDescription = "Location image",
                        modifier = Modifier.size(200.dp) // 이미지 크기 설정
                    )
                    Spacer(modifier = Modifier.height(8.dp)) // 이미지와 텍스트 사이에 공간 추가
                    Text(text = location.name) // 병원 이름 출력
                    Text(text = location.detailAddress) // 병원 주소 출력
                }
            }
        }
    }
}
