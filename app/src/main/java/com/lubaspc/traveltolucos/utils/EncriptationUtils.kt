package com.lubaspc.traveltolucos.utils

import java.security.spec.AlgorithmParameterSpec
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import android.util.Base64
import java.nio.charset.Charset
import mx.com.bancoazteca.bancadigital.qrutildecryptlib.EncryptQR
import mx.com.bancoazteca.bancadigital.qrutildecryptlib.RequestQR

private val keyQr = "757B1A65CE23D6238505DC1D569AEDFD"
private val ivQr = "0733cf9a3f5fca77d220da6fb2ac1580"


fun generateQR(amount: Double): String {


    return EncryptQR().encryptv2(
        RequestQR()
            .withKey(cypherStore)
            .withDestinationAccount(account)
            .withAmountConcept(String.format("%.2f", amount).replace(".", ""))
            .withAlias(userName)
    )
}

private fun encryptQR(value: String?) =
    try {
        if (!value.isNullOrEmpty()) {
            val binary = hex2Binary(ivQr)
            val ivSpec: AlgorithmParameterSpec = IvParameterSpec(binary)
            val keySpec = SecretKeySpec(keyQr.toByteArray(Charsets.UTF_8), "AES")
            val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding").also {
                it.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
            }
            Base64.encodeToString(cipher.doFinal(value.toByteArray()), Base64.DEFAULT)
        } else {
            ""
        }
    } catch (e: Exception) {
        //Log.i("tag", "error: " + e)
        ""
    }

private fun decryptQR(value: String?) =
    try {
        if (!value.isNullOrEmpty()) {
            val binary = hex2Binary(ivQr)
            val raw = Base64.decode(value, Base64.DEFAULT)
            val ivSpec: AlgorithmParameterSpec = IvParameterSpec(binary)
            val newKey = SecretKeySpec(keyQr.toByteArray(Charsets.UTF_8), "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec)
            String(cipher.doFinal(raw), Charset.defaultCharset())
        } else {
            ""
        }
    } catch (e: Exception) {
        //Log.i("tag", "error: " + e)
        ""
    }


private fun hex2Binary(key: String): ByteArray {
    val binary = ByteArray(key.length / 2)
    for (i in binary.indices) {
        binary[i] = Integer.parseInt(key.substring(2 * i, 2 * i + 2), 16).toByte()
    }
    return binary
}