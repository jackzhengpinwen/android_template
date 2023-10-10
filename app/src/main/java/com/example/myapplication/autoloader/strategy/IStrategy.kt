package com.example.myapplication.autoloader.strategy

interface IStrategy {
    fun canLoad():Boolean

    fun loadData(list: MutableList<String>)
}