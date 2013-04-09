package app;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import other.Translator;
import state.ConnectState;
import state.GameState;
import state.MenuState;
import state.OnlineGameState;

public class Game extends StateBasedGame {

    public static final int MENU_STATE = 0;
    public static final int GAME_STATE = 1;
    public static final int ONLINE_GAME_STATE = 2;
    public static final int CONNECT_STATE = 3;

    public static final String VERSION = "0.1";

    public static boolean isReinitializationRequried = false;
    public static boolean isHost = true;

    public Game(String title) {
        super(title);
        Translator.getInstance();
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        this.addState(new MenuState(Game.MENU_STATE));
        // this.addState(new ConnectState(Game.CONNECT_STATE));
        // this.addState(new OnlineGameState(Game.ONLINE_GAME_STATE));
    }

    @Override
    public void enterState(int id) {
        try {
            if (this.getState(id) == null) {
                org.newdawn.slick.state.GameState state = this.createState(id);
                state.init(this.getContainer(), this);
                this.addState(state);
            } else {
                org.newdawn.slick.state.GameState state = this.getState(id);
                if ((Game.isReinitializationRequried)) {
                    state.init(this.getContainer(), this);
                }
            }
        } catch (SlickException e) {
            e.printStackTrace();
            System.exit(0);
        }
        super.enterState(id);
    }

    private org.newdawn.slick.state.GameState createState(int id) {
        switch (id) {
            case Game.MENU_STATE:
                return new MenuState(Game.MENU_STATE);
            case Game.GAME_STATE:
                return new GameState(Game.GAME_STATE);
            case Game.ONLINE_GAME_STATE:
                return new OnlineGameState(Game.ONLINE_GAME_STATE);
            case Game.CONNECT_STATE:
                return new ConnectState(Game.CONNECT_STATE);
        }
        return null;
    }
}