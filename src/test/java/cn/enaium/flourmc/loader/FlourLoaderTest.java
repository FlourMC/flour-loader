package cn.enaium.flourmc.loader;

import cn.enaium.flourmc.loader.manager.ClassManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.net.URLClassLoader;
import java.util.Objects;
import java.util.Scanner;

/**
 * Project: flour-loader
 * -----------------------------------------------------------
 * Copyright © 2020 | Enaium | All rights reserved.
 */
public class FlourLoaderTest {

    protected static Logger LOGGER = LogManager.getFormatterLogger("Flour|Loader");

    public static void main(String[] args) throws Exception {
        try {
            ClassManager classManager = new ClassManager(new File("FlourMods"));//Load Mods
            LOGGER.info("Loading...");
            for (URLClassLoader urlClassLoader : classManager.getUrlClassLoaders()) {
                LOGGER.info("Loaded " + classManager.getUrlClassLoaders().size() + " mods:");
                StringBuilder stringBuilder = new StringBuilder();
                Scanner scanner = new Scanner(Objects.requireNonNull(urlClassLoader.getResourceAsStream("flour.mod.json")));//Load flour.mod.json
                while (scanner.hasNext()) {
                    stringBuilder.append(scanner.next());
                }
                JSONObject jsonObject = JSON.parseObject(stringBuilder.toString());
                LOGGER.info(jsonObject.getString("name") + " " + jsonObject.getString("version") + " | " + jsonObject.getString("description"));
                Class<?> clazz = urlClassLoader.loadClass(jsonObject.getString("mainClass"));//Load mainClass
                clazz.getMethod("onInitialize").invoke(clazz.newInstance());//Invoke onInitialize
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}