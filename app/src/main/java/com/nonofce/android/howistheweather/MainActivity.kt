package com.nonofce.android.howistheweather

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.nonofce.android.howistheweather.data.response.WeatherResponse
import com.nonofce.android.howistheweather.databinding.ActivityMainBinding
import com.nonofce.android.howistheweather.domain.CurrentWeather
import com.nonofce.android.howistheweather.domain.WorldCity
import com.nonofce.android.howistheweather.view.CitySpinnerAdapter
import com.nonofce.android.howistheweather.view.WeatherViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val weatherViewModel: WeatherViewModel by viewModel<WeatherViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(toolbar)

        binding.fab.setOnClickListener {

            val pos = (citySpinner.adapter as CitySpinnerAdapter).getPosition(weatherViewModel.defaultCity)
            binding.inner.citySpinner.setSelection(pos)

        }

        with(weatherViewModel) {
            currentWeather.body?.let {
                binding.inner.weatherMainData = it.main
            }

            icon?.let {
                loadWeatherCondition(it)
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
            binding.inner.weatherMainData = this
        }

        weatherViewModel.icon = response.weather[0].icon + "@2x.png"
        loadWeatherCondition(weatherViewModel.icon as String)
    }

    private fun loadWeatherCondition(icon: String) {
        Picasso.get().load("https://openweathermap.org/img/wn/" + icon)
            .resize(50, 50)
            .centerCrop()
            .into(binding.inner.weatherCondition)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadCities() {
//        val cityList: MutableList<WorldCity> = getListOfCities()
        weatherViewModel.getListOfCities(assets)
        val me = this
        binding.inner.citySpinner.apply {
            adapter = CitySpinnerAdapter(me, weatherViewModel.cityList)
            setSelection(0, false)

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
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

}
