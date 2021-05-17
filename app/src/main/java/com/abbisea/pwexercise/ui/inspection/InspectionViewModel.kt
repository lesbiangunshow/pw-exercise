package com.abbisea.pwexercise.ui.inspection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.abbisea.pwexercise.data.InspectionUseCase
import com.abbisea.pwexercise.data.models.Inspection
import com.abbisea.pwexercise.data.models.PendingInspection
import com.abbisea.pwexercise.utils.Constants
import com.abbisea.pwexercise.utils.SchedulerUtils
import com.abbisea.pwexercise.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class InspectionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val inspectionUseCase: InspectionUseCase,
) : ViewModel() {
    val listEntities = MutableLiveData<List<InspectionListEntity>>()
    val showSavedToast = SingleLiveEvent<Nothing>()
    val showInvalidInspectionDialog = SingleLiveEvent<Nothing>()
    val showSuccessToast = SingleLiveEvent<Nothing>()
    val showSubmissionErrorToast = SingleLiveEvent<Nothing>()

    private var isSubmitting: Boolean = false // I dont like this but i'm short on time

    private var pendingInspection: PendingInspection? = null
    private val location = savedStateHandle.get<String>("location") ?: ""
    private lateinit var inspection: Inspection

    init {
        val isResumingInspection = savedStateHandle.get<Boolean>("isResumed") ?: false
        val inspectionId = savedStateHandle.get<Int>("inspectionId") ?: 0

        if (isResumingInspection) {
            inspectionUseCase.getPendingInspections()
                .map {
                    it.first { inspection -> inspection.id == inspectionId }
                }
                .flatMap {
                    pendingInspection = it
                    inspectionUseCase.getInspection(it.id)
                }
                .compose(SchedulerUtils.ioToMain())
                .subscribeBy(
                    onSuccess = { inspection ->
                        this.inspection = inspection
                        pendingInspection?.let { it ->
                            parsePendingInspection(inspection, it)
                        } ?: parseInspection(inspection)
                    },
                    onError = {

                    }
                )

        } else {
            inspectionUseCase.getInspection(inspectionId)
                .compose(SchedulerUtils.ioToMain())
                .subscribeBy(
                    onSuccess = {
                        inspection = it
                        parseInspection(it)
                    },
                    onError = {

                    }
                )
        }
    }

    fun savePendingInspection() {
        listEntities.value?.let { entities ->
            val answerEntities = entities.filterIsInstance<AnswerEntity>()
            val answerList: MutableList<Int?> = mutableListOf()
            for (answerEntity in answerEntities) {
                answerList.add(
                    if (answerEntity.isNA) null else answerEntity.value
                )
            }
            inspectionUseCase.saveInspection(
                PendingInspection(
                    inspection.id,
                    inspection.name,
                    location,
                    answerList
                ).apply {
                    // if this is a pending inspection, set id to be equal to it, resulting in replacement in room
                    pendingInspection?.id?.let {
                        id = it
                    }
                }
            ).compose(SchedulerUtils.ioToMain<Void>())
                .subscribeBy(
                    onComplete = {
                        showSavedToast.call()
                    }
                )
        }
    }

    fun submitInspection() {
        if (isSubmitting) return // to prevent many submissions
        isSubmitting = true
        listEntities.value?.let { entities ->
            val answerEntities = entities.filterIsInstance<AnswerEntity>()
            val answerList: MutableList<Int?> = mutableListOf()
            for (answerEntity in answerEntities) {
                // check if answer is valid, early return and show error toast if not
                if (!answerEntity.isNA
                    && !(Constants.ANSWER_MIN..Constants.ANSWER_MAX).contains(
                        answerEntity.value
                    )
                ) {
                    showInvalidInspectionDialog.call()
                    return
                }
                answerList.add(
                    if (answerEntity.isNA) null else answerEntity.value
                )
            }
            // otherwise continue to submission
            inspectionUseCase.submitInspection(
                PendingInspection(
                    inspection.id,
                    inspection.name,
                    location,
                    answerList
                )
            ).compose(SchedulerUtils.ioToMain<Void>())
                .subscribeBy(
                    onComplete = {
                        pendingInspection?.let {
                            inspectionUseCase.deletePendingInspection(it)
                                .subscribeOn(Schedulers.io())
                                .subscribe()
                        }
                        showSuccessToast.call()
                    },
                    onError = {
                        isSubmitting = false
                        savePendingInspection()
                        showSubmissionErrorToast.call()
                    }
                )
        }
    }

    // below two functions are prime for refactoring, basically do the same thing and AnswerEntity
    // contains vars so can be populated from PendingInspection after the fact.
    private fun parsePendingInspection(
        inspection: Inspection,
        pending: PendingInspection
    ) {
        val inspectionEntityList = mutableListOf<InspectionListEntity>()
        for (question in inspection.questions) {
            inspectionEntityList.add(
                QuestionEntity(question.displayText)
            )
            for ((index, answer) in question.answers.withIndex()) {
                val pendingAnswer = pending.answers[index]
                inspectionEntityList.add(
                    AnswerEntity(
                        answer,
                        pendingAnswer ?: 0,
                        pendingAnswer == null
                    )
                )
            }
        }
        listEntities.postValue(inspectionEntityList)
    }

    // key assumption: the two or more answer values are themselves prompts provided by the
    // Inspection model as ultimately specified by the api. If the answer values were to be added
    // by the end user, I would provide a QuestionEntity to the adapter, which would handle
    // adding AnswerEntities to the list on request
    private fun parseInspection(inspection: Inspection) {
        val inspectionEntityList = mutableListOf<InspectionListEntity>()
        for (question in inspection.questions) {
            inspectionEntityList.add(
                QuestionEntity(question.displayText)
            )
            for (answer in question.answers) {
                inspectionEntityList.add(
                    AnswerEntity(answer, 0)
                )
            }
        }
        listEntities.postValue(inspectionEntityList)

    }
}