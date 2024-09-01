package com.example.alpha_vet.mainScreen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.alpha_vet.model.DarkModeViewModel


@Composable
fun SetScreen(navController: NavController, darkModeViewModel: DarkModeViewModel) {
    val context = LocalContext.current
    val isDarkMode by darkModeViewModel.isDarkMode.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 상단 X 버튼
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            IconButton(
                onClick = { navController.navigateUp() },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "닫기"
                )
            }
        }

        // 설정 제목
        Text(
            text = "설정",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 다크 모드 토글 스위치
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "다크 모드")
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = isDarkMode,
                onCheckedChange = { isChecked ->
                    darkModeViewModel.toggleDarkMode() // 다크 모드 토글
                    Toast.makeText(
                        context,
                        if (isChecked) "다크모드 설정 완료" else "기본 설정 완료",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))  // 아래쪽 공간 확보

        // 하단 저장 버튼
        Button(
            onClick = {
                Toast.makeText(
                    context,
                    if (isDarkMode) "다크모드 설정 완료" else "기본 설정 완료",
                    Toast.LENGTH_SHORT
                ).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(text = "저장")
        }
    }
}