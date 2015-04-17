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

public class MenuState extends BasicGameState {

    private int stateId, width, height;
    private Font ubuntuLarge, ubuntuMedium;
    private Label titleLabel;
    private InteractiveLabel[] menu;

    public MenuState(int stateId) {
        this.stateId = stateId;
    }

    @Override
    public void init(final GameContainer container, final StateBasedGame game)
            throws SlickException {
        Translator translator = Translator.getInstance();

        FontFactory fonts = FontFactory.getInstance();
        EffectFactory effects = EffectFactory.getInstance();
        ColorEffect whiteEffect = effects.getColorEffect(java.awt.Color.WHITE);
        width = container.getWidth();
        height = container.getHeight();

        ubuntuLarge = fonts.getFont("ubuntu", width / 20, whiteEffect);
        ubuntuMedium = fonts.getFont("ubuntu", width / 24, whiteEffect);

        String title = "Space Battle " + Game.VERSION;
        titleLabel = new Label(title, new Point(width / 35, width / 40), ubuntuLarge, true);
        titleLabel.setColors(Color.white, Color.darkGray);

        String labels[] = { translator.translate("Menu.SinglePlayer"),
                translator.translate("Menu.DebugAndTest"), translator.translate("Menu.Options"),
                translator.translate("Menu.Exit") };

        menu = new InteractiveLabel[labels.length];
        for (int i = 0; i < menu.length; i++) {
            Point position = new Point(width / 2, height * (i + 3) / 8);
            menu[i] = new InteractiveLabel(labels[i], position, ubuntuMedium, false, true);
        }
    }

    @Override
    public void render(final GameContainer container, final StateBasedGame game, final Graphics g)
            throws SlickException {
        g.setFont(ubuntuLarge);
        titleLabel.render(g);
        for (InteractiveLabel label : menu) {
            label.render(g);
        }
    }

    @Override
    public void update(final GameContainer container, final StateBasedGame game, final int delta)
            throws SlickException {
        Input input = container.getInput();
        Point mouse = new Point(input.getMouseX(), input.getMouseY());

        for (InteractiveLabel label : menu) {
            label.setIsMouseOver(mouse);
        }

        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            container.exit();
        }
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {

            if (menu[0].isMouseOver()) {
                game.enterState(Game.LOCAL_GAME_STATE);
            }

            if (menu[1].isMouseOver()) {
                game.enterState(Game.TEST_DEBUG_STATE);
            }

            if (menu[2].isMouseOver()) {
                game.enterState(Game.OPTIONS_STATE);
            }
            if (menu[3].isMouseOver()) {
                container.exit();
            }
        }
    }

    @Override
    public int getID() {
        return this.stateId;
    }
}
