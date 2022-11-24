package game.cards;

import fileio.Coordinates;
import game.Board;

public final class Skyjack implements MinionTargetedAbility {

    @Override
    public void execute(final Board board, final Coordinates attackerMinionCoord,
        final Coordinates targetedMinionCoord) {
        MinionCard attackerMinion = board.getRow(attackerMinionCoord.getX())
            .get(attackerMinionCoord.getY());
        MinionCard targetedMinion = board.getRow(targetedMinionCoord.getX())
            .get(targetedMinionCoord.getY());

        int temp = attackerMinion.getHealth();
        attackerMinion.setHealth(targetedMinion.getHealth());
        targetedMinion.setHealth(temp);
    }
}
