package com.nonofce.android.howistheweather.view

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.nonofce.android.howistheweather.R
import com.nonofce.android.howistheweather.domain.WorldCity
import kotlinx.android.synthetic.main.city_spinner.view.*

class CitySpinnerAdapter(ctx: Context, cities: List<WorldCity>) : ArrayAdapter<WorldCity>(ctx, 0, cities) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {

        val city = getItem(position)

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.city_spinner, parent, false)

        city?.let {
            view.cityName.text = context.getString(R.string.cities_identifier, it.name)
            view.cityCode.text = "  "
        }

        return view
    }
}