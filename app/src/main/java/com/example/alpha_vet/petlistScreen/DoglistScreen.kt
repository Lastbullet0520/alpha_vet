package com.example.alpha_vet.petlistScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
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
import com.example.alpha_vet.model.DogItem
import com.example.alpha_vet.model.PetProfile
import com.example.alpha_vet.model.PetProfileViewModel


fun decodeBase64ToImageBitmap(base64Str: String): ImageBitmap {
    val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
    val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    return bitmap.asImageBitmap() // Bitmap을 ImageBitmap으로 변환하여 반환
}

@Composable
fun DoglistScreen(
    navController: NavController,
    darkModeViewModel: DarkModeViewModel,
    petProfileViewModel: PetProfileViewModel,
    dogItems: List<DogItem> = emptyList()
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
                text = "AI가 추천해주는\n강아지 맞춤 진단",
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
                val cardCount = if (dogItems.isEmpty()) 6 else dogItems.size

                items(cardCount) { index ->
                    if (dogItems.isEmpty()) {
                        EmptyCard()
                    } else {
                        DogCard(navController = navController, dogItem = dogItems[index])
                    }
                }

                // 추가된 부분: 저장된 프로필을 리스트에 추가
                items(petProfiles.size) { index ->
                    val profile = petProfiles[index]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(Color.White)
                            .clickable { /* 클릭 이벤트 처리 */ },
                        elevation = CardDefaults.elevatedCardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .background(Color.White)
                                .padding(16.dp)
                        ) {
                            profile.photoUri?.let { uri ->
                                Image(
                                    painter = rememberAsyncImagePainter(uri),
                                    contentDescription = "Profile Image",
                                    modifier = Modifier.size(100.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = profile.name,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = profile.species,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = profile.age,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = profile.gender,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DogCard(
    navController: NavController? = null,
    dogItem: DogItem? = null,
    profile: PetProfile? = null // 프로필을 받을 수 있도록 인자를 추가
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White)
            .clickable { navController?.navigate("someDestination") },
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            when {
                dogItem?.picture1 != null -> {
                    val imageBitmap = decodeBase64ToImageBitmap(dogItem.picture1)
                    Image(bitmap = imageBitmap, contentDescription = "Dog Image")
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
                        painter = painterResource(id = R.drawable.dog),
                        contentDescription = "Default Dog Image",
                        modifier = Modifier.size(100.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = dogItem?.name ?: profile?.name ?: "",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = dogItem?.address ?: profile?.species ?: "",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = dogItem?.comment ?: profile?.age ?: "",
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
fun EmptyCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 빈 카드 내용
            Text(
                text = "Empty Card",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
        }
    }
}