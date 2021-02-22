package ru.daniilazarnov;

import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testAppClassLoader() {
        MainTest demoObject = new MainTest();
        ClassLoader applicationClassLoader = demoObject.getClass().getClassLoader();

        assertEquals("app", applicationClassLoader.getName());
        assertEquals("jdk.internal.loader.ClassLoaders$AppClassLoader", applicationClassLoader.getClass().getName());
    }

    @Test
    void testPlatformClassLoader() {
        java.sql.Date now = new Date(System.currentTimeMillis());
        ClassLoader platformClassLoader = now.getClass().getClassLoader();

        assertEquals("platform", platformClassLoader.getName());
        assertEquals("jdk.internal.loader.ClassLoaders$PlatformClassLoader", platformClassLoader.getClass().getName());
    }

    @Test
    void testBootstrapClassLoader() {
        ClassLoader bootstrapClassLoader = new int[]{}.getClass().getClassLoader();
        String s = "  ";
        Object object = new Object();

        // bootstrap classloader is represented by null in JVM
        assertNull(object.getClass().getClassLoader());
        assertNull(s.getClass().getClassLoader());
        assertNull(bootstrapClassLoader);
    }

    @Test
    void testClassNotFoundException() {
        ClassLoader classLoader = new MainTest().getClass().getClassLoader();
        assertThrows(ClassNotFoundException.class, () -> classLoader.loadClass("ru.daniilazarnov.GeekBrains"));
    }

}
