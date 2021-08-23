package net.misterslime.cubicskies.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.misterslime.cubicskies.clouds.CloudHandler;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelRenderer.class)
public final class MixinLevelRenderer {

    @Shadow
    private final int ticks;
    @Final
    @Shadow
    @NotNull
    private final Minecraft minecraft;
    @Shadow
    private int prevCloudX;
    @Shadow
    private int prevCloudY;
    @Shadow
    private int prevCloudZ;
    @Shadow
    @NotNull
    private Vec3 prevCloudColor;
    @Shadow
    @NotNull
    private CloudStatus prevCloudsType;
    @Shadow
    private boolean generateClouds;
    @Shadow
    @NotNull
    private VertexBuffer cloudBuffer;
    @Unique
    private boolean initializedClouds = false;
    @Unique
    private boolean generatedClouds = false;
    private int prevRenderDistance;

    public MixinLevelRenderer() {
        throw new NullPointerException("Null cannot be cast to non-null type.");
    }

    @Redirect(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderClouds(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/math/Matrix4f;FDDD)V"))
    public void renderClouds(LevelRenderer levelRenderer, PoseStack poseStack, Matrix4f model, float tickDelta, double cameraX, double cameraY, double cameraZ) {
        if (minecraft.level.dimension() == ClientLevel.OVERWORLD) {
            if (!this.initializedClouds) {
                CloudHandler.initClouds();
                this.initializedClouds = true;
            }
            float cloudHeight = DimensionSpecialEffects.OverworldEffects.CLOUD_LEVEL;
            if (!Float.isNaN(cloudHeight)) {
                double speed = ((this.ticks + tickDelta) * 0.03F);
                double posX = (cameraX + speed);
                double posY = (cloudHeight - (float) cameraY);
                Vec3 cloudColor = minecraft.level.getCloudColor(tickDelta);
                int floorX = (int) Math.floor(posX);
                int floorY = (int) Math.floor(posY);
                int floorZ = (int) Math.floor(cameraZ);
                if (floorX != this.prevCloudX || floorY != this.prevCloudY || floorZ != this.prevCloudZ || this.minecraft.options.getCloudsType() != this.prevCloudsType || this.prevCloudColor.distanceToSqr(cloudColor) > 2.0E-4D) {
                    this.prevCloudX = floorX;
                    this.prevCloudY = floorY;
                    this.prevCloudZ = floorZ;
                    this.prevCloudColor = cloudColor;
                    this.prevCloudsType = this.minecraft.options.getCloudsType();
                    this.generateClouds = true;
                }

                if (this.minecraft.options.renderDistance != this.prevRenderDistance) {
                    this.prevRenderDistance = this.minecraft.options.renderDistance;
                    CloudHandler.updateCloudDistance();
                    CloudHandler.resetClouds();
                    this.generatedClouds = false;
                }

                if (!this.generatedClouds) {
                    CloudHandler.generateCloudLayer(posX, posY, cameraZ);
                    this.generatedClouds = true;
                }

                CloudHandler.renderVoxelClouds(poseStack, model, cloudColor, posX, posY, cameraZ, this.prevCloudsType);
            }
        }
    }
}