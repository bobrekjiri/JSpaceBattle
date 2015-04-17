package other;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import state.GameState;

public class Player extends Rocket {

    public Player(int x, int y, int team, Rocket[] rockets, GameState gameState) {
        super(x, y, team, rockets);
        state = gameState;
    }

    private float angleToTarget;
    private float distanceToTarget;
    private GameState state;

    public void update(Input input) {
        if (target != null) {

            if (target.hp == 0) {
                target = null;
            }
        }
        boolean isBoosterOn;
        Boolean rotateUp = null;
        if (input.isKeyDown(Input.KEY_LEFT))
            rotateUp = false;
        if (input.isKeyDown(Input.KEY_RIGHT))
            rotateUp = true;
        if (input.isKeyDown(Input.KEY_UP)) {
            isBoosterOn = true;
        } else {
            isBoosterOn = false;
        }

        if (input.isKeyDown(Input.KEY_SPACE)) {
            super.shoot();
        }

        if (input.isKeyPressed(Input.KEY_X)) {
            Rocket closest = null;
            float distance = Float.MAX_VALUE;
            for (Rocket rocket : rockets) {
                if (rocket.team != this.team && rocket.hp > 0) {
                    float distance_ = position.distance(rocket.position);
                    if (closest == null || distance > distance_) {
                        closest = rocket;
                        distance = distance_;
                    }
                }
            }
            target = closest;
        }

        if (target != null) {
            Vector2f diff = position.copy().sub(target.getPosition());
            distanceToTarget = diff.length();
            angleToTarget = (float) Math.acos(Math.abs(diff.x) / distanceToTarget);
            if (diff.x < 0)
                angleToTarget = (diff.y < 0) ? angleToTarget : (float) (Math.PI * 2)
                        - angleToTarget;
            else
                angleToTarget = (diff.y < 0) ? (float) Math.PI - angleToTarget : (float) Math.PI
                        + angleToTarget;
        }

        super.update(isBoosterOn, rotateUp);
    }

    @Override
    protected Vector2f damageTake(int damage) {
        Vector2f tmp = super.damageTake(damage);
        String value = (tmp.x > 0) ? (int) tmp.x + " " : "";
        value += (tmp.y > 0) ? "(" + (int) tmp.y + ")" : "";
        state.dmgTakenNotifs.add(new Notification(NotificationType.DamageTaken, value));
        return tmp;
    }

    public Rocket getTarget() {
        return target;
    }

    public float getAngleToTarget() {
        return angleToTarget;
    }

    public float getDistanceToTarget() {
        return distanceToTarget;
    }
}
