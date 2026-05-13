package engine;

import characters.*;
import items.Potion;
import world.DungeonFloor;
import world.Shop;

import java.util.Scanner;

/**
 * GameEngine — the central controller of the game.
 * Demonstrates: Facade Pattern, Game Loop, Separation of Concerns
 */
public class GameEngine {

    private static final int MAX_FLOORS = 5;

    private Player player;
    private final Scanner scanner;
    private final CombatSystem combat;
    private final GameLogger logger;
    private boolean gameRunning;

    public GameEngine() {
        this.scanner = new Scanner(System.in);
        this.combat  = new CombatSystem(scanner);
        this.logger  = new GameLogger();
    }

    public void start() {
        printBanner();
        player = createPlayer();

        // Give starting potion
        player.addItem(new Potion("Health Potion", 50, 20));
        logger.log("Hero created: " + player.getName() + " the " + player.getHeroClass());

        gameRunning = true;
        runGameLoop();
    }

    private void runGameLoop() {
        for (int floor = 1; floor <= MAX_FLOORS && gameRunning; floor++) {
            DungeonFloor dungeonFloor = new DungeonFloor(floor);
            System.out.println("\n" + "═".repeat(50));
            System.out.printf("  🏰 ENTERING FLOOR %d OF %d%n", floor, MAX_FLOORS);
            System.out.println("═".repeat(50));
            logger.log("Entered Floor " + floor);

            boolean floorCleared = runFloor(dungeonFloor);

            if (!floorCleared || !gameRunning) break;

            if (floor < MAX_FLOORS) {
                System.out.println("\n🎉 Floor " + floor + " cleared! Descending deeper...");
                System.out.println("Your HP is restored partially between floors.");
                player.heal(player.getMaxHP() / 4);
                pause(800);
            } else {
                // Defeated the dragon on floor 5
                printVictory();
                gameRunning = false;
            }
        }

        if (!player.isAlive()) {
            printGameOver();
        }

        logger.printLog();
        System.out.println("\nThanks for playing DungeonRPG!");
    }

    private boolean runFloor(DungeonFloor floor) {
        boolean continuePlaying = true;

        while (continuePlaying && gameRunning) {
            System.out.println("\n" + floor.getFloorMap());

            DungeonFloor.RoomType roomType = floor.getCurrentRoomType();
            System.out.println("  ➤ Room type: " + formatRoomType(roomType) + "\n");
            pause(400);

            switch (roomType) {
                case MONSTER, BOSS -> {
                    Enemy enemy = floor.spawnEnemy();
                    CombatSystem.CombatResult result = combat.runCombat(player, enemy);

                    if (result == CombatSystem.CombatResult.VICTORY) {
                        handleVictory(enemy);
                    } else if (result == CombatSystem.CombatResult.DEFEAT) {
                        gameRunning = false;
                        return false;
                    } else {
                        System.out.println("You fled the battle.");
                        logger.log("Fled from " + enemy.getName());
                    }

                    if (roomType == DungeonFloor.RoomType.BOSS && result == CombatSystem.CombatResult.VICTORY) {
                        return true; // Boss beaten = floor cleared
                    }
                }
                case TREASURE -> {
                    int gold = floor.getTreasureGold();
                    player.addGold(gold);
                    System.out.println("💰 You found a treasure chest containing " + gold + " gold!");
                    logger.log("Found " + gold + " gold in treasure chest");
                }
                case SHOP -> {
                    Shop shop = new Shop(scanner);
                    shop.enter(player);
                }
                case EMPTY -> {
                    System.out.println("🌫  The room is eerily empty... You rest briefly.");
                    int healAmt = 15;
                    player.heal(healAmt);
                    System.out.println("Restored " + healAmt + " HP.");
                }
            }

            if (!player.isAlive()) {
                gameRunning = false;
                return false;
            }

            // Try to advance to next room
            boolean hasNextRoom = floor.advance();
            if (!hasNextRoom) {
                return true; // Floor complete
            }

            // Between rooms — show status and prompt
            System.out.println("\n" + player.getStatsString());
            System.out.println("\nPress ENTER to continue to the next room...");
            scanner.nextLine();
        }
        return true;
    }

    private void handleVictory(Enemy enemy) {
        System.out.println("\n✨ Victory! " + enemy.getName() + " has been defeated!");
        int xp = enemy.getXpReward();
        int gold = enemy.getGoldReward();
        player.addGold(gold);

        System.out.printf("  +%d XP  +%dg%n", xp, gold);
        logger.log("Defeated " + enemy.getName() + " (+"+xp+"xp, +"+gold+"g)");

        boolean leveledUp = player.gainExperience(xp);
        if (leveledUp) {
            System.out.println("\n🌟 LEVEL UP! You are now level " + player.getLevel() + "!");
            System.out.println("   Stats increased! HP fully restored!");
            logger.log("Leveled up to level " + player.getLevel());
        }
    }

    private Player createPlayer() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║       🗡  CREATE YOUR HERO  🗡         ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("Enter your hero's name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = "Hero";

        System.out.println("\nChoose your class:");
        System.out.println("  [1] ⚔  Warrior  — High HP, Shield Bash special");
        System.out.println("  [2] 🔮 Mage     — High damage, Fireball special");
        System.out.println("  [3] 🗡  Rogue    — Balanced, Backstab special");
        System.out.print("\nChoice: ");

        int choice = readInt(1, 3);
        Player.HeroClass heroClass = switch (choice) {
            case 1 -> Player.HeroClass.WARRIOR;
            case 2 -> Player.HeroClass.MAGE;
            default -> Player.HeroClass.ROGUE;
        };

        Player p = new Player(name, heroClass);
        System.out.println("\n✅ " + name + " the " + heroClass + " has entered the dungeon!");
        return p;
    }

    private void printBanner() {
        System.out.println();
        System.out.println("  ██████╗ ██╗   ██╗███╗  ██╗ ██████╗ ███████╗ ██████╗ ███╗  ██╗");
        System.out.println("  ██╔══██╗██║   ██║████╗ ██║██╔════╝ ██╔════╝██╔═══██╗████╗ ██║");
        System.out.println("  ██║  ██║██║   ██║██╔██╗██║██║  ███╗█████╗  ██║   ██║██╔██╗██║");
        System.out.println("  ██║  ██║██║   ██║██║╚████║██║   ██║██╔══╝  ██║   ██║██║╚████║");
        System.out.println("  ██████╔╝╚██████╔╝██║ ╚███║╚██████╔╝███████╗╚██████╔╝██║ ╚███║");
        System.out.println("  ╚═════╝  ╚═════╝ ╚═╝  ╚══╝ ╚═════╝ ╚══════╝ ╚═════╝ ╚═╝  ╚══╝");
        System.out.println();
        System.out.println("                        ⚔  R  P  G  ⚔");
        System.out.println("          A turn-based dungeon crawler built with Java OOP");
        System.out.println();
        System.out.println("  Defeat the Ancient Dragon on Floor 5 to claim victory!");
        System.out.println();
    }

    private void printVictory() {
        System.out.println("\n" + "★".repeat(50));
        System.out.println("\n  🏆  CONGRATULATIONS, CHAMPION!  🏆");
        System.out.println("\n  You have slain the Ancient Dragon and");
        System.out.println("  escaped the dungeon with your life!");
        System.out.println("\n" + player.getStatsString());
        System.out.println("\n" + "★".repeat(50));
        logger.log("VICTORY — Defeated the Ancient Dragon!");
    }

    private void printGameOver() {
        System.out.println("\n" + "╔" + "═".repeat(40) + "╗");
        System.out.println("║           💀  GAME OVER  💀              ║");
        System.out.println("║  " + player.getName() + " has fallen in the dungeon...");
        System.out.println("║  The darkness swallows you whole.        ║");
        System.out.println("╚" + "═".repeat(40) + "╝");
        logger.log("DEFEAT — " + player.getName() + " died in the dungeon.");
    }

    private String formatRoomType(DungeonFloor.RoomType type) {
        return switch (type) {
            case MONSTER  -> "⚔  Monster Encounter";
            case BOSS     -> "💀 BOSS — Ancient Dragon!";
            case TREASURE -> "💰 Treasure Chamber";
            case SHOP     -> "⚗  Merchant's Shop";
            case EMPTY    -> "🌫  Empty Room";
        };
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