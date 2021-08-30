package net.misterslime.cubicskies.clouds.gen.noise;

import com.mojang.datafixers.util.Pair;
import net.misterslime.cubicskies.core.Vector2d;

import java.util.HashSet;
import java.util.Set;

public class VoronoiNoise {

    final FastNoiseLite fastNoiseLite;

    public VoronoiNoise(int seed, int frequency) {
        fastNoiseLite = new FastNoiseLite(seed);
        fastNoiseLite.SetFrequency(frequency);
        fastNoiseLite.SetNoiseType(FastNoiseLite.NoiseType.Cellular);
        fastNoiseLite.SetCellularDistanceFunction(FastNoiseLite.CellularDistanceFunction.Euclidean);
        fastNoiseLite.SetCellularReturnType(FastNoiseLite.CellularReturnType.CellValue);
    }

    public float sample(float x, float z) {
        return fastNoiseLite.GetNoise(x, z);
    }
}
