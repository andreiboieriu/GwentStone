package game.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import game.Board;
import java.util.List;

public final class HeroCard extends Card {

    @lombok.Getter
    @lombok.Setter
    private int health;
    private final RowTargetedAbility ability;
    @JsonIgnore
    @lombok.Getter
    @lombok.Setter
    private boolean available;

    public HeroCard(final int manaCost, final String description, final List<String> colors,
        final String name, final int health,
        final List<CardProperties> cardPropertiesList,
        final RowTargetedAbility ability) {
        super(manaCost, description, colors, name, cardPropertiesList);
        this.health = health;
        this.ability = ability;
    }

    public HeroCard(final HeroCard heroCard) {
        super(heroCard);
        this.health = heroCard.health;
        this.ability = heroCard.ability;
        this.available = heroCard.available;
    }

    @Override
    public Card copy() {
        return new HeroCard(this);
    }

    /**
     * @param damage damage amount
     *
     * subtracts damage from health
     */
    public void takeDamage(final int damage) {
        health -= damage;
    }

    /**
     * @param board game board
     * @param targetedRowIdx targeted row index
     *
     * executes ability on targeted board row
     */
    public void executeAbility(final Board board, final int targetedRowIdx) {
        ability.execute(board, targetedRowIdx);
        available = false;
    }
}
