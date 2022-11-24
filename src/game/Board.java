package game;

import game.cards.MinionCard;
import java.util.ArrayList;
import java.util.List;

public final class Board {

    private static final int ROWS = 4;
    private static final int COLUMNS = 5;
    @lombok.Getter
    private final List<List<MinionCard>> minionCardMatrix;

    public Board() {
        minionCardMatrix = new ArrayList<>(ROWS);

        for (int i = 0; i < ROWS; i++) {
            minionCardMatrix.add(new ArrayList<>(COLUMNS));
        }
    }

    /**
     * clears board
     */
    public void clear() {
        for (List<MinionCard> minionCardList : minionCardMatrix) {
            minionCardList.clear();
        }
    }

    /**
     * @param playerIdx player index
     * @return front row of given player
     */
    public List<MinionCard> getFrontRow(final int playerIdx) {
        if (playerIdx == 1) {
            return minionCardMatrix.get(2);
        }

        if (playerIdx == 2) {
            return minionCardMatrix.get(1);
        }

        return null;
    }

    /**
     * @param playerIdx player index
     * @return back row of given player
     */
    public List<MinionCard> getBackRow(final int playerIdx) {
        if (playerIdx == 1) {
            return minionCardMatrix.get(3);
        }

        if (playerIdx == 2) {
            return minionCardMatrix.get(0);
        }

        return null;
    }

    /**
     * @param rowIdx row index
     * @return row with given index
     */
    public List<MinionCard> getRow(final int rowIdx) {
        return minionCardMatrix.get(rowIdx);
    }

    /**
     * @param minionCardList list of minion cards
     * @return true if given row is full, false if not
     */
    public boolean checkRowIsFull(final List<MinionCard> minionCardList) {
        return (minionCardList.size() == COLUMNS);
    }

    /**
     * @param rowIdx row index
     * @return row mirrored to given row
     */
    public List<MinionCard> getMirroredRow(final int rowIdx) {
        return minionCardMatrix.get(ROWS - 1 - rowIdx);
    }
}
