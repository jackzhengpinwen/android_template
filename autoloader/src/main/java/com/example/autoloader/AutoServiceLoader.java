package com.example.autoloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoServiceLoader {

    private static final HashMap<String, List<?>> map = new HashMap<>();

    private AutoServiceLoader() {

    }

    private static class Singleton {
        public static AutoServiceLoader INSTANCE = new AutoServiceLoader();
    }

    public void init() {
        for (Map.Entry<String, String> entry : AutoServiceInfo.map.entrySet()) {
            try {
                Class<?> aClass = Class.forName(entry.getValue());
                if (IAutoLoadManager.class.isAssignableFrom(aClass)) {
                    try {
                        IAutoLoadManager iManager = (IAutoLoadManager) aClass.newInstance();
                        map.put(entry.getKey(), iManager.load());
                    } catch (IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static AutoServiceLoader getInstance() {
        return Singleton.INSTANCE;
    }

    @SuppressWarnings("unchecked cast")
    public static <S> List<S> load(Class<S> service) {
        List<?> list = map.get(service.getCanonicalName());
        if (list != null) {
            return (List<S>) list;
        } else {
            return new ArrayList<>();
        }
    }
}
