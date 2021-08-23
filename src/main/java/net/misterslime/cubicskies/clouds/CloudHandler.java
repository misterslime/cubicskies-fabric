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
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;


import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class CloudHandler {

    public static final AABB CLOUD_BOUNDING_BOX = new AABB(0, 0, 0, 4, 3, 4);

    public static ImprovedNoise noise;
    public static VertexBuffer cloudBuffer;
    public static double cloudiness;
    public static int cloudDistance;

    private static final List<CloudVoxel> cloudVoxels = new LinkedList<>();

    public static void initClouds() {
        Random random = new Random();
        noise = new ImprovedNoise(new WorldgenRandom(random.nextLong()));
        cloudiness = random.nextDouble();
        updateCloudDistance();
    }

    public static void updateCloudDistance() {
        cloudDistance = (Minecraft.getInstance().options.renderDistance * 16) / (int) CLOUD_BOUNDING_BOX.maxX;
    }

    public static void resetClouds() {
        cloudVoxels.clear();
    }

    public static void generateCloudLayer(double posX, double posY, double posZ) {
        int cloudDepth = (int) Math.floor(172 / CLOUD_BOUNDING_BOX.maxY);

        // to do: actual cloud generation && cloud chunks
        for (int x = -cloudDistance; x <= cloudDistance; x++) {
            for (int y = 0; y < cloudDepth; y++) {
                for (int z = -cloudDistance; z <= cloudDistance; z++) {
                    if (noise.noise(x / 16.0, y / 16.0, z / 16.0) * 2.5 >= cloudiness) {
                        cloudVoxels.add(new CloudVoxel(new Vec3i(x, y, z), false));
                    }
                }
            }
        }

        buildCloudChunk(posX, posY, posZ);
    }

    public static void buildCloudChunk(double posX, double posY, double posZ) {
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        if (cloudBuffer != null) cloudBuffer.close();

        cloudBuffer = new VertexBuffer();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        int dist = (int) Math.pow(cloudDistance, 2);
        for (CloudVoxel cloudVoxel : cloudVoxels) {

            drawCloudVoxelVertices(bufferBuilder, cloudVoxel.pos, posX, posY, posZ, cloudVoxel.getColor());
        }

        bufferBuilder.end();
        cloudBuffer.upload(bufferBuilder);
    }

    private static void drawCloudVoxelVertices(BufferBuilder buffer, Vec3i cubePos, double posX, double posY, double posZ, Vec3 color) {
        AABB box = CLOUD_BOUNDING_BOX;
        Vector3d cloudPos = new Vector3d(cubePos.getX() * CLOUD_BOUNDING_BOX.maxX, cubePos.getY() * CLOUD_BOUNDING_BOX.maxY, cubePos.getZ() * CLOUD_BOUNDING_BOX.maxZ);
        box = box.move(cloudPos.x, cloudPos.y, cloudPos.z);

        // Bottom
        buffer.vertex((float) box.maxX, (float) box.minY, (float) box.minZ).color((float) (color.x * 0.7f), (float) (color.y * 0.7f), (float) (color.z * 0.7f), 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
        buffer.vertex((float) box.maxX, (float) box.minY, (float) box.maxZ).color((float) (color.x * 0.7f), (float) (color.y * 0.7f), (float) (color.z * 0.7f), 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
        buffer.vertex((float) box.minX, (float) box.minY, (float) box.maxZ).color((float) (color.x * 0.7f), (float) (color.y * 0.7f), (float) (color.z * 0.7f), 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
        buffer.vertex((float) box.minX, (float) box.minY, (float) box.minZ).color((float) (color.x * 0.7f), (float) (color.y * 0.7f), (float) (color.z * 0.7f), 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();

        // Top
        buffer.vertex((float) box.minX, (float) box.maxY, (float) box.maxZ).color((float) color.x, (float) color.y, (float) color.z, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
        buffer.vertex((float) box.maxX, (float) box.maxY, (float) box.maxZ).color((float) color.x, (float) color.y, (float) color.z, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
        buffer.vertex((float) box.maxX, (float) box.maxY, (float) box.minZ).color((float) color.x, (float) color.y, (float) color.z, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();
        buffer.vertex((float) box.minX, (float) box.maxY, (float) box.minZ).color((float) color.x, (float) color.y, (float) color.z, 0.8f).normal(0.0f, 1.0f, 0.0f).endVertex();

        // North
        buffer.vertex((float) box.minX, (float) box.maxY, (float) box.minZ).color((float) (color.x * 0.8f), (float) (color.y * 0.8f), (float) (color.z * 0.8f), 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
        buffer.vertex((float) box.maxX, (float) box.maxY, (float) box.minZ).color((float) (color.x * 0.8f), (float) (color.y * 0.8f), (float) (color.z * 0.8f), 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
        buffer.vertex((float) box.maxX, (float) box.minY, (float) box.minZ).color((float) (color.x * 0.8f), (float) (color.y * 0.8f), (float) (color.z * 0.8f), 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();
        buffer.vertex((float) box.minX, (float) box.minY, (float) box.minZ).color((float) (color.x * 0.8f), (float) (color.y * 0.8f), (float) (color.z * 0.8f), 0.8f).normal(0.0f, 0.0f, -1.0f).endVertex();

        // South
        buffer.vertex((float) box.minX, (float) box.minY, (float) box.maxZ).color((float) (color.x * 0.8f), (float) (color.y * 0.8f), (float) (color.z * 0.8f), 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
        buffer.vertex((float) box.maxX, (float) box.minY, (float) box.maxZ).color((float) (color.x * 0.8f), (float) (color.y * 0.8f), (float) (color.z * 0.8f), 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
        buffer.vertex((float) box.maxX, (float) box.maxY, (float) box.maxZ).color((float) (color.x * 0.8f), (float) (color.y * 0.8f), (float) (color.z * 0.8f), 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();
        buffer.vertex((float) box.minX, (float) box.maxY, (float) box.maxZ).color((float) (color.x * 0.8f), (float) (color.y * 0.8f), (float) (color.z * 0.8f), 0.8f).normal(0.0f, 0.0f, 1.0f).endVertex();

        // West
        buffer.vertex((float) box.minX, (float) box.minY, (float) box.minZ).color((float) (color.x * 0.9f), (float) (color.y * 0.9f), (float) (color.z * 0.9f), 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
        buffer.vertex((float) box.minX, (float) box.minY, (float) box.maxZ).color((float) (color.x * 0.9f), (float) (color.y * 0.9f), (float) (color.z * 0.9f), 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
        buffer.vertex((float) box.minX, (float) box.maxY, (float) box.maxZ).color((float) (color.x * 0.9f), (float) (color.y * 0.9f), (float) (color.z * 0.9f), 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();
        buffer.vertex((float) box.minX, (float) box.maxY, (float) box.minZ).color((float) (color.x * 0.9f), (float) (color.y * 0.9f), (float) (color.z * 0.9f), 0.8f).normal(-1.0f, 0.0f, 0.0f).endVertex();


        // East
        buffer.vertex((float) box.maxX, (float) box.maxY, (float) box.minZ).color((float) (color.x * 0.9f), (float) (color.y * 0.9f), (float) (color.z * 0.9f), 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
        buffer.vertex((float) box.maxX, (float) box.maxY, (float) box.maxZ).color((float) (color.x * 0.9f), (float) (color.y * 0.9f), (float) (color.z * 0.9f), 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
        buffer.vertex((float) box.maxX, (float) box.minY, (float) box.maxZ).color((float) (color.x * 0.9f), (float) (color.y * 0.9f), (float) (color.z * 0.9f), 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
        buffer.vertex((float) box.maxX, (float) box.minY, (float) box.minZ).color((float) (color.x * 0.9f), (float) (color.y * 0.9f), (float) (color.z * 0.9f), 0.8f).normal(1.0f, 0.0f, 0.0f).endVertex();
    }

    public static void renderVoxelClouds(PoseStack poseStack, Matrix4f model, Vec3 color, double posX, double posY, double posZ, CloudStatus prevCloudsType) {
        RenderSystem.enableCull();
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.depthMask(true);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor((float) color.x, (float) color.y, (float) color.z, 1.0F);
        FogRenderer.levelFogColor();

        poseStack.pushPose();
        poseStack.scale(1.0f, 1.0f, 1.0f);
        poseStack.translate(-posX, posY, -posZ);

        if (CloudHandler.cloudBuffer != null) {
            int cloudMainIndex = prevCloudsType == CloudStatus.FANCY ? 0 : 1;

            for (int cloudIndex = 1; cloudMainIndex <= cloudIndex; ++cloudMainIndex) {
                if (cloudMainIndex == 0) {
                    RenderSystem.colorMask(false, false, false, false);
                } else {
                    RenderSystem.colorMask(true, true, true, true);
                }

                ShaderInstance shader = RenderSystem.getShader();
                CloudHandler.cloudBuffer.drawWithShader(poseStack.last().pose(), model, shader);
            }
        }

        poseStack.popPose();

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }
}
