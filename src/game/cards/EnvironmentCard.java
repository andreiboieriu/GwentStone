package game.cards;

import game.Board;
import java.util.List;

public final class EnvironmentCard extends Card {

    private final RowTargetedAbility ability;

    public EnvironmentCard(final int manaCost, final String description, final List<String> colors,
        final String name,
        final List<CardProperties> cardPropertiesList,
        final RowTargetedAbility ability) {
        super(manaCost, description, colors, name, cardPropertiesList);
        this.ability = ability;
    }

    public EnvironmentCard(final EnvironmentCard environmentCard) {
        super(environmentCard);
        this.ability = environmentCard.ability;
    }

    /**
     * @param board game board
     * @param rowIdx row index
     *
     * executes ability on given board row
     */
    public void executeAbility(final Board board, final int rowIdx) {
        ability.execute(board, rowIdx);
    }

    @Override
    public Card copy() {
        return new EnvironmentCard(this);
    }

}
