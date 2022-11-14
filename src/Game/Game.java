package Game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Coordinates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Game {
    private Player firstPlayer;
    private Player secondPlayer;
    private int playerOneDeckIdx;
    private int playerTwoDeckIdx;
    private Hero playerOneHero;
    private Hero playerTwoHero;

    private Deck playerOneDeck;
    private Deck playerTwoDeck;
    private Deck playerOneHand;
    private Deck playerTwoHand;

    private Table table;
    private int shuffleSeed;
    private int playerTurn;
    private int turns = 0;
    private int round;

    public int calulateRound(){
        return turns/2+1;
    }
    public Game(Player firstPlayer, Player secondPlayer, int playerOneDeckIdx, int playerTwoDeckIdx, Hero playerOneHero, Hero playerTwoHero, Table table, int shuffleSeed, int startingPlayer) {
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
        for(int i=0;i<firstPlayer.getDecksPlayer().get(0).getNrCardsInDeck();i++) {
            Card newCard;
                if(firstPlayer.getDecksPlayer().get(this.playerOneDeckIdx).getCards().get(i) instanceof Minion)
                    newCard = new Minion((Minion) firstPlayer.getDecksPlayer().get(this.playerOneDeckIdx).getCards().get(i));
                else
                    newCard = new Enviroment(firstPlayer.getDecksPlayer().get(this.playerOneDeckIdx).getCards().get(i));
            playerOneDeck.getCards().add(newCard);
        }

        this.playerTwoDeck = new Deck(secondPlayer.getDecksPlayer().get(0).getNrCardsInDeck());
        for(int i=0;i<secondPlayer.getDecksPlayer().get(0).getNrCardsInDeck();i++) {
            Card newCard;
            if(secondPlayer.getDecksPlayer().get(this.playerTwoDeckIdx).getCards().get(i) instanceof Minion)
                newCard = new Minion((Minion)secondPlayer.getDecksPlayer().get(this.playerTwoDeckIdx).getCards().get(i));
            else
                newCard = new Enviroment(secondPlayer.getDecksPlayer().get(this.playerTwoDeckIdx).getCards().get(i));
            playerTwoDeck.getCards().add(newCard);
        }

        shuffleDeck(playerOneDeck.getCards());
        shuffleDeck(playerTwoDeck.getCards());

        if(calulateRound()!=round){ // inceputul primei runde
            round = calulateRound();
            addManaPlayer(firstPlayer,round);
            addManaPlayer(secondPlayer,round);
            addCardInHand(playerOneHand,playerOneDeck);
            addCardInHand(playerTwoHand,playerTwoDeck);
        }

    }
    public void addManaPlayer(Player p,int mana){
        p.setMana(p.getMana()+Math.min(mana,10));
    }
    public void addCardInHand(Deck playerHand,Deck playerDeck){
        if(playerDeck.getNrCardsInDeck()!=0){
            playerHand.getCards().add(playerDeck.getCards().get(0));
            playerDeck.getCards().remove(0);
            playerHand.setNrCardsInDeck(playerHand.getNrCardsInDeck()+1);
            playerDeck.setNrCardsInDeck(playerDeck.getNrCardsInDeck()-1);
        }

    }
    public int getShuffleSeed() {
        return shuffleSeed;
    }

    public void shuffleDeck(ArrayList<Card> unshuffledDeck){
        Collections.shuffle(unshuffledDeck,new Random(getShuffleSeed()));
    }

    public ObjectNode executeCommand(String name, int handIdx, Coordinates attacker, Coordinates attacked, int affectedrow, int playerIdx, int x, int y){
        ObjectMapper objectMapper = new ObjectMapper();
        switch (name){
            case "getPlayerDeck":return getPlayerDeck(playerIdx,objectMapper);
            case "getPlayerHero":return getPlayerHero(playerIdx,objectMapper);
            case "getPlayerTurn":return getPlayerTurn(objectMapper);
            case "getCardsInHand":return getPlayerHand(playerIdx,objectMapper);
            case "getPlayerMana":return getPlayerMana(playerIdx,objectMapper);
            case "getCardsOnTable":return getCardsOnTable(objectMapper);
            case "getEnvironmentCardsInHand":return getEnvironmentCardsInHand(playerIdx,objectMapper);
            case "getCardAtPosition":return getCardAtPosition(table,x,y,objectMapper);
            case "getFrozenCardsOnTable":return getFrozenCardsOnTable(objectMapper);

            case "cardUsesAttack":return cardUsesAttack(attacker,attacked,playerTurn,objectMapper);
            case "useEnvironmentCard":return useEnvironmentCard(handIdx,affectedrow,objectMapper);
            case "endPlayerTurn":changePlayerTurn();break;
            case "placeCard":return placeCard(handIdx,objectMapper);

           }
        return null;
    }

    private ObjectNode cardUsesAttack(Coordinates attacker, Coordinates attacked, int playerTurn,ObjectMapper objectMapper) {
            String rezultat = table.cardAttack(attacker,attacked,playerTurn);
            System.out.println(rezultat);
            if(rezultat==null)
                return null;
        ObjectNode arrayObject = objectMapper.createObjectNode();
        ObjectNode coordAttacker = objectMapper.createObjectNode();
        ObjectNode coordAttacked = objectMapper.createObjectNode();

        coordAttacker.putPOJO("x",attacker.getX());
        coordAttacker.putPOJO("y",attacker.getY());
        coordAttacked.putPOJO("x",attacked.getX());
        coordAttacked.putPOJO("y",attacked.getY());

        arrayObject.putPOJO("command","cardUsesAttack");
        arrayObject.putPOJO("cardAttacker",coordAttacker);
        arrayObject.putPOJO("cardAttacked",coordAttacked);
        arrayObject.putPOJO("error",rezultat);

        return arrayObject;
    }

    private ObjectNode getFrozenCardsOnTable( ObjectMapper objectMapper) {
        ArrayNode vect = table.printFrozenCards(objectMapper);
        ObjectNode arrayObject = objectMapper.createObjectNode();
        arrayObject.putPOJO("command","getFrozenCardsOnTable");
        arrayObject.putPOJO("output",vect);
        return arrayObject;
    }

    private ObjectNode useEnvironmentCard(int handIdx, int affectedrow, ObjectMapper objectMapper) {
        String rasp;
        if(playerTurn==1)
            rasp = table.playEnvironmentCard(playerOneHand,handIdx,firstPlayer,playerTurn,affectedrow);
        else
            rasp = table.playEnvironmentCard(playerTwoHand,handIdx,secondPlayer,playerTurn,affectedrow);

        if(rasp==null)
            return null;

        ObjectNode arrayObject = objectMapper.createObjectNode();
        arrayObject.putPOJO("command","useEnvironmentCard");
        arrayObject.putPOJO("handIdx",handIdx);
        arrayObject.putPOJO("affectedRow",affectedrow);
        arrayObject.putPOJO("error",rasp);
        return arrayObject;

    }

    private ObjectNode getCardAtPosition(Table table, int x, int y, ObjectMapper objectMapper) {
        ObjectNode arrayObject = objectMapper.createObjectNode();
        arrayObject.putPOJO("command","getCardAtPosition");
        if(table.printCardAtPosition(x,y,objectMapper)==null)
            arrayObject.putPOJO("output","No card at that position.");
        else
            arrayObject.putPOJO("output",table.printCardAtPosition(x,y,objectMapper));
        return arrayObject;
    }

    private ObjectNode getEnvironmentCardsInHand(int playerIdx, ObjectMapper objectMapper) {
        ArrayNode vect = objectMapper.createArrayNode();
        ObjectNode objFinal = objectMapper.createObjectNode();
        if(playerIdx==1){
            for(int i = 0; i< playerOneHand.getNrCardsInDeck(); i++)
                if(playerOneHand.getCards().get(i) instanceof Enviroment){
                    vect.addPOJO(playerOneHand.getCards().get(i).cardToJson(objectMapper));
                }
        }
        if(playerIdx==2){
            for(int i = 0; i< playerTwoHand.getNrCardsInDeck(); i++)
                if(playerTwoHand.getCards().get(i) instanceof Enviroment){
                    vect.addPOJO(playerTwoHand.getCards().get(i).cardToJson(objectMapper));
                }
        }
        objFinal.putPOJO("command","getEnvironmentCardsInHand");
        objFinal.putPOJO("output",vect);
        objFinal.putPOJO("playerIdx",playerIdx);
        return objFinal;
    }

    private ObjectNode getCardsOnTable(ObjectMapper objectMapper) {
        ObjectNode arrayObject = objectMapper.createObjectNode();
        arrayObject.putPOJO("command","getCardsOnTable");
        arrayObject.putPOJO("output",table.printTableToJSON(objectMapper));
        return arrayObject;
    }

    private ObjectNode getPlayerMana(int playerIdx, ObjectMapper objectMapper) {
        ObjectNode arrayObject = objectMapper.createObjectNode();
        arrayObject.putPOJO("command","getPlayerMana");
        arrayObject.putPOJO("playerIdx",playerIdx);
        if(playerIdx==1)
            arrayObject.putPOJO("output",firstPlayer.getMana());
        else
            arrayObject.putPOJO("output",secondPlayer.getMana());
        return arrayObject;
    }

    public ObjectNode placeCard(int handIdx, ObjectMapper objectMapper) {
        String rasp;
        if(playerTurn==1)
            rasp = table.putCardOnTable(playerOneHand,handIdx,firstPlayer,playerTurn);
        else
            rasp = table.putCardOnTable(playerTwoHand,handIdx,secondPlayer,playerTurn);

        if(rasp==null)
            return null;

        ObjectNode arrayObject = objectMapper.createObjectNode();
        arrayObject.putPOJO("command","placeCard");
        arrayObject.putPOJO("handIdx",handIdx);
        arrayObject.putPOJO("error",rasp);
        return arrayObject;
    }

    public ObjectNode getPlayerHand(int playerIdx, ObjectMapper objectMapper) {
        ArrayNode vect = objectMapper.createArrayNode();
        ObjectNode objFinal = objectMapper.createObjectNode();
        if(playerIdx==1){
            printVectorInJson(objectMapper, vect, playerOneHand);
        }
        if(playerIdx==2){
            printVectorInJson(objectMapper, vect, playerTwoHand);
        }
        objFinal.putPOJO("command","getCardsInHand");
        objFinal.putPOJO("playerIdx",playerIdx);
        objFinal.putPOJO("output",vect);
        return objFinal;
    }

    public ObjectNode getPlayerDeck(int playerIdx,ObjectMapper objectMapper){
        ArrayNode vect = objectMapper.createArrayNode();
        ObjectNode objFinal = objectMapper.createObjectNode();
        if(playerIdx==1){
            printVectorInJson(objectMapper, vect, playerOneDeck);
        }
        if(playerIdx==2){
            printVectorInJson(objectMapper, vect, playerTwoDeck);
        }

        objFinal.putPOJO("command","getPlayerDeck");
        objFinal.putPOJO("playerIdx",playerIdx);
        objFinal.putPOJO("output",vect);
        return objFinal;
    }

    private void printVectorInJson(ObjectMapper objectMapper, ArrayNode vect, Deck playerDeck) {
        ObjectNode card;
        for(int i = 0; i< playerDeck.getNrCardsInDeck(); i++){
            if(playerDeck.getCards().get(i) instanceof Minion m){
                card = m.cardToJson(objectMapper);
            }
               else
                 card = playerDeck.getCards().get(i).cardToJson(objectMapper);
                vect.addPOJO(card);
        }
    }

    public ObjectNode getPlayerHero(int playerIdx,ObjectMapper objectMapper){
        ObjectNode objFinal = objectMapper.createObjectNode();
        objFinal.putPOJO("command","getPlayerHero");
        objFinal.putPOJO("playerIdx",playerIdx);
       if(playerIdx==1)
        objFinal.putPOJO("output",playerOneHero.cardToJson(objectMapper));
        else
            objFinal.putPOJO("output",playerTwoHero.cardToJson(objectMapper));



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

    public void setPlayerTwoHero(Hero playerTwoHero) {
        this.playerTwoHero = playerTwoHero;
    }

    public ObjectNode getPlayerTurn(ObjectMapper objectMapper){

        ObjectNode arrayObject = objectMapper.createObjectNode();
        arrayObject.putPOJO("command","getPlayerTurn");
        arrayObject.putPOJO("output",this.playerTurn);
        return arrayObject;
    }
    public void changePlayerTurn(){
        table.deFrozeCards(playerTurn);
        if(this.playerTurn==1)
                this.playerTurn=2;
            else
                this.playerTurn=1;
            this.turns+=1;

        if(calulateRound()!=round){ // atunci cand se sare la urmatoarea runda
            round = calulateRound();
            addManaPlayer(firstPlayer,round);
            addManaPlayer(secondPlayer,round);
            addCardInHand(playerOneHand,playerOneDeck);
            addCardInHand(playerTwoHand,playerTwoDeck);
        }

    }

}
