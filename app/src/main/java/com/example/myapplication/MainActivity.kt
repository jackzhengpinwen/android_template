package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.core_common.Logger
import com.example.core_common.base.activity.BaseVDBActivity
import com.example.core_common.base.vm.EmptyViewModel
import com.example.core_common.bean.DataBindingConfig
import com.example.core_common.log.LogUtil
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.demo.demo1_activity_fragment_placeholder.activity.Demo1Activity

class MainActivity : BaseVDBActivity<EmptyViewModel, ActivityMainBinding>() {

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_main)
            .addBindingParams(BR.click, ClickProxy())
    }

    override fun startObserve() {
    }

    override fun init() {
    }

    inner class ClickProxy {

        fun navDemo1() {
            Demo1Activity.startInstance()
        }
    }
}