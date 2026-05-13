package items;

/**
 * Armor — equippable item that boosts defense.
 */
public class Armor implements Item {
    private final String name;
    private final String description;
    private final int defenseBonus;
    private final int value;

    public Armor(String name, int defenseBonus, int value) {
        this.name = name;
        this.defenseBonus = defenseBonus;
        this.value = value;
        this.description = "+" + defenseBonus + " DEF";
    }

    @Override public String getName()        { return name; }
    @Override public String getDescription() { return description; }
    @Override public int getValue()          { return value; }
    public int getDefenseBonus()             { return defenseBonus; }
}