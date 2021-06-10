package com.sarpjfhd.cashwise.models

import android.graphics.Color
import com.google.gson.annotations.SerializedName
import java.time.LocalDate

class Profile(profileName: String, dayRange: Int, startDate: LocalDate, color: String, description: String): Cloneable {
    @SerializedName("idBD")
    var idBD: Int = 0
    @SerializedName("profileName")
    var profileName: String = ""
    @SerializedName("description")
    var descrption: String = ""
    @SerializedName("dayRange")
    var dayRange: Int
    @SerializedName("startDate")
    var startDate: LocalDate
    @SerializedName("color")
    var color: String
    @SerializedName("userId")
    var userID: Int = 0
    init {
        this.profileName = profileName
        this.dayRange = dayRange
        this.startDate = startDate
        this.color = color
        this.descrption = description
    }

    public override fun clone(): Profile {
        return Profile(this.profileName, this.dayRange, this.startDate, this.color, this.descrption)
    }

    fun makeNew(profileName: String, dayRange: Int, color: String, description: String, idDB: Int) {
        this.profileName = profileName
        this.dayRange = dayRange
        this.startDate = LocalDate.now()
        this.color = color
        this.descrption = description
        this.idBD = idDB
    }
}