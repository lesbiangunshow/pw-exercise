package com.abbisea.pwexercise.data

import androidx.room.*
import com.abbisea.pwexercise.data.models.PendingInspection
import com.google.gson.Gson

@Database(entities = [PendingInspection::class], version = 1, exportSchema = false)
@TypeConverters(ListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun inspectionDao(): InspectionDao
}

class ListConverter {
    @TypeConverter
    fun intListToStrong(list: List<Int?>): String = Gson().toJson(list)

    @TypeConverter
    fun intArrayToList(string: String): List<Int?> {
        val objects = Gson().fromJson(string, Array<Int?>::class.java)
        return objects.toList()
    }
}