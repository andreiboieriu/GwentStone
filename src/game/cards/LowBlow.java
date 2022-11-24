package game.cards;

import game.Board;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class LowBlow implements RowTargetedAbility {

    @Override
    public void execute(final Board board, final int targetedRowIdx) {
        List<MinionCard> targetedRow = board.getRow(targetedRowIdx);
        MinionCard targetedMinion = Collections.max(targetedRow,
            Comparator.comparingInt(MinionCard::getHealth));

        targetedRow.remove(targetedMinion);
    }
}
