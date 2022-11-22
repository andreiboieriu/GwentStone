package game;

import fileio.Input;

import java.util.ArrayList;
import java.util.List;

public class Game implements StateTransition {
    // define Game Objects
    State currentState;
    List<Player> playerArr;
    Board board;

    public Game(Input input) {
        initGame();
    }

    // place input in corresponding classes i guess
    // one time initialization
    @Override
    public void initGame() {
        // TODO

        currentState = State.INITIALIZED;
    }

    @Override
    public void newGame() {
        if (currentState != State.INITIALIZED && currentState != State.GAMEOVER) {
            throw new IllegalStateException("Cannot run newGame() in current state:" + currentState);
        }

        // reset game objects

        currentState = State.READY;
    }

    @Override
    public void startGame() {
        if (currentState != State.READY) {
            throw new IllegalStateException("Cannot run startGame() in current state:" + currentState);
        }

        // player chooses his deck and stuff like that

        currentState = State.PLAYING;
    }

    public void runAction() {
        if (currentState != State.PLAYING) {
            throw new IllegalStateException("Cannot run stopGame() in current state:" + currentState);
        }

        // TODO
    }

    @Override
    public void stopGame() {
        if (currentState != State.PLAYING) {
            throw new IllegalStateException("Cannot run stopGame() in current state:" + currentState);
        }

        // TODO

        currentState = State.GAMEOVER;
    }

    // other methods

    // inner class to access outer class properties
    class Action {
        // TODO
    }
}
