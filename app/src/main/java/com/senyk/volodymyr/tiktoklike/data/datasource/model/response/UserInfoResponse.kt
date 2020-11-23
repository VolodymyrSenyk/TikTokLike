package com.senyk.volodymyr.tiktoklike.data.datasource.model.response

import com.google.gson.annotations.SerializedName
import com.senyk.volodymyr.tiktoklike.data.datasource.model.inner.UserInfo

data class UserInfoResponse(
	@SerializedName("statusCode") val statusCode: String?,
	@SerializedName("userInfo") val userInfo: UserInfo?
)
