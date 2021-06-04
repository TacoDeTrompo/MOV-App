package com.sarpjfhd.cashwise.models

class User(token: String, fullName: String, lastName: String, username: String, email: String, imgArray: ByteArray, idDB: Int) {
    var idDB: Int = 0
    var token: String = ""
    var fullName: String = ""
    var lastName: String = ""
    var username: String = ""
    var email: String = ""
    var imgArray: ByteArray
    init {
        this.idDB = idDB
        this.token = token
        this.fullName = fullName
        this.lastName = lastName
        this.username = username
        this.email = email
        this.imgArray = imgArray
    }
}