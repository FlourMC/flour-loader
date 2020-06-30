package cn.enaium.flourmc.loader.mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.plugface.core.PluginSource;
import org.plugface.core.internal.PluginClassLoader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Project: flour-loader
 * -----------------------------------------------------------
 * Copyright © 2020 | Enaium | All rights reserved.
 */
public class ModSource implements PluginSource {

    private final Map<File, URL> jarUrls = new HashMap<>();
    private final Map<File, String> jsons = new HashMap<>();
    private final Map<String, File> classToMod = new HashMap<>();

    public ModSource(File modPath) throws IOException {
        if (!modPath.isDirectory())
            modPath.mkdir();
        for (File file : Objects.requireNonNull(modPath.listFiles())) {
            if (file.getName().endsWith(".jar")) {
                jarUrls.put(file, file.toURL());
            }
        }
    }

    @Override
    public Collection<Class<?>> load() {
        ArrayList<Class<?>> mods = new ArrayList<>();
        PluginClassLoader loader = new PluginClassLoader(jarUrls.values().toArray(new URL[0]));
        for (File modFile : jarUrls.keySet()) {
            try {
                final JarFile jar = new JarFile(modFile);
                String className;
                for (Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements(); ) {
                    final JarEntry entry = entries.nextElement();
                    //Add Class
                    if (entry.getName().endsWith(".class")) {
                        className = toName(entry);
                        mods.add(Class.forName(className, false, loader));
                        classToMod.put(className, modFile);
                    }

                    //Add Jsons
                    else if (entry.getName().endsWith("flour.mod.json")) {
                        StringBuilder stringBuilder = new StringBuilder();
                        Scanner scanner = new Scanner(jar.getInputStream(entry));
                        while (scanner.hasNext()) {
                            stringBuilder.append(scanner.next());
                        }
                        jsons.put(modFile, stringBuilder.toString());
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
        return mods;
    }

    public Map<File, URL> getJarUrls() {
        return jarUrls;
    }

    public Map<File, String> getJsons() {
        return jsons;
    }

    public Map<String, File> getClassToMod() {
        return classToMod;
    }

    private static String toName(JarEntry entry) {
        return entry.getName().substring(0, entry.getName().length() - 6).replace('/', '.');
    }
}
