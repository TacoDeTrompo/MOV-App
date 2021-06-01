package com.sarpjfhd.cashwise.models

enum class TransactionTypes(val value: Int) {
    EXPENSE(0),
    INGRESS(1);
    companion object{
        private val map = TransactionTypes.values().associateBy(TransactionTypes::value)
        fun fromInt(type: Int) = map[type]
    }
}