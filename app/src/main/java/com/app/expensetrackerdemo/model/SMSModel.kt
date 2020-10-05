package com.app.expensetrackerdemo.model

import androidx.recyclerview.widget.DiffUtil
import com.app.expensetrackerdemo.helper.TransactionType

data class SMSModel(
    var name: String? = "",
    var id: Long? = 0,
    var date: Long? = 0,
    var address: String? = null,
    var msg: String,
    var readState: Int? = null,
    var threadId: Long? = 0,
    var type: Int? = 0,
    var status: Int? = null,
    var subject: String? = null,
    var color: Int = 0,
    var transactionType: TransactionType? = null,
    var transactionAmount: String? = null,
    var transactionDate: Long? = null
)

class SMSModelDiffUtil: DiffUtil.ItemCallback<SMSModel>() {
    override fun areItemsTheSame(oldItem: SMSModel, newItem: SMSModel): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areContentsTheSame(oldItem: SMSModel, newItem: SMSModel): Boolean {
        return newItem == oldItem
    }
}