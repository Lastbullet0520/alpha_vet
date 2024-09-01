package com.example.alpha_vet.mainScreen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.alpha_vet.model.DarkModeViewModel
import com.example.alpha_vet.R
import com.example.alpha_vet.model.PetProfileViewModel
import com.example.alpha_vet.state.Screen

@Composable
fun MenuScreen(
    navController: NavController,
    petProfile: PetProfileViewModel,
    darkModeViewModel: DarkModeViewModel,
) {  // 새로 추가된 버튼에 사용
    var isPetProfileVisible by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFF6F61),
                            Color(0xFFFF8A65)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Display the pet's image if available, else default image
                petProfile.photoUri?.let {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(Uri.parse(it)),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                } ?: run {
                    Image(
                        painter = painterResource(id = R.drawable.dog),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                }

                // "프로필 수정" button
                TextButton(
                    onClick = { navController.navigate("ProfileScreen") },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(text = "프로필 수정", color = Color.White)
                }

                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp) // Increase spacing between text elements
                ) {
                    Text(
                        text = petProfile.name,
                        color = Color.White,
                        fontSize = 16.sp // Set font size to be the same for both texts
                    )
                    Text(
                        text = "${petProfile.species}, ${petProfile.age}, ${petProfile.gender}",
                        color = Color.White,
                        fontSize = 16.sp // Set font size to be the same for both texts
                    )
                }
            }

            IconButton(
                // 이부분은 수정이 필요
                onClick = { navController.navigate(Screen.Home.route) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "진단내역", color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "마이보험", color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "쿠폰함", color = Color.Gray)
            }

            // 나의 펫 버튼
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TextButton(
                    onClick = { isPetProfileVisible = !isPetProfileVisible },
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(text = "나의 펫", color = Color.Gray)
                }
            }
        }


        Divider(thickness = 1.dp, color = Color.LightGray)
        // "나의 펫 버튼을 클릭했을 때 프로필 정보 표시"
        // "나의 펫" 버튼을 클릭했을 때 프로필 정보 표시
        if (isPetProfileVisible) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                petProfile.photoUri?.let {
                    Image(
                        painter = rememberAsyncImagePainter(Uri.parse(it)),
                        contentDescription = "Pet Profile Image",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                } ?: run {
                    Image(
                        painter = painterResource(id = R.drawable.dog),
                        contentDescription = "Default Pet Image",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "이름: ${petProfile.name}", fontSize = 18.sp, color = Color.Black)
                Text(text = "종: ${petProfile.species}", fontSize = 18.sp, color = Color.Black)
                Text(text = "나이: ${petProfile.age}", fontSize = 18.sp, color = Color.Black)
                Text(text = "성별: ${petProfile.gender}", fontSize = 18.sp, color = Color.Black)
            }
        }

        MenuItem(title = "펫보험", navController = navController)
        MenuItem(title = "고객센터", navController = navController)
    }
}

@Composable
fun MenuItem(title: String, navController: NavController) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, fontSize = 16.sp, color = Color.Black)
            // 아이콘 제거
        }
        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
    }
}
