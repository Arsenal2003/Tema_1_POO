package Game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Coordinates;

import java.util.ArrayList;

public class Table {
    private ArrayList<ArrayList<Card>> cardTable;
    private ArrayList<ArrayList<Integer>> frozenCards = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> usedCards = new ArrayList<>();

    public Table(ArrayList<ArrayList<Card>> cardTable) {
        this.cardTable = cardTable;
        for(int i=0;i<4;i++){
            this.cardTable.add(new ArrayList<>());
            this.frozenCards.add(new ArrayList<>());
            this.usedCards.add(new ArrayList<>());
        }

    }
    public String putCardOnTable(Deck deck, int poz,Player p, int playerIdx){
        if(deck.getCards().get(poz) instanceof Enviroment)
                return "Cannot place environment card on table.";
        if(p.getMana()-deck.getCards().get(poz).getMana()<0)
            return "Not enough mana to place card on table.";
        Minion m = (Minion) deck.getCards().get(poz);

        if(m.cardIsFrontRow()){
            if(playerIdx==1)
                return playCard(deck,poz,p,2);
            else
                return playCard(deck,poz,p,1);
        } else{
            if(playerIdx==1)
                return playCard(deck,poz,p,3);
            else
                return playCard(deck,poz,p,0);

        }

    }
    private String playCard(Deck deck, int poz,Player p,int row) {
        if(cardTable.get(row).size()==5)
            return "Cannot place card on table since row is full.";
        frozenCards.get(row).add(0);
        usedCards.get(row).add(0);
        cardTable.get(row).add(deck.getCards().get(poz));
        p.setMana(p.getMana()-deck.getCards().get(poz).getMana());
        deck.getCards().remove(poz);
        deck.setNrCardsInDeck(deck.getNrCardsInDeck()-1);
        return null;
    }
    public String playEnvironmentCard(Deck deck, int poz,Player p,int playerIdx,int row) {
        if(!(deck.getCards().get(poz) instanceof Enviroment))
            return "Chosen card is not of type environment.";
        if(p.getMana()-deck.getCards().get(poz).getMana()<0)
            return "Not enough mana to use environment card.";
        if( (playerIdx==1 && (row==2 ||row ==3)) || (playerIdx==2 && (row==0 ||row ==1)))
            return "Chosen row does not belong to the enemy.";

        return ((Enviroment) deck.getCards().get(poz)).abiltyCard(this,deck,poz,p,row);
    }
    public void destroyDeadCards(){
        for(int i=0;i<4;i++)
            for(int j=0;j<cardTable.get(i).size();j++)
                if(((Minion)cardTable.get(i).get(j)).getHealth()<=0){
                    cardTable.get(i).remove(j);
                    frozenCards.get(i).remove(j);
                    usedCards.get(i).remove(j);
                    j--;
                }

    }
    public void deFrozeCards(int player){
        if(player==1){
            //frozenCards.get(3).forEach((n)->n=0);
            //frozenCards.get(2).forEach((n)->n=0);
            //usedCards.get(3).forEach((n)->n=0);
            //usedCards.get(2).forEach((n)->n=0);

        }
            else{
            //usedCards.get(0).forEach((n)->n=0);
            //usedCards.get(1).forEach((n)->n=0);
            //frozenCards.get(0).forEach((n)->n=0);
            //frozenCards.get(1).forEach((n)->n=0);

        }

    }

    public boolean existsTank(int player){
        if(player==1){
            for(int i=1;i<cardTable.get(2).size();i++)
                if(((Minion)cardTable.get(2).get(i)).getIsTank() ==1)
                    return true;

        }else{
            for(int i=1;i<cardTable.get(1).size();i++)
                if(((Minion)cardTable.get(1).get(i)).getIsTank() ==1)
                    return true;

        }
        return false;
    }

    public String cardAttack(Coordinates attacker, Coordinates attacked, int playerTurn) {
        if(playerTurn==1){
            if(attacked.getX()==3 || attacked.getX()==2 )
                return "Attacked card does not belong to the enemy.";
        }else{
            if(attacked.getX()==1 || attacked.getX()==0 )
                return "Attacked card does not belong to the enemy.";
        }
        System.out.println(frozenCards);
        System.out.println(attacker);

        if(usedCards.get(attacker.getX()).get(attacker.getY()) == 1)
                return "Attacker card has already attacked this turn.";
        if(frozenCards.get(attacker.getX()).get(attacker.getY()) == 1)
            return "Attacker card is frozen.";
        if(existsTank(3-playerTurn))
            return "Attacked card is not of type 'Tank’.";

        ((Minion)cardTable.get(attacked.getX()).get(attacked.getY())).setHealth(((Minion)cardTable.get(attacked.getX()).get(attacked.getY())).getHealth()-((Minion)cardTable.get(attacker.getX()).get(attacker.getY())).getAttackDamage());
        usedCards.get(attacker.getX()).set(usedCards.get(attacker.getX()).get(attacker.getY()),1);
        destroyDeadCards();

        return null;
    }
    public ArrayNode printFrozenCards(ObjectMapper objectMapper){
        ArrayNode vectfinal = objectMapper.createArrayNode();

        for(int i=0;i<4;i++) {
            for (int j = 0; j < cardTable.get(i).size(); j++) {
                if(frozenCards.get(i).get(j)==1)
                vectfinal.addPOJO(cardTable.get(i).get(j).cardToJson(objectMapper));

            }

        }

        return vectfinal;
    }
    public ObjectNode printCardAtPosition(int x,int y,ObjectMapper objectMapper){
        if(0 <= x && x < 4)
            if(y>=0 && y<cardTable.get(x).size())
                return cardTable.get(x).get(y).cardToJson(objectMapper);
        return null;
    }
    public ArrayNode printTableToJSON(ObjectMapper objectMapper){
        ArrayNode vectfinal = objectMapper.createArrayNode();

        for(int i=0;i<4;i++) {
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

    public ArrayList<ArrayList<Integer>> getFrozenCards() {
        return frozenCards;
    }


}
