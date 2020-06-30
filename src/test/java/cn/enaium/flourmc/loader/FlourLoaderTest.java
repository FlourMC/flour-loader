package cn.enaium.flourmc.loader;

import cn.enaium.flourmc.loader.mod.ModManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    public static void main(String[] args) {
        LOGGER.info("Flour loading...");
        ModManager modManager = ModManager.getInstance();
        try {
            modManager.load(new File( "FlourMods"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}