package game.cards;

import java.util.List;

public abstract class Card {
    private int manaCost;
    private String description;
    private List<String> colors;
    private String name;

    Card(int manaCost, String description, List<String> colors, String name) {
        this.manaCost = manaCost;
        this.description = description;
        this.colors = colors;
        this.name = name;
    }

    Card(Card card) {
        this.manaCost = card.manaCost;
        this.description = card.description;
        this.colors = card.colors;
        this.name = card.name;
    }
}
