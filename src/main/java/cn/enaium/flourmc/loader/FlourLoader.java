package cn.enaium.flourmc.loader;

import cn.enaium.flourmc.loader.api.ModInitializer;
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
 * Project: flout-loader
 * -----------------------------------------------------------
 * Copyright © 2020 | Enaium | All rights reserved.
 */
public class FlourLoader {

    protected static Logger LOGGER = LogManager.getFormatterLogger("Flour|Loader");

    private File gameDir;

    private boolean isClient;

    public void setGameDir(File gameDir) {
        this.gameDir = gameDir;
    }

    public void setClient(boolean client) {
        isClient = client;
    }

    public void load() {
        try {
            ClassManager classManager = new ClassManager(new File(gameDir + "/FlourMods"));//Load Mods
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

//                //Mixin
//
//                Mixins.addConfiguration(jsonObject.getString("mixin"));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

}
