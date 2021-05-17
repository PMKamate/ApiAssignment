package com.demo.testassignment.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object GetRetrofit {
    @Volatile
    var retrofit: Retrofit? = null
    private var baseUrlString = "https://newsapi.org/v2/"

    fun updateBaseUrl(url: String) {
        baseUrlString = url
        retrofit = retrofitObj
    }

    val instance: Retrofit?
        get() {
            if (retrofit == null) {
                synchronized(GetRetrofit::class.java) {
                    if (retrofit == null) {
                        retrofit = retrofitObj
                    }
                }
            }
            return retrofit
        }

    val retrofitObj: Retrofit
        get() {
            val builder = OkHttpClient().newBuilder()
            builder.readTimeout(30, TimeUnit.SECONDS)
            builder.connectTimeout(30, TimeUnit.SECONDS)
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(interceptor)

            //  builder.addInterceptor(new UnauthorisedInterceptor(context));
            val client = builder.build()
            retrofit = Retrofit.Builder().baseUrl(baseUrlString)
                .client(client).addConverterFactory(GsonConverterFactory.create()).build()

            return retrofit as Retrofit
        }
}