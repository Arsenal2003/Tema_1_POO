package Game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Coordinates;

import java.util.ArrayList;

public class Hero extends Card {
    private int health = 30;

    public Hero(int mana, String description, ArrayList<String> colors, String name) {
        super(mana, description, colors, name);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public ObjectNode cardToJson(ObjectMapper objectMapper) {

        ObjectNode arrayObject = objectMapper.createObjectNode();
        arrayObject.putPOJO("mana", getMana());
        arrayObject.putPOJO("description", getDescription());
        arrayObject.putPOJO("colors", getColors());
        arrayObject.putPOJO("name", getName());
        arrayObject.putPOJO("health", getHealth());

        return arrayObject;
    }

    public String abilityHero(Table table, int row, Game game) {
        if (getName().equals("Lord Royce"))
            return LordRoyceAbility(table, row, game);
        if (getName().equals("Empress Thorina"))
            return EmpressThorinaAbility(table, row, game);
        if (getName().equals("General Kocioraw"))
            return GeneralKociorawAbility(table, row, game);
        if (getName().equals("King Mudface"))
            return KingMudfaceAbility(table, row, game);

        return null;
    }

    private String KingMudfaceAbility(Table table, int row, Game game) {
        if (game.getPlayerTurn() == 1) {
            if (row == 0 || row == 1)
                return "Selected row does not belong to the current player.";
            for (int i = 0; i < table.getCardTable().get(row).size(); i++)
                ((Minion) table.getCardTable().get(row).get(i)).setHealth(((Minion) table.getCardTable().get(row).get(i)).getHealth() + 1);
            game.getFirstPlayer().setMana(game.getFirstPlayer().getMana() - getMana());
            game.setPlayerOneHeroAttacked(1);
        } else {
            if (row == 2 || row == 3)
                return "Selected row does not belong to the current player.";
            for (int i = 0; i < table.getCardTable().get(row).size(); i++)
                ((Minion) table.getCardTable().get(row).get(i)).setHealth(((Minion) table.getCardTable().get(row).get(i)).getHealth() + 1);
            game.getSecondPlayer().setMana(game.getSecondPlayer().getMana() - getMana());
            game.setPlayerTwoHeroAttacked(1);

        }

        return null;
    }

    private String GeneralKociorawAbility(Table table, int row, Game game) {
        if (game.getPlayerTurn() == 1) {
            if (row == 0 || row == 1)
                return "Selected row does not belong to the current player.";
            for (int i = 0; i < table.getCardTable().get(row).size(); i++)
                ((Minion) table.getCardTable().get(row).get(i)).setAttackDamage(((Minion) table.getCardTable().get(row).get(i)).getAttackDamage() + 1);
            game.getFirstPlayer().setMana(game.getFirstPlayer().getMana() - getMana());
            game.setPlayerOneHeroAttacked(1);
        } else {
            if (row == 2 || row == 3)
                return "Selected row does not belong to the current player.";
            for (int i = 0; i < table.getCardTable().get(row).size(); i++)
                ((Minion) table.getCardTable().get(row).get(i)).setAttackDamage(((Minion) table.getCardTable().get(row).get(i)).getAttackDamage() + 1);
            game.getSecondPlayer().setMana(game.getSecondPlayer().getMana() - getMana());
            game.setPlayerTwoHeroAttacked(1);

        }
        return null;
    }

    private String EmpressThorinaAbility(Table table, int row, Game game) {
        if (game.getPlayerTurn() == 1) {
            if (row == 2 || row == 3)
                return "Selected row does not belong to the enemy.";
            int maxHealth = 0;
            for (int i = 0; i < table.getCardTable().get(row).size(); i++)
                if (((Minion) table.getCardTable().get(row).get(i)).getHealth() > maxHealth)
                    maxHealth = ((Minion) table.getCardTable().get(row).get(i)).getHealth();
            for (int i = 0; i < table.getCardTable().get(row).size(); i++)
                if (((Minion) table.getCardTable().get(row).get(i)).getHealth() == maxHealth) {
                    table.getCardTable().get(row).remove(i);
                    table.getFrozenCards()[row][i] = 0;
                    table.getUsedCards()[row][i] = 0;
                    break;
                }
            game.getFirstPlayer().setMana(game.getFirstPlayer().getMana() - getMana());
            game.setPlayerOneHeroAttacked(1);
        } else {
            if (row == 0 || row == 1)
                return "Selected row does not belong to the enemy.";
            int maxHealth = 0;
            for (int i = 0; i < table.getCardTable().get(row).size(); i++)
                if (((Minion) table.getCardTable().get(row).get(i)).getHealth() > maxHealth)
                    maxHealth = ((Minion) table.getCardTable().get(row).get(i)).getHealth();
            for (int i = 0; i < table.getCardTable().get(row).size(); i++)
                if (((Minion) table.getCardTable().get(row).get(i)).getHealth() == maxHealth) {
                    table.getCardTable().get(row).remove(i);
                    table.getFrozenCards()[row][i] = 0;
                    table.getUsedCards()[row][i] = 0;
                    break;
                }
            game.getSecondPlayer().setMana(game.getSecondPlayer().getMana() - getMana());
            game.setPlayerTwoHeroAttacked(1);

        }
        return null;
    }

    private String LordRoyceAbility(Table table, int row, Game game) {
        if (game.getPlayerTurn() == 1) {
            if (row == 2 || row == 3)
                return "Selected row does not belong to the enemy.";
            int maxAttack = 0;
            for (int i = 0; i < table.getCardTable().get(row).size(); i++)
                if (((Minion) table.getCardTable().get(row).get(i)).getAttackDamage() > maxAttack)
                    maxAttack = ((Minion) table.getCardTable().get(row).get(i)).getAttackDamage();
            for (int i = 0; i < table.getCardTable().get(row).size(); i++)
                if (((Minion) table.getCardTable().get(row).get(i)).getAttackDamage() == maxAttack) {
                    table.getFrozenCards()[row][i] = 1;
                    break;
                }
            game.getFirstPlayer().setMana(game.getFirstPlayer().getMana() - getMana());
            game.setPlayerOneHeroAttacked(1);
        } else {
            if (row == 0 || row == 1)
                return "Selected row does not belong to the enemy.";
            int maxAttack = 0;
            for (int i = 0; i < table.getCardTable().get(row).size(); i++)
                if (((Minion) table.getCardTable().get(row).get(i)).getAttackDamage() > maxAttack)
                    maxAttack = ((Minion) table.getCardTable().get(row).get(i)).getAttackDamage();
            for (int i = 0; i < table.getCardTable().get(row).size(); i++)
                if (((Minion) table.getCardTable().get(row).get(i)).getAttackDamage() == maxAttack) {
                    table.getFrozenCards()[row][i] = 1;
                    break;
                }
            game.getSecondPlayer().setMana(game.getSecondPlayer().getMana() - getMana());
            game.setPlayerTwoHeroAttacked(1);

        }
        return null;
    }

}
