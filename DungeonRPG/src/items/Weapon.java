package items;

/**
 * Weapon — equippable item that boosts attack.
 */
public class Weapon implements Item {
    private final String name;
    private final String description;
    private final int damageBonus;
    private final int value;

    public Weapon(String name, int damageBonus, int value) {
        this.name = name;
        this.damageBonus = damageBonus;
        this.value = value;
        this.description = "+" + damageBonus + " ATK";
    }

    @Override public String getName()        { return name; }
    @Override public String getDescription() { return description; }
    @Override public int getValue()          { return value; }
    public int getDamageBonus()              { return damageBonus; }
}