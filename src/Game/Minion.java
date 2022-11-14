package Game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public class Minion extends Card{

    private int attackDamage;
    private int health;
    private int isTank = 0;

    public Minion(Minion minion) {
        super(minion);
        this.attackDamage = minion.attackDamage;
        this.health = minion.health;
        setIsTank();
    }

    public Minion(int mana, String description, ArrayList<String> colors, String name, int attackDamage, int health) {
        super(mana, description, colors, name);
        this.attackDamage = attackDamage;
        this.health = health;
        setIsTank();
    }
    @Override
    public ObjectNode cardToJson(ObjectMapper objectMapper){

        ObjectNode arrayObject = objectMapper.createObjectNode();
        arrayObject.putPOJO("mana",getMana());
        arrayObject.putPOJO("attackDamage",getAttackDamage());
        arrayObject.putPOJO("health",getHealth());
        arrayObject.putPOJO("description",getDescription());
        arrayObject.putPOJO("colors",getColors());
        arrayObject.putPOJO("name",getName());

        return arrayObject;
    }

    public boolean cardIsFrontRow(){
        if(getName().equals("The Ripper") || getName().equals("Miraj") || getName().equals("Goliath") || getName().equals("Warden"))
            return true;
        else
            return false;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getIsTank() {
        return isTank;
    }

    public void setIsTank() {
        if(super.getName().equals("Goliath") || super.getName().equals("Warden"))
            this.isTank =1;
    }
}
