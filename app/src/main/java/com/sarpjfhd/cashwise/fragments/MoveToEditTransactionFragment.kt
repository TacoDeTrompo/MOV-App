package com.sarpjfhd.cashwise.fragments

import com.sarpjfhd.cashwise.models.Transaction

interface MoveToEditTransactionFragment {
    fun onUpdateClick(isExpense: Boolean, transactionId: Int)
    fun onDeleteClick(transactionId: Int, transactions: MutableList<Transaction>, index: Int)
}