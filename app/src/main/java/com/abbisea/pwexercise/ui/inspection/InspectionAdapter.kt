package com.abbisea.pwexercise.ui.inspection

import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.abbisea.pwexercise.R
import com.abbisea.pwexercise.utils.inflate
import kotlinx.android.synthetic.main.list_item_answer.view.*
import kotlinx.android.synthetic.main.list_item_question.view.*
import java.lang.Exception

class InspectionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: List<InspectionListEntity> = listOf()

    override fun getItemViewType(position: Int) = items[position].itemViewType.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (InspectionListEntityType.values()[viewType]) {
            InspectionListEntityType.QUESTION -> QuestionViewHolder(parent)
            InspectionListEntityType.ANSWER -> AnswerViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is QuestionEntity -> {
                (holder as QuestionViewHolder).bind(item)
            }
            is AnswerEntity -> {
                (holder as AnswerViewHolder).bind(item)
            }
        }
    }

    override fun getItemCount() = items.size

    fun updateData(newItems: List<InspectionListEntity>) {
        items = newItems
        notifyDataSetChanged()
    }
}

class AnswerViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(parent.inflate(R.layout.list_item_answer)) {
    fun bind(item: AnswerEntity) {
        with(itemView) {
            text_answer_choice.text = item.text
            edittext_answer_choice.setText(item.value.toString())
            edittext_answer_choice.addTextChangedListener { string ->
                try {
                    item.value = string.toString().toInt()
                } catch (e: Exception) {}
            }
            button_not_applicable.isChecked = item.isNA
            button_not_applicable.setOnCheckedChangeListener { button, isChecked ->
                item.isNA = isChecked
            }
        }
    }
}

class QuestionViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(parent.inflate(R.layout.list_item_question)) {
    fun bind(item: QuestionEntity) {
        with(itemView) {
            text_question.text = item.text
        }
    }
}


sealed class InspectionListEntity(val itemViewType: InspectionListEntityType)

data class QuestionEntity(val text: String) :
    InspectionListEntity(InspectionListEntityType.QUESTION)

data class AnswerEntity(val text: String, var value: Int, var isNA: Boolean = false) :
    InspectionListEntity(InspectionListEntityType.ANSWER)

enum class InspectionListEntityType {
    QUESTION, ANSWER
}