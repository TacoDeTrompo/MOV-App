package com.sarpjfhd.cashwise.models

import java.math.BigDecimal
import java.time.LocalDate

class TransactionFactory {
    companion object {
        fun createTransaction(type: TransactionTypes?, amount: BigDecimal, transactionDate: LocalDate, expenseType: ExpenseType?, description: String, parentProfile: Profile?): Transaction {
            return when (type) {
                TransactionTypes.EXPENSE -> Expense("", amount, transactionDate, expenseType, description, parentProfile, type)
                TransactionTypes.INGRESS -> Ingress("", amount, transactionDate, description, parentProfile, type)
                null -> Transaction()
                else -> {
                    Transaction()
                }
            }
        }
        fun createExpense(type: TransactionTypes?, name: String, amount: BigDecimal, transactionDate: LocalDate, expenseType: ExpenseType?, description: String, parentProfile: Profile?): Expense {
            return Expense(name, amount, transactionDate, expenseType, description, parentProfile, type!!)
        }
        fun createIngress(type: TransactionTypes?, name: String, amount: BigDecimal, transactionDate: LocalDate, expenseType: ExpenseType?, description: String, parentProfile: Profile?): Ingress {
            return Ingress(name, amount, transactionDate, description, parentProfile, type!!)
        }
    }
}