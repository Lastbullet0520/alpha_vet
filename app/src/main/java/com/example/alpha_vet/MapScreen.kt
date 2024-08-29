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
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

// Location 데이터 클래스: 병원 위치 정보를 저장
data class Location(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String,
    val detailAddress: String
)

@Composable
fun KakaoMapScreen(
    modifier: Modifier = Modifier,
    locations: List<Location>
) {
    // 사용자의 위치를 추적하기 위해 필요
    val localContext = LocalContext.current
    val mapView = remember { MapView(localContext) }

    // 현재 선택된 위치를 기억 (선택된 병원 위치 정보를 저장)
    var selectedLocation by remember { mutableStateOf<Location?>(null) }
    var travelTime by remember { mutableStateOf<String>("") }

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
                                    val clickedLocation = locations.find { it.name == label.texts?.get(0) }
                                    selectedLocation = clickedLocation

                                    clickedLocation?.let { location ->
                                        // 웹 API를 호출하여 경로 탐색 및 소요 시간 계산
                                        CoroutineScope(Dispatchers.IO).launch {
                                            val time = getTravelTimeFromApi(location)
                                            travelTime = time
                                        }
                                    }
                                    return true
                                }
                            })
                        }
                    })
                }
            }
        )

        // 사용자가 특정 병원을 선택한 경우, 상세 정보 및 예상 소요 시간 표시
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
                    Spacer(modifier = Modifier.height(8.dp)) // 텍스트 사이에 공간 추가
                    Text(text = "예상 소요 시간: $travelTime") // 소요 시간 출력
                }
            }
        }
    }
}

// 웹 API 호출 및 경로 탐색 (여기서는 가상의 API 사용 예시로 구현)
// 3. 함수: 사용자 위치에서 특정 위치(병원)까지의 예상 소요 시간을 계산하는 함수
suspend fun getTravelTimeFromApi(location: Location): String {

    // 1. 변수: 사용자 위치 (서울의 위도와 경도, 예시 값)
    val userLatitude = 37.5665
    val userLongitude = 126.9780

    // 1. 변수: 출발지(origin)와 도착지(destination) 좌표를 문자열로 구성 (longitude,latitude 순서)
    val origin = "${userLongitude},${userLatitude}"
    val destination = "${location.longitude},${location.latitude}"

    // 1. 변수: API 요청 URL (경로 탐색을 위한 파라미터 포함)
    val url = "https://apis-navi.kakaomobility.com/v1/directions?origin=$origin&destination=$destination&priority=RECOMMEND&car_fuel=GASOLINE&car_hipass=false"

    // 1. 변수: OkHttpClient 객체 (HTTP 요청을 보내기 위해 사용)
    val client = OkHttpClient()

    // 1. 변수: HTTP 요청 객체를 생성 (Authorization 헤더에 REST API 키 포함)
    val request = Request.Builder()
        .url(url)
        .addHeader("Authorization", "KakaoAK d0d54dc5b51b9a03b9d9ea068683a1c1") // YOUR_REST_API_KEY는 실제 발급받은 키로 대체
        .build()

    // 3. 함수: HTTP 요청을 보내고 응답을 처리하는 블록 (예외 처리 포함)
    return try {
        client.newCall(request).execute().use { response ->

            // 2. 논리 흐름: 응답이 성공적인지 확인 (HTTP 상태 코드가 200 OK인지 확인)
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            // 1. 변수: JSON 응답을 파싱하여 JSONObject로 변환
            val jsonResponse = JSONObject(response.body?.string() ?: "")

            // 1. 변수: 경로 정보 배열을 가져옴
            val routes = jsonResponse.getJSONArray("routes")

            // 2. 논리 흐름: 경로 정보가 있는지 확인 (routes 배열의 길이가 0보다 큰지 확인)
            if (routes.length() > 0) {
                val route = routes.getJSONObject(0) // 첫 번째 경로 정보 가져오기

                // 2. 논리 흐름: summary 키가 존재하는지 확인
                if (route.has("summary")) {
                    val summary = route.getJSONObject("summary") // summary 객체 가져오기

                    // 1. 변수: 소요 시간을 가져와 분 단위로 변환
                    val duration = summary.getInt("duration")
                    val minutes = duration / 60
                    return "$minutes 분" // 소요 시간을 분 단위로 반환
                } else {
                    return "요약 정보 없음" // summary 키가 없을 경우 반환
                }
            }
            return "경로 정보 없음" // routes 배열에 경로 정보가 없을 경우 반환
        }
    } catch (e: Exception) {
        e.printStackTrace() // 2. 논리 흐름: 예외 발생 시 스택 트레이스를 출력
        return "오류 발생: ${e.message}" // 예외 메시지를 반환하여 오류 정보를 사용자에게 전달
    }
}
