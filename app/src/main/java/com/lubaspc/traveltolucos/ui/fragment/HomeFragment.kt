package com.lubaspc.traveltolucos.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lubaspc.traveltolucos.App
import com.lubaspc.traveltolucos.MTViewModel
import com.lubaspc.traveltolucos.R
import com.lubaspc.traveltolucos.databinding.FragmetHomeBinding
import com.lubaspc.traveltolucos.room.TypeCharge
import com.lubaspc.traveltolucos.ui.adapter.ChargesAdapter
import com.lubaspc.traveltolucos.ui.adapter.PersonsAdapter
import com.lubaspc.traveltolucos.utils.parseDate
import com.lubaspc.traveltolucos.utils.saveTags
import java.lang.Exception
import java.util.*
import kotlin.reflect.KClass

class HomeFragment : Fragment() {
    private val adapterPersons by lazy {
        PersonsAdapter()
    }
    private val adapterCharges by lazy {
        ChargesAdapter()
    }
    private val datePicker by lazy {
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Mi dialogo")
            .build()
            .apply {
                addOnPositiveButtonClickListener {
                    vModel.dateSelected.value = Calendar.getInstance().apply {
                        timeInMillis = it
                        add(Calendar.DAY_OF_MONTH, 1)
                    }
                }
            }
    }

    private lateinit var dayRemoveSelect: Calendar

    private val dialogDays by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setPositiveButton("Eliminar") { _, _ ->
                vModel.removeDay(dayRemoveSelect)
            }
    }

    private val vModel by activityViewModels<MTViewModel>()
    private lateinit var vBind: FragmetHomeBinding
    private lateinit var handler: HandlerHome

    override fun onAttach(context: Context) {
        super.onAttach(context)
        handler = context as HandlerHome
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmetHomeBinding.inflate(inflater).apply {
        vBind = this
        rvPersons.adapter = adapterPersons
        rvCharge.adapter = adapterCharges
        bottomAppBar.setNavigationOnClickListener {
            handler.openHistory()
        }
        dateSelect.setOnClickListener {
            datePicker.show(childFragmentManager, datePicker.javaClass.simpleName)
        }
        btnSave.setOnClickListener {
            vModel.createSaveFrom(adapterCharges.getChargesChecked())
            handler.openSaveDay()
        }
        swipe.setOnRefreshListener {
            llTags.removeAllViews()
            vModel.getAccountData()
        }
        bottomAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.m_remove_day -> vModel.getDaysRegister()
            }
            return@setOnMenuItemClickListener true
        }
    }.root


    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* try {
             Class.forName("com.lubaspc.traveltolucos.utils.ClassName")
                 .getMethod("static",Context::class.java,String::class.java)
                 .invoke(null,requireContext(),"Hola mundanos")
         }catch (e: Exception){}
 */
        vModel.accountData.observe(this) { user ->
            vBind.swipe.isRefreshing = false
            vBind.llTags.removeAllViews()
            App.sharedPreferences.saveTags(user?.tags ?: listOf())
            user?.tags?.forEach {
                vBind.llTags.addView(Chip(requireContext()).apply {
                    text = it.nombre
                    textSize = 24f
                    chipIconSize = 72f
                    chipEndPadding = 24f
                    chipStartPadding = 24f
                    chipStrokeWidth = 1f
                    setChipBackgroundColorResource(R.color.purple_700)
                    setChipIconTintResource(R.color.white)
                    setChipIconResource( if (it.isActivo) R.drawable.ic_baseline_check_circle_24 else R.drawable.ic_baseline_remove_circle_outline_24)
                })
            }
        }
        vModel.daysExist.observe(this) {
            dialogDays.setSingleChoiceItems(
                it.map { d -> d.parseDate() }.toTypedArray(), 0
            ) { _, which ->
                dayRemoveSelect = it[which]
            }.show()
        }
        vModel.persons.observe(this, adapterPersons::addPersons)
        vModel.charges.observe(this){
            adapterCharges.setCharges(it.filter { it.type == TypeCharge.GROUP })
        }
        vModel.dateSelected.observe(this) {
            vBind.dateSelect.text = it.parseDate()
        }
    }

    interface HandlerHome {
        fun openSaveDay()
        fun openHistory()
    }
}