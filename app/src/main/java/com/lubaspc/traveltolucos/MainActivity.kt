package com.lubaspc.traveltolucos

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
import androidx.appcompat.app.AlertDialog
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.lubaspc.traveltolucos.model.ChargeDayMD
import com.lubaspc.traveltolucos.room.InitDB
import com.lubaspc.traveltolucos.ui.fragment.HistoryFragment
import com.lubaspc.traveltolucos.ui.fragment.HomeFragment
import com.lubaspc.traveltolucos.ui.fragment.SaveFormFragment
import com.lubaspc.traveltolucos.utils.parseDate
import java.net.URLEncoder


class MainActivity : AppCompatActivity(), HomeFragment.HandlerHome, SaveFormFragment.HandlerForm {

    private val vModel: MTViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        nextFragment(HomeFragment())
        InitDB.insertInitialDB(lifecycleScope)
    }

    private fun nextFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.container, fragment, fragment.javaClass.simpleName)
            .addToBackStack(this.javaClass.simpleName)
            .commit()
    }

    fun showDialogChagePay(days: List<ChargeDayMD>, period: String) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar pago")
            .setMessage(
                "Estas seguro de confirmar el pago de ${
                    days.firstOrNull()?.person?.name
                } en la semana del $period"
            )
            .setPositiveButton("PAGAR") { d, v ->
                d.dismiss()
                vModel.confirmPay(days)
                //vModel.consultHistory()
            }.show()
    }


    fun setMessage(message: String, phone: String) {
        Log.d("WHTASTER", message)
        startActivity(
            Intent(Intent.ACTION_VIEW)
                .setPackage("com.whatsapp.w4b")
                .setData(
                    Uri.parse(
                        "https://api.whatsapp.com/send?phone=+52$phone&text=" + URLEncoder.encode(
                            message,
                            "UTF-8"
                        )
                    )
                )
        )
    }

    fun setMessage(tasks: List<Pair<String, String>>) {
        startActivities(
            tasks.map {
                Intent(Intent.ACTION_VIEW)
                    .setPackage("com.whatsapp.w4b")
                    .setData(
                        Uri.parse(
                            "https://api.whatsapp.com/send?phone=+52${it.second}&text=" + URLEncoder.encode(
                                it.first,
                                "UTF-8"
                            )
                        )
                    )
            }.toTypedArray()
        )
    }

    override fun openSaveDay() = nextFragment(SaveFormFragment())
    override fun openHistory() = nextFragment(HistoryFragment())
    override fun saveForm() = onBackPressed()

}

