package com.lubaspc.traveltolucos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.lubaspc.traveltolucos.room.*
import com.lubaspc.traveltolucos.ui.HomeView
import com.lubaspc.traveltolucos.ui.SaveForm
import com.lubaspc.traveltolucos.ui.SettingsView
import com.lubaspc.traveltolucos.ui.WeekView
import com.lubaspc.traveltolucos.ui.theme.TravelTolucosTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import android.content.pm.PackageManager
import android.util.Log
import java.lang.Exception
import java.net.URLEncoder


class MainActivity : AppCompatActivity() {

    val vModel: MTViewModel by viewModels()
    lateinit var navController: NavHostController

    @ExperimentalComposeUiApi
    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()
            TravelTolucosTheme {
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") { HomeView() }
                    composable("week") { WeekView() }
                    composable("settings") { SettingsView() }
                    composable("create_register") { SaveForm() }
                }
            }

        }
    }


    fun setMessage(message: String, phone: String) {
        Log.d("WHTASTER", message)
        startActivity(
            Intent(Intent.ACTION_VIEW)
                .setPackage("com.whatsapp")
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
                    .setPackage("com.whatsapp")
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


    fun showDatePicker() {
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Mi dialogo")
            .build()
            .apply {
                addOnPositiveButtonClickListener {
                    vModel.setDateSelect(Calendar.getInstance().apply {
                        timeInMillis = it
                        add(Calendar.DAY_OF_MONTH, 1)
                    })
                }
            }
            .show(supportFragmentManager, "")
    }
}

