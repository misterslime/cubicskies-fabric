package net.misterslime.cubicskies.mixin.client;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.world.phys.Vec3;
import net.misterslime.cubicskies.client.Shaders;
import net.misterslime.cubicskies.clouds.CloudHandler;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelRenderer.class)
public final class MixinLevelRenderer {

    @Shadow private final int ticks;
    @Final @Shadow @NotNull private final Minecraft minecraft;
    @Shadow @NotNull private CloudStatus prevCloudsType;
    @Unique private boolean initializedClouds = false;
    @Unique private boolean generatedClouds = false;
    private int prevRenderDistance;

    public MixinLevelRenderer() {
        throw new NullPointerException("Null cannot be cast to non-null type.");
    }

    @Redirect(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderClouds(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/math/Matrix4f;FDDD)V"))
    public void renderClouds(LevelRenderer levelRenderer, PoseStack poseStack, Matrix4f model, float tickDelta, double cameraX, double cameraY, double cameraZ) {
        if (minecraft.level.dimension() == ClientLevel.OVERWORLD) {
            if (!this.initializedClouds) {
                CloudHandler.initClouds();
                CloudHandler.clearClouds();
                this.initializedClouds = true;
            }
            float cloudHeight = DimensionSpecialEffects.OverworldEffects.CLOUD_LEVEL;
            if (!Float.isNaN(cloudHeight)) {
                double speed = ((this.ticks + tickDelta) * 0.03F);
                double posX = (cameraX + CloudHandler.speed);
                double posY = (cloudHeight - (float) cameraY);
                Vec3 cloudColor = minecraft.level.getCloudColor(tickDelta);

                if (this.minecraft.options.renderDistance != this.prevRenderDistance) {
                    this.prevRenderDistance = this.minecraft.options.renderDistance;
                    CloudHandler.clearClouds();
                    this.generatedClouds = false;
                }

                if (CloudHandler.isCloudChunksNull()) {
                    if (!this.generatedClouds) {
                        CloudHandler.generateClouds(posX, cameraZ);
                        this.generatedClouds = true;
                    } else {
                        CloudHandler.updateClouds(posX, cameraZ);
                    }
                    if (Shaders.getRendertypeClouds() != null) {
                        CloudHandler.renderClouds(poseStack, model, cloudColor, posX, posY, cameraZ, this.prevCloudsType);
                    }
                } else {
                    CloudHandler.clearClouds();
                }
            }
        }
    }
}