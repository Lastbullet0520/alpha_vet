package com.example.alpha_vet.mainScreen

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.alpha_vet.MyApp
import com.example.alpha_vet.R
import com.example.alpha_vet.model.DarkModeViewModel
import com.example.alpha_vet.model.PetProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// 상단 "AI가 추천해주는..." , 하단
fun MainScreen(navController: NavController,darkModeViewModel: DarkModeViewModel, petProfileViewModel: PetProfileViewModel) {
    MyApp(darkModeViewModel, petProfileViewModel)
    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center // Center alignment for the content
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.logo),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("My Pet", color = Color(0xFFFFC0CB)) // Pink color for text
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {navController.navigate("MenuScreen")}) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.Black // Set icon color to black
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {navController.navigate("SetScreen")}) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.Black // Set icon color to black
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = Color(0xFFFFC0CB)
                    )
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.Black
                )
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(innerPadding)
            ) {
                Text(
                    text = "AI가 추천해주는\n우리아이 맞춤 진단",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
                Text(
                    text = "반려동물 종류를 선택해 주세요",
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,  // Align text to center
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp) // Space between cards
                ) {
                    PetCard(
                        title = "강아지 진단 알아보기",
                        textColor = Color.Black,
                        painter = painterResource(id = R.drawable.dog),
                        backgroundColor = Color(0xFFFFF0F0),
                        onClick = { navController.navigate("DoglistScreen") },
                        modifier = Modifier.weight(1f)
                    )
                    PetCard(
                        title = "고양이 진단 알아보기",
                        textColor = Color.Black,
                        painter = painterResource(id = R.drawable.cat),
                        backgroundColor = Color(0xFFF0F0FF),
                        onClick = { navController.navigate("CatlistScreen") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    )
}

@Composable
fun PetCard(
    title: String,
    textColor: Color,
    painter: Painter,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = title,
                color = textColor,  // Apply textColor here
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

