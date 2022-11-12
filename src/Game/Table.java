package Game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public class Table {
    private ArrayList<ArrayList<Card>> cardTable;

    public Table(ArrayList<ArrayList<Card>> cardTable) {
        this.cardTable = cardTable;
        for(int i=0;i<4;i++)
            this.cardTable.add(new ArrayList<>());
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

        cardTable.get(row).add(deck.getCards().get(poz));
        p.setMana(p.getMana()-deck.getCards().get(poz).getMana());
        deck.getCards().remove(poz);
        deck.setNrCardsInDeck(deck.getNrCardsInDeck()-1);
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
}
