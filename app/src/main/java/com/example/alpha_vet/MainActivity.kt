package com.example.alpha_vet

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.alpha_vet.ui.theme.AlphaVetTheme
import com.kakao.sdk.common.KakaoSdk
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Kakao SDK 초기화
        KakaoSdk.init(this, "45acaf256ea22896ae5df77e36cebc2f")
        enableEdgeToEdge()
        setContent {
            MyApp {
                KeyHashChecker()
            }
        }
    }
}

@Composable
fun KeyHashChecker(){
    val context = LocalContext.current
    var keyHash by remember {mutableStateOf("")}

    LaunchedEffect(Unit) {
        keyHash = getKeyHash(context.packageManager, context.packageName)
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

fun getKeyHash(pm: PackageManager, packageName: String): String{
    return try {
        val info: PackageInfo = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        val signatures = info.signatures
        val md = MessageDigest.getInstance("SHA")
        for(signature in signatures){
            md.update(signature.toByteArray())
        }
        val keyHash = String(Base64.encode(md.digest(), 0))
        Log.d("KeyHash", keyHash)
        keyHash
    } catch (e: PackageManager.NameNotFoundException){
        e.printStackTrace()
        ""
    } catch (e: NoSuchAlgorithmException){
        e.printStackTrace()
        ""
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}


