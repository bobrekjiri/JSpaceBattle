package state;

import java.awt.Point;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import other.Label;
import other.Translator;
import factory.EffectFactory;
import factory.FontFactory;

public class OnlineGameState extends BasicGameState {

    private int stateId, width, height;
    private Label titleLabel;
    private Font ubuntuMedium, ubuntuSmall;

    public OnlineGameState(int stateId) {
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

        ubuntuMedium = fonts.getFont("ubuntu", width / 32, whiteEffect);
        ubuntuSmall = fonts.getFont("ubuntu", width / 40, whiteEffect);

        titleLabel = new Label(translator.translate("OnlineGame.HostTitle"), new Point(width / 25,
                height / 30), ubuntuMedium);
        titleLabel.setColors(Color.white);

    }

    @Override
    public void render(final GameContainer container, final StateBasedGame game, final Graphics g)
            throws SlickException {
        titleLabel.render(g);

    }

    @Override
    public void update(final GameContainer container, final StateBasedGame game, final int delta)
            throws SlickException {
        // TODO Auto-generated method stub

    }

    @Override
    public int getID() {
        return stateId;
    }

}
