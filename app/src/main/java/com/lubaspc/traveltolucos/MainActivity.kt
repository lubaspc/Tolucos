package com.lubaspc.traveltolucos

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.lubaspc.traveltolucos.room.InitDB
import com.lubaspc.traveltolucos.ui.fragment.SettingsFragment
import com.lubaspc.traveltolucos.ui.fragment.HistoryFragment
import com.lubaspc.traveltolucos.ui.fragment.HomeFragment
import com.lubaspc.traveltolucos.ui.fragment.SaveFormFragment


class MainActivity : AppCompatActivity(), HomeFragment.HandlerHome {

    private val vModel: MTViewModel by viewModels()
    private val progress by lazy {
        ProgressDialog(this)
            .apply {
                setTitle("Cargando datos")
                setCancelable(false)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        nextFragment(HomeFragment())
        vModel.consultHistory()
        //InitDB.insertInitialDB(lifecycleScope)
        vModel.showProgress.observe(this) {
            if (it && !progress.isShowing) {
                progress.show()
            }
            if (!it && progress.isShowing) {
                progress.dismiss()
            }
        }
    }

    private fun nextFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, fragment.javaClass.simpleName)
            .addToBackStack(this.javaClass.simpleName)
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount <= 1) finish()
        else super.onBackPressed()
    }

    override fun openSaveDay() = SaveFormFragment().show(supportFragmentManager)
    override fun openHistory() = nextFragment(HistoryFragment())
    override fun openSettings() = nextFragment(SettingsFragment())
    override fun openRoutes() {
        startActivity(Intent(this,RoutesActivity::class.java))
    }


}

