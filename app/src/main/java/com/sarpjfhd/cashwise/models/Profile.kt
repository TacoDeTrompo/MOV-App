package com.sarpjfhd.cashwise.models

import android.graphics.Color
import java.time.LocalDate

class Profile(profileName: String, dayRange: Int, startDate: LocalDate, color: String, description: String): Cloneable {
    var idBD: Int = 0
    var profileName: String = ""
    var descrption: String = ""
    var dayRange: Int
    var startDate: LocalDate
    var color: String
    init {
        this.profileName = profileName
        this.dayRange = dayRange
        this.startDate = startDate
        this.color = color
        this.descrption = descrption
    }

    public override fun clone(): Profile {
        return Profile(this.profileName, this.dayRange, this.startDate, this.color, this.descrption)
    }

    fun makeNew(profileName: String, dayRange: Int, color: String, description: String) {
        this.profileName = profileName
        this.dayRange = dayRange
        this.startDate = LocalDate.now()
        this.color = color
        this.descrption = descrption
    }
}