package com.lubaspc.traveltolucos

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.car.app.utils.RemoteUtils

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
        views.removeAllViews(R.id.ll_container)
        context.getSharedPreferences("Travels", Context.MODE_PRIVATE)
            .getStringSet("TAGS", setOf())?.apply {
                forEach {
                    val tag = it.split("&")
                    views.addViewTag(
                        context,
                        tag.getOrNull(0) ?: "Sin nombre",
                        tag.getOrNull(1) ?: "Sin nombre",
                        if (tag.getOrNull(2)?.toBoolean() == true &&
                            tag.getOrNull(3)?.toBoolean() == false
                        )
                            R.drawable.ic_baseline_check_circle_24
                        else R.drawable.ic_baseline_remove_circle_outline_24,
                        LoadingActivity::class.java
                    )
                }
                ifEmpty {
                    views.addViewTag(
                        context,
                        "Error al bajar la informacion",
                        "Click para actualizar",
                        R.drawable.ic_launcher_foreground,
                        LoadingActivity::class.java
                    )
                }
            }
        views.addViewTag(
            context,
            "Registrar dia",
            "",
            R.drawable.ic_baseline_more_time_24,
            MainActivity::class.java
        )
        views.addViewTag(
            context,
            "Rutas",
            "",
            R.drawable.ic_baseline_route_24,
            RoutesActivity::class.java
        )
        appWidgetManager.updateAppWidget(appWidgetIds, views)
    }

    private fun RemoteViews.addViewTag(
        context: Context,
        name: String,
        number: String,
        @DrawableRes icon: Int,
        activity: Class<*>
    ) = addView(
        R.id.ll_container,
        RemoteViews(context.packageName, R.layout.widget_tag).apply {
            setTextViewText(R.id.tag_widget_name, name)
            setTextViewText(R.id.tag_widget_number, number)
            setImageViewResource(R.id.tag_widget_iv, icon)
            setOnClickPendingIntent(
                R.id.tag_widget_container,
                PendingIntent.getActivity(
                    context, activity.methods.size + 200,
                    Intent(context, activity).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK),
                    PendingIntent.FLAG_MUTABLE
                )
            )
        })
}