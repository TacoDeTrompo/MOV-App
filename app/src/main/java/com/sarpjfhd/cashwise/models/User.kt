package com.sarpjfhd.cashwise.models

class User(token: String, fullName: String, username: String, email: String, imgArray: ByteArray) {
    var token: String = ""
    var fullName: String = ""
    var username: String = ""
    var email: String = ""
    var imgArray: ByteArray
    init {
        this.token = token
        this.fullName = fullName
        this.username = username
        this.email = email
        this.imgArray = imgArray
    }
}