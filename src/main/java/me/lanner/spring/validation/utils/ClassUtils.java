package me.lanner.spring.validation.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by zhaochen.zc on 15/10/24.
 */
public class ClassUtils {
    private ClassUtils() {
        throw new UnsupportedOperationException();
    }

    public static Set<Class<?>> loadClassesImplementsTheInterface(String packageName, Class<?> interfaceClazz)
            throws IOException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace(".", "/");
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        Set<Class<?>> classes = new HashSet<>();
        for (File directory : dirs) {
            classes.addAll(loadClasses(directory, packageName, interfaceClazz));
        }
        return classes;
    }

    private static List<Class<?>> loadClasses(File directory, String packageName, Class<?> interfaceClazz) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(loadClasses(file, packageName + "." + file.getName(), interfaceClazz));
            } else if (file.getName().endsWith(".class")) {
            	String className = file.getName().substring(0, file.getName().length()-".class".length());
            	className = packageName + "." + className;
            	Class<?> clazz = Class.forName(className);
            	if (interfaceClazz != clazz && 
            			interfaceClazz.isAssignableFrom(clazz)) {
            		classes.add(clazz);
            	}
            }
        }
        return classes;
    }
}
