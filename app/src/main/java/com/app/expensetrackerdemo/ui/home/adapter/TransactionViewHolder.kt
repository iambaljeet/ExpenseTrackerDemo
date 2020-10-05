package com.app.expensetrackerdemo.ui.home.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.expensetrackerdemo.R
import com.app.expensetrackerdemo.model.SMSModel
import com.app.expensetrackerdemo.helper.TransactionType
import kotlinx.android.synthetic.main.list_item_transaction_details.view.*

class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    /**
     * Sets data and color list item based on transaction type.
     */
    fun bind(smsModel: SMSModel?) {
        itemView.text_view_detail.text = smsModel?.msg

        itemView.text_view_message.apply {
            if (smsModel?.transactionType == TransactionType.DEBITED) setTextColor(ContextCompat.getColor(itemView.context, R.color.colorDebited))
            else setTextColor(ContextCompat.getColor(itemView.context, R.color.colorCredited))
            text = smsModel?.transactionAmount
        }
    }
}
