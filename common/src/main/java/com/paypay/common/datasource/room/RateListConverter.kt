package com.paypay.common.datasource.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class RateListConverter {
    private var gson = Gson()

    @TypeConverter
    fun rateStringToList(data: String?): List<RateEntity?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object :
            TypeToken<List<RateEntity?>?>() {}.type
        return gson.fromJson<List<RateEntity?>>(data, listType)
    }

    @TypeConverter
    fun rateListToString(someObjects: List<RateEntity?>?): String? {
        return gson.toJson(someObjects)
    }
}