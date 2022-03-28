package com.lubaspc.traveltolucos.utils

import android.content.SharedPreferences
import androidx.core.content.edit
import com.lubaspc.traveltolucos.service.model.Tag

fun SharedPreferences.saveTags(tags: List<Tag>) {
    edit {
        putStringSet(
            "TAGS",
            tags.map { "${it.nombre}&${it.fullNumero}&${it.isActivo}&${it.tieneAdeudos}" }
                .toSet()
        )
    }
}