package com.abbisea.pwexercise.data

import android.accounts.NetworkErrorException
import com.abbisea.pwexercise.data.models.Inspection
import com.abbisea.pwexercise.data.models.PendingInspection
import com.abbisea.pwexercise.data.models.Question
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextInt

class InspectionUseCase @Inject constructor(
    private val inspectionDao: InspectionDao
) {
    fun getInspections(): Single<List<Inspection>> =
        Single.just(
            dummyList
        )

    fun getInspection(id: Int): Single<Inspection> =
        Single.just(dummyList.first())

    fun getPendingInspections() =
        inspectionDao.getAllPendingInspections()

    fun saveInspection(inspection: PendingInspection) =
        inspectionDao.insertPendingInspection(inspection)

    fun submitInspection(inspection: PendingInspection) =
        Completable.create { emitter ->
            if (Random.nextInt(0, 5) == 0) { // fails 1/5 of the time
                emitter.onError(NetworkErrorException())
            } else {
                emitter.onComplete()
            }
        }

    fun deletePendingInspection(inspection: PendingInspection) =
        inspectionDao.deletePendingInspection(inspection)


    private val dummyList = listOf(
        Inspection(
            id = 1,
            name = "Placeholder Inspection 1",
            questions = listOf(
                Question(
                    "Air Quality",
                    listOf(
                        "CO2 ppm",
                        "Temperature (C)"
                    )
                ),
                Question(
                    "Rate your meals today",
                    listOf(
                        "Breakfast",
                        "Elevenses",
                        "Lunch",
                        "Dinner"
                    )
                )
            ),
            possibleLocations = listOf(
                "Kitchen",
                "Laboratory",
                "Electrical",
                "Office"
            )
        )
    )
}
