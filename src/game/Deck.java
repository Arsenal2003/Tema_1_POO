package game;

import java.util.ArrayList;

public final class Deck {
    private ArrayList<Card> cards;
    private int nrCardsInDeck;

    public Deck(final int nrCardsInDeck) {
        this.nrCardsInDeck = nrCardsInDeck;
        cards = new ArrayList<Card>();
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public int getNrCardsInDeck() {
        return nrCardsInDeck;
    }

    public void setNrCardsInDeck(final int nrCardsInDeck) {
        this.nrCardsInDeck = nrCardsInDeck;
    }

}
