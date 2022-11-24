package game.cards;

import game.Board;
import java.util.List;

public final class EarthBorn implements RowTargetedAbility {

    @Override
    public void execute(final Board board, final int targetedRowIdx) {
        List<MinionCard> targetedRow = board.getRow(targetedRowIdx);

        for (MinionCard minionCard : targetedRow) {
            minionCard.setHealth(minionCard.getHealth() + 1);
        }
    }
}
