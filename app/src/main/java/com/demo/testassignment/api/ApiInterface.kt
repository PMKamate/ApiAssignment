package com.demo.testassignment.api

import com.demo.testassignment.model.book.BookItem
import com.demo.testassignment.model.news.News
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("top-headlines")
    fun getNews(
        @Query("country") country: String?,
        @Query("apiKey") apiKey: String?
    ): Call<News?>?

    @GET("books/v1/volumes")
    fun getBook(
        @Query("q") q: String?): Call<BookItem?>?

}