package srpfacadelab;

import java.util.List;
import java.util.ArrayList;


public class RpgPlayer {
    public static final int MAX_CARRYING_CAPACITY = 1000;

    private final IGameEngine gameEngine;

    private int health;

    private int maxHealth;

    private int armour;

    // How much the player can carry in pounds
    private Inventory inventory;

    public RpgPlayer(IGameEngine gameEngine) {
        this.gameEngine = gameEngine;
        inventory = new Inventory(MAX_CARRYING_CAPACITY);
    }

    public void useItem(Item item) {
        if (item.getName().equals("Stink Bomb"))
        {
            List<IEnemy> enemies = gameEngine.getEnemiesNear(this);

            for (IEnemy enemy: enemies){
                enemy.takeDamage(100);
            }
        }
    }

    public boolean pickUpItem(Item item) {
        int weight = inventory.calculateInventoryWeight();
        if (weight + item.getWeight() > inventory.getCarryingCapacity())
            return false;

        if (item.isUnique() && inventory.checkIfItemExistsInInventory(item))
            return false;

        // Don't pick up items that give health, just consume them.
        if (item.getHeal() > 0) {
            health += item.getHeal();

            if (health > maxHealth)
                health = maxHealth;

            if (item.getHeal() > 500) {
                gameEngine.playSpecialEffect("green_swirly");
            }

            return true;
        }

        if (item.isRare()) {
            if (item.isUnique())
            {
                gameEngine.playSpecialEffect("blue_swirly");
            } else
            {
                gameEngine.playSpecialEffect("cool_swirly_particles");
            }
        }

        inventory.addItem(item);

        calculateStats();

        return true;
    }

    public void calculateStats() {
        for (Item i: inventory.getInventory()) {
            armour += i.getArmour();
        }
    }

    public void takeDamage(int damage) {
        if (damage < armour) {
            gameEngine.playSpecialEffect("parry");
        }
        int damageToDeal = damage - armour;
        if (inventory.calculateInventoryWeight() < 0.5 * inventory.getCarryingCapacity())
        {
            damageToDeal = (int)((double)damageToDeal * 0.75);
        }

        health -= damageToDeal;

        gameEngine.playSpecialEffect("lots_of_gore");
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getArmour() {
        return armour;
    }

    public void setArmour(int armour) {
        this.armour = armour;
    }

    public Inventory getInventory()
    {
        return inventory;
    }
}
