package Game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Coordinates;

import java.util.ArrayList;

public class Table {
    private ArrayList<ArrayList<Card>> cardTable;
    private int[][] frozenCards = new int[4][5];
    private int[][] usedCards = new int[4][5];

    public Table(ArrayList<ArrayList<Card>> cardTable) {
        this.cardTable = cardTable;
        for (int i = 0; i < 4; i++) {
            this.cardTable.add(new ArrayList<>());
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                frozenCards[i][j] = 0;
                usedCards[i][j] = 0;
            }

        }

    }

    public String putCardOnTable(Deck deck, int poz, Player p, int playerIdx) {
        if (deck.getCards().get(poz) instanceof Enviroment)
            return "Cannot place environment card on table.";
        if (p.getMana() - deck.getCards().get(poz).getMana() < 0)
            return "Not enough mana to place card on table.";
        Minion m = (Minion) deck.getCards().get(poz);

        if (m.cardIsFrontRow()) {
            if (playerIdx == 1)
                return playCard(deck, poz, p, 2);
            else
                return playCard(deck, poz, p, 1);
        } else {
            if (playerIdx == 1)
                return playCard(deck, poz, p, 3);
            else
                return playCard(deck, poz, p, 0);

        }

    }

    private String playCard(Deck deck, int poz, Player p, int row) {
        if (cardTable.get(row).size() == 5)
            return "Cannot place card on table since row is full.";
        frozenCards[row][cardTable.get(row).size()] = 0;
        usedCards[row][cardTable.get(row).size()] = 0;
        cardTable.get(row).add(deck.getCards().get(poz));
        p.setMana(p.getMana() - deck.getCards().get(poz).getMana());
        deck.getCards().remove(poz);
        deck.setNrCardsInDeck(deck.getNrCardsInDeck() - 1);
        return null;
    }

    public String playEnvironmentCard(Deck deck, int poz, Player p, int playerIdx, int row) {
        if (!(deck.getCards().get(poz) instanceof Enviroment))
            return "Chosen card is not of type environment.";
        if (p.getMana() - deck.getCards().get(poz).getMana() < 0)
            return "Not enough mana to use environment card.";
        if ((playerIdx == 1 && (row == 2 || row == 3)) || (playerIdx == 2 && (row == 0 || row == 1)))
            return "Chosen row does not belong to the enemy.";

        return ((Enviroment) deck.getCards().get(poz)).abiltyCard(this, deck, poz, p, row);
    }

    public void destroyDeadCards() {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < cardTable.get(i).size(); j++)
                if (((Minion) cardTable.get(i).get(j)).getHealth() <= 0) {
                    cardTable.get(i).remove(j);
                    frozenCards[i][j] = 0;
                    usedCards[i][j] = 0;
                    j--;
                }

    }

    public void deFrozeCards(int player) {
        if (player == 1) {
            for (int i = 2; i <= 3; i++)
                for (int j = 0; j <= 4; j++) {
                    frozenCards[i][j] = 0;
                    usedCards[i][j] = 0;
                }
        } else {
            for (int i = 0; i <= 1; i++)
                for (int j = 0; j <= 4; j++) {
                    frozenCards[i][j] = 0;
                    usedCards[i][j] = 0;
                }
        }

    }

    public boolean existsTank(int player) {
        if (player == 1) {
            for (int i = 0; i < cardTable.get(1).size(); i++)
                if (((Minion) cardTable.get(1).get(i)).getIsTank() == 1)
                    return true;

        } else {
            for (int i = 0; i < cardTable.get(2).size(); i++)
                if (((Minion) cardTable.get(2).get(i)).getIsTank() == 1)
                    return true;

        }
        return false;
    }

    public String abilityHeroFirstPlayer(int affectedrow, Game game) {
        if (game.getFirstPlayer().getMana() - game.getPlayerOneHero().getMana() < 0)
            return "Not enough mana to use hero's ability.";
        if (game.getPlayerOneHeroAttacked() == 1)
            return "Hero has already attacked this turn.";

        return game.getPlayerOneHero().abilityHero(this, affectedrow, game);
    }

    public String abilityHeroSecondPlayer(int affectedrow, Game game) {
        if (game.getSecondPlayer().getMana() - game.getPlayerTwoHero().getMana() < 0)
            return "Not enough mana to use hero's ability.";
        if (game.getPlayerTwoHeroAttacked() == 1)
            return "Hero has already attacked this turn.";
        return game.getPlayerTwoHero().abilityHero(this, affectedrow, game);
    }

    public String attackHero(Coordinates attacker, int playerTurn, Game game) {
        if (frozenCards[attacker.getX()][attacker.getY()] == 1)
            return "Attacker card is frozen.";
        if (usedCards[attacker.getX()][attacker.getY()] == 1)
            return "Attacker card has already attacked this turn.";
        if (existsTank(playerTurn))
            return "Attacked card is not of type 'Tank'.";
        if (playerTurn == 2)
            game.getPlayerOneHero().setHealth(game.getPlayerOneHero().getHealth() - ((Minion) cardTable.get(attacker.getX()).get(attacker.getY())).getAttackDamage());
        else
            game.getPlayerTwoHero().setHealth(game.getPlayerTwoHero().getHealth() - ((Minion) cardTable.get(attacker.getX()).get(attacker.getY())).getAttackDamage());
        usedCards[attacker.getX()][attacker.getY()] = 1;
        return null;
    }

    public String cardAttack(Coordinates attacker, Coordinates attacked, int playerTurn) {
        if (playerTurn == 1) {
            if (attacked.getX() == 3 || attacked.getX() == 2)
                return "Attacked card does not belong to the enemy.";
        } else {
            if (attacked.getX() == 1 || attacked.getX() == 0)
                return "Attacked card does not belong to the enemy.";
        }

        if (usedCards[attacker.getX()][attacker.getY()] == 1)
            return "Attacker card has already attacked this turn.";
        if (frozenCards[attacker.getX()][attacker.getY()] == 1)
            return "Attacker card is frozen.";
        if (existsTank(playerTurn) && ((Minion) cardTable.get(attacked.getX()).get(attacked.getY())).getIsTank() == 0)
            return "Attacked card is not of type 'Tank'.";

        ((Minion) cardTable.get(attacked.getX()).get(attacked.getY())).setHealth(((Minion) cardTable.get(attacked.getX()).get(attacked.getY())).getHealth() - ((Minion) cardTable.get(attacker.getX()).get(attacker.getY())).getAttackDamage());
        usedCards[attacker.getX()][attacker.getY()] = 1;
        destroyDeadCards();

        return null;
    }

    public String cardAbility(Coordinates attacker, Coordinates attacked, int playerTurn) {
        if (frozenCards[attacker.getX()][attacker.getY()] == 1)
            return "Attacker card is frozen.";
        if (usedCards[attacker.getX()][attacker.getY()] == 1)
            return "Attacker card has already attacked this turn.";
        String result = ((Minion) cardTable.get(attacker.getX()).get(attacker.getY())).abiltyMinion(this, attacker, attacked, playerTurn);
        destroyDeadCards();

        return result;
    }

    public ArrayNode printFrozenCards(ObjectMapper objectMapper) {
        ArrayNode vectfinal = objectMapper.createArrayNode();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < cardTable.get(i).size(); j++) {
                if (frozenCards[i][j] == 1)
                    vectfinal.addPOJO(cardTable.get(i).get(j).cardToJson(objectMapper));
            }
        }

        return vectfinal;
    }

    public ObjectNode printCardAtPosition(int x, int y, ObjectMapper objectMapper) {
        if (0 <= x && x < 4)
            if (y >= 0 && y < cardTable.get(x).size())
                return cardTable.get(x).get(y).cardToJson(objectMapper);
        return null;
    }

    public ArrayNode printTableToJSON(ObjectMapper objectMapper) {
        ArrayNode vectfinal = objectMapper.createArrayNode();

        for (int i = 0; i < 4; i++) {
            ArrayNode vect = objectMapper.createArrayNode();
            for (int j = 0; j < cardTable.get(i).size(); j++) {
                vect.addPOJO(cardTable.get(i).get(j).cardToJson(objectMapper));

            }
            vectfinal.addPOJO(vect);
        }

        return vectfinal;
    }

    @Override
    public String toString() {
        return "Table{" +
                "cardTable=" + cardTable +
                '}';
    }

    public ArrayList<ArrayList<Card>> getCardTable() {
        return cardTable;
    }

    public int[][] getFrozenCards() {
        return frozenCards;
    }

    public int[][] getUsedCards() {
        return usedCards;
    }
}
