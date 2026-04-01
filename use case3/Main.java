import java.util.*;

/**
 * Use Case 3: Centralized Inventory using HashMap
 *
 * Demonstrates how HashMap manages room availability.
 */

class RoomInventory {

    // HashMap to store room type and count
    private HashMap<String, Integer> inventory;

    // Constructor
    RoomInventory() {
        inventory = new HashMap<>();

        // Initialize rooms
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Display inventory
    void displayInventory() {
        System.out.println("Room Inventory:\n");

        for (String roomType : inventory.keySet()) {
            System.out.println(roomType + " : " + inventory.get(roomType));
        }
    }

    // Get availability
    int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability
    void updateAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }
}

public class Main {
    public static void main(String[] args) {

        // Create inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory
        inventory.displayInventory();

        // Example update
        inventory.updateAvailability("Single Room", 4);

        System.out.println("\nAfter Update:");
        inventory.displayInventory();
    }
}