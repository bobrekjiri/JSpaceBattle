package state;

import java.awt.Point;
import java.awt.Rectangle;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import other.InteractiveLabel;
import other.Label;
import other.Translator;
import app.Game;
import factory.EffectFactory;
import factory.FontFactory;

public class ConnectState extends BasicGameState {

    private int stateId, width, height;
    private TextField ipTextField, portTextField;
    private Font ubuntuMedium, ubuntuSmall;
    private Label ipLabel, portLabel, titleLabel, infoLabel;
    private InteractiveLabel connect;
    private boolean isFirstFocused = true;

    public ConnectState(int stateId) {
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

        ipTextField = initTextField(container, width * 2 / 5, height * 3 / 8, width / 4,
                width / 25, 15);

        portTextField = initTextField(container, width * 2 / 5, height * 4 / 8, width / 4,
                width / 25, 5);

        ipLabel = new Label(translator.translate("IP:"), new Point(width * 4 / 13, height * 3 / 8),
                ubuntuMedium);
        ipLabel.setColors(Color.white);
        portLabel = new Label(translator.translate("Port:"), new Point(width * 4 / 13,
                height * 4 / 8), ubuntuMedium);
        portLabel.setColors(Color.white);
        titleLabel = new Label(translator.translate("Connect.Title"), new Point(width / 25,
                height / 30), ubuntuMedium);
        titleLabel.setColors(Color.white);
        infoLabel = new Label(translator.translate("Connect.Info.TabSwitch"), new Point(width / 30,
                height - width / 25), ubuntuSmall);
        infoLabel.setColors(Color.white);
        String connectStr = translator.translate("Connect");
        Rectangle rectangle = new Rectangle(ubuntuMedium.getWidth(connectStr),
                ubuntuMedium.getHeight(connectStr));
        connect = new InteractiveLabel(connectStr, new Point(width / 2, height * 5 / 8),
                ubuntuMedium, rectangle, false, true);
    }

    @Override
    public void render(final GameContainer container, final StateBasedGame game, Graphics g)
            throws SlickException {
        ipLabel.render(g);
        portLabel.render(g);
        titleLabel.render(g);
        infoLabel.render(g);
        connect.render(g);
        g.setColor(Color.white);
        ipTextField.render(container, g);
        portTextField.render(container, g);
    }

    @Override
    public void update(final GameContainer container, final StateBasedGame game, final int delta)
            throws SlickException {
        Input input = container.getInput();
        Point mouse = new Point(input.getMouseX(), input.getMouseY());

        if (input.isKeyPressed(Input.KEY_TAB)) {
            isFirstFocused = !isFirstFocused;
        }
        if (input.isKeyPressed(Input.KEY_ESCAPE)) {
            game.enterState(Game.MENU_STATE);
        }
        if (input.isKeyPressed(Input.KEY_ENTER)) {
            // TODO connect
        }
        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
            if (connect.isMouseOver()) {
                // TODO connect
            }
        }

        ipTextField.setFocus(isFirstFocused);
        portTextField.setFocus(!isFirstFocused);

        connect.setIsMouseOver(mouse);
    }

    @Override
    public int getID() {
        return stateId;
    }

    private TextField initTextField(final GameContainer container, int x, int y, int width,
            int height, int maxLenght) {
        TextField textField = new TextField(container, ubuntuMedium, x, y, width, height);
        textField.setBackgroundColor(new Color(0.3f, 0.3f, 0.3f, 0.3f));
        textField.setBorderColor(Color.darkGray);
        textField.setTextColor(Color.white);
        textField.setMaxLength(maxLenght);
        textField.setText("");
        return textField;
    }

}
