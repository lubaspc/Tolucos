package com.lubaspc.traveltolucos.ui.fragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.lubaspc.traveltolucos.MTViewModel
import com.lubaspc.traveltolucos.R
import com.lubaspc.traveltolucos.databinding.FragmetHomeBinding
import com.lubaspc.traveltolucos.ui.adapter.ChargesAdapter
import com.lubaspc.traveltolucos.ui.adapter.PersonsAdapter
import com.lubaspc.traveltolucos.utils.parseDate
import java.util.*

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
        AlertDialog.Builder(requireContext())
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
            vModel.consultHistory()
            handler.openHistory()
        }
        dateSelect.setOnClickListener {
            datePicker.show(childFragmentManager, datePicker.javaClass.simpleName)
        }
        btnSave.setOnClickListener {
            vModel.createSaveFrom(adapterCharges.getChargesChecked())
            handler.openSaveDay()
        }
        bottomAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.m_remove_day -> {
                    vModel.getDaysRegister()
                }
            }
            return@setOnMenuItemClickListener true
        }
    }.root



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vModel.daysExist.observe(this) {
            dialogDays.setSingleChoiceItems(
                it.map { d -> d.parseDate() }.toTypedArray(), 0
            ) { _, which ->
                dayRemoveSelect = it[which]
            }.show()
        }
        vModel.persons.observe(this, adapterPersons::addPersons)
        vModel.charges.observe(this, adapterCharges::setCharges)
        vModel.dateSelected.observe(this) {
            vBind.dateSelect.text = it.parseDate()
        }
    }

    interface HandlerHome {
        fun openSaveDay()
        fun openHistory()
    }
}