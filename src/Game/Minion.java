package Game;

import java.util.ArrayList;

public class Minion extends Card{

    private int attackDamage;
    private int health;

    public Minion(int mana, String description, ArrayList<String> colors, String name, int attackDamage, int health) {
        super(mana, description, colors, name);
        this.attackDamage = attackDamage;
        this.health = health;
    }
}
