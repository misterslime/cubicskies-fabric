package net.misterslime.cubicskies.clouds.cover;

import net.misterslime.cubicskies.api.CloudCover;

public class Clear implements CloudCover {

    @Override
    public boolean placeFairCloud(int x, int y, int z) {
        return false;
    }

    @Override
    public boolean placeRainCloud(int x, int y, int z) {
        return false;
    }
}
