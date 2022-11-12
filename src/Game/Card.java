package Game;

import java.util.ArrayList;

public class Card {
        private int mana;
        private String description;
        private ArrayList<String> colors;
        private String name;



        public Card(int mana, String description, ArrayList<String> colors, String name) {
                this.mana = mana;
                this.description = description;
                this.colors = colors;
                this.name = name;
        }

        public void cardSpecialAbility(Table table,int row){



        }

}
