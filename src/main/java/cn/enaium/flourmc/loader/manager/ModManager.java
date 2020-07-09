package cn.enaium.flourmc.loader.manager;

import cn.enaium.flourmc.loader.annotations.FlourMod;
import cn.enaium.flourmc.loader.api.ModInitializer;
import com.alibaba.fastjson.JSON;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Project: flour-loader
 * -----------------------------------------------------------
 * Copyright © 2020 | Enaium | All rights reserved.
 */
public class ModManager {

    private ArrayList<ModInitializer> mods;
    private ArrayList<String> mixins;

    public ModManager(File modPath) {
        mods = new ArrayList<>();
        mixins = new ArrayList<>();
        if (!modPath.isDirectory())
            modPath.mkdir();
        for (File file : Objects.requireNonNull(modPath.listFiles())) {
            try {
                JarFile jar = new JarFile(file);
                Enumeration<JarEntry> entries = jar.entries();
                addURL(file.toURI().toURL());
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
                    } else if (entry.getName().equals("flour.mod.json")) {
                        StringBuilder stringBuilder = new StringBuilder();
                        Scanner scanner = new Scanner(jar.getInputStream(entry));
                        while (scanner.hasNext()) {
                            stringBuilder.append(scanner.next());
                        }
                        mixins.add(JSON.parseObject(stringBuilder.toString()).getString("mixin"));
                    }
                }
                jar.close();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public static void addURL(URL url) {
        URLClassLoader systemClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(systemClassLoader, url);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public ArrayList<ModInitializer> getMods() {
        return mods;
    }

    public ArrayList<String> getMixins() {
        return mixins;
    }
}
