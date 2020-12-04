package com.senyk.volodymyr.tiktoklike.data.datasource.model.response

import com.google.gson.annotations.SerializedName
import com.senyk.volodymyr.tiktoklike.data.datasource.model.inner.ItemInfo
import com.senyk.volodymyr.tiktoklike.data.datasource.model.inner.ShareMeta

data class VideoDetailsResponse (
	@SerializedName("statusCode") val statusCode : String?,
	@SerializedName("itemInfo") val itemInfo : ItemInfo?,
	@SerializedName("shareMeta") val shareMeta : ShareMeta?
)
