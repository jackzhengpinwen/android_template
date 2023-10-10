package com.example.myapplication.autoloader.generate;

import com.example.autoloader.IAutoLoadManager;

import java.util.ArrayList;

import com.example.myapplication.autoloader.strategy.StrategyOne;
import com.example.myapplication.autoloader.strategy.StrategyTwo;
import com.example.myapplication.autoloader.strategy.StrategyThree;


public class StrategyManager implements IAutoLoadManager {
    public ArrayList<?> load() {
        ArrayList<Object> iStrategies = new ArrayList<>();
        iStrategies.add(new StrategyOne());
        iStrategies.add(new StrategyTwo());
        iStrategies.add(new StrategyThree());
        return iStrategies;
    }
}
