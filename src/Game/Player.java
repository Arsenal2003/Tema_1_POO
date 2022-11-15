package Game;

import java.util.ArrayList;

public class Player {

    private int gamesWon;
    private int mana = 0;
    private ArrayList<Deck> decksPlayer;



    public Player( ArrayList<Deck> decksPlayer) {
        this.decksPlayer = decksPlayer;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }


    public void setMana(int mana) {
        this.mana = mana;
    }


    public int getGamesWon() {
        return gamesWon;
    }



    public int getMana() {
        return mana;
    }

    public ArrayList<Deck> getDecksPlayer() {
        return decksPlayer;
    }

}
