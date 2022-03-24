package com.lubaspc.traveltolucos.ui.adapter

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.lubaspc.traveltolucos.R
import com.lubaspc.traveltolucos.databinding.ItemChargeDayBinding
import com.lubaspc.traveltolucos.databinding.ItemChargesBinding
import com.lubaspc.traveltolucos.model.ChargeDayMD
import com.lubaspc.traveltolucos.model.PersonMD
import com.lubaspc.traveltolucos.room.TypeCharge
import com.lubaspc.traveltolucos.utils.formatPrice
import com.lubaspc.traveltolucos.utils.getTotal

class SavePersonAdapter : RecyclerView.Adapter<SavePersonAdapter.ViewHolder>() {
    private var onChange: ((Double, Long) -> Unit)? = null
    private val chargesDay = mutableListOf<PersonMD>()

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val vBind = ItemChargeDayBinding.bind(v)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_charge_day, parent, false)
        )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SavePersonAdapter.ViewHolder, position: Int) {
        holder.vBind.apply {
            chargesDay[position].let {
                tvTotal.text = it.name
                refreshTotal(tvTotalPay,it)
                it.listCharges.filter { c -> c.type == TypeCharge.PERSONAL }.forEach { c ->
                    val vBindItem = ItemChargesBinding.bind(
                        LayoutInflater.from(this.root.context)
                            .inflate(R.layout.item_charges, ll, false)
                    )
                    vBindItem.cv.isCheckable = true
                    vBindItem.tv.text = c.description
                    vBindItem.edit.setText(c.total.formatPrice)
                    vBindItem.cv.setOnClickListener { _ ->
                        vBindItem.cv.isChecked = !vBindItem.cv.isChecked
                        c.checked = vBindItem.cv.isChecked
                        refreshTotal(tvTotalPay,it)
                    }
                    vBindItem.edit.addTextChangedListener { editable ->
                        if (vBindItem.cv.isChecked) {
                            c.total =
                                editable?.toString()?.replace("$", "")?.toDoubleOrNull() ?: 0.0
                            refreshTotal(tvTotalPay,it)
                        }
                    }
                    ll.addView(vBindItem.root, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT))
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun refreshTotal(tv: TextView, it :PersonMD){
        onChange?.invoke(it.getTotal(chargesDay.size + 1),it.personId)
        tv.text = "T. ${it.getTotal(chargesDay.size + 1).formatPrice}"
    }

    override fun getItemCount() = chargesDay.size

    @SuppressLint("NotifyDataSetChanged")
    fun setPerson(persons: List<PersonMD>) {
        chargesDay.clear()
        chargesDay.addAll(persons)
        notifyDataSetChanged()
    }

    fun onChangeAmountPersonal(cb: (Double,Long) -> Unit) {
        this.onChange = cb
    }

}