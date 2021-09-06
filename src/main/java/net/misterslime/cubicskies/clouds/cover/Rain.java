package net.misterslime.cubicskies.clouds.cover;

import net.minecraft.client.Minecraft;
import net.misterslime.cubicskies.api.CloudCover;
import net.misterslime.cubicskies.client.CloudRenderer;

import java.util.Random;

public class Rain implements CloudCover {

    @Override
    public boolean placeFairCloud(int x, int y, int z) {
        Random random = new Random(Minecraft.getInstance().getSingleplayerServer().overworld().getSeed());
        double cloudRandom = (random.nextDouble() - random.nextDouble()) / 16.0;

        if (CloudRenderer.noise.noise3_Classic(x / 32.0, y / 32.0, z / 32.0) * 2.5 < 0.4 + cloudRandom / 2.0) {
            return false;
        }

        if (y < 3) {
            return CloudRenderer.noise.noise3_Classic(x / 8.0, y / 8.0, z / 8.0) * 2.5 >= (y * 0.166) + cloudRandom;
        } else if (y < 8) {
            return CloudRenderer.noise.noise3_Classic(x / 8.0, y / 8.0, z / 8.0) * 2.5 >= 0.5 + cloudRandom;
        } else if (y < 11) {
            int yScale = y - 8;
            return CloudRenderer.noise.noise3_Classic(x / 8.0, y / 8.0, z / 8.0) * 2.5 >= (0.5 + yScale * 0.166) + cloudRandom;
        }

        return false;
    }

    @Override
    public boolean placeRainCloud(int x, int y, int z) {
        Random random = new Random(Minecraft.getInstance().getSingleplayerServer().overworld().getSeed());
        double cloudRandom = (random.nextDouble() - random.nextDouble()) / 16.0;

        if (y < 11) {
            return false;
        }

        if (y < 14) {
            int yScale = y - 11;
            return CloudRenderer.noise.noise3_Classic(x / 32.0, y / 32.0, z / 32.0) * 2.5 >= (0.2 + yScale * 0.166) + cloudRandom;
        } else if (y < 40) {
            return CloudRenderer.noise.noise3_Classic(x / 32.0, y / 32.0, z / 32.0) * 2.5 >= 0.1 + cloudRandom;
        } else if (y < 43) {
            int yScale = y - 40;
            return CloudRenderer.noise.noise3_Classic(x / 32.0, y / 32.0, z / 32.0) * 2.5 >= (0.2 + yScale * 0.166) + cloudRandom;
        }

        return false;
    }
}
