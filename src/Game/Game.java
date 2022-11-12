package Game;

public class Game {
    private Player firstPlayer;
    private Player secondPlayer;
    private int playerOneDeckIdx;
    private int playerTwoDeckIdx;
    private Hero playerOneHero;
    private Hero playerTwoHero;

    private Deck playerOneDeck;
    private Deck playerTwoDeck;
    private Deck playerOneHand;
    private Deck playerTwoHand;

    private Table table;
    private int shuffleSeed;
    private int startingPlayer;

    public Game(Player firstPlayer, Player secondPlayer, int playerOneDeckIdx, int playerTwoDeckIdx, Hero playerOneHero, Hero playerTwoHero, Table table, int shuffleSeed, int startingPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.playerOneDeckIdx = playerOneDeckIdx;
        this.playerTwoDeckIdx = playerTwoDeckIdx;
        this.playerOneHero = playerOneHero;
        this.playerTwoHero = playerTwoHero;
        this.table = table;
        this.shuffleSeed = shuffleSeed;
        this.startingPlayer = startingPlayer;
    }


}
