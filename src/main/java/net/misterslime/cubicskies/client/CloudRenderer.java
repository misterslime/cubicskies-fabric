package net.misterslime.cubicskies.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.phys.Vec3;
import net.misterslime.cubicskies.client.shader.Shaders;
import net.misterslime.cubicskies.clouds.CloudChunk;
import net.misterslime.cubicskies.clouds.gen.noise.OpenSimplex2F;
import net.misterslime.cubicskies.clouds.gen.noise.VoronoiNoise;
import net.misterslime.cubicskies.core.Vec2i;

import java.util.Random;

public class CloudRenderer {

    public static Vec2i prevCloudPos;
    public static Vec2i cloudOffsetPos;
    public static OpenSimplex2F noise;
    public static VoronoiNoise voronoi;
    public static double cloudiness;
    public static double speed = 0;

    private static CloudChunk[][] cloudChunks;

    public static void initClouds() {
        Random random = new Random();
        noise = new OpenSimplex2F(random.nextLong());
        cloudiness = random.nextDouble();
        cloudOffsetPos = new Vec2i(0, 0);
    }

    public static void clearClouds() {
        int cloudDistance = Minecraft.getInstance().options.renderDistance;
        cloudChunks = new CloudChunk[cloudDistance][cloudDistance];
    }

    public static void updateClouds(double posX, double posZ) {
        int cloudDistance = Minecraft.getInstance().options.renderDistance / 2;
        Vec2i cloudPos = new Vec2i((int) Math.floor(posX / 32), (int) Math.floor(posZ / 32));

        if (!prevCloudPos.equals(cloudPos)) {
            Vec2i posDelta = new Vec2i(cloudPos.getX() - prevCloudPos.getX(), cloudPos.getY() - prevCloudPos.getY());

            prevCloudPos = cloudPos;

            cloudOffsetPos.addX(posDelta.getX());
            if (posDelta.getX() >= 1) {
                // Generate new chunks
                CloudChunk[] newCloudChunks = new CloudChunk[cloudDistance * 2];
                for (int z = -cloudDistance; z < cloudDistance; z++) {
                    CloudChunk cloudChunk = new CloudChunk();
                    cloudChunk.generateCloudChunk(prevCloudPos, new Vec2i((cloudChunks.length / 2) - 1, z));
                    newCloudChunks[z + cloudDistance] = cloudChunk;
                }

                // Move array
                CloudChunk[][] newCloudChunksArray = new CloudChunk[cloudChunks.length][];
                int j = 0;
                for(int i = 0; i < newCloudChunksArray.length; i++) {
                    if(i != 0)
                    {
                        newCloudChunksArray[j] = cloudChunks[i];
                        j++;
                    }
                }
                newCloudChunksArray[cloudChunks.length - 1] = newCloudChunks;
                cloudChunks = newCloudChunksArray;
            } else if (posDelta.getX() <= -1) {
                // Generate new chunks
                CloudChunk[] newCloudChunks = new CloudChunk[cloudDistance * 2];
                for (int z = -cloudDistance; z < cloudDistance; z++) {
                    CloudChunk cloudChunk = new CloudChunk();
                    cloudChunk.generateCloudChunk(prevCloudPos, new Vec2i((cloudChunks.length / 2), z));
                    newCloudChunks[z + cloudDistance] = cloudChunk;
                }

                // Move array
                CloudChunk[][] newCloudChunksArray = new CloudChunk[cloudChunks.length][];
                for(int i = 0; i < newCloudChunksArray.length; i++) {
                    if(i != cloudChunks.length - 1)
                    {
                        newCloudChunksArray[i + 1] = cloudChunks[i];
                    }
                }
                newCloudChunksArray[0] = newCloudChunks;
                cloudChunks = newCloudChunksArray;
            }

            cloudOffsetPos.addY(posDelta.getY());
            if (posDelta.getY() >= 1) {
                for (int x = -cloudDistance; x < cloudDistance; x++) {
                    // Generate new chunks
                    CloudChunk cloudChunk = new CloudChunk();
                    cloudChunk.generateCloudChunk(prevCloudPos, new Vec2i(x, (cloudChunks.length / 2) - 1));

                    // Move array
                    CloudChunk[] newCloudChunksArray = new CloudChunk[cloudChunks[x + cloudDistance].length];
                    int j = 0;
                    for(int i = 0; i < newCloudChunksArray.length; i++) {
                        if(i != 0)
                        {
                            newCloudChunksArray[j] = cloudChunks[x + cloudDistance][i];
                            j++;
                        }
                    }
                    newCloudChunksArray[cloudChunks.length - 1] = cloudChunk;
                    cloudChunks[x + cloudDistance] = newCloudChunksArray;
                }
            } else if (posDelta.getY() <= -1) {
                for (int x = -cloudDistance; x < cloudDistance; x++) {
                    // Generate new chunks
                    CloudChunk cloudChunk = new CloudChunk();
                    cloudChunk.generateCloudChunk(prevCloudPos, new Vec2i(x, (cloudChunks.length / 2)));

                    // Move array
                    CloudChunk[] newCloudChunksArray = new CloudChunk[cloudChunks[x + cloudDistance].length];
                    for(int i = 0; i < newCloudChunksArray.length; i++) {
                        if(i != cloudChunks.length - 1)
                        {
                            newCloudChunksArray[i + 1] = cloudChunks[x + cloudDistance][i];
                        }
                    }
                    newCloudChunksArray[0] = cloudChunk;
                    cloudChunks[x + cloudDistance] = newCloudChunksArray;
                }
            }
        }
    }

    public static void generateClouds(double posX, double posZ) {
        int cloudDistance = cloudChunks.length / 2;
        prevCloudPos = new Vec2i((int) Math.floor(posX / 32), (int) Math.floor(posZ / 32));

        // to do: actual cloud generation
        for (int x = -cloudDistance; x < cloudDistance; x++) {
            for (int z = -cloudDistance; z < cloudDistance; z++) {
                CloudChunk cloudChunk = new CloudChunk();
                cloudChunk.generateCloudChunk(prevCloudPos, new Vec2i(x, z));
                cloudChunks[x + cloudDistance][z + cloudDistance] = cloudChunk;
            }
        }
    }

    public static void renderClouds(PoseStack poseStack, Matrix4f model, Vec3 color, double posX, double posY, double posZ, CloudStatus prevCloudsType) {
        RenderSystem.enableCull();
        //RenderSystem.enableBlend();
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.depthMask(true);
        RenderSystem.setShader(Shaders::getRendertypeClouds);
        RenderSystem.setShaderColor((float) color.x, (float) color.y, (float) color.z, 1.0F);
        FogRenderer.levelFogColor();

        int cloudDistance = cloudChunks.length / 2;
        for (int x = -cloudDistance; x < cloudDistance; x++) {
            for (int z = -cloudDistance; z < cloudDistance; z++) {
                cloudChunks[x + cloudDistance][z + cloudDistance].renderCloudChunk(poseStack, model, new Vec2i(x, z), posX, posY, posZ, prevCloudsType);
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
