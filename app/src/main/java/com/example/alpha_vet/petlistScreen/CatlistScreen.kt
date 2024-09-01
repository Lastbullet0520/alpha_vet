package com.example.alpha_vet.petlistScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.alpha_vet.model.DarkModeViewModel
import com.example.alpha_vet.MyApp
import com.example.alpha_vet.R
import com.example.alpha_vet.model.CatItem
import com.example.alpha_vet.model.PetProfile
import com.example.alpha_vet.model.PetProfileViewModel

@Composable
fun CatlistScreen(
    navController: NavController,
    darkModeViewModel: DarkModeViewModel,
    petProfileViewModel: PetProfileViewModel,
    catItems: List<CatItem> = emptyList()
) {
    MyApp(darkModeViewModel = darkModeViewModel, petProfileViewModel)

    val petProfiles by petProfileViewModel.petProfiles.collectAsState()

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
                        EmptyCard()
                    } else {
                        CatCard(navController = navController, catItem = catItems[index])
                    }
                }

                // 추가된 부분: 저장된 프로필을 리스트에 추가
                items(petProfiles.size) { index ->
                    val profile = petProfiles[index]
                    CatCard(navController = navController, profile = profile)
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
    catItem: CatItem? = null,
    profile: PetProfile? = null // 프로필을 받을 수 있도록 인자를 추가
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White)
            .clickable { navController?.navigate("catDetails/${catItem?.name ?: profile?.name}") },
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            when {
                catItem?.picture1 != null -> {
                    val imageBitmap = decodeBase64ToImageBitmap(catItem.picture1)
                    Image(bitmap = imageBitmap, contentDescription = "Cat Image")
                }
                profile?.photoUri != null -> {
                    Image(
                        painter = rememberAsyncImagePainter(profile.photoUri),
                        contentDescription = "Profile Image",
                        modifier = Modifier.size(100.dp)
                    )
                }
                else -> {
                    // 기본 이미지를 표시할 경우
                    Image(
                        painter = painterResource(id = R.drawable.cat),
                        contentDescription = "Default Cat Image",
                        modifier = Modifier.size(100.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = catItem?.name ?: profile?.name ?: "",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = catItem?.address ?: profile?.species ?: "",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = catItem?.comment ?: profile?.age ?: "",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = profile?.gender ?: "",
                color = Color.Gray
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


