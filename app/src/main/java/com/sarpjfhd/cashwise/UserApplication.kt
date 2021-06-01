package com.sarpjfhd.cashwise

import android.app.Application
import com.sarpjfhd.cashwise.data.DataDbHelper

class UserApplication : Application()  {

    companion object{
        lateinit var dbHelper: DataDbHelper
    }

    override fun onCreate() {
        super.onCreate()
        dbHelper =  DataDbHelper(applicationContext)
    }
}