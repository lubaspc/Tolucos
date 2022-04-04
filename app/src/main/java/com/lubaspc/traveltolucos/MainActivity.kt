package com.lubaspc.traveltolucos

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lubaspc.traveltolucos.model.ChargeDayMD
import com.lubaspc.traveltolucos.room.InitDB
import com.lubaspc.traveltolucos.ui.fragment.HistoryFragment
import com.lubaspc.traveltolucos.ui.fragment.HomeFragment
import com.lubaspc.traveltolucos.ui.fragment.SaveFormFragment
import com.lubaspc.traveltolucos.utils.parseDate
import java.net.URLEncoder


class MainActivity : AppCompatActivity(), HomeFragment.HandlerHome, SaveFormFragment.HandlerForm {

    private val vModel: MTViewModel by viewModels()
    private val register =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            Log.d("DELINK", it.data.toString())
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        nextFragment(HomeFragment())
        //InitDB.insertInitialDB(lifecycleScope)
    }

    private val queryParams = mapOf(
        Pair("QUERY_AMOUNT", "12.34"),
        Pair("QUERY_REFERENCE", "123244324"),
        Pair("QUERY_RETURN_URL", "https://send_money"),
        //Pair("QUERY_PAYMENT_MODE","12.34"),
        Pair("QUERY_SHOW_SEND_RECEIPT", true),
    )

    private val responses = mapOf(
        Pair("RESPONSE_CANCELED", ""),
        Pair("RESPONSE_REFERENCE_OLD", ""),
        Pair("RESPONSE_REFERENCE", ""),
        Pair("RESPONSE_ERROR_CODE", ""),
        Pair("RESPONSE_ERROR_NAME", ""),
        Pair("RESPONSE_ERROR_MESSAGE", ""),
        Pair("RESPONSE_HELP_NEEDED", ""),
        Pair("RESPONSE_ID", ""),
        Pair("RESPONSE_TRANSACTION_ID", ""),
        Pair("RESPONSE_USER_ID", ""),
        Pair("RESPONSE_MERCHANT_ID", ""),
        Pair("RESPONSE_TERMINAL_ID", ""),
        Pair("RESPONSE_AMOUNT", ""),
        Pair("RESPONSE_CURRENCY", ""),
        Pair("RESPONSE_DATE", ""),
        Pair("RESPONSE_SCHEME", ""),
        Pair("RESPONSE_MODE", ""),
        Pair("RESPONSE_AUTH_MODE", ""),
        Pair("RESPONSE_AUTH_CODE", ""),
        Pair("RESPONSE_PAN", ""),
        Pair("RESPONSE_AID", ""),
        Pair("RESPONSE_APP_LABEL", ""),
        Pair("RESPONSE_AUTHORIZED", ""),
        Pair("RESPONSE_TRANSACTION_TYPE", ""),
        Pair("RESPONSE_STATUS", ""),
        Pair("RESPONSE_UUID", ""),
        Pair("RESPONSE_LAT", ""),
        Pair("RESPONSE_LON", ""),
        Pair("RESPONSE_LOYALTY_ID", ""),
        Pair("RESPONSE_BACKEND_URL", ""),
        Pair("RESPONSE_FULL_STRING", ""),
        Pair("RESPONSE_RECEIPT_URL", ""),
        Pair("RESPONSE_DECLINE_ERROR", ""),
        Pair("RESPONSE_DECLINE_CAUSE", ""),
    )

    override fun onResume() {
        super.onResume()
        /*val deeplink =
            "sps://transaction?${queryParams.map { "${it.key}=${it.value}" }.joinToString("&")}"
        MaterialAlertDialogBuilder(this)
            .setTitle("Dejamobile")
            .setMessage(deeplink)
            .setPositiveButton("Aceptar") { _, _ ->
                register.launch(
                    Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse(deeplink))
                )
            }.show() */
    }

    private fun nextFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.container, fragment, fragment.javaClass.simpleName)
            .addToBackStack(this.javaClass.simpleName)
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount <= 1) {
            finish()
            startActivity(intent)
            return
        }
        super.onBackPressed()
    }

    override fun openSaveDay() = nextFragment(SaveFormFragment())
    override fun openHistory() = nextFragment(HistoryFragment())
    override fun saveForm() = onBackPressed()

}

