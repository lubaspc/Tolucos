package com.lubaspc.traveltolucos.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceFragmentCompat
import com.lubaspc.traveltolucos.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<FrameLayout>(android.R.id.list_container)
            .setBackgroundColor(ContextCompat.getColor(view.context, R.color.black))
    }
}