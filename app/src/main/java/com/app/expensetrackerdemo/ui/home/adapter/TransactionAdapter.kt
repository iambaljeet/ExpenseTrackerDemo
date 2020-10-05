package com.app.expensetrackerdemo.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.app.expensetrackerdemo.R
import com.app.expensetrackerdemo.model.SMSModel
import com.app.expensetrackerdemo.model.SMSModelDiffUtil

class TransactionAdapter: ListAdapter<SMSModel, TransactionViewHolder>(
    SMSModelDiffUtil()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_transaction_details, parent, false)
        return TransactionViewHolder(
            itemView = itemView
        )
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val smsModel = getItem(position)
        holder.bind(smsModel = smsModel)
    }
}