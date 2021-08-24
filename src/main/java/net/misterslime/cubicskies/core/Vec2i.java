package net.misterslime.cubicskies.core;

public class Vec2i {

    public static final Vec2i ZERO = new Vec2i(0, 0);
    private int x;
    private int y;

    public Vec2i(int n, int n2) {
        this.x = n;
        this.y = n2;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    protected Vec2i setX(int n) {
        this.x = n;
        return this;
    }

    protected Vec2i setY(int n) {
        this.y = n;
        return this;
    }

    public void set(Vec2i vec2i) {
        this.x = vec2i.x;
        this.y = vec2i.y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void scale(double scale) {
        this.x *= scale;
        this.y *= scale;
    }

    public void add(Vec2i vec2i) {
        this.x += vec2i.x;
        this.y += vec2i.y;
    }
}
