package net.misterslime.cubicskies.clouds.gen.shape;

import com.mojang.math.Vector3d;

public abstract class Shape {

    public boolean isRain;

    public Shape(boolean isRain) {
        this.isRain = isRain;
    }

    public abstract boolean place(int x, int y, int z);

    public abstract Vector3d toLocation(int x, int y, int z);
}
