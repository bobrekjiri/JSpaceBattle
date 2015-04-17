package state;

import java.awt.Point;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import other.InteractiveLabel;
import other.Label;
import app.Game;
import app.Translator;
import factory.EffectFactory;
import factory.FontFactory;

public class LocalGameState extends BasicGameState {

    private int stateId, width, height, opponents = 3, teams = 2, teamplayers = 2;
    private Translator translator;
    private Font ubuntuMedium;
    private Label titleLabel, teamsLabel, teamplayersLabel, opponentsLabel;
    private InteractiveLabel teamplayLabel, deathmatchLabel, teamsNumberLabel,
            teamplayersNumberLabel, opponentsNumberLabel, runLabel, backLabel;

    public LocalGameState(int stateId) {
        this.stateId = stateId;
    }

    @Override
    public void init(final GameContainer container, final StateBasedGame game)
            throws SlickException {
        translator = Translator.getInstance();

        FontFactory fonts = FontFactory.getInstance();
        EffectFactory effects = EffectFactory.getInstance();
        ColorEffect whiteEffect = effects.getColorEffect(java.awt.Color.WHITE);
        width = container.getWidth();
        height = container.getHeight();

        ubuntuMedium = fonts.getFont("ubuntu", width / 32, whiteEffect);

        titleLabel = new Label(translator.translate("Local.Title"), new Point(width / 25,
                height / 30), ubuntuMedium);
        titleLabel.setColors(Color.white);

        teamsLabel = new Label(translator.translate("Local.Teams"), new Point(width * 3 / 4
                - ubuntuMedium.getWidth(translator.translate("Local.Teams")) / 2, height * 3 / 12),
                ubuntuMedium);
        teamsLabel.setColors(Color.white);
        teamplayersLabel = new Label(translator.translate("Local.Teamplayers"), new Point(width * 3
                / 4 - ubuntuMedium.getWidth(translator.translate("Local.Teamplayers")) / 2,
                height * 5 / 12), ubuntuMedium);
        teamplayersLabel.setColors(Color.white);
        opponentsLabel = new Label(translator.translate("Local.Opponents"), new Point(width * 3 / 4
                - ubuntuMedium.getWidth(translator.translate("Local.Opponents")) / 2,
                height * 7 / 12), ubuntuMedium);
        opponentsLabel.setColors(Color.white);

        teamsNumberLabel = new InteractiveLabel(String.valueOf(teams), new Point(width * 3 / 4,
                height * 4 / 12), ubuntuMedium, false, true);

        teamplayersNumberLabel = new InteractiveLabel(String.valueOf(teamplayers), new Point(
                width * 3 / 4, height * 6 / 12), ubuntuMedium, false, true);

        opponentsNumberLabel = new InteractiveLabel(String.valueOf(opponents), new Point(
                width * 3 / 4, height * 8 / 12), ubuntuMedium, false, true);

        teamplayLabel = new InteractiveLabel(translator.translate("Local.Teamplay"), new Point(
                width / 20, height * 4 / 10), ubuntuMedium, false, false);
        teamplayLabel.setDisabledColor(Color.orange);
        deathmatchLabel = new InteractiveLabel(translator.translate("Local.Deathmatch"), new Point(
                width / 20, height * 5 / 10), ubuntuMedium, false, false);
        deathmatchLabel.setDisabledColor(Color.orange);

        runLabel = new InteractiveLabel(translator.translate("start"), new Point(width * 14 / 15,
                height * 19 / 20), ubuntuMedium, false, true);
        runLabel.setColors(Color.white, Color.white, Color.red, Color.red);
        backLabel = new InteractiveLabel(translator.translate("back"), new Point(width * 1 / 15,
                height * 19 / 20), ubuntuMedium, false, true);
        backLabel.setColors(Color.white, Color.white, Color.red, Color.red);

        teamplayLabel.setEnabled(false);
        opponentsLabel.setColors(Color.darkGray);
        opponentsNumberLabel.setEnabled(false);
    }

    @Override
    public void render(final GameContainer container, final StateBasedGame game, Graphics g)
            throws SlickException {

        titleLabel.render(g);

        teamsLabel.render(g);
        teamplayersLabel.render(g);
        opponentsLabel.render(g);

        teamsNumberLabel.render(g);
        teamplayersNumberLabel.render(g);
        opponentsNumberLabel.render(g);

        teamplayLabel.render(g);
        deathmatchLabel.render(g);

        runLabel.render(g);
        backLabel.render(g);
    }

    @Override
    public void update(final GameContainer container, final StateBasedGame game, int delta)
            throws SlickException {
        Input input = container.getInput();
        Point mouse = new Point(input.getMouseX(), input.getMouseY());

        teamsNumberLabel.setIsMouseOver(mouse);
        teamplayersNumberLabel.setIsMouseOver(mouse);
        opponentsNumberLabel.setIsMouseOver(mouse);
        teamplayLabel.setIsMouseOver(mouse);
        deathmatchLabel.setIsMouseOver(mouse);
        runLabel.setIsMouseOver(mouse);
        backLabel.setIsMouseOver(mouse);

        if (input.isKeyPressed(Input.KEY_ENTER)) {
            game.enterState(Game.GAME_STATE);
        }

        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            game.enterState(Game.MENU_STATE);
        }

        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            if (backLabel.isMouseOver()) {
                game.enterState(Game.MENU_STATE);
            }
            if (runLabel.isMouseOver()) {
                Game.teams = teamplayLabel.isEnabled() ? 0 : teams;
                Game.teamplayers = teamplayLabel.isEnabled() ? opponents + 1 : teamplayers;

                game.enterState(Game.GAME_STATE);
            }
            if (teamplayLabel.isMouseOver()) {
                teamplayLabel.setEnabled(false);
                deathmatchLabel.setEnabled(true);

                teamsNumberLabel.setEnabled(true);
                teamplayersNumberLabel.setEnabled(true);
                opponentsNumberLabel.setEnabled(false);

                teamsLabel.setColors(Color.white);
                teamplayersLabel.setColors(Color.white);
                opponentsLabel.setColors(Color.darkGray);
            }
            if (deathmatchLabel.isMouseOver()) {
                deathmatchLabel.setEnabled(false);
                teamplayLabel.setEnabled(true);

                teamsNumberLabel.setEnabled(false);
                teamplayersNumberLabel.setEnabled(false);
                opponentsNumberLabel.setEnabled(true);

                teamsLabel.setColors(Color.darkGray);
                teamplayersLabel.setColors(Color.darkGray);
                opponentsLabel.setColors(Color.white);
            }
            if (teamsNumberLabel.isMouseOver()) {
                if (teams < 4) {
                    teamsNumberLabel.setText(String.valueOf(++teams));
                }
            }
            if (teamplayersNumberLabel.isMouseOver()) {
                if (teamplayers < 4) {
                    teamplayersNumberLabel.setText(String.valueOf(++teamplayers));
                }
            }
            if (opponentsNumberLabel.isMouseOver()) {
                if (opponents < 15) {
                    opponentsNumberLabel.setText(String.valueOf(++opponents));
                }
            }
        }
        if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
            if (teamsNumberLabel.isMouseOver()) {
                if (teams > 2) {
                    teamsNumberLabel.setText(String.valueOf(--teams));
                }
            }
            if (teamplayersNumberLabel.isMouseOver()) {
                if (teamplayers > 2) {
                    teamplayersNumberLabel.setText(String.valueOf(--teamplayers));
                }
            }
            if (opponentsNumberLabel.isMouseOver()) {
                if (opponents > 1) {
                    opponentsNumberLabel.setText(String.valueOf(--opponents));
                }
            }
        }
    }

    @Override
    public int getID() {
        return stateId;
    }
}
