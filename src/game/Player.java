package game;

import java.util.ArrayList;

public final class Player {

    private int gamesWon;
    private int mana = 0;
    private final ArrayList<Deck> decksPlayer;

    public Player(final ArrayList<Deck> decksPlayer) {
        this.decksPlayer = decksPlayer;
    }

    public void setGamesWon(final int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public void setMana(final int mana) {
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
