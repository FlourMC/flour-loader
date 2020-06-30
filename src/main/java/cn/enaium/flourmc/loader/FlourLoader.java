package cn.enaium.flourmc.loader;

import cn.enaium.flourmc.loader.api.ModInitializer;
import cn.enaium.flourmc.loader.mod.ModManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
        LOGGER.info("Flour loading...");
        ModManager modManager = ModManager.getInstance();
        try {
            modManager.load(new File(gameDir + "/FlourMods"));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
