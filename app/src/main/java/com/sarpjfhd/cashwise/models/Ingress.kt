package com.sarpjfhd.cashwise.models

import java.math.BigDecimal
import java.time.LocalDate

class Ingress(name: String, amount: BigDecimal, transactionDate: LocalDate, description: String, parentProfile: Profile?, transactionType: TransactionTypes): Transaction() {
    var description: String = ""
    init {
        this.name = name
        this.amount = amount
        this.transactionDate = transactionDate
        this.description = description
        if (parentProfile != null) {
            this.parentProfile = parentProfile
        }
        this.transactionType = transactionType
    }
}