package ru.gb.putilin.cloudstorage;

import java.sql.Date;

public class Main {

    public static void main(String[] args) {
        Main demoObject = new Main();
        ClassLoader applicationClassLoader = demoObject.getClass().getClassLoader();
        printClassLoaderDetails(applicationClassLoader);

        java.sql.Date now = new Date(System.currentTimeMillis());
        ClassLoader platformClassLoader = now.getClass().getClassLoader();
        printClassLoaderDetails(platformClassLoader);

        ClassLoader bootstrapClassLoader = args.getClass().getClassLoader();

        String s = "  ";
        Object object = new Object();

        printClassLoaderDetails(object.getClass().getClassLoader());
        printClassLoaderDetails(s.getClass().getClassLoader());
        printClassLoaderDetails(bootstrapClassLoader);
    }

    private static void printClassLoaderDetails(ClassLoader classLoader) {
        // bootstrap classloader is represented by null in JVM
        if (classLoader != null) {
            System.out.println("ClassLoader name : " + classLoader.getName());
            System.out.println("ClassLoader class : " + classLoader.getClass().getName());
        } else {
            System.out.println("Bootstrap classloader");
        }
    }

}
