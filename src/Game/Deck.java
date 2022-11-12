package Game;

import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> cards;
    private int nrCardsInDeck;

    public Deck(int nrCardsInDeck) {
        this.nrCardsInDeck = nrCardsInDeck;
        cards = new ArrayList<Card>();
    }
    public ArrayList<Card> getCards() {
        return cards;
    }

    public int getNrCardsInDeck() {
        return nrCardsInDeck;
    }

    public void setNrCardsInDeck(int nrCardsInDeck) {
        this.nrCardsInDeck = nrCardsInDeck;
    }

    public String toString() {
        return "Deck{"
                + "nr_cards_in_deck="
                + nrCardsInDeck
                + ", cards="
                + cards
                + '}';
    }
}
