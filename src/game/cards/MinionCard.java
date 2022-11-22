package game.cards;

import java.util.List;

public class MinionCard extends Card {
    private int health;
    private int attackDamage;
    private MinionTargetedAbility ability;

    public MinionCard(int manaCost, String description, List<String> colors, String name,
                      int health, int attackDamage, MinionTargetedAbility ability) {
        super(manaCost, description, colors, name);
        this.health = health;
        this.attackDamage = attackDamage;
        this.ability = ability;
    }

    public MinionCard(MinionCard minionCard) {
        super(minionCard);
        this.health = minionCard.health;
        this.attackDamage = minionCard.attackDamage;
        this.ability = minionCard.ability;
    }

    public void useAbility(MinionCard minionCard) {
        if (ability == null)
                return;

        ability.execute(minionCard);
    }

    // status effects?
}
