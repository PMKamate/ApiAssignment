package com.demo.testassignment.model.book

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReadingModes {

    @SerializedName("text")
    @Expose
    var text: Boolean? = null

    @SerializedName("image")
    @Expose
    var image: Boolean? = null
}