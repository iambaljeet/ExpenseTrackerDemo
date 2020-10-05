package com.app.expensetrackerdemo.utility

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.expensetrackerdemo.callback.DialogCallback

/**
 * Extension function to check if supplied permissions are available or not.
 */
fun Context?.checkPermissionsAvailable(permissions: Array<String>): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        var isPermissionsAvailable = false
        this?.let { context ->
            for (permission in permissions) {
                isPermissionsAvailable = ActivityCompat.checkSelfPermission(
                    context,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
                if (!isPermissionsAvailable) {
                    break
                }
            }
        }
        return isPermissionsAvailable
    }
    return true
}

/**
 * Extension function to show a dialog with one button.
 */
fun Activity.showOneButtonAlertDialog(title: String, subTitle: String? = null, positiveButtonText: String, isCancellable: Boolean,
                                      dialogCallback: DialogCallback? = null
) {
    val dialog = DialogUtility.buildOneButtonAlertDialog(
        this,
        title,
        subTitle,
        positiveButtonText,
        isCancellable,
        dialogCallback
    )
    dialog.show()
}

/**
 * Extension function to show a dialog with two buttons
 */
fun Activity.showTwoButtonsAlertDialog(title: String, subTitle: String? = null, positiveButtonText: String, negativeButtonText: String, isCancellable: Boolean,
                                       dialogCallback: DialogCallback? = null
) {
    val dialog = DialogUtility.buildTwoButtonsAlertDialog(
        this,
        title,
        subTitle,
        positiveButtonText,
        negativeButtonText,
        isCancellable,
        dialogCallback
    )
    dialog.show()
}

/**
 * Open App info for current application using its package name
 */
fun Activity.showAppInfoActivity() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.addCategory(Intent.CATEGORY_DEFAULT)
    intent.data = Uri.parse("package:$packageName")
    ContextCompat.startActivity(this, intent, null)
}