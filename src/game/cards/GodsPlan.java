package game.cards;

import fileio.Coordinates;
import game.Board;

public final class GodsPlan implements MinionTargetedAbility {

    public static final int HEALAMOUNT = 2;

    @Override
    public void execute(final Board board, final Coordinates attackerMinionCoord,
        final Coordinates targetedMinionCoord) {
        MinionCard targetedMinion = board.getRow(targetedMinionCoord.getX())
            .get(targetedMinionCoord.getY());

        targetedMinion.setHealth(targetedMinion.getHealth() + HEALAMOUNT);
    }
}
