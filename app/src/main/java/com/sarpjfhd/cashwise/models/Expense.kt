package com.sarpjfhd.cashwise.models

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.time.LocalDate

class Expense(name: String, amount: BigDecimal, transactionDate: LocalDate, type: ExpenseType?, description: String, parentProfile: Profile?, transactionType: TransactionTypes): Transaction() {
    @SerializedName("description")
    var description: String = ""
    @SerializedName("expenseType")
    var type: ExpenseType = ExpenseType.FOOD
    init {
        @SerializedName("name")
        this.name = name
        @SerializedName("amount")
        this.amount = amount
        @SerializedName("transactionDate")
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