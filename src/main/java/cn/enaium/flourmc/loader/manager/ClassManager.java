package cn.enaium.flourmc.loader.manager;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Project: flout-loader
 * -----------------------------------------------------------
 * Copyright © 2020 | Enaium | All rights reserved.
 */
public class ClassManager {

    private ArrayList<URLClassLoader> urlClassLoaders = new ArrayList<>();

    public ClassManager(File filePath) throws Exception {
        for (File f : Objects.requireNonNull(filePath.listFiles())) {
            if (f.getName().endsWith(".jar")) {
                URLClassLoader clazz = new URLClassLoader(new URL[]{f.toURL()}, Thread.currentThread().getContextClassLoader());
                urlClassLoaders.add(clazz);
            }
        }
    }

    public ArrayList<URLClassLoader> getUrlClassLoaders() {
        return urlClassLoaders;
    }

}
