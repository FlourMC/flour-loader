package cn.enaium.flourmc.loader.mod;

import cn.enaium.flourmc.loader.api.ModInitializer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.plugface.core.PluginContext;
import org.plugface.core.impl.DefaultPluginContext;
import org.plugface.core.impl.DefaultPluginManager;
import org.plugface.core.internal.AnnotationProcessor;
import org.plugface.core.internal.DependencyResolver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Project: flour-loader
 * -----------------------------------------------------------
 * Copyright © 2020 | Enaium | All rights reserved.
 */
public class ModManager extends DefaultPluginManager {

    private static final ModManager INSTANCE = new ModManager(new DefaultPluginContext(), new AnnotationProcessor(), new DependencyResolver(new AnnotationProcessor()));

    protected static Logger LOGGER = LogManager.getFormatterLogger("Flour|ModManager");

    private final ArrayList<ModInitializer> mods = new ArrayList<>();

    private ModManager(PluginContext context, AnnotationProcessor annotationProcessor, DependencyResolver dependencyResolver) {
        super(context, annotationProcessor, dependencyResolver);
    }

    public void load(File modPath) throws Exception {
        ModSource source = new ModSource(modPath);
        Collection<Object> instances = loadPlugins(source);
        for (Object instance : instances) {
            if (instance instanceof ModInitializer) {
                ModInitializer plugin = (ModInitializer) instance;
                String className = instance.getClass().getName();
                String json = source.getJsons().get(source.getClassToMod().get(className));
                JSONObject jsonObject = JSON.parseObject(json);
                LOGGER.info(jsonObject.getString("name") + " By " + jsonObject.getString("author") + " " + jsonObject.getString("version") + " | " + jsonObject.getString("description"));
                plugin.onInitialize();
                mods.add(plugin);
            }
        }
    }

    public ArrayList<ModInitializer> getMods() {
        return mods;
    }

    public static ModManager getInstance() {
        return INSTANCE;
    }
}
