package com.demo.testassignment.model.book

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ListPrice {
    @SerializedName("amount")
    @Expose
    var amount: Float? = null

    @SerializedName("currencyCode")
    @Expose
    var currencyCode: String? = null
}