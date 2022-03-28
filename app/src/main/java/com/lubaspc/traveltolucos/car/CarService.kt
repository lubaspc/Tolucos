package com.lubaspc.traveltolucos.car

import android.annotation.SuppressLint
import android.content.Intent
import androidx.car.app.CarAppService
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.Session
import androidx.car.app.model.*
import androidx.car.app.validation.HostValidator
import androidx.core.graphics.drawable.IconCompat
import com.lubaspc.traveltolucos.R

class CarService : CarAppService() {
    override fun createHostValidator() =
        HostValidator.Builder(this)
            .build()

    override fun onCreateSession() = MyCarSession()

    class MyCarSession : Session() {
        override fun onCreateScreen(intent: Intent) = MyCarScreen(carContext)
    }

    class MyCarScreen(carContext: CarContext) : Screen(carContext) {
        @SuppressLint("UnsafeOptInUsageError")
        override fun onGetTemplate() =
            PaneTemplate.Builder(
                Pane.Builder()
                    .addAction(Action.Builder().build())
                    .addRow(Row.Builder()
                        .setTitle("Tags")
                        .build())
                    .build()
            ).setTitle("Car Tolucos")
                .setHeaderAction(Action.APP_ICON)
                .build()

    }
}