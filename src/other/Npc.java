package other;

import java.util.ArrayList;

import org.newdawn.slick.geom.Vector2f;

import app.Game;

public class Npc extends Rocket {

    private enum Action {
        Move, Aim, Fire, Drift;
    }

    private int inactivityCounter;

    private Action action;
    private Vector2f advancedAim;

    public Npc(int x, int y, int team, Rocket[] rockets) {
        super(x, y, team, rockets);

        action = Action.Move;
    }

    protected Rocket changeTarget() {
        ArrayList<Rocket> enemies = new ArrayList<Rocket>();
        for (Rocket rocket : rockets) {
            if (rocket.hp > 0 && rocket.team != team) {
                enemies.add(rocket);
            }
        }

        if (enemies.size() == 0) {
            return null;
        }
        return enemies.get(Game.random.nextInt(enemies.size()));
    }

    protected Vector2f advanceAimPosition(Rocket target) {
        float distance = position.distance(target.position);
        double time = distance / Bullet.BULLET_SPEED;
        double pointDistance = target.direction.length() * time;

        Vector2f pointRelativePosition = target.direction.copy().scale((float) pointDistance);

        return target.position.copy().add(pointRelativePosition);
    }

    protected Vector2f moveCompensation(Vector2f aimPosition) {

        float distance = position.distance(aimPosition);

        Vector2f pointRelativePosition = direction.copy().scale(-distance);

        Vector2f advancedAim = aimPosition.copy().add(pointRelativePosition);
        return advancedAim;
    }

    protected Boolean directToPoint(Vector2f point) {
        Boolean rotateUp = null;

        double diffX = position.x - point.x;
        double diffY = position.y - point.y;
        double distance = Math.sqrt(diffX * diffX + diffY * diffY);

        double angleToTarget = Math.acos(Math.abs(diffX) / distance);

        if (diffX < 0)
            angleToTarget = (diffY < 0) ? angleToTarget : Math.PI * 2 - angleToTarget;
        else
            angleToTarget = (diffY < 0) ? Math.PI - angleToTarget : Math.PI + angleToTarget;

        if (rotation > angleToTarget + 0.05 || rotation < angleToTarget - 0.05) {
            if (angleToTarget > Math.PI) {
                rotateUp = !(angleToTarget > rotation + Math.PI || angleToTarget < rotation);
            } else {
                rotateUp = (angleToTarget > rotation || angleToTarget < rotation - Math.PI);
            }
        } else {
            if (action == Action.Aim) {
                if (engineEnergy >= 0.997f)
                    action = Action.Move;
                else
                    action = Action.Drift;
            }
            if (action == Action.Fire) {
                if (weaponEnergy > 0.3) {
                    super.shoot();
                } else {
                    if (engineEnergy >= 1)
                        action = Action.Move;
                    else
                        action = Action.Aim;
                }
            }
        }
        return rotateUp;
    }

    public void setTarget() {
        target = changeTarget();
    }

    public void update() {
        boolean isBoosterOn = false;
        Boolean rotateUp = null;
        if (target == null || target.hp == 0) {
            target = changeTarget();
            if (target == null) {
                return;
            }
        }

        if (weaponEnergy >= 0.997f)
            action = Action.Fire;

        advancedAim = advanceAimPosition(target);

        if (action == Action.Aim)
            rotateUp = directToPoint(moveCompensation(advancedAim));
        else if (action == Action.Fire) {
            rotateUp = directToPoint(advancedAim);
        } else if (action == Action.Move) {
            if (engineEnergy > 0.015)
                isBoosterOn = true;
            else
                action = Action.Aim;
        } else if (action == Action.Drift) {
            inactivityCounter++;
            if (engineEnergy >= 0.997f || inactivityCounter >= 240) {
                inactivityCounter = 0;
                action = Action.Aim;
            }
            isBoosterOn = false;
        }
        super.update(isBoosterOn, rotateUp);
    }

}