package game.cards;

import game.Board;
import java.util.List;

public final class Winterfell implements RowTargetedAbility {

    @Override
    public void execute(final Board board, final int rowIdx) {
        List<MinionCard> targetedRow = board.getRow(rowIdx);

        for (MinionCard minionCard : targetedRow) {
            minionCard.addStatusEffect(StatusEffects.FROZEN);
        }
    }
}
