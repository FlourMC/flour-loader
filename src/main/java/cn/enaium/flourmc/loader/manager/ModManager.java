package cn.enaium.flourmc.loader.manager;

import cn.enaium.flourmc.loader.annotations.FlourMod;
import cn.enaium.flourmc.loader.api.ModInitializer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Project: flour-loader
 * -----------------------------------------------------------
 * Copyright © 2020 | Enaium | All rights reserved.
 */
public class ModManager {

    private final File modPath;
    private ArrayList<ModInitializer> mods;

    public ModManager(File modPath) {
        this.modPath = modPath;
        mods = new ArrayList<>();
        if (!modPath.isDirectory())
            modPath.mkdir();
        for (File file : Objects.requireNonNull(modPath.listFiles())) {
            try {
                JarFile jar = new JarFile(file);
                Enumeration<JarEntry> entries = jar.entries();
                addURL(file.toURL());
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (entry.getName().endsWith(".class")) {
                        String className = entry.getName().substring(0, entry.getName().length() - 6);
                        className = className.replace('/', '.');
//                        Class<?> clazz = new URLClassLoader(new URL[]{file.toURL()}, Thread.currentThread().getContextClassLoader()).loadClass(className);
                        Class<?> clazz = Class.forName(className, true, ClassLoader.getSystemClassLoader());
                        if (clazz.getAnnotation(FlourMod.class) != null) {
                            mods.add((ModInitializer) clazz.newInstance());
                        }
                    }
                }
                jar.close();
            } catch (IOException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addURL(URL u) throws IOException {
        URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<?> sysclass = URLClassLoader.class;
        try {
            Method method = sysclass.getDeclaredMethod("addURL", new Class[] { URL.class });
            method.setAccessible(true);
            method.invoke(sysloader, new Object[] { u });
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    public ArrayList<ModInitializer> getMods() {
        return mods;
    }
}
