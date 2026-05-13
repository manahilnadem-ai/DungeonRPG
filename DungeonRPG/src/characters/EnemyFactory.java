package characters;

import java.util.Random;

/**
 * EnemyFactory — creates enemies appropriate to dungeon depth.
 * Demonstrates: Factory Design Pattern
 */
public class EnemyFactory {

    private static final Random random = new Random();

    public static Enemy createEnemy(int dungeonDepth) {
        if (dungeonDepth >= 5) {
            return new Dragon(); // Boss on floor 5+
        }

        // Weighted random selection based on depth
        int roll = random.nextInt(100);

        if (dungeonDepth == 1) {
            return new Goblin();
        } else if (dungeonDepth == 2) {
            return roll < 60 ? new Goblin() : new Skeleton();
        } else if (dungeonDepth == 3) {
            return roll < 30 ? new Goblin() : roll < 70 ? new Skeleton() : new Troll();
        } else { // depth 4
            return roll < 20 ? new Skeleton() : roll < 60 ? new Troll() : new DarkMage();
        }
    }

    public static Enemy createBoss() {
        return new Dragon();
    }
}