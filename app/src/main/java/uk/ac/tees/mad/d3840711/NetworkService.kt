package uk.ac.tees.mad.d3840711

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

object NetworkService {

    private const val BASE_URL = "https://newsapi.org"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    val apiService : NewsApiService by lazy {
        retrofit.create(NewsApiService::class.java)
    }
}