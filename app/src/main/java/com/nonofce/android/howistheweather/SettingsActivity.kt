package com.nonofce.android.howistheweather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.DropDownPreference
import androidx.preference.PreferenceFragmentCompat
import com.nonofce.android.howistheweather.view.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        private val settingsViewModel: SettingsViewModel by viewModel<SettingsViewModel>()

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            settingsViewModel.getListOfCities(context!!.assets)

            val cityNames = arrayOfNulls<String>(settingsViewModel.cityList.size)
            val cityCodes = arrayOfNulls<String>(settingsViewModel.cityList.size)
            for (i in 0 until settingsViewModel.cityList.size){
                cityNames[i] = settingsViewModel.cityList[i].name
                //cityCodes[i] = settingsViewModel.cityList[i].id.toString()
                cityCodes[i] = i.toString()
            }

            val citiesDrop = findPreference<DropDownPreference>("preferredCity")
            citiesDrop?.entries = cityNames
            citiesDrop?.entryValues = cityCodes

        }
    }
}