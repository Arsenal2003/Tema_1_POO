package Game;

import java.util.ArrayList;

public class Enviroment extends Card{

    public Enviroment(Card enviroment){
        super(enviroment);
    }
    public Enviroment(int mana, String description, ArrayList<String> colors, String name) {
        super(mana, description, colors, name);
    }
}