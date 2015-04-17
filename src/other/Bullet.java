package other;

import org.newdawn.slick.geom.Vector2f;

public class Bullet {
    private int energy;
    private float rotation;
    private Vector2f position;

    public static final float BULLET_SPEED = 5;

    public Bullet(float rotation, Vector2f position) {
        energy = 600;
        this.rotation = rotation;
        this.position = position.copy();
    }

    public float getRotation() {
        return rotation;
    }

    public Vector2f getPosition() {
        return position;
    }

    public boolean update() {
        energy--;
        position.add(new Vector2f((float) Math.cos(rotation) * BULLET_SPEED, (float) Math
                .sin(rotation) * BULLET_SPEED));
        return energy > 0;
    }
}
