package net.misterslime.cubicskies.clouds;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3d;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.misterslime.cubicskies.core.Vec2i;

import java.util.LinkedList;
import java.util.List;

public class CloudChunk {

    public VertexBuffer cloudBuffer;
    public Vec2i chunkPos;

    public CloudChunk(int x, int z) {
        this.cloudBuffer = new VertexBuffer();
        this.chunkPos = new Vec2i(x, z);
    }

    public void generateCloudLayer() {
        int cloudDepth = (int) Math.floor(172 / CloudVoxel.CLOUD_BOUNDING_BOX.maxY);
        int cloudDistance = (int) Math.floor(12 / CloudVoxel.CLOUD_BOUNDING_BOX.maxX);

        int xOffset = this.chunkPos.getX() * cloudDistance;
        int zOffset = this.chunkPos.getX() * cloudDistance;

        List<CloudVoxel> cloudVoxels = new LinkedList<>();

        // to do: actual cloud generation
        for (int x = 0; x < cloudDistance; x++) {
            for (int y = 0; y < cloudDepth; y++) {
                for (int z = 0; z < cloudDistance; z++) {
                    if (CloudHandler.noise.noise((x + xOffset) / 16.0, y / 16.0, (z + zOffset) / 16.0) * 2.5 >= CloudHandler.cloudiness) {
                        cloudVoxels.add(new CloudVoxel(new Vec3i(x, y, z), false));
                    }
                }
            }
        }

        buildCloudChunk(cloudVoxels);
    }

    public void buildCloudChunk(List<CloudVoxel> cloudVoxels) {
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        for (CloudVoxel cloudVoxel : cloudVoxels) {
            drawCloudVoxelVertices(bufferBuilder, cloudVoxel.pos, cloudVoxel.getColor());
        }

        bufferBuilder.end();
        this.cloudBuffer.upload(bufferBuilder);
    }

    private void drawCloudVoxelVertices(BufferBuilder buffer, Vec3i cubePos, Vec3 color) {
        AABB box = CloudVoxel.CLOUD_BOUNDING_BOX;
        Vector3d cloudPos = new Vector3d(cubePos.getX() * CloudVoxel.CLOUD_BOUNDING_BOX.maxX, cubePos.getY() * CloudVoxel.CLOUD_BOUNDING_BOX.maxY, cubePos.getZ() * CloudVoxel.CLOUD_BOUNDING_BOX.maxZ);
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

    public void renderCloudChunk(PoseStack poseStack, Matrix4f model, Vec3 color, double posX, double posY, double posZ, CloudStatus prevCloudsType) {
        poseStack.pushPose();
        poseStack.scale(1.0f, 1.0f, 1.0f);
        poseStack.translate(-posX - this.chunkPos.getX() * 12, posY, -posZ - this.chunkPos.getY() * 12);

        if (CloudHandler.cloudBuffer != null) {
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
