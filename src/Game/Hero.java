package Game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public class Hero extends Card{
    private int health = 30;

    public Hero(int mana, String description, ArrayList<String> colors, String name) {
        super(mana, description, colors, name);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public ObjectNode cardToJson(ObjectMapper objectMapper){

        ObjectNode arrayObject = objectMapper.createObjectNode();
        arrayObject.putPOJO("mana",getMana());
        arrayObject.putPOJO("description",getDescription());
        arrayObject.putPOJO("colors",getColors());
        arrayObject.putPOJO("name",getName());
        arrayObject.putPOJO("health",getHealth());

        return arrayObject;
    }
}
