package com.example.myapplication.autoloader.test;

import android.util.Log;

import com.example.core_autoloader_annotation.AutoLoader;

@AutoLoader(ITest.class)
public class TestThree implements ITest{
    @Override
    public void test() {
        Log.d("MainActivity",getClass().getCanonicalName());
    }
}
