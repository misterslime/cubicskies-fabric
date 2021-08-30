package net.misterslime.cubicskies.clouds;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3d;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.misterslime.cubicskies.api.CloudCover;
import net.misterslime.cubicskies.clouds.cover.*;
import net.misterslime.cubicskies.clouds.gen.noise.VoronoiNoise;
import net.misterslime.cubicskies.core.Vec2i;

import java.util.LinkedList;
import java.util.List;

public class CloudChunk {

    public VertexBuffer cloudBuffer;
    public CloudCover cloudCover;

    private int rainClouds;

    public CloudChunk() {
        this.cloudBuffer = new VertexBuffer();
    }

    public void generateCloudChunk(Vec2i cloudPos, Vec2i chunkPos) {
        int xOffset = cloudPos.getX() * 8 + chunkPos.getX() * 8;
        int zOffset = cloudPos.getY() * 8 + chunkPos.getY() * 8;

        List<CloudVoxel> cloudVoxels = new LinkedList<>();

        if (CloudHandler.voronoi == null) {
            CloudHandler.voronoi = new VoronoiNoise(0, 32);
        }

        double voronoiEval = CloudHandler.voronoi.sample((cloudPos.getX() + chunkPos.getX()) / 512f, (cloudPos.getY() + chunkPos.getY()) / 512f);

        this.cloudCover = new Cloudy();
        switch ((int) Math.ceil(voronoiEval * 5)) {
            case 1 -> this.cloudCover = new Clear();
            case 2 -> this.cloudCover = new Cloudy();
            case 3 -> this.cloudCover = new Overcast();
            case 4 -> this.cloudCover = new Rain();
            case 5 -> this.cloudCover = new Thunderstorm();
        }

        for (int x = 0; x < 8; x++) {
            for (int z = 0; z < 8; z++) {
                for (int y = 0; y < 57; y++) {
                    if (this.cloudCover.placeFairCloud(x + xOffset, y, z + zOffset)) {
                        cloudVoxels.add(new CloudVoxel(new Vec3i(x, y, z), false));
                    } else if (this.cloudCover.placeRainCloud(x + xOffset, y, z + zOffset)) {
                        cloudVoxels.add(new CloudVoxel(new Vec3i(x, y, z), true));
                        rainClouds++;
                    }
                }
            }
        }

        buildCloudChunk(cloudVoxels, xOffset, zOffset);
    }

    public void buildCloudChunk(List<CloudVoxel> cloudVoxels, int xOffset, int zOffset) {
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_NORMAL);

        for (CloudVoxel cloudVoxel : cloudVoxels) {
            drawCloudVoxelVertices(bufferBuilder, cloudVoxel.pos, cloudVoxel.getColor(), xOffset, zOffset);
        }

        bufferBuilder.end();
        this.cloudBuffer.upload(bufferBuilder);
    }

    private void drawCloudVoxelVertices(BufferBuilder buffer, Vec3i cubePos, Vec3 color, int xOffset, int zOffset) {
        AABB box = CloudVoxel.CLOUD_BOUNDING_BOX;
        Vector3d cloudPos = new Vector3d(cubePos.getX() * CloudVoxel.CLOUD_BOUNDING_BOX.maxX, cubePos.getY() * CloudVoxel.CLOUD_BOUNDING_BOX.maxY, cubePos.getZ() * CloudVoxel.CLOUD_BOUNDING_BOX.maxZ);
        box = box.move(cloudPos.x, cloudPos.y, cloudPos.z);

        // Down
        buffer.vertex((float) box.maxX, (float) box.minY, (float) box.minZ).color((float) (color.x * 0.7f), (float) (color.y * 0.7f), (float) (color.z * 0.7f), 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
        buffer.vertex((float) box.maxX, (float) box.minY, (float) box.maxZ).color((float) (color.x * 0.7f), (float) (color.y * 0.7f), (float) (color.z * 0.7f), 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
        buffer.vertex((float) box.minX, (float) box.minY, (float) box.maxZ).color((float) (color.x * 0.7f), (float) (color.y * 0.7f), (float) (color.z * 0.7f), 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();
        buffer.vertex((float) box.minX, (float) box.minY, (float) box.minZ).color((float) (color.x * 0.7f), (float) (color.y * 0.7f), (float) (color.z * 0.7f), 0.8f).normal(0.0f, -1.0f, 0.0f).endVertex();

        // Up
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

    public void renderCloudChunk(PoseStack poseStack, Matrix4f model, Vec2i chunkPos, double posX, double posY, double posZ, CloudStatus prevCloudsType) {
        poseStack.pushPose();
        poseStack.scale(1.0f, 1.0f, 1.0f);
        poseStack.translate(-posX + CloudHandler.prevCloudPos.getX() * 32 + chunkPos.getX() * 32, posY, -posZ + CloudHandler.prevCloudPos.getY() * 32 + chunkPos.getY() * 32);

        if (this.cloudBuffer != null) {
            int cloudMainIndex = prevCloudsType == CloudStatus.FANCY ? 0 : 1;

            for (int cloudIndex = 1; cloudMainIndex <= cloudIndex; ++cloudMainIndex) {
                if (cloudMainIndex == 0) {
                    RenderSystem.colorMask(false, false, false, false);
                } else {
                    RenderSystem.colorMask(true, true, true, true);
                }

                ShaderInstance shader = RenderSystem.getShader();
                this.cloudBuffer.drawWithShader(poseStack.last().pose(), model, shader);
            }
        }

        poseStack.popPose();
    }
}
