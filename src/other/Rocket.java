package other;

import java.util.ArrayList;

import org.newdawn.slick.geom.Vector2f;

import app.Game;

public abstract class Rocket {
    public Rocket(int x, int y, int team, Rocket[] rockets) {
        position = new Vector2f(x, y);
        direction = new Vector2f(0, 0);
        this.team = team;
        this.rockets = rockets;
        shieldEnergy = 1;
        engineEnergy = 1;
        weaponEnergy = 0;
        shieldOpacity = shieldEnergy / 2f;
        hp = 100;
        bullets = new ArrayList<Bullet>();
        rotation = Game.random.nextFloat() * (float) Math.PI * 2f;
        damageDone = 0;
    }

    protected static ArrayList<Notification> globalNotifs;

    protected Vector2f position, direction;
    protected float rotation, shieldEnergy, engineEnergy, weaponEnergy, shieldOpacity;
    protected int hp, team, weaponCooldown, damageDone;

    protected Rocket target;

    protected Rocket[] rockets;
    protected ArrayList<Bullet> bullets;

    protected float cropAngle(float angle) {
        if (angle > Math.PI * 2f)
            angle -= Math.PI * 2f;
        else if (angle < 0)
            angle += Math.PI * 2f;
        return angle;
    }

    private void move(boolean isBoosterOn) {
        if (engineEnergy > 0.015 && isBoosterOn) {
            engineEnergy -= 0.015f;
        }

        Vector2f change = new Vector2f((float) Math.cos(rotation), (float) Math.sin(rotation))
                .scale(isBoosterOn ? 0.06f : 0.02f);
        direction = direction.add(change);
        position = position.add(direction);
    }

    private void rotate(boolean isIncrease) {
        rotation += (isIncrease) ? 0.05f : -0.05f;
        rotation = cropAngle(rotation);
    }

    protected Vector2f damageTake(int damage) {
        if (shieldEnergy >= 0.005 * damage) {
            shieldEnergy -= 0.005 * damage;
            if (shieldOpacity <= 0.85)
                shieldOpacity += 0.15f;
            return new Vector2f(0, damage);
        }
        int absorb = (int) (shieldEnergy / 0.005);
        shieldEnergy = 0;
        damage -= absorb;

        if (hp < damage) {
            hp = 0;
            return new Vector2f(damage, absorb);
        }
        hp -= damage;
        return new Vector2f(damage, absorb);
    }

    protected void update(boolean isBoosterOn, Boolean rotateUp) {
        if (weaponEnergy <= 0.997)
            weaponEnergy += 0.003f;
        if (shieldEnergy <= 0.99985)
            shieldEnergy += 0.00015f;
        if (engineEnergy <= 0.997)
            engineEnergy += 0.003f;
        if (weaponCooldown > 0)
            weaponCooldown--;

        if (rotateUp != null) {
            rotate((boolean) rotateUp);
        }
        move(isBoosterOn);

        if (shieldOpacity >= (shieldEnergy / 2 - 0.01)) {
            shieldOpacity -= 0.01f;
        } else if (shieldOpacity < shieldEnergy / 2f) {
            shieldOpacity += 0.01f;
        }
    }

    public void bulletUpdate(ArrayList<Notification> dmgDoneNotifs) {
        for (int i = 0; i < bullets.size(); i++) {
            if (!bullets.get(i).update()) {
                bullets.remove(i);
                i--;
                continue;
            }

            for (Rocket enemy : rockets) {
                if (this.team != enemy.team) {
                    if (bullets.get(i).getPosition().distance(enemy.position) < 30 && enemy.hp > 0) {
                        int damage = Game.random.nextInt(11) + 25;
                        enemy.damageTake(damage);
                        damageDone += damage;
                        if (dmgDoneNotifs != null) {
                            dmgDoneNotifs.add(new Notification(NotificationType.DamageDone, String
                                    .valueOf(damage)));
                        }
                        bullets.remove(i);
                        i--;
                        break;
                    }
                }
            }
        }
    }

    protected void shoot() {
        if (weaponEnergy > 0.3 && weaponCooldown == 0) {
            weaponCooldown = 15;
            weaponEnergy -= 0.3f;
            bullets.add(new Bullet(rotation, position));
        }
    }

    public int getHP() {
        return hp;
    }

    public int getTeam() {
        return team;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getDirection() {
        return direction;
    }

    public float getRotation() {
        return rotation;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public float getWeaponEnergy() {
        return weaponEnergy;
    }

    public float getEngineEnergy() {
        return engineEnergy;
    }

    public float getShieldEnergy() {
        return shieldEnergy;
    }

    public float getShieldOpacity() {
        return shieldOpacity;
    }

    public Rocket getTarget() {
        return target;
    }

    public float getDistanceToTarget() {
        if (target == null)
            return 0;
        Vector2f diff = position.copy().sub(target.getPosition());
        return diff.length();
    }
}
