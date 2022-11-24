package game;

import fileio.DecksInput;
import fileio.StartGameInput;

public interface StateTransition {

    /**
     * @param playerOneDecks input data for player one decks
     * @param playerTwoDecks input data for player two decks
     *
     * initializes game
     */
    default void initGame(DecksInput playerOneDecks, DecksInput playerTwoDecks) {
    }

    /**
     * resets game data
     */
    default void newGame() {
    }

    /**
     * @param startGameInput input data
     *
     * starts a new game using given data
     */
    default void startGame(StartGameInput startGameInput) {
    }

    /**
     * stops game
     */
    default void stopGame() {
    }
}
