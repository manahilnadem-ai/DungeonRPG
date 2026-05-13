package engine;

import characters.Character;
import characters.Enemy;
import characters.Player;

import java.util.Random;
import java.util.Scanner;

/**
 * CombatSystem — handles turn-based battle logic.
 * Demonstrates: Single Responsibility Principle, Encapsulation
 */
public class CombatSystem {

    private final Scanner scanner;
    private final Random random;

    public CombatSystem(Scanner scanner) {
        this.scanner = scanner;
        this.random = new Random();
    }

    public enum CombatResult { VICTORY, DEFEAT, FLED }

    /**
     * Run a full combat encounter between the player and an enemy.
     */
    public CombatResult runCombat(Player player, Enemy enemy) {
        System.out.println("\n" + "═".repeat(50));
        System.out.println("  ⚔  ENCOUNTER: " + enemy.getName().toUpperCase());
        System.out.println("  " + enemy.getDescription());
        System.out.println("═".repeat(50));

        int turn = 1;

        while (player.isAlive() && enemy.isAlive()) {
            System.out.println("\n── Turn " + turn + " " + "─".repeat(30));
            printBattleStatus(player, enemy);
            System.out.println();

            // Player's turn
            System.out.println("What will you do?");
            System.out.println("  [1] Attack");
            System.out.println("  [2] Special Ability" +
                (player.getSpecialCooldown() > 0 ? " (CD: " + player.getSpecialCooldown() + ")" : " ✨"));
            System.out.println("  [3] Use Potion" +
                (player.getPotionCount() > 0 ? " (" + player.getPotionCount() + " left)" : " (none)"));
            System.out.println("  [4] Flee");
            System.out.print("\nChoice: ");

            int action = readInt(1, 4);
            System.out.println();

            switch (action) {
                case 1 -> {
                    int dmg = player.attack();
                    int dealt = enemy.takeDamage(dmg);
                    System.out.println("⚔  " + player.getName() + " attacks for " + dealt + " damage!");
                }
                case 2 -> {
                    String result = player.useSpecialAbility(enemy);
                    System.out.println(result);
                }
                case 3 -> {
                    if (player.getPotionCount() == 0) {
                        System.out.println("❌ No potions in inventory!");
                    } else {
                        boolean used = player.usePotion();
                        if (used) System.out.println("💊 Used a potion! HP restored.");
                    }
                }
                case 4 -> {
                    // 40% chance to flee
                    if (random.nextInt(100) < 40) {
                        System.out.println("💨 " + player.getName() + " successfully fled!");
                        return CombatResult.FLED;
                    } else {
                        System.out.println("❌ Couldn't escape!");
                    }
                }
            }

            player.tickCooldowns();

            // Check if enemy is dead
            if (!enemy.isAlive()) break;

            // Enemy's turn
            System.out.println();
            boolean useSpecial = random.nextInt(100) < 30; // 30% chance
            if (useSpecial) {
                String specialResult = enemy.useSpecialAbility(player);
                if (!specialResult.isEmpty()) {
                    System.out.println(specialResult);
                } else {
                    enemyNormalAttack(enemy, player);
                }
            } else {
                enemyNormalAttack(enemy, player);
            }

            turn++;
            pause(400);
        }

        if (player.isAlive()) {
            return CombatResult.VICTORY;
        } else {
            return CombatResult.DEFEAT;
        }
    }

    private void enemyNormalAttack(Enemy enemy, Player player) {
        int dmg = enemy.attack();
        int dealt = player.takeDamage(dmg);
        System.out.println("💥 " + enemy.getName() + " attacks " + player.getName() + " for " + dealt + " damage!");
    }

    private void printBattleStatus(Player player, Enemy enemy) {
        System.out.println(player.getStatusBar());
        System.out.println(enemy.getStatusBar());
    }

    private int readInt(int min, int max) {
        while (true) {
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                if (val >= min && val <= max) return val;
                System.out.print("Enter " + min + "-" + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("Invalid. Try again: ");
            }
        }
    }

    private void pause(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}