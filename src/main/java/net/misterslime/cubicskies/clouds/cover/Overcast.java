package net.misterslime.cubicskies.clouds.cover;

import net.misterslime.cubicskies.api.CloudCover;
import net.misterslime.cubicskies.clouds.CloudHandler;

import java.util.Random;

public class Overcast implements CloudCover {

    @Override
    public boolean placeFairCloud(int x, int y, int z) {
        Random random = new Random();
        double cloudRandom = (random.nextDouble() - random.nextDouble()) / 16.0;

        if (y < 12) {
            return false;
        }

        if (y < 15) {
            int yScale = y - 12;
            return CloudHandler.noise.noise3_Classic(x / 32.0, y / 32.0, z / 32.0) * 2.5 >= (0.2 + yScale * 0.166) + cloudRandom;
        } else if (y < 30) {
            return CloudHandler.noise.noise3_Classic(x / 32.0, y / 32.0, z / 32.0) * 2.5 >= 0.1 + cloudRandom;
        } else if (y < 33) {
            int yScale = y - 30;
            return CloudHandler.noise.noise3_Classic(x / 32.0, y / 32.0, z / 32.0) * 2.5 >= (0.2 + yScale * 0.166) + cloudRandom;
        }
        return false;
    }

    @Override
    public boolean placeRainCloud(int x, int y, int z) {
        Random random = new Random();
        double cloudRandom = (random.nextDouble() - random.nextDouble()) / 16.0;

        if (CloudHandler.noise.noise3_Classic(x / 32.0, y / 32.0, z / 32.0) * 2.5 < 0.4 + cloudRandom / 2.0) {
            return false;
        }

        if (y < 3) {
            return CloudHandler.noise.noise3_Classic(x / 8.0, y / 8.0, z / 8.0) * 2.5 >= (y * 0.166) + cloudRandom;
        } else if (y < 8) {
            return CloudHandler.noise.noise3_Classic(x / 8.0, y / 8.0, z / 8.0) * 2.5 >= 0.5 + cloudRandom;
        } else if (y < 11) {
            int yScale = y - 8;
            return CloudHandler.noise.noise3_Classic(x / 8.0, y / 8.0, z / 8.0) * 2.5 >= (0.5 + yScale * 0.166) + cloudRandom;
        }
        return false;
    }
}
