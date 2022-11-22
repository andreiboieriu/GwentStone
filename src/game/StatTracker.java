package game;

public class StatTracker {
    private static StatTracker instance = null;

    private StatTracker() {}

    public static StatTracker getInstance() {
        if (instance == null) {
            instance = new StatTracker();
        }

        return instance;
    }
}
