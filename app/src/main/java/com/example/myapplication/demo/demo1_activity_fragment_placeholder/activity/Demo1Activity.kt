package com.example.myapplication.demo.demo1_activity_fragment_placeholder.activity

import android.content.Intent
import com.example.core_common.base.activity.BaseVDBActivity
import com.example.core_common.base.vm.EmptyViewModel
import com.example.core_common.bean.DataBindingConfig
import com.example.core_common.ext.commContext
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityDemo1Binding
import com.example.myapplication.BR

class Demo1Activity: BaseVDBActivity<EmptyViewModel, ActivityDemo1Binding>() {
    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo1Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    inner class ClickProxy {

        fun jumpLoadingActivity() {
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo_1)
            .addBindingParams(BR.click, ClickProxy())
    }

    override fun startObserve() {
    }

    override fun init() {
    }
}