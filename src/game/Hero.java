package game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public final class Hero extends Card {
    private int health = 30;

    public Hero(final int mana, final String description,
                final ArrayList<String> colors, final String name) {
        super(mana, description, colors, name);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(final int health) {
        this.health = health;
    }

    @Override
    public ObjectNode cardToJson(final ObjectMapper objectMapper) {

        ObjectNode arrayObject = objectMapper.createObjectNode();
        arrayObject.putPOJO("mana", getMana());
        arrayObject.putPOJO("description", getDescription());
        arrayObject.putPOJO("colors", getColors());
        arrayObject.putPOJO("name", getName());
        arrayObject.putPOJO("health", getHealth());

        return arrayObject;
    }

    /**
     *
     * @param table the table were the cards are placed
     * @param row the row that the ability affects
     * @param game the instance of the current game
     * @return null if the hero ability was cast with success or an error otherwise
     */
    public String abilityHero(final Table table, final int row, final Game game) {
        if (getName().equals("Lord Royce")) {
            return lordRoyceAbility(table, row, game);
        }
        if (getName().equals("Empress Thorina")) {
            return empressThorinaAbility(table, row, game);
        }
        if (getName().equals("General Kocioraw")) {
            return generalKociorawAbility(table, row, game);
        }
        if (getName().equals("King Mudface")) {
            return kingMudfaceAbility(table, row, game);
        }

        return null;
    }

    private String kingMudfaceAbility(final Table table, final int row, final Game game) {
        if (game.getPlayerTurn() == 1) {
            if (row == 0 || row == 1) {
                return "Selected row does not belong to the current player.";
            }
            for (int i = 0; i < table.getCardTable().get(row).size(); i++) {
                ((Minion) table.getCardTable().get(row).get(i))
                        .setHealth(((Minion) table.getCardTable().get(row).get(i))
                                .getHealth() + 1);
            }
            game.getFirstPlayer().setMana(game.getFirstPlayer().getMana() - getMana());
            game.setPlayerOneHeroAttacked(1);
        } else {
            if (row == 2 || row == 3) {
                return "Selected row does not belong to the current player.";
            }
            for (int i = 0; i < table.getCardTable().get(row).size(); i++) {
                ((Minion) table.getCardTable().get(row).get(i))
                        .setHealth(((Minion) table.getCardTable().get(row).get(i))
                                .getHealth() + 1);
            }
            game.getSecondPlayer().setMana(game.getSecondPlayer().getMana() - getMana());
            game.setPlayerTwoHeroAttacked(1);

        }

        return null;
    }

    private String generalKociorawAbility(final Table table, final int row, final Game game) {
        if (game.getPlayerTurn() == 1) {
            if (row == 0 || row == 1) {
                return "Selected row does not belong to the current player.";
            }
            for (int i = 0; i < table.getCardTable().get(row).size(); i++) {
                ((Minion) table.getCardTable().get(row).get(i))
                        .setAttackDamage(((Minion) table.getCardTable().get(row).get(i))
                                .getAttackDamage() + 1);
            }
            game.getFirstPlayer().setMana(game.getFirstPlayer().getMana() - getMana());
            game.setPlayerOneHeroAttacked(1);
        } else {
            if (row == 2 || row == 3) {
                return "Selected row does not belong to the current player.";
            }
            for (int i = 0; i < table.getCardTable().get(row).size(); i++) {
                ((Minion) table.getCardTable().get(row).get(i))
                        .setAttackDamage(((Minion) table.getCardTable().get(row).get(i))
                                .getAttackDamage() + 1);
            }
            game.getSecondPlayer().setMana(game.getSecondPlayer().getMana() - getMana());
            game.setPlayerTwoHeroAttacked(1);

        }
        return null;
    }

    private String empressThorinaAbility(final Table table, final int row, final Game game) {
        if (game.getPlayerTurn() == 1) {
            if (row == 2 || row == 3) {
                return "Selected row does not belong to the enemy.";
            }
            int maxHealth = 0;
            for (int i = 0; i < table.getCardTable().get(row).size(); i++) {
                if (((Minion) table.getCardTable().get(row).get(i)).getHealth() > maxHealth) {
                    maxHealth = ((Minion) table.getCardTable().get(row).get(i)).getHealth();
                }
            }
            for (int i = 0; i < table.getCardTable().get(row).size(); i++) {
                if (((Minion) table.getCardTable().get(row).get(i)).getHealth() == maxHealth) {
                    table.getCardTable().get(row).remove(i);
                    table.getFrozenCards()[row][i] = 0;
                    table.getUsedCards()[row][i] = 0;
                    break;
                }
            }
            game.getFirstPlayer().setMana(game.getFirstPlayer().getMana() - getMana());
            game.setPlayerOneHeroAttacked(1);
        } else {
            if (row == 0 || row == 1) {
                return "Selected row does not belong to the enemy.";
            }
            int maxHealth = 0;
            for (int i = 0; i < table.getCardTable().get(row).size(); i++) {
                if (((Minion) table.getCardTable().get(row).get(i)).getHealth() > maxHealth) {
                    maxHealth = ((Minion) table.getCardTable().get(row).get(i)).getHealth();
                }
            }
            for (int i = 0; i < table.getCardTable().get(row).size(); i++) {
                if (((Minion) table.getCardTable().get(row).get(i)).getHealth() == maxHealth) {
                    table.getCardTable().get(row).remove(i);
                    table.getFrozenCards()[row][i] = 0;
                    table.getUsedCards()[row][i] = 0;
                    break;
                }
            }
            game.getSecondPlayer().setMana(game.getSecondPlayer().getMana() - getMana());
            game.setPlayerTwoHeroAttacked(1);

        }
        return null;
    }

    private String lordRoyceAbility(final Table table, final int row, final Game game) {
        if (game.getPlayerTurn() == 1) {
            if (row == 2 || row == 3) {
                return "Selected row does not belong to the enemy.";
            }
            int maxAttack = 0;
            for (int i = 0; i < table.getCardTable().get(row).size(); i++) {
                if (((Minion) table.getCardTable().get(row).get(i)).getAttackDamage() > maxAttack) {
                    maxAttack = ((Minion) table.getCardTable().get(row).get(i)).getAttackDamage();
                }
            }
            for (int i = 0; i < table.getCardTable().get(row).size(); i++) {
                if (((Minion) table.getCardTable().get(row).get(i))
                        .getAttackDamage() == maxAttack) {
                    table.getFrozenCards()[row][i] = 1;
                    break;
                }
            }
            game.getFirstPlayer().setMana(game.getFirstPlayer().getMana() - getMana());
            game.setPlayerOneHeroAttacked(1);
        } else {
            if (row == 0 || row == 1) {
                return "Selected row does not belong to the enemy.";
            }
            int maxAttack = 0;
            for (int i = 0; i < table.getCardTable().get(row).size(); i++) {
                if (((Minion) table.getCardTable().get(row).get(i)).getAttackDamage() > maxAttack) {
                    maxAttack = ((Minion) table.getCardTable().get(row).get(i)).getAttackDamage();
                }
            }
            for (int i = 0; i < table.getCardTable().get(row).size(); i++) {
                if (((Minion) table.getCardTable().get(row).get(i))
                        .getAttackDamage() == maxAttack) {
                    table.getFrozenCards()[row][i] = 1;
                    break;
                }
            }
            game.getSecondPlayer().setMana(game.getSecondPlayer().getMana() - getMana());
            game.setPlayerTwoHeroAttacked(1);

        }
        return null;
    }

}
