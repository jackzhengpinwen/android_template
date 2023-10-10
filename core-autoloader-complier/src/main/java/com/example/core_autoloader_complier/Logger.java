package com.example.core_autoloader_complier;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

public class Logger {
    private static Messager messager;

    public static void setMessager(Messager messager) {
        Logger.messager = messager;
    }


    public static void e(Element e, String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }

    public static void e(String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    public static void e(String msg) {
        messager.printMessage(Diagnostic.Kind.ERROR,msg);
    }

    public static void i(String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }

    public static void i(String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, msg);
    }
}
