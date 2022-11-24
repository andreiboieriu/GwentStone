package game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fileio.CardInput;
import fileio.DecksInput;
import game.cards.HeroCard;
import java.util.ArrayList;
import java.util.List;

public final class Player {

    @lombok.Getter
    @lombok.Setter
    private Hand hand;
    private final List<Deck> deckList;
    @lombok.Getter
    private Deck deck;
    @lombok.Getter
    @lombok.Setter
    private HeroCard heroCard;
    @lombok.Getter
    @lombok.Setter
    private int mana;
    @JsonIgnore
    @lombok.Getter
    @lombok.Setter
    private boolean endedTurn;

    public Player(final DecksInput decksInput) {
        hand = new Hand();
        deckList = new ArrayList<>();

        for (List<CardInput> cardInputList : decksInput.getDecks()) {
            deckList.add(new Deck(cardInputList));
        }
    }

    /**
     * @param deckIdx index of deck in deck list
     * @param shuffleSeed seed for shuffling deck
     *
     * deep copies deck with given index from deck list into deck then shuffles deck
     */
    public void chooseDeck(final int deckIdx, final int shuffleSeed) {
        deck = deckList.get(deckIdx).copy();
        deck.shuffle(shuffleSeed);
    }

    /**
     * @param manaAmount mana amount to be subtracted from mana
     */
    public void spendMana(final int manaAmount) {
        mana -= manaAmount;
    }

    /**
     * @param manaAmount mana amount to be added to mana
     */
    public void gainMana(final int manaAmount) {
        mana += manaAmount;
    }

    /**
     * resets mana back to 0
     */
    public void resetMana() {
        mana = 0;
    }
}
