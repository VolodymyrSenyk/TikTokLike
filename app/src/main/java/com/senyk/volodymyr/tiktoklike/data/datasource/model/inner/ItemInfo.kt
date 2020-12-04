package com.senyk.volodymyr.tiktoklike.data.datasource.model.inner

import com.google.gson.annotations.SerializedName

data class ItemInfo (
	@SerializedName("itemStruct") val itemStruct : ItemStruct?
)
