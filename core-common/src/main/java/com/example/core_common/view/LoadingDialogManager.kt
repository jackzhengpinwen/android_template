package com.example.core_common.view

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import com.example.core_common.R

class LoadingDialogManager {
    companion object {
        private val instance = LoadingDialogManager()

        fun get(): LoadingDialogManager = instance
    }

    private lateinit var dialog: Dialog
    private lateinit var progressDialog: Dialog

    fun isShowing(activity: Activity): Boolean {
        initDialog(activity)
        return dialog.isShowing
    }

    /**
     * 展示dialog
     */
    fun showLoading(activity: Activity) {
        initDialog(activity)
        if (dialog.isShowing) {
            return
        }

        dialog.show()
    }

    private fun initDialog(activity: Activity) {
        if (dialog != null) return
        dialog = Dialog(activity, R.style.Theme_LoadingDialog)
        //自定义布局
        val view: View = LayoutInflater.from(activity).inflate(R.layout.base_layout_loading, null)
        dialog.setContentView(view)
        //一定要先show出来再设置dialog的参数，不然就不会改变dialog的大小了
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnCancelListener {
            if (mListener != null) mListener?.onLoadingCancel()
        }
    }


    /**
     * 展示长条的大Dialog
     */
    fun showBigLoading(activity: Activity) {
        initProgressDialog(activity)
        if (progressDialog.isShowing) {
            return
        }
        progressDialog.show()
    }

    /**
     * 隐藏dialog
     */
    fun dismissLoading(activity: Activity) {
        initDialog(activity)
        if (dialog.isShowing) {
            dialog.dismiss()
            dialog.cancel()
        }
        initProgressDialog(activity)
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
            progressDialog.cancel()
        }
    }

    private fun initProgressDialog(activity: Activity) {
        if (progressDialog != null) return
        progressDialog = Dialog(activity!!, R.style.Theme_LoadingDialog)
        //自定义布局
        val view: View =
            LayoutInflater.from(activity).inflate(R.layout.base_layout_uploading_progress, null)
        progressDialog.setContentView(view)
        //一定要先show出来再设置dialog的参数，不然就不会改变dialog的大小了
        progressDialog.setCancelable(true)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setOnCancelListener {
            if (mListener != null) mListener!!.onLoadingCancel()
        }
    }

    private var mListener: OnLoadingCancelListener? = null

    fun setOnLoadingCancelListener(listener: OnLoadingCancelListener?) {
        mListener = listener
    }

    interface OnLoadingCancelListener {
        fun onLoadingCancel()
    }
}