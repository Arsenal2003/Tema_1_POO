package Game;

import java.util.ArrayList;

public class Enviroment extends Card {

    public Enviroment(Card enviroment) {
        super(enviroment);
    }

    public Enviroment(int mana, String description, ArrayList<String> colors, String name) {
        super(mana, description, colors, name);
    }

    public String abiltyCard(Table table, Deck deck, int idx, Player p, int row) {
        if (getName().equals("Firestorm"))
            return FirestormAbility(table, deck, idx, p, row);
        if (getName().equals("Winterfell"))
            return WinterfellAbility(table, deck, idx, p, row);
        if (getName().equals("Heart Hound"))
            return HeartHoundAbility(table, deck, idx, p, row);

        return null;
    }

    private String FirestormAbility(Table table, Deck deck, int idx, Player p, int row) {
        p.setMana(p.getMana() - deck.getCards().get(idx).getMana());
        deck.getCards().remove(idx);
        deck.setNrCardsInDeck(deck.getNrCardsInDeck() - 1);

        for (int i = 0; i < table.getCardTable().get(row).size(); i++)
            ((Minion) table.getCardTable().get(row).get(i)).setHealth(((Minion) table.getCardTable().get(row).get(i)).getHealth() - 1);
        table.destroyDeadCards();
        return null;
    }

    private String WinterfellAbility(Table table, Deck deck, int idx, Player p, int row) {
        p.setMana(p.getMana() - deck.getCards().get(idx).getMana());
        deck.getCards().remove(idx);
        deck.setNrCardsInDeck(deck.getNrCardsInDeck() - 1);
        for (int i = 0; i < 5; i++)
            table.getFrozenCards()[row][i] = 1;


        return null;
    }

    private String HeartHoundAbility(Table table, Deck deck, int idx, Player p, int row) {
        if (table.getCardTable().get(3 - row).size() == 5)
            return "Cannot steal enemy card since the player's row is full.";

        p.setMana(p.getMana() - deck.getCards().get(idx).getMana());
        deck.getCards().remove(idx);
        deck.setNrCardsInDeck(deck.getNrCardsInDeck() - 1);
        int maxHealthMinion = 0;
        for (int i = 0; i < table.getCardTable().get(row).size(); i++)
            if (((Minion) table.getCardTable().get(row).get(i)).getHealth() > maxHealthMinion)
                maxHealthMinion = ((Minion) table.getCardTable().get(row).get(i)).getHealth();
        for (int i = 0; i < table.getCardTable().get(row).size(); i++)
            if (((Minion) table.getCardTable().get(row).get(i)).getHealth() == maxHealthMinion) {
                table.getCardTable().get(3 - row).add(table.getCardTable().get(row).get(i));
                table.getCardTable().get(row).remove(i);
                break;
            }
        return null;
    }
}
