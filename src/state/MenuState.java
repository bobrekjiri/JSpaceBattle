package state;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import other.Translator;
import app.Game;
import factory.EffectFactory;
import factory.FontFactory;

public class MenuState extends BasicGameState {

    private int stateId, width, trainTextWidth, trainTextHeight;
    private Font ubuntuLarge;
    private String trainText;

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

        ubuntuLarge = fonts.getFont("ubuntu", width / 16, whiteEffect);

        trainText = "Space Battle " + Game.VERSION;
        trainTextWidth = ubuntuLarge.getWidth(trainText);
        trainTextHeight = ubuntuLarge.getHeight(trainText);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {
        g.setFont(ubuntuLarge);
        g.setColor(Color.gray);
        drawString(g, ubuntuLarge, trainText, (int) (trainTextWidth / 1.75) + width / 500,
                (int) (trainTextHeight / 1.5) + width / 750);
        g.setColor(Color.white);
        drawString(g, ubuntuLarge, trainText, (int) (trainTextWidth / 1.75),
                (int) (trainTextHeight / 1.5));
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta)
            throws SlickException {
        Input input = container.getInput();

        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            container.exit();
        }

    }

    @Override
    public int getID() {
        return this.stateId;
    }

    private void drawString(Graphics g, Font font, String text, float x, float y) {
        int width = font.getWidth(text);
        int height = font.getHeight(text);
        g.drawString(text, x - width / 2, y - height / 2);
    }
}
