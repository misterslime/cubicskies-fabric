package net.misterslime.cubicskies.clouds;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3d;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;
import net.minecraft.world.phys.Vec3;
import net.misterslime.cubicskies.core.Vec2i;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class CloudHandler {

    public static Vec2i cloudStartPos;
    public static Vec2i cloudPos;
    public static ImprovedNoise noise;
    public static double cloudiness;

    private static CloudChunk[][] cloudChunks;

    public static void initClouds() {
        Random random = new Random();
        noise = new ImprovedNoise(new WorldgenRandom(random.nextLong()));
        cloudiness = random.nextDouble();
    }

    public static void clearClouds() {
        int cloudDistance = Minecraft.getInstance().options.renderDistance;
        cloudChunks = new CloudChunk[cloudDistance * 2][cloudDistance * 2];
    }

    public static void updateClouds(double posX, double posZ) {
        cloudPos = new Vec2i((int) Math.floor(posX / 16), (int) Math.floor(posZ / 16));
    }

    public static void generateCloudChunks(double posX, double posZ) {
        int cloudDistance = cloudChunks.length / 2;
        updateClouds(posX, posZ);

        // to do: actual cloud generation
        for (int x = -cloudDistance; x < cloudDistance; x++) {
            for (int z = -cloudDistance; z < cloudDistance; z++) {
                CloudChunk cloudChunk = new CloudChunk(x, z);
                cloudChunk.generateClouds(cloudPos);
                cloudChunks[x + cloudDistance][z + cloudDistance] = cloudChunk;
            }
        }

        cloudStartPos = new Vec2i(0, 0);
    }

    public static void renderCloudChunks(PoseStack poseStack, Matrix4f model, Vec3 color, double posX, double posY, double posZ, CloudStatus prevCloudsType) {
        RenderSystem.enableCull();
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.depthMask(true);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor((float) color.x, (float) color.y, (float) color.z, 1.0F);
        FogRenderer.levelFogColor();

        for (CloudChunk[] cloudChunkArray : cloudChunks) {
            for (CloudChunk cloudChunk : cloudChunkArray) {
                cloudChunk.renderCloudChunk(poseStack, model, color, posX, posY, posZ, prevCloudsType);
            }
        }

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    public static boolean isCloudChunksNull() {
        return cloudChunks != null;
    }
}
