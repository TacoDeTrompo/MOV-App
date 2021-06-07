package com.sarpjfhd.cashwise.fragments

import android.widget.TextView
import java.math.BigDecimal

interface CardOnClickListener {
    fun onCardClickListener(profileId: Int)
    fun getTotalAmount(profileId: Int, text: TextView)
}