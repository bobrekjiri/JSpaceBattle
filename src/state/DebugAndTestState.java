package state;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import other.Bullet;
import other.InteractiveLabel;
import other.Label;
import other.Notification;
import other.Npc;
import other.Rocket;
import app.Game;
import app.Translator;
import factory.EffectFactory;
import factory.FontFactory;

public class DebugAndTestState extends BasicGameState {

    private boolean paused, end;

    private Translator translator;
    private int stateId, width, height, teams, teamplayers;
    int[] remaining;
    private Image space, shieldImage, barImage, arrowImage;
    private Image[] rocketImages, bulletImages;
    private Rocket[] rockets;
    private Rocket inView;
    private Vector2f centerScreen;
    public ArrayList<Notification> dmgDoneNotifs, dmgTakenNotifs, globalNotifs;

    private Font ubuntuLarge, ubuntuMedium, ubuntuTiny;
    private InteractiveLabel distanceLabel, remainingOpponentsLabel, endLabel;
    private InteractiveLabel[] remainingOpponentsLabels;
    private Label[] titleLabels, energyLabels;
    private Label playerLabel, targetLabel, pausedLabel, returnLabel, remainingTitleLabel;

    public DebugAndTestState(int stateId) {
        this.stateId = stateId;
    }

    @Override
    public void init(final GameContainer container, final StateBasedGame game)
            throws SlickException {
        translator = Translator.getInstance();

        width = container.getWidth();
        height = container.getHeight();

        teams = Game.teams;
        teamplayers = Game.teamplayers;
        paused = false;
        end = false;

        FontFactory fonts = FontFactory.getInstance();
        EffectFactory effects = EffectFactory.getInstance();
        ColorEffect whiteEffect = effects.getColorEffect(java.awt.Color.WHITE);

        ubuntuLarge = fonts.getFont("ubuntu", width / 24, whiteEffect);
        ubuntuMedium = fonts.getFont("ubuntu", width / 48, whiteEffect);
        ubuntuTiny = fonts.getFont("ubuntu", width / 72, whiteEffect);

        centerScreen = new Vector2f(width / 2f, height / 2f);
        remaining = new int[4];

        targetLabel = new Label(translator.translate("Target"), new Point(width * 5 / 6, 0),
                ubuntuMedium);
        targetLabel.setColors(Color.white);
        playerLabel = new Label(translator.translate("Player"), new Point(0, 0), ubuntuMedium);
        playerLabel.setColors(Color.blue);

        titleLabels = new Label[6];
        energyLabels = new Label[6];

        for (int i = 0; i < titleLabels.length; i++) {
            titleLabels[i] = new Label("", new Point(width * (5 * (i / 3)) / 6, width
                    * ((i % 3) + 1) / 48), ubuntuMedium);
            titleLabels[i].setColors(Color.white);
        }

        for (int i = 0; i < energyLabels.length; i++) {
            energyLabels[i] = new Label("", new Point((i < 3) ? width / 11 : width * 89 / 96, width
                    * ((i % 3) + 1) / 48), ubuntuMedium);
            energyLabels[i].setColors(Color.white);
        }

        titleLabels[0].setText(translator.translate("Weapon") + ":");
        titleLabels[1].setText(translator.translate("Engine") + ":");
        titleLabels[2].setText(translator.translate("Shield") + ":");

        titleLabels[3].setText(translator.translate("Weapon") + ":");
        titleLabels[4].setText(translator.translate("Engine") + ":");
        titleLabels[5].setText(translator.translate("Shield") + ":");

        String tmp = translator.translate("Game.Paused");
        pausedLabel = new Label(tmp, new Point(width / 2 - ubuntuLarge.getWidth(tmp) / 2, height
                / 2 - ubuntuLarge.getHeight(tmp) / 2), ubuntuLarge);
        pausedLabel.setColors(Color.white);
        tmp = translator.translate("Game.Return");
        returnLabel = new Label(tmp, new Point(width / 2 - ubuntuMedium.getWidth(tmp) / 2, height
                / 2 + height / 18), ubuntuMedium);
        returnLabel.setColors(Color.white);

        endLabel = new InteractiveLabel("", new Point(width / 2, height / 2), ubuntuLarge, false,
                true);
        endLabel.setColors(Color.white);

        distanceLabel = new InteractiveLabel("", new Point((int) centerScreen.x,
                (int) centerScreen.y - 100), ubuntuTiny, false, true);
        distanceLabel.setColors(Color.white);

        tmp = translator.translate("Game.Remaining");
        remainingTitleLabel = new Label(tmp, new Point(width / 2 - ubuntuMedium.getWidth(tmp) / 2,
                0), ubuntuMedium);
        remainingTitleLabel.setColors(Color.white);

        remainingOpponentsLabels = new InteractiveLabel[4];
        for (int i = 0; i < remainingOpponentsLabels.length; i++) {
            remainingOpponentsLabels[i] = new InteractiveLabel("", new Point(
                    (int) (width / 2 + (width / 32) * (i - 1.5)), height / 18), ubuntuMedium,
                    false, true);
        }
        remainingOpponentsLabels[0].setColors(Color.blue);
        remainingOpponentsLabels[1].setColors(Color.green);
        remainingOpponentsLabels[2].setColors(Color.red);
        remainingOpponentsLabels[3].setColors(Color.yellow);

        remainingOpponentsLabel = new InteractiveLabel("", new Point(width / 2, height / 18),
                ubuntuMedium, false, true);
        remainingOpponentsLabel.setColors(Color.white);

        rockets = new Rocket[(teams > 0) ? teams * teamplayers : teamplayers];
        for (int i = 0; i < rockets.length; i++) {
            rockets[i] = new Npc(0, 0, (teams > 0) ? i % teams : i, rockets);
        }

        for (int i = 1; i < rockets.length; i++) {
            Npc npc = (Npc) rockets[i];
            npc.setTarget();
        }

        inView = rockets[0];

        dmgDoneNotifs = new ArrayList<>();
        dmgTakenNotifs = new ArrayList<>();
        globalNotifs = new ArrayList<>();

        Notification.setFont(ubuntuMedium);
        ;

        rocketImages = new Image[4];
        rocketImages[0] = new Image("content/graphics/blue.png");
        rocketImages[1] = new Image("content/graphics/green.png");
        rocketImages[2] = new Image("content/graphics/red.png");
        rocketImages[3] = new Image("content/graphics/yellow.png");
        for (int i = 0; i < rocketImages.length; i++) {
            rocketImages[i] = rocketImages[i].getScaledCopy(0.5f);
            rocketImages[i].setCenterOfRotation(25f, 20.5f);
        }

        bulletImages = new Image[4];
        bulletImages[0] = new Image("content/graphics/shot1.png");
        bulletImages[1] = new Image("content/graphics/shot2.png");
        bulletImages[2] = new Image("content/graphics/shot3.png");
        bulletImages[3] = new Image("content/graphics/shot4.png");

        space = new Image("content/graphics/space.png");
        shieldImage = new Image("content/graphics/shield.png").getScaledCopy(0.5f);
        barImage = new Image("content/graphics/bar.png").getScaledCopy(64, 4);
        arrowImage = new Image("content/graphics/arrow.png").getScaledCopy(0.25f);
        shieldImage.setCenterOfRotation(28f, 23f);
        barImage.setAlpha(0.25f);
        arrowImage.setCenterOfRotation(12.5f, 10);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        float x = (inView.getPosition().x % 2560), y = inView.getPosition().y % 1516;
        drawBackgroud(x, y);
        Rocket player = rockets[0];

        for (Rocket rocket : rockets) {

            if (rocket.getHP() > 0) {
                Vector2f basePosition = rocket.getPosition().copy().sub(inView.getPosition())
                        .add(centerScreen);
                rocketImages[(teams > 0) ? rocket.getTeam() % teams : rocket.getTeam() % 4]
                        .setRotation((float) (rocket.getRotation() * 180 / Math.PI));
                rocketImages[(teams > 0) ? rocket.getTeam() % teams : rocket.getTeam() % 4]
                        .drawCentered(basePosition.x, basePosition.y);
                shieldImage.setAlpha(rocket.getShieldOpacity());
                shieldImage.setRotation((float) (rocket.getRotation() * 180 / Math.PI));
                shieldImage.drawCentered(basePosition.x, basePosition.y);

                Image hpBar = barImage.getSubImage(0, 0, (int) (64 * rocket.getHP() / 100f), 4);
                barImage.draw(basePosition.x - 32, basePosition.y - 40);
                hpBar.draw(basePosition.x - 32, basePosition.y - 40, new Color(0, 0.5f, 0));
                if (rocket == player.getTarget()) {
                    g.setColor(Color.white);
                    g.drawOval(basePosition.x - 32, basePosition.y - 32, 64, 64);
                }
            }
            for (Bullet bullet : rocket.getBullets()) {
                Vector2f bulletBasePosition = bullet.getPosition().copy().sub(inView.getPosition())
                        .add(centerScreen);
                Image localBulletImage = bulletImages[rocket.getTeam()];
                localBulletImage.setRotation((float) (bullet.getRotation() * 180 / Math.PI));
                localBulletImage.drawCentered(bulletBasePosition.x, bulletBasePosition.y);
            }
        }
        if (player.getHP() > 0) {
            playerLabel.render(g);

            for (int i = 0; i < 3; i++) {
                titleLabels[i].render(g);
                energyLabels[i].render(g);
            }

            if (player.getTarget() != null) {
                targetLabel.render(g);
                for (int i = 3; i < 6; i++) {
                    titleLabels[i].render(g);
                    energyLabels[i].render(g);
                }
                arrowImage.drawCentered(centerScreen.x, centerScreen.y - 75);
                if (player.getDistanceToTarget() > 600) {
                    distanceLabel.render(g);
                }
            }
            g.setFont(ubuntuMedium);
            for (Notification notif : dmgTakenNotifs) {
                if (notif.isVisible) {
                    g.drawString(notif.value, centerScreen.x - 120 - notif.getOffsetX(),
                            centerScreen.y - 60 + notif.timer);
                }
            }
            for (Notification notif : dmgDoneNotifs) {
                if (notif.isVisible) {
                    g.drawString(notif.value, centerScreen.x + 120 - notif.getOffsetX(),
                            centerScreen.y - 60 + notif.timer);
                }
            }
        }

        remainingTitleLabel.render(g);
        if (teams > 0) {
            for (int i = 0; i < remainingOpponentsLabels.length; i++) {
                remainingOpponentsLabels[i].render(g);
            }
        } else {
            remainingOpponentsLabel.render(g);
        }

        if (paused) {
            pausedLabel.render(g);
            returnLabel.render(g);
        }
        if (end) {
            endLabel.render(g);
            returnLabel.render(g);
        }
    }

    @Override
    public void update(final GameContainer container, final StateBasedGame game, int delta)
            throws SlickException {
        Input input = container.getInput();

        if (!paused && !end) {
            for (int i = 0; i < remaining.length; i++) {
                remaining[i] = 0;
            }

            boolean canAddNotif = true;
            for (int i = 0; i < dmgTakenNotifs.size(); i++) {
                Notification notif = dmgTakenNotifs.get(i);
                if (notif.isVisible) {
                    if (notif.timer >= 120) {
                        dmgTakenNotifs.remove(i);
                        i--;
                        continue;
                    }
                    notif.timer++;
                    if (notif.timer < 30) {
                        canAddNotif = false;
                    }
                } else {
                    if (canAddNotif) {
                        canAddNotif = false;
                        notif.isVisible = true;
                    }
                }
            }

            canAddNotif = true;
            for (int i = 0; i < dmgDoneNotifs.size(); i++) {
                Notification notif = dmgDoneNotifs.get(i);
                if (notif.isVisible) {
                    if (notif.timer >= 120) {
                        dmgDoneNotifs.remove(i);
                        i--;
                        continue;
                    }
                    notif.timer++;
                    if (notif.timer < 30) {
                        canAddNotif = false;
                    }
                } else {
                    if (canAddNotif) {
                        canAddNotif = false;
                        notif.isVisible = true;
                    }
                }
            }

            for (int i = 0; i < globalNotifs.size(); i++) {

            }
            for (int i = 0; i < rockets.length; i++) {
                Npc npc = (Npc) rockets[i];
                if (npc.getHP() > 0) {
                    npc.update();
                    if (teams > 0) {
                        remaining[npc.getTeam()]++;
                    } else {
                        remaining[0]++;
                    }
                }
                npc.bulletUpdate(null);
            }
            remainingOpponentsLabel.setText(String.valueOf(remaining[0]));
            for (int i = 0; i < remaining.length; i++) {
                remainingOpponentsLabels[i].setText(String.valueOf(remaining[i]));
            }

            if (teams > 0) {
                int zeros = 0, survived = -1;
                for (int i = 0; i < remaining.length; i++) {
                    if (remaining[i] == 0) {
                        zeros++;
                    } else {
                        survived = i;
                    }
                }
                if (zeros >= 3) {
                    end = true;
                    if (survived == -1) {
                        endLabel.setText(translator.translate("Game.Draw"));
                        endLabel.setColors(Color.yellow);
                    } else if (survived == 0) {
                        endLabel.setText(translator.translate("Game.YourTeamWon"));
                        endLabel.setColors(Color.green);
                    } else {
                        endLabel.setText(translator.translate("Game.EnemyTeamWon"));
                        endLabel.setColors(Color.red);
                    }
                }
            } else {
                if (remaining[0] < 2) {
                    end = true;
                    if (remaining[0] == 0) {
                        endLabel.setText(translator.translate("Game.Draw"));
                        endLabel.setColors(Color.yellow);
                    } else if (rockets[0].getHP() > 0) {
                        endLabel.setText(translator.translate("Game.YouWon"));
                        endLabel.setColors(Color.green);
                    } else {
                        endLabel.setText(translator.translate("Game.EnemyWon"));
                        endLabel.setColors(Color.red);
                    }
                }
            }

            if (input.isKeyPressed(Input.KEY_T)) {
                for (int i = 0; i < rockets.length; i++) {
                    if (inView == rockets[i]) {
                        do {
                            i++;
                            if (i == rockets.length) {
                                i = 0;
                            }
                        } while (rockets[i].getHP() == 0);
                        inView = rockets[i];
                        break;
                    }
                }
            }

            if (input.isKeyPressed(Input.KEY_ESCAPE)) {
                paused = true;
            }

            energyLabels[0].setText(String.format("%02d",
                    Math.round(rockets[0].getWeaponEnergy() * 100))
                    + "%");
            energyLabels[1].setText(String.format("%02d",
                    Math.round((rockets[0].getEngineEnergy() < 0.02) ? 0 : rockets[0]
                            .getEngineEnergy() * 100))
                    + "%");
            energyLabels[2].setText(String.format("%02d",
                    Math.round(rockets[0].getShieldEnergy() * 100))
                    + "%");

        } else if (paused && !end) {
            if (input.isKeyPressed(Input.KEY_ESCAPE)) {
                paused = false;
            }
            if (input.isKeyPressed(Input.KEY_Q)) {
                game.enterState(Game.LOCAL_GAME_STATE);
            }
        } else {
            if (input.isKeyPressed(Input.KEY_ESCAPE)) {
                game.enterState(Game.LOCAL_GAME_STATE);
            }
            if (input.isKeyPressed(Input.KEY_Q)) {
                game.enterState(Game.LOCAL_GAME_STATE);
            }
        }
        input.clearKeyPressedRecord();
    }

    @Override
    public int getID() {
        return this.stateId;
    }

    private void drawBackgroud(float x, float y) {
        space.draw(-x, -y);
        if (x > 0) {
            space.draw(-(x - 2560), -y);
            if (y > 0) {
                space.draw(-x, -(y - 1516));
                space.draw(-(x - 2560), -(y - 1516));
            } else if (y < 0) {
                space.draw(-x, -(y + 1516));
                space.draw(-(x - 2560), -(y + 1516));
            }
        } else if (x < 0) {
            space.draw(-(x + 2560), -y);
            if (y > 0) {
                space.draw(-x, -(y - 1516));
                space.draw(-(x + 2560), -(y - 1516));
            } else if (y < 0) {
                space.draw(-(x), -(y + 1516));
                space.draw(-(x + 2560), -(y + 1516));
            }
        }
    }
}
