package com.abbisea.pwexercise.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abbisea.pwexercise.data.InspectionUseCase
import com.abbisea.pwexercise.data.models.Inspection
import com.abbisea.pwexercise.data.models.PendingInspection
import com.abbisea.pwexercise.utils.SchedulerUtils
import com.abbisea.pwexercise.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val inspectionUseCase: InspectionUseCase
) : ViewModel() {

    val showInspectionsDialog = SingleLiveEvent<List<Inspection>>()
    val resumeInspection = SingleLiveEvent<PendingInspection>()
    val pendingInspections = MutableLiveData<List<PendingInspection>>()

    private var inspections: List<Inspection>? = null

    init {
        refreshData()
        inspectionUseCase.getInspections()
            .compose(SchedulerUtils.ioToMain())
            .subscribeBy(
                onSuccess = {
                    inspections = it
                }
            )
    }


    fun onClickNewInspection() {
        inspections?.let {
            showInspectionsDialog.postValue(it)
        }
    }

    fun onClickPendingInspection(inspection: PendingInspection) {
        resumeInspection.postValue(inspection)
    }

    fun refreshData() {
        inspectionUseCase.getPendingInspections()
            .compose(SchedulerUtils.ioToMain())
            .subscribeBy(
                onSuccess = {
                    pendingInspections.postValue(it)
                }
            )
    }
}