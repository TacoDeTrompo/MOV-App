package com.sarpjfhd.cashwise.models

class Advice(imgArray: ByteArray, title: String, content: String) {
    var imgArray: ByteArray
    var title: String
    var content: String
    init {
        this.imgArray = imgArray
        this.title = title
        this.content = content
    }
}