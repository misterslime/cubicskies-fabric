package net.misterslime.cubicskies.client;

import net.minecraft.client.renderer.ShaderInstance;
import org.jetbrains.annotations.Nullable;

public class Shaders {

    private static ShaderInstance rendertypeClouds;

    public static void setRendertypeClouds(ShaderInstance shaderInstance) {
        rendertypeClouds = shaderInstance;
    }

    public static @Nullable ShaderInstance getRendertypeClouds() {
        return rendertypeClouds;
    }
}
