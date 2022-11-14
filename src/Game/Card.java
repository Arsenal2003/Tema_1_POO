package Game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public class Card {
    private int mana;
    private String description;
    private ArrayList<String> colors;
    private String name;


    public Card(Card card) {
        this.mana = card.mana;
        this.description = card.description;
        this.colors = card.colors;
        this.name = card.name;
    }

    public Card(int mana, String description, ArrayList<String> colors, String name) {
        this.mana = mana;
        this.description = description;
        this.colors = colors;
        this.name = name;
    }


    public ObjectNode cardToJson(ObjectMapper objectMapper) {

        ObjectNode arrayObject = objectMapper.createObjectNode();
        arrayObject.putPOJO("mana", getMana());
        arrayObject.putPOJO("description", getDescription());
        arrayObject.putPOJO("colors", getColors());
        arrayObject.putPOJO("name", getName());

        return arrayObject;
    }

    public int getMana() {
        return mana;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public String getName() {
        return name;
    }
}
