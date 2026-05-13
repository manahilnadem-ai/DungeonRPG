package world;

import characters.Enemy;
import characters.EnemyFactory;

import java.util.Random;

/**
 * DungeonFloor — represents one level of the dungeon.
 * Demonstrates: Composition, Encapsulation
 */
public class DungeonFloor {

    public enum RoomType { MONSTER, TREASURE, SHOP, BOSS, EMPTY }

    private final int floorNumber;
    private final int totalRooms;
    private int currentRoom;
    private final RoomType[] rooms;
    private final Random random;

    public DungeonFloor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.totalRooms = 4 + floorNumber; // More rooms deeper in
        this.currentRoom = 0;
        this.random = new Random();
        this.rooms = generateRooms();
    }

    private RoomType[] generateRooms() {
        RoomType[] r = new RoomType[totalRooms];

        // First room is always a monster
        r[0] = RoomType.MONSTER;

        // Last room is always a boss on floor 5, otherwise a monster
        r[totalRooms - 1] = (floorNumber == 5) ? RoomType.BOSS : RoomType.MONSTER;

        // Fill middle rooms
        for (int i = 1; i < totalRooms - 1; i++) {
            int roll = random.nextInt(100);
            if (roll < 10)      r[i] = RoomType.EMPTY;
            else if (roll < 25) r[i] = RoomType.TREASURE;
            else if (roll < 40) r[i] = RoomType.SHOP;
            else                r[i] = RoomType.MONSTER;
        }

        return r;
    }

    public RoomType getCurrentRoomType() {
        return rooms[currentRoom];
    }

    public boolean advance() {
        if (currentRoom < totalRooms - 1) {
            currentRoom++;
            return true;
        }
        return false; // Floor complete
    }

    public boolean isComplete() {
        return currentRoom >= totalRooms - 1 && rooms[currentRoom] != RoomType.BOSS && rooms[currentRoom] != RoomType.MONSTER;
    }

    public Enemy spawnEnemy() {
        return EnemyFactory.createEnemy(floorNumber);
    }

    public int getTreasureGold() {
        return 15 + random.nextInt(floorNumber * 10);
    }

    public String getFloorMap() {
        StringBuilder sb = new StringBuilder();
        sb.append("  Floor ").append(floorNumber).append(" — Room ").append(currentRoom + 1)
          .append("/").append(totalRooms).append("\n  ");
        for (int i = 0; i < totalRooms; i++) {
            if (i == currentRoom) {
                sb.append("[★]");
            } else if (i < currentRoom) {
                sb.append("[✓]");
            } else {
                sb.append("[?]");
            }
            if (i < totalRooms - 1) sb.append("─");
        }
        return sb.toString();
    }

    public int getFloorNumber() { return floorNumber; }
    public int getTotalRooms()  { return totalRooms; }
    public int getCurrentRoom() { return currentRoom; }
}