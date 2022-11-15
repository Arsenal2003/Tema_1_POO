package game;

import java.util.ArrayList;

public final class Environment extends Card {

    public Environment(final Card environment) {
        super(environment);
    }

    public Environment(final int mana,
                       final String description,
                       final ArrayList<String> colors, final String name) {
        super(mana, description, colors, name);
    }

    /**
     *
     * @param table represents the table were the cards are placed
     * @param deck the hand of the player where the card we want to play is
     * @param idx where in the hand of the player the card is
     * @param p a reference to the player that is playing the card
     * @param row the row on the table where the card hits
     * @return null if the card was used successfully or a string with an error message
     */
    public String abilityCard(final Table table, final Deck deck,
                              final int idx, final Player p, final int row) {
        if (getName().equals("Firestorm")) {
            return firestormAbility(table, deck, idx, p, row);
        }
        if (getName().equals("Winterfell")) {
            return winterfellAbility(table, deck, idx, p, row);
        }
        if (getName().equals("Heart Hound")) {
            return heartHoundAbility(table, deck, idx, p, row);
        }
        return null;
    }

    private String firestormAbility(final Table table,
                                    final Deck deck,
                                    final int idx, final Player p, final int row) {
        p.setMana(p.getMana() - deck.getCards().get(idx).getMana());
        deck.getCards().remove(idx);
        deck.setNrCardsInDeck(deck.getNrCardsInDeck() - 1);

        for (int i = 0; i < table.getCardTable().get(row).size(); i++) {
            ((Minion) table.getCardTable().get(row).get(i))
                    .setHealth(((Minion) table.getCardTable().get(row).get(i))
                            .getHealth() - 1);
        }
            table.destroyDeadCards();
        return null;
    }

    private String winterfellAbility(final Table table, final Deck deck,
                                     final int idx,
                                     final Player p, final int row) {
        p.setMana(p.getMana() - deck.getCards().get(idx).getMana());
        deck.getCards().remove(idx);
        deck.setNrCardsInDeck(deck.getNrCardsInDeck() - 1);
        for (int i = 0; i < table.getCollums(); i++) {
            table.getFrozenCards()[row][i] = 1;
        }
        return null;
    }

    private String heartHoundAbility(final Table table, final Deck deck,
                                     final int idx, final Player p, final int row) {
        if (table.getCardTable().get(3 - row).size() == table.getCollums()) {
            return "Cannot steal enemy card since the player's row is full.";
        }

        p.setMana(p.getMana() - deck.getCards().get(idx).getMana());
        deck.getCards().remove(idx);
        deck.setNrCardsInDeck(deck.getNrCardsInDeck() - 1);
        int maxHealthMinion = 0;
        for (int i = 0; i < table.getCardTable().get(row).size(); i++) {
            if (((Minion) table.getCardTable().get(row).get(i)).getHealth() > maxHealthMinion) {
                maxHealthMinion = ((Minion) table.getCardTable().get(row).get(i)).getHealth();
            }
        }
        for (int i = 0; i < table.getCardTable().get(row).size(); i++) {
            if (((Minion) table.getCardTable().get(row).get(i)).getHealth() == maxHealthMinion) {
                table.getCardTable().get(3 - row).add(table.getCardTable().get(row).get(i));
                table.getCardTable().get(row).remove(i);
                break;
            }
        }
        return null;
    }
}
