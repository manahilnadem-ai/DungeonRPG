package engine;

import java.util.ArrayList;
import java.util.List;

/**
 * GameLogger — records significant game events.
 * Demonstrates: Observer Pattern (simplified), Single Responsibility Principle
 */
public class GameLogger {

    private final List<String> log;

    public GameLogger() {
        this.log = new ArrayList<>();
    }

    public void log(String event) {
        log.add(event);
    }

    public void printLog() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║          📜 ADVENTURE LOG             ║");
        System.out.println("╚══════════════════════════════════════╝");
        if (log.isEmpty()) {
            System.out.println("  (no events recorded)");
        } else {
            for (int i = 0; i < log.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + log.get(i));
            }
        }
    }

    public List<String> getLog() { return log; }
}