package state;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameState extends BasicGameState {

    private int stateId;

    public GameState(int stateId) {
        this.stateId = stateId;
    }

    @Override
    public void init(final GameContainer container, final StateBasedGame game)
            throws SlickException {

    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g)
            throws SlickException {

    }

    @Override
    public void update(final GameContainer container, final StateBasedGame game, int delta)
            throws SlickException {
        Input input = container.getInput();

        input.clearKeyPressedRecord();
    }

    @Override
    public int getID() {
        return this.stateId;
    }

}
