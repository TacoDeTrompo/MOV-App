package com.sarpjfhd.cashwise.fragments

import com.sarpjfhd.cashwise.models.Transaction

interface MoveToEditTransactionFragment {
    fun onUpdateClick()
    fun onDeleteClick(transactionId: Int, transactions: MutableList<Transaction>, index: Int)
}