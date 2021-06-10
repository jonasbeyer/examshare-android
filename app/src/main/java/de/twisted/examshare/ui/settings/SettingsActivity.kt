package de.twisted.examshare.ui.settings

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commitNow
import de.twisted.examshare.R
import de.twisted.examshare.ui.shared.base.ExamActivity

class SettingsActivity : ExamActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager.commitNow {
            replace(R.id.settings_container, SettingsFragment())
        }
    }
}