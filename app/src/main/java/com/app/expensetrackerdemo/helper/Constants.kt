package com.app.expensetrackerdemo.helper

import android.Manifest
import android.net.Uri
import android.provider.Telephony
import java.util.regex.Pattern

const val REQUEST_SMS_PERMISSION = 25
const val SMS_LOADER_ID = 100
val smsPermissions: Array<String> = arrayOf(Manifest.permission.READ_SMS)

val SMS_URI: Uri = Telephony.Sms.CONTENT_URI
const val SORT_DESC = "date desc"

val patternMoneyExtractor: Pattern = Pattern.compile("(?:[rR][sS]|(?:[iI][nN][rR]))+[\\.]*+[\\s]*[0-9+[\\,]*+[0-9]*]+[\\.]*[0-9]+")
val patternAmountExtractor: Pattern = Pattern.compile("[0-9+[\\,]*+[0-9]*]+[\\.]*[0-9]+")

const val SHARED_PREFERENCES_NAME = "expenseTracker"
const val IS_CONTACT_PERMISSIONS_REQUESTED = "isContactPermissionsRequested"