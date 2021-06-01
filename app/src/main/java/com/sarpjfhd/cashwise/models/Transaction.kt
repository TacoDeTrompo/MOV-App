package com.sarpjfhd.cashwise.models

import android.graphics.Color
import java.math.BigDecimal
import java.time.LocalDate

open class Transaction {
    var idBD: Int = 0
    var name: String = ""
    var transactionType: TransactionTypes = TransactionTypes.INGRESS
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