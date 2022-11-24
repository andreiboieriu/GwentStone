package game;

import game.cards.Card;
import java.util.ArrayList;
import java.util.List;

public final class Hand {

    @lombok.Getter
    private final List<Card> cardList;

    public Hand() {
        cardList = new ArrayList<>();
    }

    /**
     * @param cardIdx card index
     * @return card with given index from card list
     */
    public Card getCard(final int cardIdx) {
        return cardList.get(cardIdx);
    }

    /**
     * @param card card
     *
     * adds given card to card list
     */
    public void addCard(final Card card) {
        cardList.add(card);
    }

    /**
     * @param cardIdx card index
     *
     * removes card with given index from card list
     */
    public void removeCard(final int cardIdx) {
        cardList.remove(cardIdx);
    }

    /**
     * clears card list
     */
    public void clear() {
        cardList.clear();
    }
}
