package com.demo.testassignment.model.book

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RetailPrice__1 {
    @SerializedName("amountInMicros")
    @Expose
    var amountInMicros = Long

    @SerializedName("currencyCode")
    @Expose
    var currencyC = String

}