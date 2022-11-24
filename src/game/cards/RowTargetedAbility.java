package game.cards;

import game.Board;

public interface RowTargetedAbility {

    /**
     * @param board game board
     * @param targetedRowIdx targeted row index
     *
     * executes ability on targeted board row
     */
    void execute(Board board, int targetedRowIdx);
}
