package cn.enaium.flourmc.loader;

import cn.enaium.flourmc.loader.api.ModInitializer;
import cn.enaium.flourmc.loader.manager.ModManager;

import java.io.File;

/**
 * Project: flour-loader
 * -----------------------------------------------------------
 * Copyright © 2020 | Enaium | All rights reserved.
 */
public class FlourLoader {
    private ModManager modManager;

    public FlourLoader(File gameDir) {
        modManager = new ModManager(new File(gameDir.toString() + "/FlourMods"));
    }

    public void load() {
        modManager.getMods().forEach(ModInitializer::onInitialize);
    }
}
