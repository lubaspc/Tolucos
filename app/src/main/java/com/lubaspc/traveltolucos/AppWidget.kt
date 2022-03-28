package com.lubaspc.traveltolucos

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi

/**
 * Implementation of App Widget functionality.
 */
class AppWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val views = RemoteViews(context.packageName, R.layout.app_widget)
        views.setOnClickPendingIntent(
            R.id.ll_container,
            PendingIntent.getActivity(
                context, 400,
                Intent(context, LoadingActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK),
                PendingIntent.FLAG_MUTABLE
            )
        )
        views.removeAllViews( R.id.ll_container)
        context.getSharedPreferences("Travels", Context.MODE_PRIVATE)
            .getStringSet("TAGS", setOf())?.forEach { it ->
                val tag = it.split("&")
                views.addView(
                    R.id.ll_container,
                    RemoteViews(context.packageName, R.layout.widget_tag).apply {
                        setTextViewText(R.id.tag_widget_name, tag.getOrNull(0) ?: "Sin nombre")
                        setTextViewText(R.id.tag_widget_number, tag.getOrNull(1) ?: "Sin nombre")
                        setImageViewResource(
                            R.id.tag_widget_iv,
                            if (tag.getOrNull(2)?.toBoolean() == true && tag.getOrNull(3)?.toBoolean() == false)
                                R.drawable.ic_baseline_check_circle_24
                            else R.drawable.ic_baseline_remove_circle_outline_24
                        )
                    })
            }
        appWidgetManager.updateAppWidget(appWidgetIds, views)
    }

}