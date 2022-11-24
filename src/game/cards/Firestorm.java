package game.cards;

import game.Board;
import java.util.List;

public final class Firestorm implements RowTargetedAbility {

    private static final int ABILITYDAMAGE = 1;

    @Override
    public void execute(final Board board, final int rowIdx) {
        List<MinionCard> targetedRow = board.getRow(rowIdx);

        for (int i = targetedRow.size() - 1; i >= 0; i--) {
            MinionCard minionCard = targetedRow.get(i);

            minionCard.takeDamage(ABILITYDAMAGE);

            if (minionCard.getHealth() <= 0) {
                targetedRow.remove(minionCard);
            }
        }
    }
}
