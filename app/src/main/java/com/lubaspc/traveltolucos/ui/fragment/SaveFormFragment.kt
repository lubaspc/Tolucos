package com.lubaspc.traveltolucos.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.lubaspc.traveltolucos.MTViewModel
import com.lubaspc.traveltolucos.databinding.FragmentSaveFormBinding
import com.lubaspc.traveltolucos.ui.adapter.SavePersonAdapter
import com.lubaspc.traveltolucos.utils.formatPrice
import com.lubaspc.traveltolucos.utils.getTotal

class SaveFormFragment: DialogFragment() {

    private lateinit var vBind: FragmentSaveFormBinding
    private val vModel by activityViewModels<MTViewModel>()
    private val adapterPerson by lazy{
        SavePersonAdapter()
    }
    private val amounts = mutableMapOf<Long,Double>()


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSaveFormBinding.inflate(layoutInflater).apply {
        vBind = this
        rvPersons.adapter = adapterPerson
        adapterPerson.onChangeAmountPersonal { d, l ->
            amounts[l] = d
            vBind.tvTotalPay.text = "P. ${amounts.values.sum().formatPrice}"
        }
        btnSave.setOnClickListener {
            vModel.saveForm()
            dismiss()

        }
    }.root


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vModel.personSave.observe(this,adapterPerson::setPerson)
        vModel.charges.observe(this){
            vBind.tvTotal.text = "T. ${it.filter { c -> c.checked }.sumOf { c -> c.total }.formatPrice}"
        }
    }


    fun show(manager: FragmentManager) {
        super.show(manager, this::class.java.simpleName)
    }

}