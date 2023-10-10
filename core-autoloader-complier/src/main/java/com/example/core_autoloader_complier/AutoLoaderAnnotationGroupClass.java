package com.example.core_autoloader_complier;

import java.util.HashSet;
import java.util.Set;


public class AutoLoaderAnnotationGroupClass {
    private Set<AutoLoaderAnnotationClass> itemsList = new HashSet<>();
    private String className;

    public AutoLoaderAnnotationGroupClass(String className) {
        this.className = className;
    }

    public void addAnnotationClass(AutoLoaderAnnotationClass autoLoaderAnnotationClass) {
        itemsList.add(autoLoaderAnnotationClass);
    }
}
