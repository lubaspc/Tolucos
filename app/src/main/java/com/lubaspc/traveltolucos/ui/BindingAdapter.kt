package com.lubaspc.traveltolucos.ui

import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.databinding.InverseBindingAdapter
import com.lubaspc.traveltolucos.utils.formatPrice

@InverseBindingAdapter(attribute = "total")
fun EditText.editText() = text.toString().replace("$","").toDoubleOrNull() ?: 0.0

@BindingAdapter("total")
fun EditText.editText(total: Double?){
    setText(total?.formatPrice ?: "")
}