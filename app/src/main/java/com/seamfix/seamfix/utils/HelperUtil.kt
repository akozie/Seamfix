package com.seamfix.seamfix.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import java.io.ByteArrayOutputStream

object HelperUtil {

    /**
     * Converts Bitmap to a base64 string
     * */
    fun encodeImageToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    /**
     * An extension function that displays a message to the user
     * */
    fun Activity.showToast(context: Context, message: String, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, length).show()
    }
}