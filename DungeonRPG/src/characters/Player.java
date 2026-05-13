package characters;

import items.Item;
import items.Weapon;
import items.Armor;
import items.Potion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Player class — extends Character.
 * Demonstrates: Inheritance, Encapsulation, Composition (Inventory)
 */
public class Player extends Character {

    public enum HeroClass { WARRIOR, MAGE, ROGUE }

    private HeroClass heroClass;
    private int experience;
    private int experienceToNextLevel;
    private List<Item> inventory;
    private Weapon equippedWeapon;
    private Armor equippedArmor;
    private int specialCooldown;
    private Random random;

    public Player(String name, HeroClass heroClass) {
        super(name,
              heroClass == HeroClass.WARRIOR ? 120 : heroClass == HeroClass.MAGE ? 80 : 100,
              heroClass == HeroClass.WARRIOR ? 15 : heroClass == HeroClass.MAGE ? 22 : 18,
              heroClass == HeroClass.WARRIOR ? 8  : heroClass == HeroClass.MAGE ? 3  : 5);
        this.heroClass = heroClass;
        this.experience = 0;
        this.experienceToNextLevel = 100;
        this.inventory = new ArrayList<>();
        this.specialCooldown = 0;
        this.random = new Random();

        // Starting gold
        this.gold = 30;
    }

    @Override
    public int attack() {
        int weaponBonus = (equippedWeapon != null) ? equippedWeapon.getDamageBonus() : 0;
        int base = attackPower + weaponBonus;
        // Add some variance (±20%)
        int variance = (int)(base * 0.2);
        return base + random.nextInt(variance + 1) - variance / 2;
    }

    @Override
    public String useSpecialAbility(Character target) {
        if (specialCooldown > 0) {
            return "⚠  Special ability on cooldown for " + specialCooldown + " more turn(s)!";
        }

        String result;
        switch (heroClass) {
            case WARRIOR -> {
                // Shield Bash: deals 1.5x damage + stuns (ignores defense)
                int damage = (int)(attackPower * 1.5);
                target.setCurrentHP(target.getCurrentHP() - damage);
                result = "⚔  SHIELD BASH! " + name + " deals " + damage + " true damage to " + target.getName() + "!";
                specialCooldown = 3;
            }
            case MAGE -> {
                // Fireball: high damage to target
                int damage = attackPower * 2 + random.nextInt(15);
                int dealt = target.takeDamage(damage);
                result = "🔥 FIREBALL! " + name + " blasts " + target.getName() + " for " + dealt + " fire damage!";
                specialCooldown = 3;
            }
            case ROGUE -> {
                // Backstab: guaranteed critical (3x damage)
                int damage = attackPower * 3;
                int dealt = target.takeDamage(damage);
                result = "🗡  BACKSTAB! " + name + " critically strikes for " + dealt + " damage!";
                specialCooldown = 4;
            }
            default -> result = "Nothing happened.";
        }
        return result;
    }

    /**
     * Gain XP and check for level-up.
     * @return true if leveled up
     */
    public boolean gainExperience(int xp) {
        experience += xp;
        if (experience >= experienceToNextLevel) {
            levelUp();
            return true;
        }
        return false;
    }

    private void levelUp() {
        level++;
        experience -= experienceToNextLevel;
        experienceToNextLevel = (int)(experienceToNextLevel * 1.5);

        int hpGain, atkGain, defGain;
        switch (heroClass) {
            case WARRIOR -> { hpGain = 20; atkGain = 3; defGain = 2; }
            case MAGE    -> { hpGain = 10; atkGain = 6; defGain = 1; }
            case ROGUE   -> { hpGain = 15; atkGain = 4; defGain = 1; }
            default      -> { hpGain = 15; atkGain = 4; defGain = 1; }
        }

        maxHP += hpGain;
        currentHP = maxHP; // Full heal on level up
        attackPower += atkGain;
        defense += defGain;
    }

    public void tickCooldowns() {
        if (specialCooldown > 0) specialCooldown--;
    }

    public void addItem(Item item) { inventory.add(item); }

    public boolean usePotion() {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i) instanceof Potion p) {
                heal(p.getHealAmount());
                inventory.remove(i);
                return true;
            }
        }
        return false;
    }

    public void equipWeapon(Weapon w) { this.equippedWeapon = w; }
    public void equipArmor(Armor a) {
        this.equippedArmor = a;
        this.defense = (heroClass == HeroClass.WARRIOR ? 8 : heroClass == HeroClass.MAGE ? 3 : 5)
                       + (level - 1) * 2 + a.getDefenseBonus();
    }

    public String getInventoryString() {
        if (inventory.isEmpty()) return "  (empty)";
        StringBuilder sb = new StringBuilder();
        for (Item item : inventory) {
            sb.append("  - ").append(item.getName()).append(": ").append(item.getDescription()).append("\n");
        }
        return sb.toString();
    }

    public String getStatsString() {
        return String.format(
            "┌─────────────────────────────────┐\n" +
            "│  %s the %s  (Level %d)\n" +
            "│  HP:     %d / %d\n" +
            "│  ATK:    %d%s\n" +
            "│  DEF:    %d%s\n" +
            "│  EXP:    %d / %d\n" +
            "│  Gold:   %dg\n" +
            "│  Special Cooldown: %d\n" +
            "└─────────────────────────────────┘",
            name, heroClass, level,
            currentHP, maxHP,
            attackPower, (equippedWeapon != null ? " (+" + equippedWeapon.getDamageBonus() + " weapon)" : ""),
            defense, (equippedArmor != null ? " (+" + equippedArmor.getDefenseBonus() + " armor)" : ""),
            experience, experienceToNextLevel,
            gold,
            specialCooldown
        );
    }

    // Getters
    public HeroClass getHeroClass()     { return heroClass; }
    public int getExperience()          { return experience; }
    public int getExperienceToNext()    { return experienceToNextLevel; }
    public List<Item> getInventory()    { return inventory; }
    public Weapon getEquippedWeapon()   { return equippedWeapon; }
    public Armor getEquippedArmor()     { return equippedArmor; }
    public int getSpecialCooldown()     { return specialCooldown; }

    public int getPotionCount() {
        return (int) inventory.stream().filter(i -> i instanceof Potion).count();
    }
}