package game.cards;

import fileio.Coordinates;
import game.Board;

public final class Shapeshift implements MinionTargetedAbility {

    @Override
    public void execute(final Board board, final Coordinates attackerMinionCoord,
        final Coordinates targetedMinionCoord) {
        MinionCard targetedMinion = board.getRow(targetedMinionCoord.getX())
            .get(targetedMinionCoord.getY());

        int temp = targetedMinion.getAttackDamage();

        if (temp == 0) {
            board.getRow(targetedMinionCoord.getX()).remove(targetedMinion);
            return;
        }

        targetedMinion.setAttackDamage(targetedMinion.getHealth());
        targetedMinion.setHealth(temp);
    }
}
