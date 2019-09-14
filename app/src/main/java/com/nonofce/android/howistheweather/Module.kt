package com.nonofce.android.howistheweather

import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.nonofce.android.howistheweather.data.WeatherApi
import com.nonofce.android.howistheweather.repository.WeatherRepository
import com.nonofce.android.howistheweather.view.WeatherViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val viewModelModule = module {
    viewModel {
        WeatherViewModel(get())
    }
}

val repositoryModule = module {
    single {
        WeatherRepository(get())
    }
}

val apiModule = module {
    fun provideApi(retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }

    single {
        provideApi(get())
    }
}

var retrofitModule = module {

    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(
                GsonConverterFactory
                    .create(Gson())
            )
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
    }

    single {
        provideRetrofit()
    }
}