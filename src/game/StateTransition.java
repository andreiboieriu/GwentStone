package game;

public interface StateTransition {
    default void initGame() {}

    default void newGame() {}

    default void startGame() {}

    default void stopGame() {}

    // runAction() ???????????
}
