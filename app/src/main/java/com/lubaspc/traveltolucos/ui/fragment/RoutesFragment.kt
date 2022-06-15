package com.lubaspc.traveltolucos.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.lubaspc.traveltolucos.R

class RoutesFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.routes_preferences, rootKey)

        val onClick = Preference.OnPreferenceClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        when (it.key) {
                            "all_ht" ->
                                "https://www.google.com/maps/dir/?api=1&destination=19.309204,-99.187311"
                            "three_ht" -> "https://www.google.com/maps/dir/?api=1&destination=19.3367552,-99.2127283&waypoints=19.2979665,-99.3955443"
                            "glorieta" -> "https://www.google.com/maps/dir/?api=1&destination=19.3553168,-99.2731229"
                            "two_ht" -> "https://www.google.com/maps/dir/?api=1&destination=19.3367552,-99.2127283&waypoints=19.2979665,-99.3955443%7C19.299234,-99.3455663%7C19.3559372,-99.3006196"
                            "free_ht" -> "https://www.google.com/maps/dir/?api=1&destination=19.3367552,-99.2127283&waypoints=19.2979665,-99.3955443%7C19.299234,-99.3455663%7C19.3559372,-99.3006196%7c19.410498, -99.194206"
                            else ->
                                "https://www.google.com/maps/dir/?api=1&destination=19.309204,-99.187311"
                        }
                    )
                ).setPackage("com.google.android.apps.maps")
            )
            return@OnPreferenceClickListener true
        }

        findPreference<Preference>("all_ht")?.onPreferenceClickListener = onClick
        findPreference<Preference>("glorieta")?.onPreferenceClickListener = onClick
        findPreference<Preference>("three_ht")?.onPreferenceClickListener = onClick
        findPreference<Preference>("two_ht")?.onPreferenceClickListener = onClick
        findPreference<Preference>("free_ht")?.onPreferenceClickListener = onClick
    }
}