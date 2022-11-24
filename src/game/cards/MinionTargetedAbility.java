package game.cards;

import fileio.Coordinates;
import game.Board;

public interface MinionTargetedAbility {

    /**
     * @param board game board
     * @param attackerMinionCoord board coordinates of attacker minion
     * @param targetedMinionCoord board coordinates of attacked minion
     *
     * executes ability on attacked minion
     */
    void execute(Board board, Coordinates attackerMinionCoord, Coordinates targetedMinionCoord);
}
