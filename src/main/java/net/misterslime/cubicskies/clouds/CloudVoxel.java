package net.misterslime.cubicskies.clouds;

import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;

public class CloudVoxel {

    public Vec3i pos;
    public boolean rainCloud;

    public CloudVoxel(Vec3i pos, boolean rainCloud) {
        this.pos = pos;
        this.rainCloud = rainCloud;
    }

    public CloudVoxel(int x, int y, int z, boolean rainCloud) {
        this(new Vec3i(x, y, z), rainCloud);
    }

    public Vec3 getColor() {
        return this.rainCloud ? new Vec3(0.62f, 0.62f, 0.62f) : new Vec3(1f, 1f, 1f);
    }
}
