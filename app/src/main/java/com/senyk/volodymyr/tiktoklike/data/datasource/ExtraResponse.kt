package com.senyk.volodymyr.tiktoklike.data.datasource

import com.google.gson.annotations.SerializedName

data class ExtraResponse(
    @SerializedName("now") val now: String,
    @SerializedName("fatal_item_ids") val fatalItemsIds: List<String>,
    @SerializedName("logid") val logId: String
)
