package items;

/**
 * Item interface — base contract for all items.
 * Demonstrates: Interface, Polymorphism
 */
public interface Item {
    String getName();
    String getDescription();
    int getValue(); // gold value
}