package com.abbisea.pwexercise.ui.main

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abbisea.pwexercise.R
import com.abbisea.pwexercise.data.models.PendingInspection
import com.abbisea.pwexercise.utils.inflate
import kotlinx.android.synthetic.main.list_item_pending_inspection.view.*

class PendingInspectionAdapter(private val onClickInspection: (PendingInspection) -> Unit) :
    RecyclerView.Adapter<PendingInspectionViewHolder>() {
    private var items: List<PendingInspection> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingInspectionViewHolder =
        PendingInspectionViewHolder(parent, onClickInspection)

    override fun onBindViewHolder(holder: PendingInspectionViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<PendingInspection>) {
        items = newItems
        notifyDataSetChanged()
    }
}

class PendingInspectionViewHolder(
    parent: ViewGroup,
    private val onClickInspection: (PendingInspection) -> Unit
) :
    RecyclerView.ViewHolder(parent.inflate(R.layout.list_item_pending_inspection)) {

    fun bind(pendingInspection: PendingInspection) {
        with(itemView) {
            text_inspection_name.text = pendingInspection.inspectionName
            text_location.text = pendingInspection.location
            cell_pending_inspection.setOnClickListener {
                onClickInspection(pendingInspection)
            }
        }
    }
}
