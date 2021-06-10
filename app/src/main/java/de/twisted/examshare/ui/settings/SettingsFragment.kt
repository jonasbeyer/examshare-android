package de.twisted.examshare.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.core.net.toUri
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import de.twisted.examshare.R
import de.twisted.examshare.util.Preferences

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

        findPreference<Preference>("imprint")?.setOnPreferenceClickListener {
            openWebsite(Preferences.IMPRINT_URL)
            true
        }

        findPreference<Preference>("privacy_policy")?.setOnPreferenceClickListener {
            openWebsite(Preferences.PRIVACY_URL)
            true
        }
    }

    private fun openWebsite(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(intent)
    }
}