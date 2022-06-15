package com.lubaspc.traveltolucos.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lubaspc.traveltolucos.R
import com.lubaspc.traveltolucos.databinding.ItemPersonBinding
import com.lubaspc.traveltolucos.model.PersonMD

class PersonsAdapter : RecyclerView.Adapter<PersonsAdapter.ViewHolder>() {

    private val persons = mutableListOf<PersonMD>()

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val vBind = ItemPersonBinding.bind(view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_person, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        persons[position].let {
            holder.vBind.apply {
                tv.text = it.name
                cv.isChecked = it.checked
                cv.setOnClickListener { _ ->
                    it.checked = !it.checked
                    notifyItemChanged(position)
                }
            }
        }
    }

    override fun getItemCount() = persons.size

    @SuppressLint("NotifyDataSetChanged")
    fun addPersons(persons: List<PersonMD>) {
        this.persons.clear()
        this.persons.addAll(persons)
        notifyDataSetChanged()
    }
}