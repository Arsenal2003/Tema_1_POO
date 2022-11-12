package Game;

import java.util.ArrayList;

public class Player {

    private int gamesWon;
    private int gamesPlayed;
    private int mana;
    private ArrayList<Deck> decksPlayer;



    public Player( ArrayList<Deck> decksPlayer) {
        this.decksPlayer = decksPlayer;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }


    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getMana() {
        return mana;
    }

    public ArrayList<Deck> getDecksPlayer() {
        return decksPlayer;
    }

    public void setDecksPlayer(ArrayList<Deck> decksPlayer) {
        this.decksPlayer = decksPlayer;
    }

}
