package game.cards;

import java.util.List;

public interface RowTargetedAbility {
    void execute(List<MinionCard> row);
}
