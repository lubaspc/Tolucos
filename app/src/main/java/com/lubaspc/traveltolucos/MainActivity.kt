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

        //InitDB.insertInitialDB(lifecycleScope)
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

