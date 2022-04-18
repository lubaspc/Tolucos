package com.lubaspc.traveltolucos.utils

import android.graphics.Bitmap
import android.os.Environment
import androidx.core.content.ContextCompat
import com.lubaspc.traveltolucos.App
import java.io.*

fun Bitmap.convertToFile(): File {
    //create a file to write bitmap data
    val file = File(App.dirCache, "image_cache_${System.currentTimeMillis()}")
    file.createNewFile()

    //Convert bitmap to byte array
    val bos = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, 100, bos)
    val bitMapData = bos.toByteArray()

    //write the bytes in file
    var fos: FileOutputStream? = null
    try {
        fos = FileOutputStream(file)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
    try {
        fos?.write(bitMapData)
        fos?.flush()
        fos?.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return file
}
