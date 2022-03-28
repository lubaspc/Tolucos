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
import androidx.lifecycle.lifecycleScope
import com.lubaspc.traveltolucos.LoadingActivity
import com.lubaspc.traveltolucos.R
import com.lubaspc.traveltolucos.service.RetrofitService
import com.lubaspc.traveltolucos.service.model.Tag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CarService : CarAppService() {

    override fun createHostValidator() =
        HostValidator.Builder(this)
            .build()

    override fun onCreateSession() = MyCarSession()

    class MyCarSession : Session() {
        private val repository by lazy {
            RetrofitService()
        }


        override fun onCreateScreen(intent: Intent) = MyCarScreen(carContext, repository)
    }

    class MyLoadCardScreen(carContext: CarContext, private val repository: RetrofitService) :
        Screen(carContext) {
        override fun onGetTemplate(): Template {
            lifecycleScope.launch(Dispatchers.IO) {
                val tags = repository.getAccount().data?.tags
                withContext(Dispatchers.Main) {
                    setResult(tags)
                    screenManager.pop()
                }
            }
            return PaneTemplate.Builder(
                Pane.Builder()
                    .setLoading(true)
                    .build()
            ).setTitle("Consultado")
                .build()
        }


    }

    class MyCarScreen(carContext: CarContext, private val repository: RetrofitService) :
        Screen(carContext) {
        private val tags = mutableListOf<Tag>()

        private fun getTags() {
            screenManager.pushForResult(MyLoadCardScreen(carContext, repository)) {
                tags.clear()
                tags.addAll(it as? List<Tag> ?: listOf())
                invalidate()
            }
        }

        @SuppressLint("UnsafeOptInUsageError")
        override fun onGetTemplate() =
            PaneTemplate.Builder(
                Pane.Builder()
                    .addAction(
                        Action.Builder()
                            .setTitle("Reload Tags")
                            .setOnClickListener(::getTags)
                            .build()
                    )
                    .apply {
                        tags.forEach {
                            addRow(
                                Row.Builder()
                                    .setImage(
                                        CarIcon.Builder(
                                            IconCompat.createWithResource(
                                                carContext,
                                                if (it.isActivo && !it.tieneAdeudos)
                                                    R.drawable.ic_baseline_check_circle_outline_24
                                                else R.drawable.ic_baseline_remove_circle_outline_24
                                            )
                                        ).build()
                                    )
                                    .setTitle(it.nombre)
                                    .addText(it.numeroFormat)
                                    .build()
                            )
                        }
                    }
                    .build()
            ).setTitle("Car Tolucos")
                .setHeaderAction(Action.APP_ICON)
                .build()


    }
}