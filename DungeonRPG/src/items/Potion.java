package items;

/**
 * Potion — consumable item that restores HP.
 */
public class Potion implements Item {
    private final String name;
    private final int healAmount;
    private final int value;

    public Potion(String name, int healAmount, int value) {
        this.name = name;
        this.healAmount = healAmount;
        this.value = value;
    }

    @Override public String getName()        { return name; }
    @Override public String getDescription() { return "Restores " + healAmount + " HP"; }
    @Override public int getValue()          { return value; }
    public int getHealAmount()               { return healAmount; }
}