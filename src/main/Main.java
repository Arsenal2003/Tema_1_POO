package main;

import Game.*;
import checker.Checker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import checker.CheckerConstants;
import fileio.Input;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Input inputData = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + filePath1),
                Input.class);

        ArrayNode output = objectMapper.createArrayNode();

        //TODO add here the entry point to your implementation

        // citire deck pt player 1
        ArrayList<Deck> player1Decks = new ArrayList<>();
        for(int i=0;i<inputData.getPlayerOneDecks().getNrDecks();i++) {
            Deck player1Deck = new Deck(inputData.getPlayerOneDecks().getNrCardsInDeck());
            for (int j = 0; j < inputData.getPlayerOneDecks().getNrCardsInDeck(); j++) {
                String cardType = inputData.getPlayerOneDecks().getDecks().get(i).get(j).getName();
                Card copyCard;
                switch (cardType) {
                    case "Disciple", "The Cursed One", "Miraj", "The Ripper", "Sentinel", "Berserker", "Goliath", "Warden" -> {
                        copyCard = new Minion(inputData.getPlayerOneDecks().getDecks().get(i).get(j).getMana(),
                                inputData.getPlayerOneDecks().getDecks().get(i).get(j).getDescription(),
                                inputData.getPlayerOneDecks().getDecks().get(i).get(j).getColors(),
                                inputData.getPlayerOneDecks().getDecks().get(i).get(j).getName(),
                                inputData.getPlayerOneDecks().getDecks().get(i).get(j).getAttackDamage(),
                                inputData.getPlayerOneDecks().getDecks().get(i).get(j).getHealth());
                        player1Deck.getCards().add(copyCard);

                    }
                    case "Firestorm", "Winterfell", "Heart Hound" -> {
                        copyCard = new Enviroment(inputData.getPlayerOneDecks().getDecks().get(i).get(j).getMana(),
                                inputData.getPlayerOneDecks().getDecks().get(i).get(j).getDescription(),
                                inputData.getPlayerOneDecks().getDecks().get(i).get(j).getColors(),
                                inputData.getPlayerOneDecks().getDecks().get(i).get(j).getName());
                        player1Deck.getCards().add(copyCard);
                    }
                }
            }
            player1Decks.add(player1Deck);
        }
        // citire deck pt player 2
        ArrayList<Deck> player2Decks = new ArrayList<>();
        for(int i=0;i<inputData.getPlayerTwoDecks().getNrDecks();i++) {
            Deck player2Deck = new Deck(inputData.getPlayerTwoDecks().getNrCardsInDeck());
            for (int j = 0; j < inputData.getPlayerTwoDecks().getNrCardsInDeck(); j++) {
                String cardType = inputData.getPlayerTwoDecks().getDecks().get(i).get(j).getName();
                Card copyCard;
                switch (cardType) {
                    case "Disciple", "The Cursed One", "Miraj", "The Ripper", "Sentinel", "Berserker", "Goliath", "Warden" -> {
                        copyCard = new Minion(inputData.getPlayerTwoDecks().getDecks().get(i).get(j).getMana(),
                                inputData.getPlayerTwoDecks().getDecks().get(i).get(j).getDescription(),
                                inputData.getPlayerTwoDecks().getDecks().get(i).get(j).getColors(),
                                inputData.getPlayerTwoDecks().getDecks().get(i).get(j).getName(),
                                inputData.getPlayerTwoDecks().getDecks().get(i).get(j).getAttackDamage(),
                                inputData.getPlayerTwoDecks().getDecks().get(i).get(j).getHealth());
                        player2Deck.getCards().add(copyCard);

                    }
                    case "Firestorm", "Winterfell", "Heart Hound" -> {
                        copyCard = new Enviroment(inputData.getPlayerTwoDecks().getDecks().get(i).get(j).getMana(),
                                inputData.getPlayerTwoDecks().getDecks().get(i).get(j).getDescription(),
                                inputData.getPlayerTwoDecks().getDecks().get(i).get(j).getColors(),
                                inputData.getPlayerTwoDecks().getDecks().get(i).get(j).getName());
                        player2Deck.getCards().add(copyCard);
                    }
                }
            }
            player2Decks.add(player2Deck);
        }

        Player p1 = new Player(player1Decks);
        Player p2 = new Player(player2Decks);
        //System.out.println(p2.getDecksPlayer());

        //  loop-ul de jocuri
           for(int i=0;i<inputData.getGames().size();i++){
               // TODO initializarea unui joc
              Table table = new Table();
              Hero player1Hero = new Hero(inputData.getGames().get(i).getStartGame().getPlayerOneHero().getMana(),
                      inputData.getGames().get(i).getStartGame().getPlayerOneHero().getDescription(),
                      inputData.getGames().get(i).getStartGame().getPlayerOneHero().getColors(),
                      inputData.getGames().get(i).getStartGame().getPlayerOneHero().getName());
               Hero player2Hero = new Hero(inputData.getGames().get(i).getStartGame().getPlayerTwoHero().getMana(),
                       inputData.getGames().get(i).getStartGame().getPlayerTwoHero().getDescription(),
                       inputData.getGames().get(i).getStartGame().getPlayerTwoHero().getColors(),
                       inputData.getGames().get(i).getStartGame().getPlayerTwoHero().getName());

               Game game = new Game(p1,
                       p2,
                       inputData.getGames().get(i).getStartGame().getPlayerOneDeckIdx(),
                       inputData.getGames().get(i).getStartGame().getPlayerTwoDeckIdx(),
                       player1Hero,
                       player2Hero,
                       table,
                       inputData.getGames().get(i).getStartGame().getShuffleSeed(),
                       inputData.getGames().get(i).getStartGame().getStartingPlayer()
                       );

               // TODO loop pt urmarirea actiunilor din jocul respectiv
               for(int j=0;j<inputData.getGames().get(i).getActions().size();j++){
                    System.out.println(inputData.getGames().get(i).getActions().get(j));

               }

           }



        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }
}
