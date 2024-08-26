package com.example.alpha_vet

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader

@Composable
fun STTScreen() {
    val context = LocalContext.current // 현재 Context를 가져옵니다 (Activity를 가리킴)

    // CSV 파일을 내부 저장소로 복사
    LaunchedEffect(Unit) {
        copyCsvFileFromAssets(context)
    }

    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
    // SpeechRecognizer를 생성합니다 (음성 인식을 처리하는 클래스)

    val speechIntent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR") // 한국어 인식을 위해 언어 설정
        }
    }

    // 권한 요청을 처리하는 런처를 설정합니다
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // 권한이 부여되면 음성 인식을 시작합니다
            speechRecognizer.startListening(speechIntent)
        } else {
            // 권한이 부여되지 않으면 사용자에게 알림을 표시합니다
            Toast.makeText(context, "권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 음성 인식 결과를 저장하는 상태 변수
    var resultText by remember { mutableStateOf("음성 인식 결과가 여기에 표시됩니다.") }
    var responseText by remember { mutableStateOf("여기에 대답이 표시됩니다.") }

    // 음성 인식 결과를 처리하는 리스너 설정
    speechRecognizer.setRecognitionListener(object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {}
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() {}
        override fun onError(error: Int) {
            // 오류가 발생한 경우 오류 메시지를 resultText에 설정
            resultText = "인식 실패: $error"
        }
        override fun onResults(results: Bundle?) {
            // 인식된 결과를 가져와서 resultText에 설정
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            val recognizedText = matches?.get(0) ?: "인식 실패"
            resultText = recognizedText

            // CSV 파일에서 대답을 찾기 위해 확인
            val response = getResponseFromCSV(context, recognizedText)
            responseText = response ?: "일치하는 대답이 없습니다."
        }
        override fun onPartialResults(partialResults: Bundle?) {}
        override fun onEvent(eventType: Int, params: Bundle?) {}
    })

    // UI 구성: 버튼과 텍스트
    Column {
        Button(onClick = {
            // 버튼이 클릭되었을 때 권한을 확인
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
                // 권한이 없으면 권한 요청
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            } else {
                // 권한이 있으면 음성 인식을 시작
                speechRecognizer.startListening(speechIntent)
            }
        }) {
            Text(text = "음성 인식 시작") // 버튼에 표시될 텍스트
        }
        Button(onClick = {
            speechRecognizer.cancel() // 음성 인식 취소
            resultText = "음성 인식 취소됨"
            responseText = "" // 취소된 경우 응답 텍스트를 비웁니다
        }) {
            Text(text = "음성 인식 취소")
        }

        Text(text = resultText) // 음성 인식 결과를 화면에 표시
        Text(text = responseText) // CSV 파일에서 가져온 대답을 화면에 표시
    }
}

// CSV 파일을 assets에서 내부 저장소로 복사하는 함수
fun copyCsvFileFromAssets(context: Context) {
    val fileName = "stt_responses.csv"
    val file = File(context.filesDir, fileName)

    if (!file.exists()) {
        context.assets.open(fileName).use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    }
}

// CSV 파일에서 사용자 입력에 해당하는 대답을 찾는 함수
fun getResponseFromCSV(context: Context, userInput: String): String? {
    val fileName = "stt_responses.csv"
    val file = File(context.filesDir, fileName)

    // 먼저 파일이 존재하는지 확인
    if (file.exists()) {

        // 파일을 읽기 위해 FileInputStream을 생성
        val inputStream = FileInputStream(file)

        // InputStreamReader를 사용하여 입력 스트림을 문자로 변환, 그리고 BufferedReader로 감싸서 성능 향상
        val reader = BufferedReader(InputStreamReader(inputStream))

        // useLines를 사용하여 파일을 줄 단위로 읽기 시작
        reader.useLines { lines ->

            // 각 줄을 반복 처리
            lines.forEach { line ->

                // 현재 줄을 쉼표(,)로 구분하여 열(columns)로 분리
                val columns = line.split(",")

                // 분리된 열의 수가 2인지 확인
                // (즉, "User Input"과 "Response" 두 개의 열이 존재하는지 확인)
                if (columns.size == 2) {

                    // 첫 번째 열 ("User Input")과 사용자의 입력을 비교
                    // 양쪽 모두 trim()을 사용해 불필요한 공백을 제거한 후 비교
                    if (columns[0].trim() == userInput.trim()) {

                        // "User Input"과 일치하는 경우, 두 번째 열 ("Response")의 값을 반환
                        return columns[1].trim()
                    }
                }
            }
        }
    }
    return null // 일치하는 값이 없을 경우 null 반환
}
