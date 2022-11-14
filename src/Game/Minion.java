package Game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Coordinates;

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

    public void setAttackDamage(int attackDamage) {
        if(attackDamage>0)
            this.attackDamage = attackDamage;
        else
            this.attackDamage = 0;
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
        if(getName().equals("Goliath") || getName().equals("Warden"))
            this.isTank =1;
    }

    public String abiltyMinion(Table table, Coordinates attacker, Coordinates attacked, int playerTurn) {
        if (getName().equals("The Ripper"))
            return TheRipperAbility(table, attacker, attacked, playerTurn);
        if (getName().equals("Miraj"))
            return MirajlAbility(table, attacker, attacked, playerTurn);
        if (getName().equals("The Cursed One"))
            return TheCursedOneAbility(table, attacker, attacked, playerTurn);
        if (getName().equals("Disciple"))
            return DiscipleAbility(table, attacker, attacked, playerTurn);

        return null;
    }

    private String DiscipleAbility(Table table, Coordinates attacker, Coordinates attacked, int playerTurn) {
        if(playerTurn==1){
            if(attacked.getX()==1 || attacked.getX()==0 )
                return "Attacked card does not belong to the current player.";
        }else{
            if(attacked.getX()==2 || attacked.getX()==3 )
                return "Attacked card does not belong to the current player.";
        }
        ((Minion)table.getCardTable().get(attacked.getX()).get(attacked.getY())).setHealth(((Minion)table.getCardTable().get(attacked.getX()).get(attacked.getY())).getHealth()+2);
        table.getUsedCards()[attacker.getX()][attacker.getY()]=1;
        return null;
    }

    private String TheCursedOneAbility(Table table, Coordinates attacker, Coordinates attacked, int playerTurn) {
        if(playerTurn==1){
            if(attacked.getX()==3 || attacked.getX()==2 )
                return "Attacked card does not belong to the enemy.";
        }else {
            if (attacked.getX() == 1 || attacked.getX() == 0)
                return "Attacked card does not belong to the enemy.";
        }
        if(table.existsTank(playerTurn) && ((Minion)table.getCardTable().get(attacked.getX()).get(attacked.getY())).getIsTank()==0)
            return "Attacked card is not of type 'Tank'.";
        int copieViata = ((Minion)table.getCardTable().get(attacked.getX()).get(attacked.getY())).getHealth();
        ((Minion)table.getCardTable().get(attacked.getX()).get(attacked.getY())).setHealth(((Minion)table.getCardTable().get(attacked.getX()).get(attacked.getY())).getAttackDamage());
        ((Minion)table.getCardTable().get(attacked.getX()).get(attacked.getY())).setAttackDamage(copieViata);
        table.getUsedCards()[attacker.getX()][attacker.getY()]=1;
        return null;
    }

    private String MirajlAbility(Table table, Coordinates attacker, Coordinates attacked, int playerTurn) {
        if(playerTurn==1){
            if(attacked.getX()==3 || attacked.getX()==2 )
                return "Attacked card does not belong to the enemy.";
        }else {
            if (attacked.getX() == 1 || attacked.getX() == 0)
                return "Attacked card does not belong to the enemy.";
        }
        if(table.existsTank(playerTurn) && ((Minion)table.getCardTable().get(attacked.getX()).get(attacked.getY())).getIsTank()==0)
            return "Attacked card is not of type 'Tank'.";
        int copieViata = ((Minion)table.getCardTable().get(attacked.getX()).get(attacked.getY())).getHealth();
        ((Minion)table.getCardTable().get(attacked.getX()).get(attacked.getY())).setHealth(((Minion)table.getCardTable().get(attacker.getX()).get(attacker.getY())).getHealth());
        ((Minion)table.getCardTable().get(attacker.getX()).get(attacker.getY())).setHealth(copieViata);
        table.getUsedCards()[attacker.getX()][attacker.getY()]=1;
        return null;
    }

    private String TheRipperAbility(Table table, Coordinates attacker, Coordinates attacked, int playerTurn) {
        if(playerTurn==1){
            if(attacked.getX()==3 || attacked.getX()==2 )
                return "Attacked card does not belong to the enemy.";
        }else {
            if (attacked.getX() == 1 || attacked.getX() == 0)
                return "Attacked card does not belong to the enemy.";
        }
        if(table.existsTank(playerTurn) && ((Minion)table.getCardTable().get(attacked.getX()).get(attacked.getY())).getIsTank()==0)
            return "Attacked card is not of type 'Tank'.";
        ((Minion)table.getCardTable().get(attacked.getX()).get(attacked.getY())).setAttackDamage(((Minion)table.getCardTable().get(attacked.getX()).get(attacked.getY())).getAttackDamage()-2);
        table.getUsedCards()[attacker.getX()][attacker.getY()]=1;
        return null;
    }
}
