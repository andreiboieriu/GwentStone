package game;

import fileio.CardInput;
import game.cards.Card;
import game.cards.CardFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class Deck {

    @lombok.Getter
    private final List<Card> cardList;

    public Deck(final List<CardInput> cardInputList) {
        cardList = new ArrayList<>();

        for (CardInput cardInput : cardInputList) {
            cardList.add(CardFactory.getCard(cardInput));
        }
    }

    public Deck(final Deck deck) {
        List<Card> newCardList = new ArrayList<>();

        for (Card card : deck.cardList) {
            newCardList.add(card.copy());
        }

        this.cardList = newCardList;
    }

    /**
     * @return deep copy of current instance
     */
    public Deck copy() {
        return new Deck(this);
    }

    /**
     * @param hand hand
     *
     * removes first card from card list and places it in hand
     */
    public void dealCard(final Hand hand) {
        if (cardList.isEmpty()) {
            return;
        }

        hand.addCard(cardList.get(0));
        cardList.remove(0);
    }

    /**
     * @param shuffleSeed seed
     *
     * shuffles card list with given seed
     */
    public void shuffle(final int shuffleSeed) {
        Collections.shuffle(cardList, new Random(shuffleSeed));
    }

}
