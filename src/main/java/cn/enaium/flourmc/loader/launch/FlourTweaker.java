package cn.enaium.flourmc.loader.launch;

import cn.enaium.flourmc.loader.FlourLoader;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Project: flout-loader
 * -----------------------------------------------------------
 * Copyright © 2020 | Enaium | All rights reserved.
 */
public abstract class FlourTweaker implements ITweaker {

    private List<String> args;
    private FlourLoader flourLoader;

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        this.args = new ArrayList<>();
        flourLoader = new FlourLoader(gameDir);
        addOptions(args, gameDir, assetsDir, profile);
    }

    void addArg(String name, String value) {
        if (value != null) {
            args.add(name);
            args.add(value);
        }
    }

    protected void addOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        this.args.addAll(args);
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        flourLoader.load();
    }

    protected abstract boolean isClient();

    @Override
    public String[] getLaunchArguments() {
        return args.toArray(new String[0]);
    }

}
