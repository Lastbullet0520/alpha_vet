package com.example.alpha_vet.ui.util

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.alpha_vet.utils.KeyHashUtil

@Composable
fun KeyHashChecker() {
    val context = LocalContext.current
    var keyHash by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        keyHash = KeyHashUtil.getKeyHash(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Your Key Hash is:")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = keyHash)
    }
}