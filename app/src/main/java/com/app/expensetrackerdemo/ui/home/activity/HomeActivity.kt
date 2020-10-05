package com.app.expensetrackerdemo.ui.home.activity

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.app.expensetrackerdemo.R
import com.app.expensetrackerdemo.callback.DialogCallback
import com.app.expensetrackerdemo.helper.*
import com.app.expensetrackerdemo.model.SMSModel
import com.app.expensetrackerdemo.ui.home.adapter.TransactionAdapter
import com.app.expensetrackerdemo.ui.home.viewmodel.HomeViewModel
import com.app.expensetrackerdemo.utility.PreferenceManager
import com.app.expensetrackerdemo.utility.checkPermissionsAvailable
import com.app.expensetrackerdemo.utility.showAppInfoActivity
import com.app.expensetrackerdemo.utility.showOneButtonAlertDialog
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor>,
    CompoundButton.OnCheckedChangeListener {
    private lateinit var manager: LoaderManager
    private val smsList = arrayListOf<SMSModel>()
    private var debitedMessagesList = listOf<SMSModel>()
    private var creditedMessagesList = listOf<SMSModel>()
    private var transactionAdapter: TransactionAdapter? = null
    private var isSwitchChecked: Boolean = true

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        switch_filter.setOnCheckedChangeListener(this)
        manager = LoaderManager.getInstance(this)

        setAdapter()
        checkPermissionsAndGetData()
    }

    /**
     * Check if SMS permissions is available otherwise request permissions from user.
     */
    private fun checkPermissionsAndGetData() {
        if (checkPermissionsAvailable(smsPermissions)) {
            manager.initLoader(SMS_LOADER_ID, null, this)
        } else {
            PreferenceManager.setIsContactPermissionRequested(isPermissionsRequested = true)
            requestSmsPermissions()
        }
    }

    /**
     * Requests SMS permissions from user
     */
    @SuppressLint("NewApi")
    private fun requestSmsPermissions() {
        requestPermissions(
            smsPermissions,
            REQUEST_SMS_PERMISSION
        )
    }

    /**
     * Initialize and set adapter to recycler view.
     */
    private fun setAdapter() {
        transactionAdapter = TransactionAdapter()
        recycler_view_details.adapter = transactionAdapter
    }

    /**
     * Callback to give the status of permissions accepted or denied by user
     */
    @SuppressLint("NewApi")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            REQUEST_SMS_PERMISSION -> {
                var arePermissionsGranted = true
                if (grantResults.isNotEmpty()) {
                    for (result in grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            arePermissionsGranted = false
                            break
                        }
                    }
                }
                if (arePermissionsGranted) {
                    manager.initLoader(SMS_LOADER_ID, null, this)
                } else {
                    val isContactPermissionRequested =
                        PreferenceManager.getIsContactPermissionRequested()
                    if (isContactPermissionRequested) {
                        if (!shouldShowRequestPermissionRationale(smsPermissions[0])) {
                            showRequestPermissionsFromSettingsError()
                        } else {
                            showSmsPermissionsDeniedError()
                        }
                    } else {
                        showSmsPermissionsDeniedError()
                    }
                }
            }
        }
    }

    /**
     * Show error dialog to user if user has selected to Never ask for SMS permissions again.
     */
    private fun showRequestPermissionsFromSettingsError() {
        showOneButtonAlertDialog(title = getString(R.string.error), subTitle = getString(
            R.string.grant_sms_permission_from_settings
        ), positiveButtonText = getString(
            R.string.ok
        ), isCancellable = false,
            dialogCallback = object : DialogCallback {
                override fun onPositiveButtonClicked(dialogInterface: DialogInterface) {
                    showAppInfoActivity()
                }

                override fun onNegativeButtonClicked(dialogInterface: DialogInterface) {
                }
            })
    }

    /**
     * Show error dialog to user if SMS permission is not granted
     */
    private fun showSmsPermissionsDeniedError() {
        showOneButtonAlertDialog(title = getString(R.string.error), subTitle = getString(
            R.string.sms_permissions_are_required_error
        ), positiveButtonText = getString(
            R.string.ok
        ), isCancellable = false,
            dialogCallback = object : DialogCallback {
                override fun onPositiveButtonClicked(dialogInterface: DialogInterface) {
                    requestSmsPermissions()
                }

                override fun onNegativeButtonClicked(dialogInterface: DialogInterface) {
                }
            })
    }

    /**
     * Creates/Initializes a loader using CursorAdapter
     * It loads data asynchronously in a background thread.
     */
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        progress_bar_loading.isVisible = true

        return CursorLoader(this,
            SMS_URI,
            null,
            null,
            null,
            SORT_DESC
        )
    }

    /**
     * Called when the Loader finishes its work and the result is available.
     */
    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor) {
        progress_bar_loading.isVisible = false
        smsList.clear()
        val smsListFromCursor = homeViewModel.getSmsListFromCursor(cursor)
        smsList.addAll(smsListFromCursor)

        if (!smsList.isNullOrEmpty()) {
            homeViewModel.filterTransactions(smsList = smsList) { debitedMessagesList, creditedMessagesList, debitedAmount, creditedAmount ->
                setData(debitedAmount = debitedAmount, creditedAmount = creditedAmount)
                this.debitedMessagesList = debitedMessagesList
                this.creditedMessagesList = creditedMessagesList
                switch_filter.isChecked = isSwitchChecked
            }
        }
    }

    /**
     * Clears the list to clean memory when loader is no longer running.
     */
    override fun onLoaderReset(loader: Loader<Cursor>) {
        progress_bar_loading.isVisible = false
        smsList.clear()
    }

    /**
     * Clearing resources when no longer required.
     */
    override fun onDestroy() {
        super.onDestroy()
        manager.destroyLoader(SMS_LOADER_ID)
    }

    /**
     * Check of result of empty and restarts the query to get data.
     */
    @SuppressLint("NewApi")
    override fun onResume() {
        super.onResume()
        if (smsList.isNullOrEmpty()) {
            if (checkPermissionsAvailable(smsPermissions)) {
                manager.restartLoader(SMS_LOADER_ID, null, this)
            }
        }
    }

    /**
     * Sets basic configuration and transaction data in chart view.
     */
    private fun setData(debitedAmount: Float, creditedAmount: Float) {
        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(
            PieEntry(
                debitedAmount,
                getString(R.string.debited)
            )
        )
        entries.add(
            PieEntry(
                creditedAmount,
                getString(R.string.credited)
            )
        )

        val dataSet = PieDataSet(entries, null)
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f
        val colors: ArrayList<Int> = ArrayList()
        colors.add(ContextCompat.getColor(this,
            R.color.colorDebited
        ))
        colors.add(ContextCompat.getColor(this,
            R.color.colorCredited
        ))
        dataSet.colors = colors
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(pie_chart_data))
        data.setValueTextSize(14f)
        data.setValueTextColor(Color.WHITE)
        pie_chart_data.description.isEnabled = false
        pie_chart_data.data = data
        pie_chart_data.highlightValues(null)
        pie_chart_data.invalidate()
    }

    /**
     * Checked change listener to listen to event of transaction switch
     */
    override fun onCheckedChanged(compoundButton: CompoundButton?, checked: Boolean) {
        when(compoundButton?.id) {
            R.id.switch_filter -> {
                isSwitchChecked = checked
                if (checked) {
                    text_view_header_title.text = getString(R.string.transactions_credited)
                    transactionAdapter?.submitList(creditedMessagesList)
                    transactionAdapter?.notifyDataSetChanged()
                } else {
                    text_view_header_title.text = getString(R.string.transactions_debited)
                    transactionAdapter?.submitList(debitedMessagesList)
                    transactionAdapter?.notifyDataSetChanged()
                }
            }
        }
    }
}