package com.abbisea.pwexercise.data

import androidx.room.*
import com.abbisea.pwexercise.data.models.PendingInspection
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface InspectionDao {

    @Query("SELECT * FROM pending_inspections")
    fun getAllPendingInspections(): Single<List<PendingInspection>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPendingInspection(inspection: PendingInspection): Completable

    @Delete
    fun deletePendingInspection(inspection: PendingInspection): Completable
}