package com.example.myapplication.autoloader.strategy

import android.util.Log
import com.example.core_autoloader_annotation.AutoLoader

@AutoLoader(IStrategy::class)
class StrategyOne: IStrategy {
    override fun canLoad(): Boolean {
        return true
    }

    override fun loadData(list: MutableList<String>) {
        Log.d("MainActivity","Load First Strategy Data")
    }
}