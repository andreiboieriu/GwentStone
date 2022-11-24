package game.cards;

import game.Board;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class HeartHound implements RowTargetedAbility {

    @Override
    public void execute(final Board board, final int rowIdx) {
        List<MinionCard> targetedRow = board.getRow(rowIdx);
        List<MinionCard> mirroredRow = board.getMirroredRow(rowIdx);

        MinionCard minionCard = Collections.max(targetedRow,
            Comparator.comparingInt(MinionCard::getHealth));

        mirroredRow.add(minionCard);
        targetedRow.remove(minionCard);
    }
}
