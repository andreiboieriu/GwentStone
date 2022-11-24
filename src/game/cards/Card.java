package game.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

public abstract class Card {

    @lombok.Getter
    @lombok.Setter
    private int mana;
    @lombok.Getter
    @lombok.Setter
    private String description;
    @lombok.Getter
    @lombok.Setter
    private List<String> colors;
    @lombok.Getter
    @lombok.Setter
    private String name;
    @JsonIgnore
    @lombok.Getter
    private final List<CardProperties> cardPropertiesList;

    Card(final int mana, final String description, final List<String> colors, final String name,
        final List<CardProperties> cardPropertiesList) {
        this.mana = mana;
        this.description = description;
        this.colors = colors;
        this.name = name;
        this.cardPropertiesList = cardPropertiesList;
    }

    Card(final Card card) {
        this.mana = card.mana;
        this.description = card.description;
        this.colors = card.colors;
        this.name = card.name;
        this.cardPropertiesList = card.cardPropertiesList;
    }

    /**
     * @return deep copy of card
     */
    public abstract Card copy();
}
