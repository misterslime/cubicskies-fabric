package net.misterslime.cubicskies.mixin;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.misterslime.cubicskies.client.Shaders;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

    @Shadow @Final private final Map<String, ShaderInstance> shaders = Maps.newHashMap();

    @Shadow
    protected abstract ShaderInstance preloadShader(ResourceProvider arg, String string, VertexFormat vertexFormat) throws IOException;

    @Inject(method = "reloadShaders", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/renderer/GameRenderer;shutdownShaders()V"), cancellable = true)
    private void reloadShaders(ResourceManager resourceManager, CallbackInfo ci) throws IOException {
        //list2.add(Pair.of(new ShaderInstance(resourceManager, "rendertype_clouds", DefaultVertexFormat.POSITION_COLOR_NORMAL), (Consumer<ShaderInstance>) Shaders::setRendertypeClouds));

        Pair pair = Pair.of(new ShaderInstance(resourceManager, "rendertype_clouds", DefaultVertexFormat.POSITION_COLOR_NORMAL), (Consumer<ShaderInstance>) Shaders::setRendertypeClouds);

        ShaderInstance shaderInstance = (ShaderInstance)Pair.unbox(pair).getFirst();
        this.shaders.put(shaderInstance.getName(), shaderInstance);
        ((Consumer)Pair.unbox(pair).getSecond()).accept(shaderInstance);
    }
}
