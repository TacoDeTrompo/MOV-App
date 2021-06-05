package com.sarpjfhd.cashwise.models

import com.google.gson.annotations.SerializedName

class User(token: String, fullName: String, lastName: String, username: String, email: String, imgArray: ByteArray, idDB: Int, cloudID: Int) {
    var idDB: Int = 0
    @SerializedName("token")
    var token: String = ""
    @SerializedName("firstName")
    var fullName: String = ""
    @SerializedName("lastName")
    var lastName: String = ""
    @SerializedName("username")
    var username: String = ""
    @SerializedName("email")
    var email: String = ""
    @SerializedName("imgArray")
    var imgArray: ByteArray
    @SerializedName("cloudId")
    var cloudId: Int = 0
    @SerializedName("encodedImage")
    lateinit var encodedImage: String
    init {
        this.idDB = idDB
        this.token = token
        this.fullName = fullName
        this.lastName = lastName
        this.username = username
        this.email = email
        this.imgArray = imgArray
        this.cloudId = cloudID
    }
}