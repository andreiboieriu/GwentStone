package game;

public final class StatTracker {

    private static StatTracker instance = null;
    @lombok.Getter
    private int totalGamesPlayed = 0;
    @lombok.Getter
    private int playerOneWins = 0;
    @lombok.Getter
    private int playerTwoWins = 0;

    private StatTracker() {
    }

    /**
     * @return instance of singleton class
     */
    public static StatTracker getInstance() {
        if (instance == null) {
            instance = new StatTracker();
        }

        return instance;
    }

    /**
     * increments totalGamesPlayed
     */
    public void incrementTotalGamesPlayed() {
        totalGamesPlayed++;
    }

    /**
     * increments playerOneWins
     */
    public void incrementPlayerOneWins() {
        playerOneWins++;
    }

    /**
     * increments playerTwoWins
     */
    public void incrementPlayerTwoWins() {
        playerTwoWins++;
    }

    /**
     * resets stats back to 0
     */
    public void resetStats() {
        totalGamesPlayed = 0;
        playerOneWins = 0;
        playerTwoWins = 0;
    }
}
