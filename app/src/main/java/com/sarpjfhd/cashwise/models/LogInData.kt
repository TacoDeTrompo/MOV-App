package com.sarpjfhd.cashwise.models

import com.google.gson.annotations.SerializedName

class LogInData(email: String, password: String) {
    @SerializedName("email")
    var email = email
    @SerializedName("password")
    var password = password
}