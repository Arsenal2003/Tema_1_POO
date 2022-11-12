package Game;

import java.util.ArrayList;

public class Hero extends Card{
    private int health = 30;

    public Hero(int mana, String description, ArrayList<String> colors, String name) {
        super(mana, description, colors, name);
    }

}
