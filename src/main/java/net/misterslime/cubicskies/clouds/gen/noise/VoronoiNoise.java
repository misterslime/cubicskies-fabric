package net.misterslime.cubicskies.clouds.gen.noise;

public class VoronoiNoise {

    final FastNoiseLite fastNoiseLite;

    public VoronoiNoise(int seed, int frequency) {
        fastNoiseLite = new FastNoiseLite(seed);
        fastNoiseLite.SetFrequency(frequency);
        fastNoiseLite.SetNoiseType(FastNoiseLite.NoiseType.Cellular);
        fastNoiseLite.SetCellularDistanceFunction(FastNoiseLite.CellularDistanceFunction.Euclidean);
    }

    public float sample(float x, float z) {
        fastNoiseLite.SetCellularReturnType(FastNoiseLite.CellularReturnType.CellValue);
        return fastNoiseLite.GetNoise(x, z);
    }

    public float sample(float x, float y, float z) {
        fastNoiseLite.SetCellularReturnType(FastNoiseLite.CellularReturnType.CellValue);
        return fastNoiseLite.GetNoise(x, y, z);
    }

    public float distance(float x, float z) {
        fastNoiseLite.SetCellularReturnType(FastNoiseLite.CellularReturnType.Distance);
        return fastNoiseLite.GetNoise(x, z);
    }

    public float distance(float x, float y, float z) {
        fastNoiseLite.SetCellularReturnType(FastNoiseLite.CellularReturnType.Distance);
        return fastNoiseLite.GetNoise(x, y, z);
    }
}
