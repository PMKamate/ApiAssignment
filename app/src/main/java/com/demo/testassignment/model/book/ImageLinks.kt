package com.demo.testassignment.model.book

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ImageLinks {

    @SerializedName("smallThumbnail")
    @Expose
    var smallThumbnail: String? = null

    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String? = null
}