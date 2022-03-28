package com.lubaspc.traveltolucos.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

class ClassName {
    companion object{
        @JvmStatic
        fun static(context: Context,str: String){
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
        }
    }
}