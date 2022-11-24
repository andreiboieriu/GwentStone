package game.cards;

import fileio.Coordinates;
import game.Board;

public final class WeakKnees implements MinionTargetedAbility {

    private final int removedAttackDamageAmount = 2;

    @Override
    public void execute(final Board board, final Coordinates attackerMinionCoord,
        final Coordinates targetedMinionCoord) {
        MinionCard targetedMinion = board.getRow(targetedMinionCoord.getX())
            .get(targetedMinionCoord.getY());

        targetedMinion.setAttackDamage(
            targetedMinion.getAttackDamage() - removedAttackDamageAmount);

        if (targetedMinion.getAttackDamage() < 0) {
            targetedMinion.setAttackDamage(0);
        }
    }
}
