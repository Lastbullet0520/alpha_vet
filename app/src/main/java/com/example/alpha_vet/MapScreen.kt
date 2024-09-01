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
import com.kakao.vectormap.route.RouteLineLayer
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles
import com.kakao.vectormap.route.RouteLineStylesSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

// Location 데이터 클래스: 병원 위치 정보를 저장
data class Location(
    val name: String,          // 병원 이름
    val latitude: Double,      // 병원 위도
    val longitude: Double,     // 병원 경도
    val imageUrl: String,      // 병원 이미지 URL
    val detailAddress: String  // 병원 상세 주소
)

@Composable
fun KakaoMapScreen(
    modifier: Modifier = Modifier, // Modifier를 통해 컴포저블의 크기, 패딩 등을 조정
    locations: List<Location>      // 병원 위치 리스트
) {
    val localContext = LocalContext.current
    val mapView = remember { MapView(localContext) }
    var selectedLocation by remember { mutableStateOf<Location?>(null) }
    var travelTime by remember { mutableStateOf<String>("") }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { _ ->
                mapView.apply {
                    start(object : MapLifeCycleCallback() {
                        override fun onMapDestroy() {
                            Log.d("KakaoMap", "Map destroyed")
                        }

                        override fun onMapError(exception: Exception?) {
                            Log.e("KakaoMap", "Map error: ${exception?.message}")
                        }
                    }, object : KakaoMapReadyCallback() {
                        override fun onMapReady(kakaoMap: KakaoMap) {
                            Log.d("KakaoMap", "Map is ready")
                            val centerLat = locations.map { it.latitude }.average()
                            val centerLon = locations.map { it.longitude }.average()
                            val center = LatLng.from(centerLat, centerLon)
                            kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(center))
                            kakaoMap.moveCamera(CameraUpdateFactory.zoomTo(11)) // 줌 레벨을 15로 설정

                            val labelStyle = LabelStyle.from(R.drawable.transparentmarkerresize)
                                .setTextStyles(32, Color.BLACK, 1, Color.GRAY)
                            val labelStyles = kakaoMap.labelManager!!.addLabelStyles(LabelStyles.from(labelStyle))

                            locations.forEach { location ->
                                val options = LabelOptions.from(LatLng.from(location.latitude, location.longitude))
                                    .setStyles(labelStyles)
                                    .setTexts(LabelTextBuilder().setTexts(location.name))
                                kakaoMap.labelManager?.layer?.addLabel(options)
                            }

                            kakaoMap.routeLineManager?.let { routeLineManager ->
                                val routeLineLayer = routeLineManager.layer

                                kakaoMap.setOnLabelClickListener(object : KakaoMap.OnLabelClickListener {
                                    override fun onLabelClicked(
                                        kakaoMap: KakaoMap,
                                        labelLayer: LabelLayer,
                                        label: Label
                                    ): Boolean {
                                        val clickedLocation = locations.find { it.name == label.texts?.get(0) }
                                        selectedLocation = clickedLocation

                                        clickedLocation?.let { location ->
                                            CoroutineScope(Dispatchers.IO).launch {
                                                // 경로 데이터를 가져오고 예상 소요 시간을 가져옵니다.
                                                val time = getTravelTimeFromApi(location)
                                                withContext(Dispatchers.Main) {
                                                    travelTime = time
                                                    drawRouteLineFromApi(kakaoMap, routeLineLayer, location)
                                                }
                                            }
                                        }
                                        return true
                                    }
                                })
                            }
                        }
                    })
                }
            }
        )

        selectedLocation?.let { location ->
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(androidx.compose.ui.graphics.Color.White)
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = rememberAsyncImagePainter(location.imageUrl),
                        contentDescription = "Location image",
                        modifier = Modifier.size(200.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = location.name)
                    Text(text = location.detailAddress)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "예상 소요 시간: $travelTime")
                }
            }
        }
    }
}

// 경로 데이터를 가져오는 함수
suspend fun fetchRouteData(location: Location): JSONObject? {
    val userLatitude = 37.5665
    val userLongitude = 126.9780

    val origin = "${userLongitude},${userLatitude}"
    val destination = "${location.longitude},${location.latitude}"

    val url = "https://apis-navi.kakaomobility.com/v1/directions?origin=$origin&destination=$destination&priority=RECOMMEND&car_fuel=GASOLINE&car_hipass=false"

    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .addHeader("Authorization", "KakaoAK d0d54dc5b51b9a03b9d9ea068683a1c1")
        .build()

    return try {
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val jsonResponse = JSONObject(response.body?.string() ?: "")
            Log.d("KakaoMap", "Fetched route data: $jsonResponse")
            if (jsonResponse.getJSONArray("routes").length() > 0) {
                return jsonResponse.getJSONArray("routes").getJSONObject(0)
            } else {
                Log.d("KakaoMap", "No route data available")
                null
            }
        }
    } catch (e: Exception) {
        Log.e("KakaoMap", "Error fetching route data: ${e.message}")
        e.printStackTrace()
        null
    }
}

// 예상 소요 시간을 가져오는 함수
suspend fun getTravelTimeFromApi(location: Location): String {
    Log.d("KakaoMap", "getTravelTimeFromApi called for location: ${location.name}")

    val route = fetchRouteData(location) ?: return "경로 정보 없음"

    return try {
        if (route.has("summary")) {
            val summary = route.getJSONObject("summary")
            val duration = summary.getInt("duration")
            val minutes = duration / 60
            Log.d("KakaoMap", "Travel time calculated: $minutes 분")
            return "$minutes 분"
        } else {
            return "요약 정보 없음"
        }
    } catch (e: Exception) {
        Log.e("KakaoMap", "Error getting travel time: ${e.message}")
        e.printStackTrace()
        return "오류 발생: ${e.message}"
    }
}

// 경로를 그리는 함수
suspend fun drawRouteLineFromApi(kakaoMap: KakaoMap, routeLineLayer: RouteLineLayer, location: Location) {
    Log.d("KakaoMap", "drawRouteLineFromApi called with location: ${location.name}")

    val route = fetchRouteData(location) ?: return

    try {
        val sections = route.getJSONArray("sections")
        val polylinePoints = mutableListOf<LatLng>()

        for (i in 0 until sections.length()) {
            val section = sections.getJSONObject(i)
            val roads = section.getJSONArray("roads")

            for (j in 0 until roads.length()) {
                val road = roads.getJSONObject(j)
                val vertexes = road.getJSONArray("vertexes")

                for (k in 0 until vertexes.length() step 2) {
                    val longitude = vertexes.getDouble(k)
                    val latitude = vertexes.getDouble(k + 1)
                    polylinePoints.add(LatLng.from(latitude, longitude))
                }
            }
        }

        // 기존 라우트 라인 제거
        routeLineLayer.removeAll()

        // RouteLineStyles 생성
        val routeLineStyle = RouteLineStyle.from(
            16f, // 라인의 두께 (float 타입)
            Color.RED, // 라인의 색상 (int 타입)
            4f, // 테두리 두께 (float 타입)
            Color.BLACK // 테두리 색상 (int 타입)
        )

        // RouteLineStylesSet 생성
        val routeLineStylesSet = RouteLineStylesSet.from(RouteLineStyles.from(routeLineStyle))

        // RouteLineSegment 생성
        val routeLineSegment = RouteLineSegment.from(polylinePoints)
            .setStyles(routeLineStylesSet.getStyles(0)) // 스타일 설정

        // RouteLineOptions 생성
        val routeLineOptions = RouteLineOptions.from(routeLineSegment)
            .setStylesSet(routeLineStylesSet)

        // 새로운 라우트 라인 추가
        routeLineLayer.addRouteLine(routeLineOptions)
        Log.d("KakaoMap", "Route line added to map")
    } catch (e: Exception) {
        Log.e("KakaoMap", "Error drawing route line: ${e.message}")
        e.printStackTrace()
    }
}