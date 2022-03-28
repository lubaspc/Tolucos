package com.lubaspc.traveltolucos

import android.app.ProgressDialog
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lubaspc.traveltolucos.service.RetrofitService
import com.lubaspc.traveltolucos.utils.saveTags
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.appwidget.AppWidgetManager

import android.content.ComponentName

import android.content.Intent


class LoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ProgressDialog(this).apply {
            setMessage("Cargando tags")
        }.show()
        lifecycleScope.launch(Dispatchers.IO) {
            App.sharedPreferences.saveTags(RetrofitService().getAccount().data?.tags ?: listOf())
            sendBroadcast(
                Intent(this@LoadingActivity, AppWidget::class.java)
                    .setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
                    .putExtra(
                        AppWidgetManager.EXTRA_APPWIDGET_IDS,
                        AppWidgetManager.getInstance(application)
                            .getAppWidgetIds(ComponentName(application, AppWidget::class.java))
                    )
            )
            finish()
        }
    }
}