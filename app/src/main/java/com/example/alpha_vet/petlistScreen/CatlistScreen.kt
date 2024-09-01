package com.example.alpha_vet.petlistScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.alpha_vet.model.DarkModeViewModel
import com.example.alpha_vet.MyApp
import com.example.alpha_vet.model.CatItem


@Composable
fun CatlistScreen(navController: NavController, darkModeViewModel: DarkModeViewModel, catItems: List<CatItem> = emptyList()) {
    MyApp(darkModeViewModel = darkModeViewModel)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "AI가 추천해주는\n고양이 맞춤 진단",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val cardCount = if (catItems.isEmpty()) 6 else catItems.size

                items(cardCount) { index ->
                    if (catItems.isEmpty()) {
                        EmptyCard()  // 빈 카드
                    } else {
                        CatCard(navController = navController, catItem = catItems[index])
                    }
                }
            }
        }

        // AI 챗봇 버튼을 우측 하단에 배치
        AIChatbotButton(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomEnd) // 우측 하단으로 정렬
        )
    }
}
@Composable
fun CatCard(
    navController: NavController? = null,
    catItem: CatItem? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White)
            .clickable { navController?.navigate("catDetails/${catItem?.name}") },
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            catItem?.picture1?.let {
                val imageBitmap = decodeBase64ToBitmap(it)
                Image(bitmap = imageBitmap.asImageBitmap(), contentDescription = "Cat Image")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = catItem?.name ?: "",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "5.0 ",
                    color = Color(0xFFFFD700),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = catItem?.address ?: "",
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = catItem?.comment ?: "",
                color = Color(0xFF6A1B9A)
            )
        }
    }
}
@Composable
fun AIChatbotButton(navController: NavController, modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = {
            navController.navigate("aiChatbot")
        },
        modifier = modifier.padding(16.dp),
        containerColor = Color(0xFF6200EE)
    ) {
        Text(text = "AI 상담", color = Color.White)
    }
}

