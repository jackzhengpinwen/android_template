package com.example.core_common.base.activity

import android.os.Bundle
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core_common.base.vm.BaseViewModel
import com.example.core_common.bean.DataBindingConfig
import com.example.core_common.bean.LoadAction
import com.example.core_common.ext.getVMCls
import com.example.core_common.utils.NetWorkUtil
import com.example.core_common.view.LoadingDialogManager

abstract class BaseVMActivity<VM: BaseViewModel>: AbsActivity() {
    protected lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startObserve()
    }

    override fun setContentView() {
        mViewModel = createViewModel()
        mViewModel.getActionLiveData().observe(this, startObserver)

        setContentView(getLayoutIdRes())
    }

    protected fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVMCls(this))
    }

    abstract fun getLayoutIdRes(): Int
    abstract fun startObserve()

    override fun onNetworkConnectionChanged(isConnected: Boolean, networkType: NetWorkUtil.NetworkType?) {
    }

    private var startObserver: Observer<LoadAction> = Observer { loadAction ->
        if (loadAction != null) {
            when(loadAction.action) {
                LoadAction.STATE_NORMAL -> showStateNormal()
                LoadAction.STATE_ERROR -> showStateError(loadAction.message)
                LoadAction.STATE_SUCCESS -> showStateSuccess()
                LoadAction.STATE_LOADING -> showStateLoading()
                LoadAction.STATE_NO_DATA -> showStateNoData()
                LoadAction.STATE_PROGRESS -> showStateProgress()
                LoadAction.STATE_HIDE_PROGRESS -> hideStateProgress()
            }
        }
    }

    protected open fun showStateNormal() {}

    protected open fun showStateError(message: String?) {
        LoadingDialogManager.get().dismissLoading(mActivity)
    }

    protected open fun showStateSuccess() {
        LoadingDialogManager.get().dismissLoading(mActivity)
    }

    protected open fun showStateLoading() {
        LoadingDialogManager.get().showLoading(mActivity)
    }

    protected open fun showStateNoData() {
        LoadingDialogManager.get().dismissLoading(mActivity)
    }

    protected fun showStateProgress() {
        LoadingDialogManager.get().showLoading(mActivity)
    }

    protected fun hideStateProgress() {
        LoadingDialogManager.get().dismissLoading(mActivity)
    }
}