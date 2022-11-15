package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Coordinates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public final class Game {
    private Player firstPlayer;
    private Player secondPlayer;
    private int playerOneDeckIdx;
    private int playerTwoDeckIdx;
    private Hero playerOneHero;
    private Hero playerTwoHero;
    private int playerOneHeroAttacked = 0;
    private int playerTwoHeroAttacked = 0;
    private Deck playerOneDeck;
    private Deck playerTwoDeck;
    private Deck playerOneHand;
    private Deck playerTwoHand;

    private Table table;
    private int shuffleSeed;
    private int playerTurn;
    private int turns = 0;
    private int round;
    private int gameEnded = 0;

    /**
     *
     * @return the round number that we are currently in
     */
    public int calulateRound() {
        return turns / 2 + 1;
    }

    public Game(final Player firstPlayer, final Player secondPlayer,
                 final int playerOneDeckIdx, final int playerTwoDeckIdx,
                final Hero playerOneHero, final Hero playerTwoHero,
                final Table table, final int shuffleSeed, final int startingPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.playerOneDeckIdx = playerOneDeckIdx;
        this.playerTwoDeckIdx = playerTwoDeckIdx;
        this.playerOneHero = playerOneHero;
        this.playerTwoHero = playerTwoHero;
        this.table = table;
        this.shuffleSeed = shuffleSeed;
        this.playerTurn = startingPlayer;
        this.playerOneHand = new Deck(0);
        this.playerTwoHand = new Deck(0);


        this.playerOneDeck = new Deck(firstPlayer.getDecksPlayer().get(0).getNrCardsInDeck());
        for (int i = 0; i < firstPlayer.getDecksPlayer().get(0).getNrCardsInDeck(); i++) {
            Card newCard;
            if (firstPlayer.getDecksPlayer()
                    .get(this.playerOneDeckIdx).getCards().get(i) instanceof Minion) {
                newCard = new Minion((Minion) firstPlayer.getDecksPlayer()
                        .get(this.playerOneDeckIdx).getCards().get(i));
            } else {
                newCard = new Environment(firstPlayer.getDecksPlayer()
                        .get(this.playerOneDeckIdx).getCards().get(i));
            }
            playerOneDeck.getCards().add(newCard);
        }

        this.playerTwoDeck = new Deck(secondPlayer
                .getDecksPlayer().get(0).getNrCardsInDeck());
        for (int i = 0; i < secondPlayer.getDecksPlayer().get(0).getNrCardsInDeck(); i++) {
            Card newCard;
            if (secondPlayer.getDecksPlayer()
                    .get(this.playerTwoDeckIdx).getCards().get(i) instanceof Minion) {
                newCard = new Minion((Minion) secondPlayer
                        .getDecksPlayer().get(this.playerTwoDeckIdx).getCards().get(i));
            } else {
                newCard = new Environment(
                        secondPlayer.getDecksPlayer()
                                .get(this.playerTwoDeckIdx).getCards().get(i));
            }
            playerTwoDeck.getCards().add(newCard);
        }

        shuffleDeck(playerOneDeck.getCards());
        shuffleDeck(playerTwoDeck.getCards());

        if (calulateRound() != round) { // the start of the first round
            round = calulateRound();
            addManaPlayer(firstPlayer, round);
            addManaPlayer(secondPlayer, round);
            addCardInHand(playerOneHand, playerOneDeck);
            addCardInHand(playerTwoHand, playerTwoDeck);
        }

    }

    /**
     *
     * @param p the player
     * @param mana the number of mana to add
     */
    public void addManaPlayer(final Player p, final int mana) {
        p.setMana(p.getMana() + Math.min(mana, 10));
    }

    /**
     * adds a card from deck in hand
     * @param playerHand the players hand
     * @param playerDeck the players deck
     */
    public void addCardInHand(final Deck playerHand, final Deck playerDeck) {
        if (playerDeck.getNrCardsInDeck() != 0) {
            playerHand.getCards().add(playerDeck.getCards().get(0));
            playerDeck.getCards().remove(0);
            playerHand.setNrCardsInDeck(playerHand.getNrCardsInDeck() + 1);
            playerDeck.setNrCardsInDeck(playerDeck.getNrCardsInDeck() - 1);
        }

    }

    /**
     *
     * @return the shuffle seed necessary to shuffle the decks
     */
    public int getShuffleSeed() {
        return shuffleSeed;
    }

    /**
     * returns a shuffled deck shuffled with the shuffle seed
     * @param unshuffledDeck the un shuffled deck
     */
    public void shuffleDeck(final ArrayList<Card> unshuffledDeck) {
        Collections.shuffle(unshuffledDeck, new Random(getShuffleSeed()));
    }

    /**
     * a function that maps the name of the command with the actual function implementation
     * @param name the name of the command
     * @param handIdx the position where the card is in the hand of the player
     * @param attacker the card that attacks
     * @param attacked the card that is attacked
     * @param affectedrow the row that is effected by the spell
     * @param playerIdx the player Index
     * @param x the row on the table
     * @param y the index in the row on the table
     * @return an object node that contains the data that is added in the .json file
     */
    public ObjectNode executeCommand(final String name, final int handIdx,
                                     final Coordinates attacker,
                                     final Coordinates attacked,
                                     final int affectedrow,
                                     final int playerIdx,
                                     final int x, final int y) {
        ObjectMapper objectMapper = new ObjectMapper();
        switch (name) {
            case "getPlayerDeck":
                return getPlayerDeck(playerIdx, objectMapper);
            case "getPlayerHero":
                return getPlayerHero(playerIdx, objectMapper);
            case "getPlayerTurn":
                return getPlayerTurn(objectMapper);
            case "getCardsInHand":
                return getPlayerHand(playerIdx, objectMapper);
            case "getPlayerMana":
                return getPlayerMana(playerIdx, objectMapper);
            case "getCardsOnTable":
                return getCardsOnTable(objectMapper);
            case "getEnvironmentCardsInHand":
                return getEnvironmentCardsInHand(playerIdx, objectMapper);
            case "getCardAtPosition":
                return getCardAtPosition(table, x, y, objectMapper);
            case "getFrozenCardsOnTable":
                return getFrozenCardsOnTable(objectMapper);
            case "getPlayerOneWins":
                return getPlayerOneWins(objectMapper);
            case "getPlayerTwoWins":
                return getPlayerTwoWins(objectMapper);
            case "getTotalGamesPlayed":
                return getTotalGamesPlayed(objectMapper);
            case "useHeroAbility":
                return abilityHero(affectedrow, objectMapper);
            case "useAttackHero":
                return attackHero(attacker, playerTurn, objectMapper);
            case "cardUsesAbility":
                return cardUsesAbility(attacker, attacked, playerTurn, objectMapper);
            case "cardUsesAttack":
                return cardUsesAttack(attacker, attacked, playerTurn, objectMapper);
            case "useEnvironmentCard":
                return useEnvironmentCard(handIdx, affectedrow, objectMapper);
            case "endPlayerTurn":
                changePlayerTurn();
                break;
            case "placeCard":
                return placeCard(handIdx, objectMapper);
            default:break;
        }
        return null;
    }

    private ObjectNode getPlayerOneWins(final ObjectMapper objectMapper) {
        ObjectNode arrayObject = objectMapper.createObjectNode();
        arrayObject.putPOJO("command", "getPlayerOneWins");
        arrayObject.putPOJO("output", firstPlayer.getGamesWon());
        return arrayObject;
    }

    private ObjectNode getPlayerTwoWins(final ObjectMapper objectMapper) {
        ObjectNode arrayObject = objectMapper.createObjectNode();
        arrayObject.putPOJO("command", "getPlayerTwoWins");
        arrayObject.putPOJO("output", secondPlayer.getGamesWon());
        return arrayObject;
    }

    private ObjectNode getTotalGamesPlayed(final ObjectMapper objectMapper) {
        ObjectNode arrayObject = objectMapper.createObjectNode();
        arrayObject.putPOJO("command", "getTotalGamesPlayed");
        arrayObject.putPOJO("output", firstPlayer.getGamesWon() + secondPlayer.getGamesWon());
        return arrayObject;
    }

    private ObjectNode abilityHero(final int affectedrow, final ObjectMapper objectMapper) {
        String error;
        if (playerTurn == 1) {
            error = table.abilityHeroFirstPlayer(affectedrow, this);
        } else {
            error = table.abilityHeroSecondPlayer(affectedrow, this);
        }

        if (error == null) {
            return null;
        }
        ObjectNode arrayObject = objectMapper.createObjectNode();
        arrayObject.putPOJO("command", "useHeroAbility");
        arrayObject.putPOJO("affectedRow", affectedrow);
        arrayObject.putPOJO("error", error);
        return arrayObject;
    }

    private ObjectNode attackHero(final Coordinates attacker,
                                  final int playerTurn, final ObjectMapper objectMapper) {
        String error = table.attackHero(attacker, playerTurn, this);
        if (error == null) {
            return null;
        }
        ObjectNode arrayObject = objectMapper.createObjectNode();
        ObjectNode coordAttacker = objectMapper.createObjectNode();

        coordAttacker.putPOJO("x", attacker.getX());
        coordAttacker.putPOJO("y", attacker.getY());
        arrayObject.putPOJO("command", "useAttackHero");
        arrayObject.putPOJO("cardAttacker", coordAttacker);
        arrayObject.putPOJO("error", error);
        return arrayObject;
    }

    private ObjectNode cardUsesAbility(final Coordinates attacker, final Coordinates attacked, final int playerTurn, final ObjectMapper objectMapper) {
        String error = table.cardAbility(attacker, attacked, playerTurn);
        if (error == null) {
            return null;
        }
        ObjectNode arrayObject = objectMapper.createObjectNode();
        ObjectNode coordAttacker = objectMapper.createObjectNode();
        ObjectNode coordAttacked = objectMapper.createObjectNode();

        coordAttacker.putPOJO("x", attacker.getX());
        coordAttacker.putPOJO("y", attacker.getY());
        coordAttacked.putPOJO("x", attacked.getX());
        coordAttacked.putPOJO("y", attacked.getY());

        arrayObject.putPOJO("command", "cardUsesAbility");
        arrayObject.putPOJO("cardAttacker", coordAttacker);
        arrayObject.putPOJO("cardAttacked", coordAttacked);
        arrayObject.putPOJO("error", error);

        return arrayObject;
    }

    private ObjectNode cardUsesAttack(final Coordinates attacker, final Coordinates attacked, final int playerTurn, final ObjectMapper objectMapper) {
        String error = table.cardAttack(attacker, attacked, playerTurn);
        if (error == null) {
            return null;
        }
        ObjectNode arrayObject = objectMapper.createObjectNode();
        ObjectNode coordAttacker = objectMapper.createObjectNode();
        ObjectNode coordAttacked = objectMapper.createObjectNode();

        coordAttacker.putPOJO("x", attacker.getX());
        coordAttacker.putPOJO("y", attacker.getY());
        coordAttacked.putPOJO("x", attacked.getX());
        coordAttacked.putPOJO("y", attacked.getY());

        arrayObject.putPOJO("command", "cardUsesAttack");
        arrayObject.putPOJO("cardAttacker", coordAttacker);
        arrayObject.putPOJO("cardAttacked", coordAttacked);
        arrayObject.putPOJO("error", error);

        return arrayObject;
    }

    private ObjectNode getFrozenCardsOnTable(final ObjectMapper objectMapper) {
        ArrayNode vect = table.printFrozenCards(objectMapper);
        ObjectNode arrayObject = objectMapper.createObjectNode();
        arrayObject.putPOJO("command", "getFrozenCardsOnTable");
        arrayObject.putPOJO("output", vect);
        return arrayObject;
    }

    private ObjectNode useEnvironmentCard(final int handIdx, final int affectedrow, final ObjectMapper objectMapper) {
        String rasp;
        if (playerTurn == 1) {
            rasp = table.playEnvironmentCard(playerOneHand, handIdx, firstPlayer, playerTurn, affectedrow);
        } else {
            rasp = table.playEnvironmentCard(playerTwoHand, handIdx, secondPlayer, playerTurn, affectedrow);
        }

        if (rasp == null) {
            return null;
        }

        ObjectNode arrayObject = objectMapper.createObjectNode();
        arrayObject.putPOJO("command", "useEnvironmentCard");
        arrayObject.putPOJO("handIdx", handIdx);
        arrayObject.putPOJO("affectedRow", affectedrow);
        arrayObject.putPOJO("error", rasp);
        return arrayObject;

    }

    private ObjectNode getCardAtPosition(final Table table, final int x, final int y, final ObjectMapper objectMapper) {
        ObjectNode arrayObject = objectMapper.createObjectNode();
        arrayObject.putPOJO("command", "getCardAtPosition");
        if (table.printCardAtPosition(x, y, objectMapper) == null) {
            arrayObject.putPOJO("output", "No card available at that position.");
        } else {
            arrayObject.putPOJO("output", table.printCardAtPosition(x, y, objectMapper));
        }
        arrayObject.putPOJO("x", x);
        arrayObject.putPOJO("y", y);
        return arrayObject;
    }

    private ObjectNode getEnvironmentCardsInHand(final int playerIdx, final ObjectMapper objectMapper) {
        ArrayNode vect = objectMapper.createArrayNode();
        ObjectNode objFinal = objectMapper.createObjectNode();
        if (playerIdx == 1) {
            for (int i = 0; i < playerOneHand.getNrCardsInDeck(); i++) {
                if (playerOneHand.getCards().get(i) instanceof Environment) {
                    vect.addPOJO(playerOneHand.getCards().get(i).cardToJson(objectMapper));
                }
            }
        }
        if (playerIdx == 2) {
            for (int i = 0; i < playerTwoHand.getNrCardsInDeck(); i++) {
                if (playerTwoHand.getCards().get(i) instanceof Environment) {
                    vect.addPOJO(playerTwoHand.getCards().get(i).cardToJson(objectMapper));
                }
            }
        }
        objFinal.putPOJO("command", "getEnvironmentCardsInHand");
        objFinal.putPOJO("output", vect);
        objFinal.putPOJO("playerIdx", playerIdx);
        return objFinal;
    }

    private ObjectNode getCardsOnTable(final ObjectMapper objectMapper) {
        ObjectNode arrayObject = objectMapper.createObjectNode();
        arrayObject.putPOJO("command", "getCardsOnTable");
        arrayObject.putPOJO("output", table.printTableToJSON(objectMapper));
        return arrayObject;
    }

    private ObjectNode getPlayerMana(final int playerIdx, final ObjectMapper objectMapper) {
        ObjectNode arrayObject = objectMapper.createObjectNode();
        arrayObject.putPOJO("command", "getPlayerMana");
        arrayObject.putPOJO("playerIdx", playerIdx);
        if (playerIdx == 1) {
            arrayObject.putPOJO("output", firstPlayer.getMana());
        } else {
            arrayObject.putPOJO("output", secondPlayer.getMana());
        }
        return arrayObject;
    }

    private ObjectNode placeCard(final int handIdx, final ObjectMapper objectMapper) {
        String rasp;
        if (playerTurn == 1) {
            rasp = table.putCardOnTable(playerOneHand, handIdx, firstPlayer, playerTurn);
        } else {
            rasp = table.putCardOnTable(playerTwoHand, handIdx, secondPlayer, playerTurn);
        }

        if (rasp == null) {
            return null;
        }

        ObjectNode arrayObject = objectMapper.createObjectNode();
        arrayObject.putPOJO("command", "placeCard");
        arrayObject.putPOJO("handIdx", handIdx);
        arrayObject.putPOJO("error", rasp);
        return arrayObject;
    }

    private ObjectNode getPlayerHand(final int playerIdx, final ObjectMapper objectMapper) {
        ArrayNode vect = objectMapper.createArrayNode();
        ObjectNode objFinal = objectMapper.createObjectNode();
        if (playerIdx == 1) {
            printVectorInJson(objectMapper, vect, playerOneHand);
        }
        if (playerIdx == 2) {
            printVectorInJson(objectMapper, vect, playerTwoHand);
        }
        objFinal.putPOJO("command", "getCardsInHand");
        objFinal.putPOJO("playerIdx", playerIdx);
        objFinal.putPOJO("output", vect);
        return objFinal;
    }

    private ObjectNode getPlayerDeck(final int playerIdx, final ObjectMapper objectMapper) {
        ArrayNode vect = objectMapper.createArrayNode();
        ObjectNode objFinal = objectMapper.createObjectNode();
        if (playerIdx == 1) {
            printVectorInJson(objectMapper, vect, playerOneDeck);
        }
        if (playerIdx == 2) {
            printVectorInJson(objectMapper, vect, playerTwoDeck);
        }

        objFinal.putPOJO("command", "getPlayerDeck");
        objFinal.putPOJO("playerIdx", playerIdx);
        objFinal.putPOJO("output", vect);
        return objFinal;
    }

    private void printVectorInJson(final ObjectMapper objectMapper, final ArrayNode vect, final Deck playerDeck) {
        ObjectNode card;
        for (int i = 0; i < playerDeck.getNrCardsInDeck(); i++) {
            if (playerDeck.getCards().get(i) instanceof Minion m) {
                card = m.cardToJson(objectMapper);
            } else {
                card = playerDeck.getCards().get(i).cardToJson(objectMapper);
            }
            vect.addPOJO(card);
        }
    }

    private ObjectNode getPlayerHero(final int playerIdx, final ObjectMapper objectMapper) {
        ObjectNode objFinal = objectMapper.createObjectNode();
        objFinal.putPOJO("command", "getPlayerHero");
        objFinal.putPOJO("playerIdx", playerIdx);
        if (playerIdx == 1) {
            objFinal.putPOJO("output", playerOneHero.cardToJson(objectMapper));
        } else {
            objFinal.putPOJO("output", playerTwoHero.cardToJson(objectMapper));
        }
        return objFinal;
    }

    public Hero getPlayerOneHero() {
        return playerOneHero;
    }

    public void setPlayerOneHero(Hero playerOneHero) {
        this.playerOneHero = playerOneHero;
    }

    public Hero getPlayerTwoHero() {
        return playerTwoHero;
    }

    public void setPlayerTwoHero(final Hero playerTwoHero) {
        this.playerTwoHero = playerTwoHero;
    }

    private ObjectNode getPlayerTurn(final ObjectMapper objectMapper) {

        ObjectNode arrayObject = objectMapper.createObjectNode();
        arrayObject.putPOJO("command", "getPlayerTurn");
        arrayObject.putPOJO("output", this.playerTurn);
        return arrayObject;
    }

    /**
     * function that changes the current player
     * turn from 1 to 2 and back around
     */
    public void changePlayerTurn() {
        table.deFrozeCards(playerTurn);

        if (playerTurn == 1) {
            playerOneHeroAttacked = 0;
            playerTurn = 2;
        } else {
            playerTwoHeroAttacked = 0;
            playerTurn = 1;
        }
        turns += 1;

        if (calulateRound() != round) { // jump to next round
            round = calulateRound();
            addManaPlayer(firstPlayer, round);
            addManaPlayer(secondPlayer, round);
            addCardInHand(playerOneHand, playerOneDeck);
            addCardInHand(playerTwoHand, playerTwoDeck);
        }

    }

    public int getGameEnded() {
        return gameEnded;
    }

    public void setGameEnded(final int gameEnded) {
        this.gameEnded = gameEnded;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }


    public Player getSecondPlayer() {
        return secondPlayer;
    }


    public int getPlayerOneHeroAttacked() {
        return playerOneHeroAttacked;
    }

    public int getPlayerTwoHeroAttacked() {
        return playerTwoHeroAttacked;
    }

    public void setPlayerOneHeroAttacked(final int playerOneHeroAttacked) {
        this.playerOneHeroAttacked = playerOneHeroAttacked;
    }

    public void setPlayerTwoHeroAttacked(final int playerTwoHeroAttacked) {
        this.playerTwoHeroAttacked = playerTwoHeroAttacked;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }
}
