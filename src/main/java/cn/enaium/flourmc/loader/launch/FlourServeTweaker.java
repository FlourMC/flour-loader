package cn.enaium.flourmc.loader.launch;

public class FlourServeTweaker extends FlourTweaker {
    @Override
    public String getLaunchTarget() {
        return "net.minecraft.server.MinecraftServer";
    }

    @Override
    protected boolean isClient() {
        return false;
    }

}
