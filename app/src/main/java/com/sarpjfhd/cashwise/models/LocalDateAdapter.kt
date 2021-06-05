package com.sarpjfhd.cashwise.models

import com.google.gson.*
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class LocalDateTypeAdapter : TypeAdapter<LocalDate>() {

    override fun write(out: JsonWriter, value: LocalDate) {
        out.value(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(value))
    }

    override fun read(input: JsonReader): LocalDate = LocalDate.parse(input.nextString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}

class TransactionTypeAdapter : TypeAdapter<TransactionTypes>() {

    override fun write(out: JsonWriter, value: TransactionTypes) {
        out.value(value.value)
    }

    override fun read(input: JsonReader): TransactionTypes = TransactionTypes.fromInt(input.nextInt())!!
}

class ExpenseTypeAdapter : TypeAdapter<ExpenseType>() {

    override fun write(out: JsonWriter, value: ExpenseType) {
        out.value(value.value)
    }

    override fun read(input: JsonReader): ExpenseType = ExpenseType.fromInt(input.nextInt())!!
}