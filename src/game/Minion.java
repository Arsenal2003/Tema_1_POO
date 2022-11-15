package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Coordinates;

import java.util.ArrayList;

public class Minion extends Card {

    private int attackDamage;
    private int health;
    private int isTank = 0;

    public Minion(final Minion minion) {
        super(minion);
        this.attackDamage = minion.attackDamage;
        this.health = minion.health;
        setIsTank();
    }

    public Minion(final int mana, final String description,
                  final ArrayList<String> colors, final String name,
                  final int attackDamage, final int health) {
        super(mana, description, colors, name);
        this.attackDamage = attackDamage;
        this.health = health;
        setIsTank();
    }

    @Override
    public final ObjectNode cardToJson(final ObjectMapper objectMapper) {

        ObjectNode arrayObject = objectMapper.createObjectNode();
        arrayObject.putPOJO("mana", getMana());
        arrayObject.putPOJO("attackDamage", getAttackDamage());
        arrayObject.putPOJO("health", getHealth());
        arrayObject.putPOJO("description", getDescription());
        arrayObject.putPOJO("colors", getColors());
        arrayObject.putPOJO("name", getName());

        return arrayObject;
    }

    /**
     *
     * @return true if the card needs to be placed on the front row on the table
     */
    public final boolean cardIsFrontRow() {
        if (getName().equals("The Ripper") || getName().equals("Miraj")
                || getName().equals("Goliath")
                || getName().equals("Warden")) {
            return true;
        } else {
            return false;
        }
    }

    public final int getAttackDamage() {
        return attackDamage;
    }

    /**
     *
     * @param attackDamage the attack damage to set to card
     */
    public final void setAttackDamage(final int attackDamage) {
        if (attackDamage > 0) {
            this.attackDamage = attackDamage;
        } else {
            this.attackDamage = 0;
        }
    }

    public final int getHealth() {
        return health;
    }

    public final void setHealth(final int health) {
        this.health = health;
    }

    public final int getIsTank() {
        return isTank;
    }

    /**
     *  verifies if the card is of type tank
     */
    public final void setIsTank() {
        if (getName().equals("Goliath") || getName().equals("Warden")) {
            this.isTank = 1;
        }
    }

    /**
     *
     * @param table the table where the cards are placed
     * @param attacker the card that attacks
     * @param attacked tha card that is attacked
     * @param playerTurn the first player or the second player turn to play
     * @return null is the minion used its ability ar an error otherwise
     */
    public String abiltyMinion(final Table table, final Coordinates attacker,
                               final Coordinates attacked, final int playerTurn) {
        if (getName().equals("The Ripper")) {
            return theRipperAbility(table, attacker, attacked, playerTurn);
        }
        if (getName().equals("Miraj")) {
            return mirajlAbility(table, attacker, attacked, playerTurn);
        }
        if (getName().equals("The Cursed One")) {
            return theCursedOneAbility(table, attacker, attacked, playerTurn);
        }
        if (getName().equals("Disciple")) {
            return discipleAbility(table, attacker, attacked, playerTurn);
        }

        return null;
    }

    private String discipleAbility(final Table table, final Coordinates attacker,
                                   final Coordinates attacked, final int playerTurn) {
        if (playerTurn == 1) {
            if (attacked.getX() == 1 || attacked.getX() == 0) {
                return "Attacked card does not belong to the current player.";
            }
        } else {
            if (attacked.getX() == 2 || attacked.getX() == 3) {
                return "Attacked card does not belong to the current player.";
            }
        }
        ((Minion) table.getCardTable().get(attacked.getX()).get(attacked.getY()))
                .setHealth(((Minion) table.getCardTable().get(attacked.getX())
                        .get(attacked.getY())).getHealth() + 2);
        table.getUsedCards()[attacker.getX()][attacker.getY()] = 1;
        return null;
    }

    private String theCursedOneAbility(final Table table, final Coordinates attacker,
                                       final Coordinates attacked, final int playerTurn) {
        if (playerTurn == 1) {
            if (attacked.getX() == 3 || attacked.getX() == 2) {
                return "Attacked card does not belong to the enemy.";
            }
        } else {
            if (attacked.getX() == 1 || attacked.getX() == 0) {
                return "Attacked card does not belong to the enemy.";
            }
        }
        if (table.existsTank(playerTurn)
                && ((Minion) table.getCardTable().get(attacked.getX())
                .get(attacked.getY())).getIsTank() == 0) {
            return "Attacked card is not of type 'Tank'.";
        }
        int copieViata = ((Minion) table.getCardTable().get(attacked.getX())
                .get(attacked.getY())).getHealth();
        ((Minion) table.getCardTable().get(attacked.getX()).get(attacked.getY()))
                .setHealth(((Minion) table.getCardTable().get(attacked.getX())
                        .get(attacked.getY())).getAttackDamage());
        ((Minion) table.getCardTable().get(attacked.getX()).get(attacked.getY()))
                .setAttackDamage(copieViata);
        table.getUsedCards()[attacker.getX()][attacker.getY()] = 1;
        return null;
    }

    private String mirajlAbility(final Table table, final Coordinates attacker,
                                 final Coordinates attacked, final int playerTurn) {
        if (playerTurn == 1) {
            if (attacked.getX() == 3 || attacked.getX() == 2) {
                return "Attacked card does not belong to the enemy.";
            }
        } else {
            if (attacked.getX() == 1 || attacked.getX() == 0) {
                return "Attacked card does not belong to the enemy.";
            }
        }
        if (table.existsTank(playerTurn)
                && ((Minion) table.getCardTable().get(attacked.getX()).get(attacked.getY()))
                .getIsTank() == 0) {
            return "Attacked card is not of type 'Tank'.";
        }
        int copieViata = ((Minion) table.getCardTable().get(attacked.getX())
                .get(attacked.getY())).getHealth();
        ((Minion) table.getCardTable().get(attacked.getX()).get(attacked.getY()))
                .setHealth(((Minion) table.getCardTable().get(attacker.getX())
                        .get(attacker.getY())).getHealth());
        ((Minion) table.getCardTable().get(attacker.getX())
                .get(attacker.getY()))
                .setHealth(copieViata);
        table.getUsedCards()[attacker.getX()][attacker.getY()] = 1;
        return null;
    }

    private String theRipperAbility(final Table table, final Coordinates attacker,
                                    final Coordinates attacked, final int playerTurn) {
        if (playerTurn == 1) {
            if (attacked.getX() == 3 || attacked.getX() == 2) {
                return "Attacked card does not belong to the enemy.";
            }
        } else {
            if (attacked.getX() == 1 || attacked.getX() == 0) {
                return "Attacked card does not belong to the enemy.";
            }
        }
        if (table.existsTank(playerTurn) && ((Minion) table.getCardTable()
                .get(attacked.getX()).get(attacked.getY())).getIsTank() == 0) {
            return "Attacked card is not of type 'Tank'.";
        }
        ((Minion) table.getCardTable().get(attacked.getX()).get(attacked.getY()))
                .setAttackDamage(((Minion) table.getCardTable().get(attacked.getX())
                        .get(attacked.getY())).getAttackDamage() - 2);
        table.getUsedCards()[attacker.getX()][attacker.getY()] = 1;
        return null;
    }
}
