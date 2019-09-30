package com.nonofce.android.howistheweather

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.nonofce.android.howistheweather.data.response.WeatherResponse
import com.nonofce.android.howistheweather.databinding.ActivityMainBinding
import com.nonofce.android.howistheweather.databinding.ContentMainBinding
import com.nonofce.android.howistheweather.domain.CurrentWeather
import com.nonofce.android.howistheweather.domain.WorldCity
import com.nonofce.android.howistheweather.view.CitySpinnerAdapter
import com.nonofce.android.howistheweather.view.WeatherViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val weatherViewModel: WeatherViewModel by viewModel<WeatherViewModel>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var inner: ContentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        inner = binding.inner as ContentMainBinding

        setSupportActionBar(toolbar)


        binding.fab.setOnClickListener {

            val preferredCity =
                PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
                    .getString("preferredCity", null)
            if (preferredCity == null) {
                Snackbar.make(
                    binding.fab,
                    "Go to Settings and select your preferred city",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Action", null).show()
                return@setOnClickListener
            }

            inner.citySpinner.setSelection(preferredCity.toInt())

        }

        with(weatherViewModel) {
            currentWeather.body?.let {
                inner.weatherMainData = it.main
            }

            icon?.let {
                inner.weatherCondition.loadUrl(it)
            }
        }

        loadCities()
    }

    private fun processResponse(currentWeather: CurrentWeather) {

        currentWeather.body?.let {
            updateUI(it)
        } ?: Snackbar.make(binding.fab, currentWeather.message, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

    private fun updateUI(response: WeatherResponse) {
        Snackbar.make(binding.fab, getString(R.string.responseReceived), Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()

        with(response.main) {
            inner.weatherMainData = this
        }

        weatherViewModel.icon = response.weather[0].icon + "@2x.png"
        inner.weatherCondition.loadUrl(weatherViewModel.icon as String)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val settingIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingIntent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun loadCities() {
        weatherViewModel.getListOfCities(assets)
        val me = this
        inner.citySpinner.apply {
            adapter = CitySpinnerAdapter(me, weatherViewModel.cityList)
            setSelection(0, false)

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position != weatherViewModel.currentSelection) {
                        val selectedCity = parent!!.getItemAtPosition(position) as WorldCity

                        weatherViewModel.getWeatherInformation("" + selectedCity.id)
                            .observe(me, androidx.lifecycle.Observer {
                                processResponse(it)
                            })

                        weatherViewModel.currentSelection = position

                    }
                }
            }
        }

        /*
        Gson().fromJson(assets.open("city.list.json").bufferedReader(), WorldCity::class.java)
        */

    }

    fun ImageView.loadUrl(icon: String) {
        Picasso.get().load("https://openweathermap.org/img/wn/" + icon)
            .resize(50, 50)
            .centerCrop()
            .into(this)
    }

}
