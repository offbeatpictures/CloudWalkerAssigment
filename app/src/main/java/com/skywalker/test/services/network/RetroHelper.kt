package com.skywalker.test.services.network

import com.skywalker.test.services.interfaces.ApiInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroHelper {

    private var retrofit: Retrofit? = null;
    private const val BASE_URL: String = "https://jsonplaceholder.typicode.com/";
    val apiInterface: ApiInterface = getRetroInstance().create(ApiInterface::class.java)

    private fun getRetroInstance(): Retrofit = synchronized(this) {

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }

        return retrofit!!
    }


}