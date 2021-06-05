package com.sarpjfhd.cashwise.data

class SetDB {

    companion object{
        val DB_NAME =  "bdCashWise"
        val DB_VERSION =  1
    }

    //VAMOS ES DEFINIR EL ESQUEMA DE UNA DE LAS TABLAS
    abstract class tblProfile{
        //DEFINIMOS LOS ATRIBUTOS DE LA CLASE USANDO CONTANTES
        companion object{
            val TABLE_NAME = "Profiles"
            val COL_ID =  "intID"
            val COL_NAME =  "strName"
            val COL_DESCRIPTION = "strDescription"
            val COL_DAYRANGE = "intDayRange"
            val COL_STARTDAY = "dateStartDay"
            val COL_COLOR =  "strColor"
        }
    }

    abstract class tblTransaction{
        //DEFINIMOS LOS ATRIBUTOS DE LA CLASE USANDO CONTANTES
        companion object{
            val TABLE_NAME = "Transactions"
            val COL_ID =  "intID"
            val COL_NAME =  "strName"
            val COL_DESCRIPTION = "strDescription"
            val COL_AMOUNT =  "floatAmount"
            val COL_DATE = "dateDate"
            val COL_IDEXPENSETYPE =  "intIdType"
            val COL_TTYPE =  "intTransactionType" // byte Array image
            val COL_IDPROFILE = "intIdProfile"
        }
    }

    abstract class tblExpenseType{
        companion object{
            val TABLE_NAME = "ExpenseType"
            val COL_ID =  "intID"
            val COL_TITLE =  "strTitle"
            val COL_VALUE = "intValue"
        }
    }

    abstract class tblUserData{
        companion object{
            val TABLE_NAME = "UserData"
            val COL_ID =  "intID"
            val COL_NAME =  "strName"
            val COL_LAST_NAME = "strLastName"
            val COL_USERNAME = "strUsername"
            val COL_EMAIL =  "strEmail"
            val COL_TOKEN = "strToken"
            val COL_IMG = "imgArray"
            val COL_CLOUD_ID = "intCloudID"
        }
    }
}