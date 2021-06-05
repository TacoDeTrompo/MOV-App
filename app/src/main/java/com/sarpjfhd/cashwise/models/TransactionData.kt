package com.sarpjfhd.cashwise.models

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

class TransactionData {
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
    @SerializedName("description")
    var description: String = ""
    @SerializedName("expenseType")
    var expenseType: ExpenseType = ExpenseType.INVESTMENT
}