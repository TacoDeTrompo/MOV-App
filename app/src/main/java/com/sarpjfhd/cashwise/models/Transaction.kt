package com.sarpjfhd.cashwise.models

import android.graphics.Color
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.time.LocalDate

open class Transaction {
    @SerializedName("profileId")
    var profileId: Int = 0
    @SerializedName("idBD")
    var idBD: Int = 0
    @SerializedName("name")
    var name: String = ""
    @SerializedName("ttype")
    var transactionType: TransactionTypes = TransactionTypes.INGRESS
    @SerializedName("amount")
    var amount: BigDecimal = BigDecimal.ZERO
        get() = field
        set(value) {
            field = value
        }
    var transactionDate = LocalDate.now()
        get() = field
        set(value) {
            field = value
        }
    var parentProfile: Profile = Profile("", 0, LocalDate.now(), "", "")
        get() = field
        set(value) {
            field = value
        }
}