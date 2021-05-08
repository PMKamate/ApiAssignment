package com.demo.testassignment.model.book

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SearchInfo {

    @SerializedName("textSnippet")
    @Expose
    var textSnippet: String? = null
}