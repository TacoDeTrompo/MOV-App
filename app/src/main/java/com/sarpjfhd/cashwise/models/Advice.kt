package com.sarpjfhd.cashwise.models

import com.google.gson.annotations.SerializedName

class Advice(imgArray: ByteArray, title: String, content: String) {
    @SerializedName("imgArray")
    var imgArray: ByteArray
    @SerializedName("title")
    var title: String
    @SerializedName("content")
    var content: String
    @SerializedName("encodedImage")
    var encodedImage: String = ""
    init {
        this.imgArray = imgArray
        this.title = title
        this.content = content
    }
}