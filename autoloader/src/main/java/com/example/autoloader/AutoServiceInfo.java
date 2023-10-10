package com.example.autoloader;

import java.util.HashMap;
import java.util.Map;

public class AutoServiceInfo {

    public static Map<String, String> map = new HashMap<>();

    static {
        map.put("com.example.myapplication.autoloader.test.ITest", "com.example.myapplication.autoloader.generate.TestManager");
        map.put("com.example.myapplication.autoloader.strategy.IStrategy", "com.example.myapplication.autoloader.generate.StrategyManager");
    }
}
