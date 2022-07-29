package com.lubaspc.traveltolucos.firebase

import android.util.Log
import com.google.firebase.database.FirebaseDatabase

object RealTime {
    private val database by lazy {
        FirebaseDatabase.getInstance()
    }

    fun getPersons(){
        database.getReference("persons").get().addOnSuccessListener {
            Log.d("PEROSNS",(it.value as? HashMap<String,*>)?.keys?.joinToString(",") ?: "")
        }.addOnFailureListener {
            throw it
        }
    }

    fun getWeeks(person: String){
        database.getReference("persons").child(person).get().result
    }

    fun saveWeek(person: String,){
        database.getReference("persons").child(person).setValue("")
    }
}