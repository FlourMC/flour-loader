package cn.enaium.flourmc.loader;

import cn.enaium.flourmc.loader.api.ModInitializer;
import cn.enaium.flourmc.loader.manager.ModManager;

import java.io.File;

/**
 * Project: flour-loader
 * -----------------------------------------------------------
 * Copyright © 2020 | Enaium | All rights reserved.
 */
public class FlourLoaderTest {
    private static ModManager modManager = new ModManager(new File("FlourMods"));

    public static void main(String[] args) {
        modManager.getMods().forEach(ModInitializer::onInitialize);
    }
}
