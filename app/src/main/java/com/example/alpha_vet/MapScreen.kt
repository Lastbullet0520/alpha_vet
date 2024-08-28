package com.example.alpha_vet

import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextBuilder

data class Location(val name: String, val latitude: Double, val longitude: Double)

@Composable
fun KakaoMapScreen(
    modifier: Modifier = Modifier,
    locations: List<Location> // 여러 위치 정보를 리스트로 받음
) {
    Text(text = "Loading Kakao Map...")
    val localContext = LocalContext.current
    val mapView = remember { MapView(localContext) }

    AndroidView(
        modifier = modifier
            .fillMaxSize()
            .height(200.dp),
        factory = { _ ->
            mapView.apply {
                mapView.start(
                    object : MapLifeCycleCallback() {
                        override fun onMapDestroy() {
                            Log.d("KakaoMap", "Map destroyed")
                        }

                        override fun onMapError(exception: Exception?) {
                            Log.e("KakaoMap", "Map error: ${exception?.message}")
                        }
                    },
                    object : KakaoMapReadyCallback() {
                        override fun onMapReady(kakaoMap: KakaoMap) {
                            Log.d("KakaoMap", "Map is ready")

                            // 모든 위치의 중심점 계산
                            val centerLat = locations.map { it.latitude }.average()
                            val centerLon = locations.map { it.longitude }.average()
                            val center = LatLng.from(centerLat, centerLon)

                            // 중심점으로 카메라 이동
                            val cameraUpdate = CameraUpdateFactory.newCenterPosition(center)
                            kakaoMap.moveCamera(cameraUpdate)

                            // 줌 레벨 설정 (1에서 19 사이의 값, 19가 가장 확대된 상태)
                            kakaoMap.moveCamera(CameraUpdateFactory.zoomTo(11))

                            // LabelStyle 생성
                            val labelStyle = LabelStyle.from(R.drawable.transparentmarkerresize).setTextStyles(32, Color.BLACK, 1, Color.GRAY)
                            val labelStyles = kakaoMap.labelManager!!.addLabelStyles(LabelStyles.from(labelStyle))

                            // 각 위치에 마커 추가
                            locations.forEach { location ->
                                val options = LabelOptions.from(LatLng.from(location.latitude, location.longitude))
                                    .setStyles(labelStyles)
                                    .setTexts(LabelTextBuilder().setTexts(location.name))
                                kakaoMap.labelManager?.layer?.addLabel(options)
                            }
                        }
                    }
                )
            }
        }
    )
}

