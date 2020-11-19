package com.senyk.volodymyr.tiktoklike.data.datasource

import com.google.gson.annotations.SerializedName

data class SetLikeResponse(
    @SerializedName("is_digg") val isDigg: Int, // 0 if liked, 1 if not liked
    @SerializedName("extra") val extraResponse: ExtraResponse,
    @SerializedName("log_pb") val logPbResponse: LogPbResponse,
    @SerializedName("status_code") val statusCode: String // 0 if all is good
)
