import java.util.*;

/**
 * Use Case 4: Search Available Rooms (Read-Only)
 *
 * Demonstrates safe data access without modifying system state.
 */

// Room class (Domain Model)
class Room {
    String type;
    double price;

    Room(String type, double price) {
        this.type = type;
        this.price = price;
    }

    void display() {
        System.out.println(type + " | Price: " + price);
    }
}

// Inventory class (State Holder)
class RoomInventory {
    private HashMap<String, Integer> inventory;

    RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 0);
        inventory.put("Suite Room", 1);
    }

    // Read-only access
    int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }
}

// Search Service (Read-only logic)
class SearchService {

    void searchRooms(ArrayList<Room> rooms, RoomInventory inventory) {

        System.out.println("Available Rooms:\n");

        for (Room r : rooms) {

            int available = inventory.getAvailability(r.type);

            // Filter only available rooms
            if (available > 0) {
                r.display();
                System.out.println("Available Count: " + available);
            }
        }
    }
}

// Main class
public class Main {
    public static void main(String[] args) {

        // Room data
        ArrayList<Room> rooms = new ArrayList<>();
        rooms.add(new Room("Single Room", 1000));
        rooms.add(new Room("Double Room", 2000));
        rooms.add(new Room("Suite Room", 5000));

        // Inventory
        RoomInventory inventory = new RoomInventory();

        // Search
        SearchService service = new SearchService();
        service.searchRooms(rooms, inventory);
    }
}