package game.cards;

import java.util.List;

public class HeroCard extends Card {
    int health;
    RowTargetedAbility ability;

    public HeroCard(int manaCost, String description, List<String> colors, String name, int health,
                    RowTargetedAbility ability) {
        super(manaCost, description, colors, name);
        this.health = health;
        this.ability = ability;
    }

    public HeroCard(HeroCard heroCard) {
        super(heroCard);
        this.health = heroCard.health;
        this.ability = heroCard.ability;
    }
}
