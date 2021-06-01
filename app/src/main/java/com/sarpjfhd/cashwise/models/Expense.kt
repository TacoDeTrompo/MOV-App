package com.sarpjfhd.cashwise.models

import java.math.BigDecimal
import java.time.LocalDate

class Expense(name: String, amount: BigDecimal, transactionDate: LocalDate, type: ExpenseType?, description: String, parentProfile: Profile?, transactionType: TransactionTypes): Transaction() {
    var description: String = ""
    var type: ExpenseType = ExpenseType.FOOD
    init {
        this.name = name
        this.amount = amount
        this.transactionDate = transactionDate
        if (type != null) {
            this.type = type
        }
        this.description = description
        if (parentProfile != null) {
            this.parentProfile = parentProfile
        }
        this.transactionType = transactionType
    }
}