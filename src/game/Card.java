package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public class Card {
    private int mana;
    private String description;
    private ArrayList<String> colors;
    private String name;


    public Card(final Card card) {
        this.mana = card.mana;
        this.description = card.description;
        this.colors = card.colors;
        this.name = card.name;
    }

    public Card(final int mana, final String description,
                final ArrayList<String> colors, final String name) {
        this.mana = mana;
        this.description = description;
        this.colors = colors;
        this.name = name;
    }

    /**
     *
     * @param objectMapper used to create obj of type ObjectNode
     * @return an ObjectNode that stores all the information of the card
     */
    public ObjectNode cardToJson(final ObjectMapper objectMapper) {

        ObjectNode arrayObject = objectMapper.createObjectNode();
        arrayObject.putPOJO("mana", getMana());
        arrayObject.putPOJO("description", getDescription());
        arrayObject.putPOJO("colors", getColors());
        arrayObject.putPOJO("name", getName());

        return arrayObject;
    }

    /**
     *
     * @return the mana necessary to play the card
     */
    public int getMana() {
        return mana;
    }

    /**
     *
     * @return the description of the card
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return a vector of the colors of the card
     */
    public ArrayList<String> getColors() {
        return colors;
    }

    /**
     *
     * @return the name of the card
     */
    public String getName() {
        return name;
    }
}
