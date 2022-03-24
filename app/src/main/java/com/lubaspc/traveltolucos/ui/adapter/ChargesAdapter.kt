package com.lubaspc.traveltolucos.ui.adapter

import android.annotation.SuppressLint
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.lubaspc.traveltolucos.R
import com.lubaspc.traveltolucos.databinding.ItemChargesBinding
import com.lubaspc.traveltolucos.model.ChargeMD
import com.lubaspc.traveltolucos.utils.formatPrice

class ChargesAdapter : RecyclerView.Adapter<ChargesAdapter.ViewHolder>() {

    private val charges = mutableListOf<ChargeMD>()
    private val textWachers = mutableListOf<Pair<TextView, TextWatcher>>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val vBind = ItemChargesBinding.bind(view)
        var onTextChange: ((Double) -> Unit)? = null

        init {
            vBind.edit.addTextChangedListener {
                onTextChange?.invoke(it?.toString()?.replace("$","")?.toDoubleOrNull() ?: 0.0)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_charges, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        charges[position].let {
            holder.vBind.apply {
                holder.onTextChange = { total ->
                    it.total = total
                }
                edit.isEnabled = it.price == 0.0
                edit.setText(it.total.formatPrice)
                tv.text = it.description
                cv.isChecked = it.checked
                cv.setOnClickListener { _ ->
                    it.checked = !it.checked
                    notifyItemChanged(position)
                }
            }
        }
    }

    override fun getItemCount() = charges.size

    @SuppressLint("NotifyDataSetChanged")
    fun setCharges(charges: List<ChargeMD>) {
        textWachers.forEach { it.first.removeTextChangedListener(it.second) }
        textWachers.clear()
        this.charges.clear()
        this.charges.addAll(charges)
        notifyDataSetChanged()
    }

    fun getChargesChecked() = charges.filter { it.checked }.map { it.copy() }

}