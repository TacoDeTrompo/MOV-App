package com.sarpjfhd.cashwise

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sarpjfhd.cashwise.models.Expense

class MainViewModel: ViewModel() {
    val modelo = MutableLiveData<String>()
    var profileId: Int = 0
}