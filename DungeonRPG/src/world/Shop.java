package world;

import characters.Player;
import items.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Shop — allows players to spend gold on items between floors.
 * Demonstrates: Encapsulation, Composition
 */
public class Shop {

    private record ShopItem(Item item, int price) {}

    private final List<ShopItem> stock;
    private final Scanner scanner;

    public Shop(Scanner scanner) {
        this.scanner = scanner;
        this.stock = new ArrayList<>();
        stock.add(new ShopItem(new Potion("Health Potion", 50, 20),    20));
        stock.add(new ShopItem(new Potion("Mega Potion",   100, 40),   40));
        stock.add(new ShopItem(new Weapon("Iron Sword",    8,   50),   50));
        stock.add(new ShopItem(new Weapon("Enchanted Blade", 15, 90),  90));
        stock.add(new ShopItem(new Armor("Leather Vest",   5,   45),   45));
        stock.add(new ShopItem(new Armor("Plate Mail",     10,  85),   85));
    }

    public void enter(Player player) {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║         ⚗  MERCHANT'S SHOP  ⚗        ║");
        System.out.println("╚══════════════════════════════════════╝");

        boolean shopping = true;
        while (shopping) {
            System.out.println("\nYour gold: " + player.getGold() + "g\n");
            System.out.println("  #  Item                   Price");
            System.out.println("  ─────────────────────────────────");
            for (int i = 0; i < stock.size(); i++) {
                ShopItem si = stock.get(i);
                System.out.printf("  %d. %-22s %dg%n", i + 1, si.item().getName() + " (" + si.item().getDescription() + ")", si.price());
            }
            System.out.println("  0. Leave shop");
            System.out.print("\nChoose item to buy: ");

            int choice = readInt(0, stock.size());
            if (choice == 0) {
                shopping = false;
            } else {
                ShopItem selected = stock.get(choice - 1);
                if (player.getGold() < selected.price()) {
                    System.out.println("❌ Not enough gold!");
                } else {
                    player.addGold(-selected.price());
                    Item bought = selected.item();
                    if (bought instanceof Weapon w) {
                        player.equipWeapon(w);
                        System.out.println("✅ Equipped " + w.getName() + "!");
                    } else if (bought instanceof Armor a) {
                        player.equipArmor(a);
                        System.out.println("✅ Equipped " + a.getName() + "!");
                    } else {
                        player.addItem(bought);
                        System.out.println("✅ Added " + bought.getName() + " to inventory!");
                    }
                }
            }
        }
        System.out.println("\nMerchant: \"Safe travels, adventurer!\"\n");
    }

    private int readInt(int min, int max) {
        while (true) {
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                if (val >= min && val <= max) return val;
                System.out.print("Enter a number between " + min + " and " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Try again: ");
            }
        }
    }
}