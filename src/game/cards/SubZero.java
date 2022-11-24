package game.cards;

import game.Board;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class SubZero implements RowTargetedAbility {

    @Override
    public void execute(final Board board, final int targetedRowIdx) {
        List<MinionCard> targetedRow = board.getRow(targetedRowIdx);
        MinionCard targetedMinion = Collections.max(targetedRow,
            Comparator.comparingInt(MinionCard::getAttackDamage));

        targetedMinion.addStatusEffect(StatusEffects.FROZEN);
    }
}
