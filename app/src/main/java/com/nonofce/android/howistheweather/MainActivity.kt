package com.nonofce.android.howistheweather

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.nonofce.android.howistheweather.data.response.WeatherResponse
import com.nonofce.android.howistheweather.view.WeatherViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val weatherViewModel: WeatherViewModel by viewModel<WeatherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            weatherViewModel.weatherInformation.observe(this, androidx.lifecycle.Observer {
                updateUI(it)
            })
        }

        weatherViewModel.getWeatherInformation("3553478").observe(this, androidx.lifecycle.Observer {
            updateUI(it)
        })


//        weatherViewModel.getWeatherInformation("3553478").observe(this, androidx.lifecycle.Observer {
//            Snackbar.make(fab, "Replace with your own action " + it.name, Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        })


    }

    fun updateUI(response: WeatherResponse){
        Snackbar.make(fab, getString(R.string.responseReceived), Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()

        currentTemp.text = response.main.temp.toString()
        pressure.text = response.main.pressure.toString()
        humidity.text = response.main.humidity.toString()
        minimunTemp.text = response.main.temp_min.toString()
        maximumTemp.text = response.main.temp_max.toString()
        val conditionUrl = "https://openweathermap.org/img/wn/"+response.weather[0].icon+"@2x.png"
        Picasso.get().load(conditionUrl)
            .resize(100,100)
//            .fit()
            .centerCrop()
            .into(weatherCondition)
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
}
