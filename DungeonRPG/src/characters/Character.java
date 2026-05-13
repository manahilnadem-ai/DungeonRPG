package characters;

/**
 * Abstract base class for all characters in the game.
 * Demonstrates: Abstraction, Encapsulation, Polymorphism
 */
public abstract class Character {

    protected String name;
    protected int maxHP;
    protected int currentHP;
    protected int attackPower;
    protected int defense;
    protected int level;
    protected int gold;

    public Character(String name, int maxHP, int attackPower, int defense) {
        this.name = name;
        this.maxHP = maxHP;
        this.currentHP = maxHP;
        this.attackPower = attackPower;
        this.defense = defense;
        this.level = 1;
        this.gold = 0;
    }

    /**
     * Every character must define how it attacks — polymorphic behavior.
     */
    public abstract int attack();

    /**
     * Every character must define its special ability.
     */
    public abstract String useSpecialAbility(Character target);

    /**
     * Shared damage logic with defense reduction.
     */
    public int takeDamage(int damage) {
        int reducedDamage = Math.max(1, damage - defense);
        currentHP = Math.max(0, currentHP - reducedDamage);
        return reducedDamage;
    }

    public void heal(int amount) {
        currentHP = Math.min(maxHP, currentHP + amount);
    }

    public boolean isAlive() {
        return currentHP > 0;
    }

    public String getStatusBar() {
        int barLength = 20;
        int filled = (int) ((double) currentHP / maxHP * barLength);
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < barLength; i++) {
            bar.append(i < filled ? "█" : "░");
        }
        bar.append("]");
        return String.format("%-14s HP: %s %d/%d", name, bar, currentHP, maxHP);
    }

    // Getters
    public String getName()      { return name; }
    public int getCurrentHP()    { return currentHP; }
    public int getMaxHP()        { return maxHP; }
    public int getAttackPower()  { return attackPower; }
    public int getDefense()      { return defense; }
    public int getLevel()        { return level; }
    public int getGold()         { return gold; }

    // Setters
    public void setCurrentHP(int hp) { this.currentHP = Math.max(0, Math.min(maxHP, hp)); }
    public void addGold(int amount)  { this.gold += amount; }
}