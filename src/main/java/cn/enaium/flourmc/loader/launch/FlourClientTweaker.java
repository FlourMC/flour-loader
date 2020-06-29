package cn.enaium.flourmc.loader.launch;

import java.io.File;
import java.util.List;

public class FlourClientTweaker extends FlourTweaker {
    @Override
    protected void addOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        super.addOptions(args, gameDir, assetsDir, profile);
        addArg("--gameDir", gameDir != null ? gameDir.getAbsolutePath() : null);
        addArg("--assetsDir", assetsDir != null ? assetsDir.getPath() : null);
        addArg("--version", profile);
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @Override
    protected boolean isClient() {
        return true;
    }
}
