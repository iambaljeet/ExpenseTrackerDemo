package com.app.expensetrackerdemo.ui.home.viewmodel

import android.database.Cursor
import android.provider.Telephony
import androidx.lifecycle.ViewModel
import com.app.expensetrackerdemo.model.SMSModel
import com.app.expensetrackerdemo.utility.TransactionsUtility

class HomeViewModel: ViewModel() {
    /**
     * Filter out messages for debited and credited messages along with total sum of debited amount and credited amount.
     */
    fun filterTransactions(smsList: List<SMSModel>, result: (debitedMessagesList: List<SMSModel>,
                                                             creditedMessagesList: List<SMSModel>, debitedAmount: Float,
                                                             creditedAmount: Float) -> Unit) {
        TransactionsUtility.filterTransactions(smsList = smsList, result = result)
    }

    /**
     * Returns a list of SMS from Cursor available after retrieving all SMS from user's device
     */
    fun getSmsListFromCursor(cursor: Cursor): ArrayList<SMSModel> {
        val smsList = arrayListOf<SMSModel>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(Telephony.Sms._ID))
            val threadId = cursor.getLong(cursor.getColumnIndexOrThrow(Telephony.Sms.THREAD_ID))
            val date = cursor.getLong(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE))
            val type = cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Sms.TYPE))
            val body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY))
            val address = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
            val read = cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Sms.READ))
            val status = cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Sms.STATUS))
            val subject = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.SUBJECT))

            val smsModel = SMSModel(
                id = id,
                date = date,
                address = address,
                msg = body,
                readState = read,
                threadId = threadId,
                type = type,
                status = status,
                subject = subject
            )
            smsList.add(smsModel)
        }
        return smsList
    }
}