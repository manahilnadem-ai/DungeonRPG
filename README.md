# ⚔ DungeonRPG — Java OOP Console Game

A fully-featured, turn-based dungeon crawler built entirely in **Java**, showcasing core Object-Oriented Programming principles. Designed as a portfolio project demonstrating clean architecture and professional-grade code.

---

## 🎮 Gameplay

Navigate 5 floors of a procedurally-generated dungeon. Fight monsters, collect loot, visit shops, level up, and face the **Ancient Dragon** boss on Floor 5.

```
  ██████╗ ██╗   ██╗███╗  ██╗ ██████╗ ███████╗ ██████╗ ███╗  ██╗
  ██╔══██╗██║   ██║████╗ ██║██╔════╝ ██╔════╝██╔═══██╗████╗ ██║
  ██║  ██║██║   ██║██╔██╗██║██║  ███╗█████╗  ██║   ██║██╔██╗██║
  ██║  ██║██║   ██║██║╚████║██║   ██║██╔══╝  ██║   ██║██║╚████║
  ██████╔╝╚██████╔╝██║ ╚███║╚██████╔╝███████╗╚██████╔╝██║ ╚███║
  ╚═════╝  ╚═════╝ ╚═╝  ╚══╝ ╚═════╝ ╚══════╝ ╚═════╝ ╚═╝  ╚══╝
```

---

## 🏗 OOP Concepts Demonstrated

| Concept | Where Used |
|---|---|
| **Abstraction** | `Character` abstract class defines template for all beings |
| **Inheritance** | `Player` and `Enemy` extend `Character`; `Goblin`, `Troll`, etc. extend `Enemy` |
| **Polymorphism** | `attack()` and `useSpecialAbility()` behave differently per subclass |
| **Encapsulation** | All fields private/protected; accessed through controlled getters/setters |
| **Interfaces** | `Item` interface implemented by `Weapon`, `Armor`, `Potion` |
| **Factory Pattern** | `EnemyFactory` creates appropriate enemies per dungeon floor |
| **Facade Pattern** | `GameEngine` orchestrates all subsystems through one clean API |
| **Composition** | `Player` contains an inventory (List\<Item\>); `GameEngine` composes `CombatSystem`, `GameLogger` |
| **Single Responsibility** | `CombatSystem`, `Shop`, `DungeonFloor`, `GameLogger` each handle one concern |

---

## 🧱 Project Structure

```
DungeonRPG/
├── src/
│   ├── main/
│   │   └── Main.java               # Entry point
│   ├── characters/
│   │   ├── Character.java          # Abstract base class
│   │   ├── Player.java             # Player with classes, XP, inventory
│   │   ├── Enemy.java              # Abstract enemy + Goblin, Skeleton, Troll, DarkMage, Dragon
│   │   └── EnemyFactory.java       # Factory pattern for enemy creation
│   ├── items/
│   │   ├── Item.java               # Item interface
│   │   ├── Weapon.java
│   │   ├── Armor.java
│   │   └── Potion.java
│   ├── world/
│   │   ├── DungeonFloor.java       # Procedural room generation
│   │   └── Shop.java               # In-game merchant
│   └── engine/
│       ├── GameEngine.java         # Main game loop (Facade)
│       ├── CombatSystem.java       # Turn-based combat logic
│       └── GameLogger.java         # Event logging (Observer-lite)
└── README.md
```

---

## 🎭 Hero Classes

| Class | HP | ATK | DEF | Special Ability |
|---|---|---|---|---|
| ⚔ **Warrior** | 120 | 15 | 8 | Shield Bash — 1.5× true damage, CD:3 |
| 🔮 **Mage** | 80 | 22 | 3 | Fireball — 2× magic damage, CD:3 |
| 🗡 **Rogue** | 100 | 18 | 5 | Backstab — 3× critical, CD:4 |

---

## 👾 Enemies

| Enemy | HP | Trait |
|---|---|---|
| Goblin | 40 | Sneak Attack (50% chance extra dmg) |
| Skeleton | 55 | Bone Throw (true damage) |
| Troll | 90 | Regenerates 5 HP per hit |
| Dark Mage | 70 | Life Drain (steals HP) |
| 🐉 **Ancient Dragon** | 200 | Enters rage at 50% HP, +15 ATK |

---

## ⚙ How to Run

### Prerequisites
- Java 17+ (uses sealed classes / switch expressions)

### Compile & Run

```bash
# From project root
find src -name "*.java" > sources.txt
javac -d out @sources.txt
java -cp out main.Main
```

Or with an IDE (IntelliJ IDEA / Eclipse):
1. Open the `DungeonRPG` folder as a project
2. Mark `src` as the source root
3. Run `main.Main`

---

## 📈 Features

- ✅ Turn-based combat with flee mechanic
- ✅ 3 hero classes with unique special abilities + cooldowns
- ✅ XP system with stat-boosting level-ups
- ✅ Procedurally generated dungeon floors
- ✅ Item shop with weapons, armor, and potions
- ✅ 5 unique enemy types including a Boss
- ✅ Boss rage mechanic (Dragon enters fury at 50% HP)
- ✅ HP bars rendered in console
- ✅ Adventure event log printed at game end
- ✅ Troll regeneration mechanic

---

## 🔭 Potential Extensions

- [ ] Save/load game state via file serialization
- [ ] Multiple boss types per floor
- [ ] Skill trees per class
- [ ] Random event rooms (riddles, traps)
- [ ] Multiplayer over sockets
- [ ] GUI using JavaFX or Swing

---

## 👤 Author

**Your Name**  
[LinkedIn](https://linkedin.com/in/yourprofile) · [GitHub](https://github.com/yourusername)

---

## 📄 License

MIT License — free to use and modify.
