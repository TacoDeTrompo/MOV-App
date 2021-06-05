package com.sarpjfhd.cashwise.models

import com.google.gson.annotations.SerializedName

class PasswordReset(token: String, newPassword: String, userId: Int) {
    @SerializedName("token")
    var token: String
    @SerializedName("newPassword")
    var newPassword: String
    @SerializedName("userId")
    var userId: Int
    init {
        this.token = token
        this.newPassword = newPassword
        this.userId = userId
    }
}