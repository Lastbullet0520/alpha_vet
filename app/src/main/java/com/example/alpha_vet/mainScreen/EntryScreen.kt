package com.example.alpha_vet.mainScreen

import androidx.compose.runtime.*
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EntryScreen(navController: NavController) {
    val backgroundColor = Color(255, 182, 193)
    val textColor = Color.White


    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate("MainScreen") {
            popUpTo("EntryScreen") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 100.dp)
        ) {
            Text(
                text = "My Pet",
                color = textColor,
                style = TextStyle(
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EntryScreenPreview() {
    EntryScreen(navController = NavController(LocalContext.current))

}