package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Coordinates;

import java.util.ArrayList;

public final class Table {


    private ArrayList<ArrayList<Card>> cardTable;
    private final int rows = 4;
    private final int collums = 5;
    private int[][] frozenCards = new int[rows][collums];
    private int[][] usedCards = new int[rows][collums];


    public Table(final ArrayList<ArrayList<Card>> cardTable) {
        this.cardTable = cardTable;
        for (int i = 0; i < rows; i++) {
            this.cardTable.add(new ArrayList<>());
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < collums; j++) {
                frozenCards[i][j] = 0;
                usedCards[i][j] = 0;
            }
        }
    }

    /**
     *
     * @param deck the current hand of the player
     * @param poz the card position in the player hand
     * @param p the player
     * @param playerIdx the first or the second player
     * @return null if the card in hand was placed successfully on the table or an error otherwise
     */
    public String putCardOnTable(final Deck deck, final int poz,
                                 final Player p, final int playerIdx) {
        if (deck.getCards().get(poz) instanceof Environment) {
            return "Cannot place environment card on table.";
        }
        if (p.getMana() - deck.getCards().get(poz).getMana() < 0) {
            return "Not enough mana to place card on table.";
        }
        Minion m = (Minion) deck.getCards().get(poz);

        if (m.cardIsFrontRow()) {
            if (playerIdx == 1) {
                return playCard(deck, poz, p, 2);
            } else {
                return playCard(deck, poz, p, 1);
            }
        } else {
            if (playerIdx == 1) {
                return playCard(deck, poz, p, 3);
            } else {
                return playCard(deck, poz, p, 0);
            }

        }

    }

    private String playCard(final Deck deck, final int poz, final Player p, final int row) {
        if (cardTable.get(row).size() == collums) {
            return "Cannot place card on table since row is full.";
        }
        frozenCards[row][cardTable.get(row).size()] = 0;
        usedCards[row][cardTable.get(row).size()] = 0;
        cardTable.get(row).add(deck.getCards().get(poz));
        p.setMana(p.getMana() - deck.getCards().get(poz).getMana());
        deck.getCards().remove(poz);
        deck.setNrCardsInDeck(deck.getNrCardsInDeck() - 1);
        return null;
    }

    /**
     *
     * @param deck the current hand of the player
     * @param poz the poz of the environment card
     * @param p the player that is playing the card
     * @param playerIdx the first or the second player
     * @param row the row on the table that is affected by the card
     * @return null if the environment card hit or an error string otherwise
     */
    public String playEnvironmentCard(final Deck deck, final int poz, final Player p,
                                      final int playerIdx, final int row) {
        if (!(deck.getCards().get(poz) instanceof Environment)) {
            return "Chosen card is not of type environment.";
        }
        if (p.getMana() - deck.getCards().get(poz).getMana() < 0) {
            return "Not enough mana to use environment card.";
        }
        if ((playerIdx == 1 && (row == 2 || row == 3))
                || (playerIdx == 2 && (row == 0 || row == 1))) {
            return "Chosen row does not belong to the enemy.";
        }
        return ((Environment) deck.getCards().get(poz)).abilityCard(this, deck, poz, p, row);
    }

    /**
     * this function makes sure that there are no cards on the table with 0 health
     */
    public void destroyDeadCards() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cardTable.get(i).size(); j++) {
                if (((Minion) cardTable.get(i).get(j)).getHealth() <= 0) {
                    cardTable.get(i).remove(j);
                    frozenCards[i][j] = 0;
                    usedCards[i][j] = 0;
                    j--;
                }
            }
        }
    }

    /**
     *
     * @param player the player for which we unfroze the cards on the table
     */
    public void deFrozeCards(final int player) {
        if (player == 1) {
            for (int i = 2; i <= rows - 1; i++) {
                for (int j = 0; j <= rows; j++) {
                    frozenCards[i][j] = 0;
                    usedCards[i][j] = 0;
                }
            }
        } else {
            for (int i = 0; i <= 1; i++) {
                for (int j = 0; j <= rows; j++) {
                    frozenCards[i][j] = 0;
                    usedCards[i][j] = 0;
                }
            }
        }

    }

    /**
     *
     * @param player the player that we check for
     * @return true if the player has a tank card on the table
     */
    public boolean existsTank(final int player) {
        if (player == 1) {
            for (int i = 0; i < cardTable.get(1).size(); i++) {
                if (((Minion) cardTable.get(1).get(i)).getIsTank() == 1) {
                    return true;
                }
            }

        } else {
            for (int i = 0; i < cardTable.get(2).size(); i++) {
                if (((Minion) cardTable.get(2).get(i)).getIsTank() == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @param affectedrow row affected by the second hero ability
     * @param game the instance of the current game
     * @return null if the hero ability hit or an error string otherwise
     */
    public String abilityHeroFirstPlayer(final int affectedrow, final Game game) {
        if (game.getFirstPlayer().getMana() - game.getPlayerOneHero().getMana() < 0) {
            return "Not enough mana to use hero's ability.";
        }
        if (game.getPlayerOneHeroAttacked() == 1) {
            return "Hero has already attacked this turn.";
        }

        return game.getPlayerOneHero().abilityHero(this, affectedrow, game);
    }

    /**
     *
     * @param affectedrow row affected by the second hero ability
     * @param game  the instance of the current game
     * @return null if the hero ability hit or an error string otherwise
     */
    public String abilityHeroSecondPlayer(final int affectedrow, final Game game) {
        if (game.getSecondPlayer().getMana()
                - game.getPlayerTwoHero().getMana() < 0) {
            return "Not enough mana to use hero's ability.";
        }
        if (game.getPlayerTwoHeroAttacked() == 1) {
            return "Hero has already attacked this turn.";
        }
        return game.getPlayerTwoHero().abilityHero(this, affectedrow, game);
    }

    /**
     *
     * @param attacker the card that attacks the hero
     * @param playerTurn the current player that is playing the current turn
     * @param game the instance of the current game
     * @return null if the card hit the hero or an error string otherwise
     */
    public String attackHero(final Coordinates attacker, final int playerTurn, final Game game) {
        if (frozenCards[attacker.getX()][attacker.getY()] == 1) {
            return "Attacker card is frozen.";
        }
        if (usedCards[attacker.getX()][attacker.getY()] == 1) {
            return "Attacker card has already attacked this turn.";
        }
        if (existsTank(playerTurn)) {
            return "Attacked card is not of type 'Tank'.";
        }
        if (playerTurn == 2) {
            game.getPlayerOneHero().setHealth(game.getPlayerOneHero().getHealth()
                    - ((Minion) cardTable.get(attacker.getX()).get(attacker.getY()))
                    .getAttackDamage());
        } else {
            game.getPlayerTwoHero().setHealth(game.getPlayerTwoHero().getHealth()
                    - ((Minion) cardTable.get(attacker.getX()).get(attacker.getY()))
                    .getAttackDamage());
        usedCards[attacker.getX()][attacker.getY()] = 1;
        }
        return null;
    }

    /**
     *
     * @param attacker the card that attacks
     * @param attacked the card that is being attacked
     * @param playerTurn the current player that is playing the current turn
     * @return null if the card attack was successful or an error string otherwise
     */
    public String cardAttack(final Coordinates attacker,
                             final Coordinates attacked, final int playerTurn) {
        if (playerTurn == 1) {
            if (attacked.getX() == rows - 1 || attacked.getX() == rows - 2) {
                return "Attacked card does not belong to the enemy.";
            }
        } else {
            if (attacked.getX() == 1 || attacked.getX() == 0) {
                return "Attacked card does not belong to the enemy.";
            }
        }

        if (usedCards[attacker.getX()][attacker.getY()] == 1) {
            return "Attacker card has already attacked this turn.";
        }
        if (frozenCards[attacker.getX()][attacker.getY()] == 1) {
            return "Attacker card is frozen.";
        }
        if (existsTank(playerTurn)
                && ((Minion) cardTable.get(attacked.getX()).get(attacked.getY()))
                .getIsTank() == 0) {
            return "Attacked card is not of type 'Tank'.";
        }
        ((Minion) cardTable.get(attacked.getX()).get(attacked.getY())).setHealth(
                ((Minion) cardTable.get(attacked.getX()).get(attacked.getY())).getHealth()
                        - ((Minion) cardTable.get(attacker.getX()).get(attacker.getY()))
                        .getAttackDamage());
        usedCards[attacker.getX()][attacker.getY()] = 1;
        destroyDeadCards();

        return null;
    }

    /**
     *
     * @param attacker the coordinates on the table of the card that attacks
     * @param attacked the coordinates on the table of the card that is attacked
     * @param playerTurn the current player that is playing the current turn
     * @return null if the cards ability was successful or an error string otherwise
     */
    public String cardAbility(final Coordinates attacker,
                              final Coordinates attacked, final int playerTurn) {
        if (frozenCards[attacker.getX()][attacker.getY()] == 1) {
            return "Attacker card is frozen.";
        }
        if (usedCards[attacker.getX()][attacker.getY()] == 1) {
            return "Attacker card has already attacked this turn.";
        }
        String result = ((Minion) cardTable.get(attacker.getX()).get(attacker.getY()))
                .abiltyMinion(this, attacker, attacked, playerTurn);
        destroyDeadCards();

        return result;
    }

    /**
     *
     * @param objectMapper used to create obj of type ArrayNode
     * @return all the cards that are marked as frozen on the table at a given point in the game
     */
    public ArrayNode printFrozenCards(final ObjectMapper objectMapper) {
        ArrayNode vectfinal = objectMapper.createArrayNode();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cardTable.get(i).size(); j++) {
                if (frozenCards[i][j] == 1) {
                    vectfinal.addPOJO(cardTable.get(i).get(j).cardToJson(objectMapper));
                }
            }
        }
        return vectfinal;
    }

    /**
     *
     * @param x the row of the card
     * @param y the index in the row
     * @param objectMapper used to create obj of type ObjectNode
     * @return the information of the card positioned at (x,y) on the table
     */
    public ObjectNode printCardAtPosition(final int x,
                                          final int y, final ObjectMapper objectMapper) {
        if (0 <= x && x < rows) {
            if (y >= 0 && y < cardTable.get(x).size()) {
                return cardTable.get(x).get(y).cardToJson(objectMapper);
            }
        }
        return null;
    }

    /**
     *
     * @param objectMapper used to create obj of type ArrayNode
     * @return an ArrayNode that stores all the cards on the table at a given time
     */
    public ArrayNode printTableToJSON(final ObjectMapper objectMapper) {
        ArrayNode vectfinal = objectMapper.createArrayNode();

        for (int i = 0; i < rows; i++) {
            ArrayNode vect = objectMapper.createArrayNode();
            for (int j = 0; j < cardTable.get(i).size(); j++) {
                vect.addPOJO(cardTable.get(i).get(j).cardToJson(objectMapper));

            }
            vectfinal.addPOJO(vect);
        }

        return vectfinal;
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

    public int getRows() {
        return rows;
    }

    public int getCollums() {
        return collums;
    }
}
