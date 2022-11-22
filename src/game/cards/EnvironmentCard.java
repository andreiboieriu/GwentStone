package game.cards;

import java.util.List;

public class EnvironmentCard extends Card {
    RowTargetedAbility ability;
    public EnvironmentCard(int manaCost, String description, List<String> colors, String name,
                           RowTargetedAbility ability) {
        super(manaCost, description, colors, name);
        this.ability = ability;
    }

    public EnvironmentCard(EnvironmentCard environmentCard) {
        super(environmentCard);
        this.ability = environmentCard.ability;
    }
}
