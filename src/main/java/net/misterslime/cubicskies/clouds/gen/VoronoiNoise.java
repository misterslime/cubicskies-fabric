package net.misterslime.cubicskies.clouds.gen;

import com.mojang.datafixers.util.Pair;
import net.misterslime.cubicskies.core.Vector2d;

import java.util.HashSet;
import java.util.Set;

public class VoronoiNoise {

    public final OpenSimplex2F noise;
    public final OpenSimplex2F yNoise;

    private int size;

    public VoronoiNoise(long seed) {
        noise = new OpenSimplex2F(seed);
        yNoise = new OpenSimplex2F(seed + 1);

        this.size = 80;
    }

    public Vector2d nearestFeature(double x, double y, boolean getSecond) {
        Pair<Double, Vector2d> first = new Pair<>(Double.MAX_VALUE, null);
        Pair<Double, Vector2d> second = new Pair<>(Double.MAX_VALUE, null);

        Set<Vector2d> surroundingFeatures = surroundingFeatures(x, y);
        for (Vector2d feature : surroundingFeatures) {
            double distance = Math.sqrt(Math.pow(feature.x - x, 2) + Math.pow(feature.y - y, 2));

            if(distance <= first.getFirst()) {
                if(first.getSecond() != null) {
                    second = new Pair<>(first.getFirst(), first.getSecond());
                }

                first = new Pair<>(distance, feature);
            } else if (distance < second.getFirst()) {
                second = new Pair<>(distance, feature);
            }
        }

        return getSecond ? second.getSecond() : first.getSecond();
    }

    public Set<Vector2d> surroundingFeatures(double x, double y) {
        Set<Vector2d> surrounding = new HashSet<>();

        for(int cellX = x <= size ? - 2 : -1; cellX <= 1; cellX++) {
            for (int cellY = y <= size ? -2 : -1; cellY <= 1; cellY++) {
                Vector2d feature = getFeature(x + cellX * size, y + cellY * size);
                surrounding.add(feature);
            }
        }

        return surrounding;
    }

    public Vector2d evaluate(double x, double y) {
        return nearestFeature(x, y, false);
    }

    public double sample(double x, double z) {
        Vector2d nearestFeature = evaluate(x, z);
        return (noise.noise2(nearestFeature.x, nearestFeature.y) + 1) / 2;
    }

    public double sampleWithEdge(double x, double z, double leeway) {
        Vector2d nearestFeature = nearestFeature(x, z, false);
        Vector2d secondNearestFeature = nearestFeature(x, z, true);

        double distanceToFirst = Math.sqrt(Math.pow(nearestFeature.x - x, 2) + Math.pow(nearestFeature.y - z, 2));
        double distanceToSecond = Math.sqrt(Math.pow(secondNearestFeature.x - x, 2) + Math.pow(secondNearestFeature.y - z, 2));

        double close = distanceToSecond - distanceToFirst;
        if(Math.abs(close) < leeway) {
            return 0;
        }

        return (noise.noise2(nearestFeature.x, nearestFeature.y) + 1) / 2;
    }

    public Vector2d getFeature(double x, double y) {
        int gridX = ((int) x / size) * size;
        int gridY = ((int) y / size) * size;

        return new Vector2d(gridX + evaluateNormalizedNoise(noise, gridX, gridY) * size, gridY + evaluateNormalizedNoise(yNoise, gridX, gridY) * size);
    }

    private double evaluateNormalizedNoise(OpenSimplex2F noise, double x, double y) {
        if(x == 0) {
            x = 0.001;
        } else if (y == 0) {
            y = 0.001;
        }

        return (noise.noise2(x, y) + 1) / 2;
    }
}
