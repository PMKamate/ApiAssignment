package com.demo.testassignment.model.book

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PanelizationSummary {
    @SerializedName("containsEpubBubbles")
    @Expose
    var containsEpubBubbles: Boolean? = null

    @SerializedName("containsImageBubbles")
    @Expose
    var containsImageBubbles: Boolean? = null
}