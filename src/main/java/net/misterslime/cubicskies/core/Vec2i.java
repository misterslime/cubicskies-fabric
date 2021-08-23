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
}
