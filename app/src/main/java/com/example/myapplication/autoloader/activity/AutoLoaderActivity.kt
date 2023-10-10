package com.example.myapplication.autoloader.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.autoloader.AutoServiceLoader
import com.example.core_common.ext.commContext
import com.example.myapplication.R
import com.example.myapplication.autoloader.strategy.IStrategy
import com.example.myapplication.autoloader.test.ITest
import com.example.myapplication.demo.demo1_activity_fragment_placeholder.activity.Demo1Activity

class AutoLoaderActivity : AppCompatActivity() {
    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, AutoLoaderActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_loader)
        val arrayList = ArrayList<String>()
        AutoServiceLoader.load(IStrategy::class.java).forEach {
            if (it.canLoad()) it.loadData(arrayList)
        }
        AutoServiceLoader.load(ITest::class.java).forEach {
            it.test()
        }
    }
}