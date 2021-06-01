package com.sarpjfhd.cashwise.models

enum class ExpenseType(val value: Int) {
    FOOD(0), ENTERTAINMENT(1), SERVICES(2), INVESTMENT(3);
    companion object{
        private val map = ExpenseType.values().associateBy(ExpenseType::value)
        fun fromInt(type: Int) = map[type]
    }
}