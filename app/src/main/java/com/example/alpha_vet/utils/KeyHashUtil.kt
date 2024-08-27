package com.example.alpha_vet.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object KeyHashUtil {
    fun getKeyHash(context: Context): String {
        return try {
            val packageInfo: PackageInfo = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNATURES
            )
            val signatures = packageInfo.signatures
            val md = MessageDigest.getInstance("SHA")
            for (signature in signatures) {
                md.update(signature.toByteArray())
            }
            val keyHash = String(Base64.encode(md.digest(), Base64.NO_WRAP))
            Log.d("KeyHash", keyHash)
            keyHash
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            ""
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            ""
        }
    }
}