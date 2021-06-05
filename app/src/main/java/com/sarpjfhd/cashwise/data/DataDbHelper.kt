package com.sarpjfhd.cashwise.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sarpjfhd.cashwise.MainViewModel
import com.sarpjfhd.cashwise.models.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DataDbHelper(var context: Context): SQLiteOpenHelper(context, SetDB.DB_NAME, null, SetDB.DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        try{

            val createProfileTable:String =  "CREATE TABLE " + SetDB.tblProfile.TABLE_NAME + "(" +
                    SetDB.tblProfile.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SetDB.tblProfile.COL_NAME + " VARCHAR(50)," +
                    SetDB.tblProfile.COL_DESCRIPTION + " VARCHAR(250)," +
                    SetDB.tblProfile.COL_DAYRANGE + " INTEGER," +
                    SetDB.tblProfile.COL_STARTDAY + " DATE," +
                    SetDB.tblProfile.COL_COLOR + " VARCHAR(10))"

            db?.execSQL(createProfileTable)

            val createUserDataTable:String =  "CREATE TABLE " + SetDB.tblUserData.TABLE_NAME + "(" +
                    SetDB.tblUserData.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SetDB.tblUserData.COL_NAME + " VARCHAR(50)," +
                    SetDB.tblUserData.COL_LAST_NAME + " VARCHAR(50)," +
                    SetDB.tblUserData.COL_USERNAME + " VARCHAR(50)," +
                    SetDB.tblUserData.COL_EMAIL + " VARCHAR(50)," +
                    SetDB.tblUserData.COL_TOKEN + " VARCHAR(50)," +
                    SetDB.tblUserData.COL_IMG + " BLOB," +
                    SetDB.tblUserData.COL_CLOUD_ID + " INTEGER)"

            db?.execSQL(createUserDataTable)

            val createTransactionTable:String =  "CREATE TABLE " + SetDB.tblTransaction.TABLE_NAME + "(" +
                    SetDB.tblTransaction.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SetDB.tblTransaction.COL_NAME + " VARCHAR(50)," +
                    SetDB.tblTransaction.COL_DESCRIPTION + " VARCHAR(250)," +
                    SetDB.tblTransaction.COL_AMOUNT + " FLOAT," +
                    SetDB.tblTransaction.COL_DATE + " DATE," +
                    SetDB.tblTransaction.COL_IDEXPENSETYPE + " INTEGER," +
                    SetDB.tblTransaction.COL_TTYPE + " INTEGER," +
                    SetDB.tblTransaction.COL_IDPROFILE + " INTEGER)"

            db?.execSQL(createTransactionTable)

            val createExpenseTypeTable:String =  "CREATE TABLE " + SetDB.tblExpenseType.TABLE_NAME + "(" +
                    SetDB.tblExpenseType.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SetDB.tblExpenseType.COL_TITLE + " VARCHAR(50)," +
                    SetDB.tblExpenseType.COL_VALUE + " INTEGER)"

            db?.execSQL(createExpenseTypeTable)

            Log.e("ENTRO","CREO TABLAS")

        }catch (e: Exception){
            Log.e("Execption", e.toString())
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    public fun insertProfile(profile: Profile): Boolean {
        val dataBase:SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        values.put(SetDB.tblProfile.COL_NAME, profile.profileName)
        values.put(SetDB.tblProfile.COL_DAYRANGE, profile.dayRange)
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        values.put(SetDB.tblProfile.COL_STARTDAY, profile.startDate.format(formatter))
        values.put(SetDB.tblProfile.COL_COLOR, profile.color)
        values.put(SetDB.tblProfile.COL_DESCRIPTION, profile.descrption)

        try {
            val result =  dataBase.insert(SetDB.tblProfile.TABLE_NAME, null, values)

            if (result == (0).toLong()) {
                Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }

        }catch (e: java.lang.Exception){
            Log.e("Execption", e.toString())
            boolResult =  false
        }

        dataBase.close()

        return boolResult
    }

    public fun getListOfProfiles():MutableList<Profile>{
        val List:MutableList<Profile> = ArrayList()

        val dataBase:SQLiteDatabase = this.writableDatabase

        //QUE COLUMNAS QUEREMOS QUE ESTE EN EL SELECT
        val columns:Array<String> =  arrayOf(SetDB.tblProfile.COL_ID,
            SetDB.tblProfile.COL_NAME,
            SetDB.tblProfile.COL_DESCRIPTION,
            SetDB.tblProfile.COL_DAYRANGE,
            SetDB.tblProfile.COL_STARTDAY,
            SetDB.tblProfile.COL_COLOR)

        val data =  dataBase.query(SetDB.tblProfile.TABLE_NAME,
            columns,
            null,
            null,
            null,
            null,
            SetDB.tblProfile.COL_ID + " ASC")

        // SI NO TIENE DATOS DEVUELVE FALSO
        //SE MUEVE A LA PRIMERA POSICION DE LOS DATOS
        if(data.moveToFirst()){

            do{
                var profileName =  data.getString(data.getColumnIndex(SetDB.tblProfile.COL_NAME)).toString()
                var dayRange = data.getString(data.getColumnIndex(SetDB.tblProfile.COL_DAYRANGE)).toInt()
                val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                var startDate =  LocalDate.parse(data.getString(data.getColumnIndex(SetDB.tblProfile.COL_STARTDAY)).toString(), formatter)
                var color =  data.getString(data.getColumnIndex(SetDB.tblProfile.COL_COLOR)).toString()
                var description = data.getString(data.getColumnIndex(SetDB.tblProfile.COL_DESCRIPTION)).toString()
                val profile = Profile(profileName, dayRange, startDate, color, description)
                profile.idBD = data.getString(data.getColumnIndex(SetDB.tblProfile.COL_ID)).toInt()

                List.add(profile)

                //SE MUEVE A LA SIGUIENTE POSICION, REGRESA FALSO SI SE PASO DE LA CANTIDAD DE DATOS
            }while (data.moveToNext())

        }
        return  List
    }

    public fun getProfile(intID:Int):Profile?{
        var profile:Profile? = null
        val dataBase:SQLiteDatabase = this.writableDatabase

        //QUE COLUMNAS QUEREMOS QUE ESTE EN EL SELECT
        val columns:Array<String> =  arrayOf(SetDB.tblProfile.COL_ID,
            SetDB.tblProfile.COL_NAME,
            SetDB.tblProfile.COL_DESCRIPTION,
            SetDB.tblProfile.COL_DAYRANGE,
            SetDB.tblProfile.COL_STARTDAY,
            SetDB.tblProfile.COL_COLOR)

        val where:String =  SetDB.tblProfile.COL_ID + "= ${intID.toString()}"

        val data =  dataBase.query(SetDB.tblProfile.TABLE_NAME,
            columns,
            where,
            null,
            null,
            null,
            SetDB.tblProfile.COL_ID + " ASC")

        if(data.moveToFirst()){
            var profileName =  data.getString(data.getColumnIndex(SetDB.tblProfile.COL_NAME)).toString()
            var dayRange = data.getString(data.getColumnIndex(SetDB.tblProfile.COL_DAYRANGE)).toInt()
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            var startDate =  LocalDate.parse(data.getString(data.getColumnIndex(SetDB.tblProfile.COL_STARTDAY)).toString(), formatter)
            var color =  data.getString(data.getColumnIndex(SetDB.tblProfile.COL_COLOR)).toString()
            var description = data.getString(data.getColumnIndex(SetDB.tblProfile.COL_DESCRIPTION)).toString()
            profile = Profile(profileName, dayRange, startDate, color, description)
            profile.idBD = data.getString(data.getColumnIndex(SetDB.tblProfile.COL_ID)).toInt()

        }

        data.close()
        return profile
    }

    public fun getListOfTransactions(profileId: Int = -1):MutableList<Transaction>{
        val List:MutableList<Transaction> = ArrayList()

        val dataBase:SQLiteDatabase = this.writableDatabase

        //QUE COLUMNAS QUEREMOS QUE ESTE EN EL SELECT
        val columns:Array<String> =  arrayOf(SetDB.tblTransaction.COL_ID,
            SetDB.tblTransaction.COL_NAME,
            SetDB.tblTransaction.COL_DESCRIPTION,
            SetDB.tblTransaction.COL_AMOUNT,
            SetDB.tblTransaction.COL_IDEXPENSETYPE,
            SetDB.tblTransaction.COL_TTYPE,
            SetDB.tblTransaction.COL_IDPROFILE)

        val where: String

        if(profileId == -1) {
            where = null.toString()
        } else {
            where = SetDB.tblTransaction.COL_IDPROFILE + "= ${profileId.toString()}"
        }

        val data =  dataBase.query(SetDB.tblTransaction.TABLE_NAME,
            columns,
            where,
            null,
            null,
            null,
            SetDB.tblTransaction.COL_ID + " ASC")

        // SI NO TIENE DATOS DEVUELVE FALSO
        //SE MUEVE A LA PRIMERA POSICION DE LOS DATOS
        if(data.moveToFirst()){

            do{
                val transactionType = TransactionTypes.fromInt(data.getString(data.getColumnIndex(SetDB.tblTransaction.COL_TTYPE)).toInt())
                var amount = data.getString(data.getColumnIndex(SetDB.tblTransaction.COL_AMOUNT)).toBigDecimal()
                val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                var date =  LocalDate.parse(data.getString(data.getColumnIndex(SetDB.tblTransaction.COL_DATE)).toString(), formatter)
                var expenseType = ExpenseType.fromInt(data.getString(data.getColumnIndex(SetDB.tblTransaction.COL_IDEXPENSETYPE)).toInt())
                var description =  data.getString(data.getColumnIndex(SetDB.tblTransaction.COL_DESCRIPTION)).toString()
                var profile = this.getProfile(data.getString(data.getColumnIndex(SetDB.tblTransaction.COL_IDPROFILE)).toInt())
                val transaction = TransactionFactory.createTransaction(transactionType, amount, date, expenseType, description, profile)
                transaction.idBD = data.getString(data.getColumnIndex(SetDB.tblTransaction.COL_ID)).toInt()

                List.add(transaction)

                //SE MUEVE A LA SIGUIENTE POSICION, REGRESA FALSO SI SE PASO DE LA CANTIDAD DE DATOS
            }while (data.moveToNext())

        }
        return  List
    }

    public fun getListOfExpense(profileId: Int = -1):MutableList<Expense>{
        val List:MutableList<Expense> = ArrayList()

        val dataBase:SQLiteDatabase = this.writableDatabase

        //QUE COLUMNAS QUEREMOS QUE ESTE EN EL SELECT
        val columns:Array<String> =  arrayOf(SetDB.tblTransaction.COL_ID,
            SetDB.tblTransaction.COL_NAME,
            SetDB.tblTransaction.COL_DESCRIPTION,
            SetDB.tblTransaction.COL_AMOUNT,
                SetDB.tblTransaction.COL_DATE,
            SetDB.tblTransaction.COL_IDEXPENSETYPE,
            SetDB.tblTransaction.COL_TTYPE,
            SetDB.tblTransaction.COL_IDPROFILE)

        val where: String

        if(profileId == -1) {
            where = null.toString()
        } else {
            where = SetDB.tblTransaction.COL_IDPROFILE + "= ${profileId.toString()}"
        }

        val data =  dataBase.query(SetDB.tblTransaction.TABLE_NAME,
            columns,
            where,
            null,
            null,
            null,
            SetDB.tblTransaction.COL_ID + " ASC")

        // SI NO TIENE DATOS DEVUELVE FALSO
        //SE MUEVE A LA PRIMERA POSICION DE LOS DATOS
        if(data.moveToFirst()){

            do{
                val transactionType = TransactionTypes.fromInt(data.getString(data.getColumnIndex(SetDB.tblTransaction.COL_TTYPE)).toInt())
                if (transactionType == TransactionTypes.EXPENSE) {
                    var amount = data.getString(data.getColumnIndex(SetDB.tblTransaction.COL_AMOUNT)).toBigDecimal()
                    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    var date =  LocalDate.parse(data.getString(data.getColumnIndex(SetDB.tblTransaction.COL_DATE)).toString(), formatter)
                    var expenseType = ExpenseType.fromInt(data.getString(data.getColumnIndex(SetDB.tblTransaction.COL_IDEXPENSETYPE)).toInt())
                    var description =  data.getString(data.getColumnIndex(SetDB.tblTransaction.COL_DESCRIPTION)).toString()
                    var name =  data.getString(data.getColumnIndex(SetDB.tblTransaction.COL_NAME)).toString()
                    var profile = this.getProfile(data.getString(data.getColumnIndex(SetDB.tblTransaction.COL_IDPROFILE)).toInt())
                    val expense = TransactionFactory.createExpense(transactionType, name, amount, date, expenseType, description, profile)
                    expense.idBD = data.getString(data.getColumnIndex(SetDB.tblTransaction.COL_ID)).toInt()

                    List.add(expense)
                }
            }while (data.moveToNext())

        }
        return  List
    }

    public fun getListOfIngress(profileId: Int = -1):MutableList<Ingress>{
        val List:MutableList<Ingress> = ArrayList()

        val dataBase:SQLiteDatabase = this.writableDatabase

        //QUE COLUMNAS QUEREMOS QUE ESTE EN EL SELECT
        val columns:Array<String> =  arrayOf(SetDB.tblTransaction.COL_ID,
            SetDB.tblTransaction.COL_NAME,
            SetDB.tblTransaction.COL_DESCRIPTION,
            SetDB.tblTransaction.COL_AMOUNT,
                SetDB.tblTransaction.COL_DATE,
            SetDB.tblTransaction.COL_IDEXPENSETYPE,
            SetDB.tblTransaction.COL_TTYPE,
            SetDB.tblTransaction.COL_IDPROFILE)

        val where: String

        if(profileId == -1) {
            where = null.toString()
        } else {
            where = SetDB.tblTransaction.COL_IDPROFILE + "= ${profileId.toString()}"
        }

        val data =  dataBase.query(SetDB.tblTransaction.TABLE_NAME,
            columns,
            where,
            null,
            null,
            null,
            SetDB.tblTransaction.COL_ID + " ASC")

        // SI NO TIENE DATOS DEVUELVE FALSO
        //SE MUEVE A LA PRIMERA POSICION DE LOS DATOS
        if(data.moveToFirst()){

            do{
                val transactionType = TransactionTypes.fromInt(data.getString(data.getColumnIndex(SetDB.tblTransaction.COL_TTYPE)).toInt())
                if (transactionType == TransactionTypes.INGRESS) {
                    var amount = data.getString(data.getColumnIndex(SetDB.tblTransaction.COL_AMOUNT)).toBigDecimal()
                    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    var date =  LocalDate.parse(data.getString(data.getColumnIndex(SetDB.tblTransaction.COL_DATE)).toString(), formatter)
                    var expenseType = ExpenseType.fromInt(data.getString(data.getColumnIndex(SetDB.tblTransaction.COL_IDEXPENSETYPE)).toInt())
                    var description =  data.getString(data.getColumnIndex(SetDB.tblTransaction.COL_DESCRIPTION)).toString()
                    var name =  data.getString(data.getColumnIndex(SetDB.tblTransaction.COL_NAME)).toString()
                    var profile = this.getProfile(data.getString(data.getColumnIndex(SetDB.tblTransaction.COL_IDPROFILE)).toInt())
                    val ingress = TransactionFactory.createIngress(transactionType, name, amount, date, expenseType, description, profile)
                    ingress.idBD = data.getString(data.getColumnIndex(SetDB.tblTransaction.COL_ID)).toInt()

                    List.add(ingress)
                }
            }while (data.moveToNext())

        }
        return  List
    }

    public fun insertExpense(expense: Expense, profileId: Int): Boolean {
        val dataBase:SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        values.put(SetDB.tblTransaction.COL_NAME, expense.name)
        values.put(SetDB.tblTransaction.COL_DESCRIPTION, expense.description)
        values.put(SetDB.tblTransaction.COL_AMOUNT, expense.amount.toFloat())
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        values.put(SetDB.tblTransaction.COL_DATE, expense.transactionDate.format(formatter))
        values.put(SetDB.tblTransaction.COL_TTYPE, expense.transactionType.value)
        values.put(SetDB.tblTransaction.COL_IDEXPENSETYPE, expense.type.value)
        values.put(SetDB.tblTransaction.COL_IDPROFILE, profileId)

        try {
            val result =  dataBase.insert(SetDB.tblTransaction.TABLE_NAME, null, values)

            if (result == (0).toLong()) {
                Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }

        }catch (e: java.lang.Exception){
            Log.e("Execption", e.toString())
            boolResult =  false
        }

        dataBase.close()

        return boolResult
    }

    public fun insertIngress(ingress: Ingress, profileId: Int): Boolean {
        val dataBase:SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        values.put(SetDB.tblTransaction.COL_NAME, ingress.name)
        values.put(SetDB.tblTransaction.COL_DESCRIPTION, ingress.description)
        values.put(SetDB.tblTransaction.COL_AMOUNT, ingress.amount.toFloat())
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        values.put(SetDB.tblTransaction.COL_DATE, ingress.transactionDate.format(formatter))
        values.put(SetDB.tblTransaction.COL_TTYPE, ingress.transactionType.value)
        values.put(SetDB.tblTransaction.COL_IDEXPENSETYPE, 0)
        values.put(SetDB.tblTransaction.COL_IDPROFILE, profileId)

        try {
            val result =  dataBase.insert(SetDB.tblTransaction.TABLE_NAME, null, values)

            if (result == (0).toLong()) {
                Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }

        }catch (e: java.lang.Exception){
            Log.e("Execption", e.toString())
            boolResult =  false
        }

        dataBase.close()

        return boolResult
    }

    public fun insertExpenseType(expenseType: ExpenseType): Boolean {
        val dataBase:SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        values.put(SetDB.tblExpenseType.COL_TITLE, expenseType.name)
        values.put(SetDB.tblExpenseType.COL_VALUE, expenseType.value)

        try {
            val result =  dataBase.insert(SetDB.tblExpenseType.TABLE_NAME, null, values)

            if (result == (0).toLong()) {
                Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }

        }catch (e: java.lang.Exception){
            Log.e("Execption", e.toString())
            boolResult =  false
        }

        dataBase.close()

        return boolResult
    }

    public fun deleteTransaction(intID: Int): Boolean {
        val db = this.writableDatabase
        var boolResult:Boolean =  false
        try{

            val where:String =  SetDB.tblTransaction.COL_ID + "=?"
            val _success = db.delete(SetDB.tblTransaction.TABLE_NAME, where, arrayOf(intID.toString()))
            db.close()

            boolResult = Integer.parseInt("$_success") != -1


        }catch (e: Exception){

            Log.e("Execption", e.toString())
        }

        return  boolResult
    }

    public fun deleteProfile(intID: Int): Boolean {
        val db = this.writableDatabase
        var boolResult:Boolean =  false
        try{

            val where:String =  SetDB.tblProfile.COL_ID + "=?"
            val _success = db.delete(SetDB.tblProfile.TABLE_NAME, where, arrayOf(intID.toString()))
            db.close()

            boolResult = Integer.parseInt("$_success") != -1


        }catch (e: Exception){

            Log.e("Execption", e.toString())
        }

        return  boolResult
    }

    public fun updateProfile(profile:Profile):Boolean{

        val dataBase:SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        values.put(SetDB.tblProfile.COL_NAME, profile.profileName)
        values.put(SetDB.tblProfile.COL_DAYRANGE, profile.dayRange)
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        values.put(SetDB.tblProfile.COL_STARTDAY, profile.startDate.format(formatter))
        values.put(SetDB.tblProfile.COL_COLOR, profile.color)
        values.put(SetDB.tblProfile.COL_DESCRIPTION, profile.descrption)

        val where:String = SetDB.tblProfile.COL_ID + "=?"

        try{

            val result =  dataBase.update(SetDB.tblProfile.TABLE_NAME,
                    values,
                    where,
                    arrayOf(profile.idBD.toString()))

            if (result != -1 ) {
                Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
            }

        }catch (e: Exception){
            boolResult = false
            Log.e("Execption", e.toString())
        }

        dataBase.close()
        return  boolResult
    }

    public fun updateExpense(expense:Expense, profileId: Int):Boolean{

        val dataBase:SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        values.put(SetDB.tblTransaction.COL_NAME, expense.name)
        values.put(SetDB.tblTransaction.COL_DESCRIPTION, expense.description)
        values.put(SetDB.tblTransaction.COL_AMOUNT, expense.amount.toFloat())
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        values.put(SetDB.tblTransaction.COL_DATE, expense.transactionDate.format(formatter))
        values.put(SetDB.tblTransaction.COL_TTYPE, expense.transactionType.value)
        values.put(SetDB.tblTransaction.COL_IDEXPENSETYPE, expense.type.value)
        values.put(SetDB.tblTransaction.COL_IDPROFILE, profileId)

        val where:String =  SetDB.tblTransaction.COL_ID + "=?"

        try{

            val result =  dataBase.update(SetDB.tblTransaction.TABLE_NAME,
                    values,
                    where,
                    arrayOf(expense.idBD.toString()))

            if (result != -1 ) {
                Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()

            }

        }catch (e: Exception){
            boolResult = false
            Log.e("Execption", e.toString())
        }

        dataBase.close()
        return  boolResult
    }

    public fun insertUserData(user: User): Boolean {
        val dataBase:SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        values.put(SetDB.tblUserData.COL_NAME, user.fullName)
        values.put(SetDB.tblUserData.COL_LAST_NAME, user.lastName)
        values.put(SetDB.tblUserData.COL_USERNAME, user.username)
        values.put(SetDB.tblUserData.COL_EMAIL, user.email)
        values.put(SetDB.tblUserData.COL_TOKEN, user.token)
        values.put(SetDB.tblUserData.COL_IMG, user.imgArray)
        values.put(SetDB.tblUserData.COL_CLOUD_ID, user.cloudId)

        try {
            val result =  dataBase.insert(SetDB.tblUserData.TABLE_NAME, null, values)

            if (result == (0).toLong()) {
                Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }

        }catch (e: java.lang.Exception){
            Log.e("Execption", e.toString())
            boolResult =  false
        }

        dataBase.close()

        return boolResult
    }

    public fun getUserData(intID:Int):User?{
        var user:User? = null
        val dataBase:SQLiteDatabase = this.writableDatabase

        //QUE COLUMNAS QUEREMOS QUE ESTE EN EL SELECT
        val columns:Array<String> =  arrayOf(SetDB.tblUserData.COL_ID,
            SetDB.tblUserData.COL_NAME,
            SetDB.tblUserData.COL_LAST_NAME,
            SetDB.tblUserData.COL_USERNAME,
            SetDB.tblUserData.COL_EMAIL,
            SetDB.tblUserData.COL_TOKEN,
            SetDB.tblUserData.COL_IMG,
            SetDB.tblUserData.COL_CLOUD_ID)

        val where:String =  SetDB.tblUserData.COL_ID + " = ${intID.toString()}"

        val data =  dataBase.query(SetDB.tblUserData.TABLE_NAME,
            columns,
            where,
            null,
            null,
            null,
            SetDB.tblProfile.COL_ID + " ASC")

        if(data.moveToFirst()){
            var idDB = data.getString(data.getColumnIndex(SetDB.tblUserData.COL_ID)).toInt()
            var token = data.getString(data.getColumnIndex(SetDB.tblUserData.COL_TOKEN)).toString()
            var fullName = data.getString(data.getColumnIndex(SetDB.tblUserData.COL_NAME)).toString()
            var lastName = data.getString(data.getColumnIndex(SetDB.tblUserData.COL_LAST_NAME)).toString()
            var userName = data.getString(data.getColumnIndex(SetDB.tblUserData.COL_USERNAME)).toString()
            var email = data.getString(data.getColumnIndex(SetDB.tblUserData.COL_EMAIL)).toString()
            var image = data.getBlob(data.getColumnIndex(SetDB.tblUserData.COL_IMG))
            var cloudId = data.getString(data.getColumnIndex(SetDB.tblUserData.COL_CLOUD_ID)).toInt()
            user = User(token, fullName, lastName, userName, email, image, idDB, cloudId)

        }

        data.close()
        return user
    }

    public fun getListOfUserData():MutableList<User>{
        val List:MutableList<User> = ArrayList()

        val dataBase:SQLiteDatabase = this.writableDatabase

        //QUE COLUMNAS QUEREMOS QUE ESTE EN EL SELECT
        val columns:Array<String> =  arrayOf(SetDB.tblUserData.COL_ID,
            SetDB.tblUserData.COL_NAME,
            SetDB.tblUserData.COL_LAST_NAME,
            SetDB.tblUserData.COL_USERNAME,
            SetDB.tblUserData.COL_EMAIL,
            SetDB.tblUserData.COL_TOKEN,
            SetDB.tblUserData.COL_IMG,
            SetDB.tblUserData.COL_CLOUD_ID)

        val data =  dataBase.query(SetDB.tblUserData.TABLE_NAME,
            columns,
            null,
            null,
            null,
            null,
            SetDB.tblTransaction.COL_ID + " ASC")

        // SI NO TIENE DATOS DEVUELVE FALSO
        //SE MUEVE A LA PRIMERA POSICION DE LOS DATOS
        if(data.moveToFirst()){

            do{
                var idDB = data.getString(data.getColumnIndex(SetDB.tblUserData.COL_ID)).toInt()
                var fullName = data.getString(data.getColumnIndex(SetDB.tblUserData.COL_NAME)).toString()
                var lastName = data.getString(data.getColumnIndex(SetDB.tblUserData.COL_LAST_NAME)).toString()
                var userName = data.getString(data.getColumnIndex(SetDB.tblUserData.COL_USERNAME)).toString()
                var email = data.getString(data.getColumnIndex(SetDB.tblUserData.COL_EMAIL)).toString()
                var token = data.getString(data.getColumnIndex(SetDB.tblUserData.COL_TOKEN)).toString()
                var image = data.getBlob(data.getColumnIndex(SetDB.tblUserData.COL_IMG))
                var cloudId = data.getString(data.getColumnIndex(SetDB.tblUserData.COL_CLOUD_ID)).toInt()
                val user = User(token, fullName, lastName, userName, email, image, idDB, cloudId)

                List.add(user)
            }while (data.moveToNext())

        }
        return  List
    }

    public fun updateUser(user: User):Boolean{

        val dataBase:SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        values.put(SetDB.tblUserData.COL_NAME, user.fullName)
        values.put(SetDB.tblUserData.COL_LAST_NAME, user.lastName)
        values.put(SetDB.tblUserData.COL_USERNAME, user.username)
        values.put(SetDB.tblUserData.COL_EMAIL, user.email)
        values.put(SetDB.tblUserData.COL_TOKEN, user.token)
        values.put(SetDB.tblUserData.COL_IMG, user.imgArray)
        values.put(SetDB.tblUserData.COL_CLOUD_ID, user.cloudId)

        val where:String = SetDB.tblUserData.COL_ID + "=?"

        try{

            val result =  dataBase.update(SetDB.tblUserData.TABLE_NAME,
                values,
                where,
                arrayOf(user.idDB.toString()))

            if (result != -1 ) {
                Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
            }

        }catch (e: Exception){
            boolResult = false
            Log.e("Execption", e.toString())
        }

        dataBase.close()
        return  boolResult
    }

    public fun deleteUser(intID: Int): Boolean {
        val db = this.writableDatabase
        var boolResult:Boolean =  false
        try{

            val where:String =  SetDB.tblUserData.COL_ID + "=?"
            val _success = db.delete(SetDB.tblUserData.TABLE_NAME, where, arrayOf(intID.toString()))
            db.close()

            boolResult = Integer.parseInt("$_success") != -1


        }catch (e: Exception){

            Log.e("Execption", e.toString())
        }

        return  boolResult
    }
}