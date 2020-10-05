package com.app.expensetrackerdemo.utility

import android.util.Log
import com.app.expensetrackerdemo.helper.TransactionType
import com.app.expensetrackerdemo.helper.patternAmountExtractor
import com.app.expensetrackerdemo.helper.patternMoneyExtractor
import com.app.expensetrackerdemo.model.SMSModel
import java.util.regex.Matcher

object TransactionsUtility {

    /**
     * Filter all SMS messages and returns only transaction messages and total amounts.
     */
    fun filterTransactions(smsList: List<SMSModel>, result: (debitedMessagesList: List<SMSModel>,
                                                             creditedMessagesList: List<SMSModel>, debitedAmount: Float,
                                                             creditedAmount: Float) -> Unit) {
        val debitedMessagesList = arrayListOf<SMSModel>()
        val creditedMessagesList = arrayListOf<SMSModel>()
        var debitedAmount = 0.0f
        var creditedAmount = 0.0f

        smsList.forEachIndexed { index, smsModel ->
            val message = smsModel.msg
            if (checkIfMessageIsTransactionBased(
                    message
                )
            ) {
                if (message.contains("debited", ignoreCase = true)) {
                    val amountWithCurrencyFromString =
                        getAmountWithCurrencyFromString(
                            message
                        )
                    val amountFromString =
                        getAmountFromString(
                            amountWithCurrencyFromString
                        )

                    val transaction = smsModel.apply {
                        transactionType = TransactionType.DEBITED
                        transactionAmount = amountWithCurrencyFromString
                        transactionDate = smsModel.date
                    }
                    debitedMessagesList.add(transaction)
                    debitedAmount += amountFromString
                }
                else if (message.contains("credited", ignoreCase = true)) {
                    val amountWithCurrencyFromString =
                        getAmountWithCurrencyFromString(
                            message
                        )
                    val amountFromString =
                        getAmountFromString(
                            amountWithCurrencyFromString
                        )

                    val transaction = smsModel.apply {
                        transactionType = TransactionType.CREDITED
                        transactionAmount = amountWithCurrencyFromString
                        transactionDate = smsModel.date
                    }
                    creditedMessagesList.add(transaction)
                    creditedAmount += amountFromString
                }
            }
        }

        Log.e(
            "TransactionsUtility",
            "debitedAmount: $debitedAmount :: creditedAmount: $creditedAmount"
        )
        result(debitedMessagesList, creditedMessagesList, debitedAmount, creditedAmount)
    }

    /**
     * Checks if a string contains a transactional message.
     */
    private fun checkIfMessageIsTransactionBased(message: String): Boolean {
        return message.contains("debited", ignoreCase = true) ||
                message.contains("credited", ignoreCase = true)
    }

    /**
     * Extracts and returns the amount with currency from a string.
     */
    private fun getAmountWithCurrencyFromString(message: String): String? {
        val amountMatcher: Matcher = patternMoneyExtractor.matcher(message)
        val amountWithCurrency = if (amountMatcher.find()) {
            amountMatcher.group()
        } else {
            null
        }
        return amountWithCurrency
    }

    /**
     * Checks and returns the amount from a string with currency.
     */
    private fun getAmountFromString(message: String?): Float {
        val amountMatcher: Matcher = patternAmountExtractor.matcher(message)
        val amount = if (amountMatcher.find()) {
            amountMatcher.group().replace(",", "").toFloat()
        } else {
            0.0f
        }
        return amount
    }
}