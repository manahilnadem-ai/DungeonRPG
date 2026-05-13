package characters;

import java.util.Random;

/**
 * Abstract Enemy class — extends Character.
 * Demonstrates: Inheritance, Abstract Methods, Polymorphism
 */
public abstract class Enemy extends Character {

    protected int xpReward;
    protected int goldReward;
    protected String description;
    protected Random random;

    public Enemy(String name, int maxHP, int attackPower, int defense, int xpReward, int goldReward, String description) {
        super(name, maxHP, attackPower, defense);
        this.xpReward = xpReward;
        this.goldReward = goldReward;
        this.description = description;
        this.random = new Random();
    }

    @Override
    public int attack() {
        int variance = Math.max(1, (int)(attackPower * 0.25));
        return attackPower + random.nextInt(variance);
    }

    public int getXpReward()       { return xpReward; }
    public int getGoldReward()     { return goldReward; }
    public String getDescription() { return description; }
}


// ─── Concrete Enemy: Goblin ───────────────────────────────────────────────────
class Goblin extends Enemy {
    public Goblin() {
        super("Goblin", 40, 8, 2, 25, 10, "A sneaky little green creature with a rusty dagger.");
    }

    @Override
    public String useSpecialAbility(Character target) {
        // Sneak Attack — 50% chance to deal extra damage
        if (random.nextBoolean()) {
            int bonus = 10;
            int dealt = target.takeDamage(attack() + bonus);
            return "🐾 Goblin uses SNEAK ATTACK for " + dealt + " damage!";
        }
        return "";
    }
}


// ─── Concrete Enemy: Skeleton ─────────────────────────────────────────────────
class Skeleton extends Enemy {
    public Skeleton() {
        super("Skeleton", 55, 12, 4, 40, 15, "An undead warrior rattling with bones and malice.");
    }

    @Override
    public String useSpecialAbility(Character target) {
        // Bone Throw — ranged attack ignoring defense
        int damage = 14;
        target.setCurrentHP(target.getCurrentHP() - damage);
        return "💀 Skeleton hurls a BONE for " + damage + " true damage!";
    }
}


// ─── Concrete Enemy: Troll ────────────────────────────────────────────────────
class Troll extends Enemy {
    public Troll() {
        super("Troll", 90, 18, 6, 65, 25, "A massive beast with regenerating flesh and a stone club.");
    }

    @Override
    public int takeDamage(int damage) {
        // Trolls regenerate 5 HP each time they're hit
        int taken = super.takeDamage(damage);
        heal(5);
        return taken;
    }

    @Override
    public String useSpecialAbility(Character target) {
        int damage = attackPower + 10;
        int dealt = target.takeDamage(damage);
        return "🪨 Troll uses SMASH for " + dealt + " damage!";
    }
}


// ─── Concrete Enemy: Dark Mage ────────────────────────────────────────────────
class DarkMage extends Enemy {
    public DarkMage() {
        super("Dark Mage", 70, 20, 3, 80, 35, "A robed sorcerer channeling dark energy from the abyss.");
    }

    @Override
    public String useSpecialAbility(Character target) {
        // Curse — drains HP and gives it to self
        int drain = 15;
        target.setCurrentHP(target.getCurrentHP() - drain);
        heal(drain / 2);
        return "🌑 Dark Mage casts LIFE DRAIN, stealing " + drain + " HP!";
    }
}


// ─── BOSS: Dragon ─────────────────────────────────────────────────────────────
class Dragon extends Enemy {
    private boolean rageMode = false;

    public Dragon() {
        super("Ancient Dragon", 200, 28, 12, 250, 100, "A titanic serpent of fire and fury. The dungeon's final guardian.");
    }

    @Override
    public int takeDamage(int damage) {
        int taken = super.takeDamage(damage);
        // Enter rage mode below 50% HP
        if (!rageMode && currentHP < maxHP / 2) {
            rageMode = true;
            attackPower += 15;
        }
        return taken;
    }

    @Override
    public String useSpecialAbility(Character target) {
        String result;
        if (rageMode) {
            // Inferno — massive damage
            int damage = 35 + random.nextInt(20);
            int dealt = target.takeDamage(damage);
            result = "🔥🔥 Dragon RAGES with INFERNO BREATH for " + dealt + " damage! 🔥🔥";
        } else {
            int damage = 22 + random.nextInt(10);
            int dealt = target.takeDamage(damage);
            result = "🔥 Dragon breathes FIRE for " + dealt + " damage!";
        }
        return result;
    }

    public boolean isEnraged() { return rageMode; }
}