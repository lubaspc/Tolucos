package com.lubaspc.traveltolucos.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class PersonMD(
    var personId: Long,
    var name: String,
    var phone: String,
    var checked: MutableState<Boolean> = mutableStateOf(false),
    var listCharges: SnapshotStateList<ChargeMD> = mutableStateListOf()
)