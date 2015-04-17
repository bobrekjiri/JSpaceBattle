package app;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import state.GameState;
import state.LocalGameState;
import state.MenuState;
import state.OptionsState;

public class Game extends StateBasedGame {

    public static final int MENU_STATE = 0;
    public static final int GAME_STATE = 1;
    public static final int TEST_DEBUG_STATE = 2;
    public static final int LOCAL_GAME_STATE = 3;
    public static final int OPTIONS_STATE = 4;

    public static final String VERSION = "1.0";

    public static boolean isHost = true;
    public static Random random;

    public static int teams;
    public static int teamplayers;

    public Game(String title) {
        super(title);
        Translator.getInstance();
        random = new Random();
    }

    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        this.addState(new MenuState(Game.MENU_STATE));
    }

    @Override
    public void enterState(int id) {
        try {
            if (getState(id) == null) {
                org.newdawn.slick.state.GameState state = createState(id);
                addState(state);
            }
            getState(id).init(getContainer(), this);
            super.enterState(id);
        } catch (SlickException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private org.newdawn.slick.state.GameState createState(int id) {
        switch (id) {
            case Game.MENU_STATE:
                return new MenuState(Game.MENU_STATE);
            case Game.GAME_STATE:
                return new GameState(Game.GAME_STATE);
            case Game.TEST_DEBUG_STATE:
                return new GameState(Game.TEST_DEBUG_STATE);
            case Game.LOCAL_GAME_STATE:
                return new LocalGameState(Game.LOCAL_GAME_STATE);
            case Game.OPTIONS_STATE:
                return new OptionsState(Game.OPTIONS_STATE);
        }
        return null;
    }
}