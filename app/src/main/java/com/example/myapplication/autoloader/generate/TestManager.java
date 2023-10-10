package com.example.myapplication.autoloader.generate;

import com.example.autoloader.IAutoLoadManager;
import com.example.myapplication.autoloader.test.TestOne;
import com.example.myapplication.autoloader.test.TestThree;
import com.example.myapplication.autoloader.test.TestTwo;

import java.util.ArrayList;


public class TestManager implements IAutoLoadManager {
    @Override
    public ArrayList<?> load() {
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(new TestOne());
        objects.add(new TestTwo());
        objects.add(new TestThree());
        return objects;
    }
}
