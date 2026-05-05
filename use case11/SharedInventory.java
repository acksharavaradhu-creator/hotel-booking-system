import java.util.*;

/**
 * Use Case 11: Concurrent Booking Requests
 *
 * Thread-safe inventory management for concurrent room allocation.
 * Ensures no double booking through synchronized access to shared state.
 *
 * Key Concepts:
 * - Shared State: Single inventory instance accessed by multiple threads
 * - Thread Safety: Synchronized methods prevent race conditions
 * - Atomic Operations: Check-and-decrement as single operation
 * - Consistency: Inventory counts always reflect true availability
 * - Fail-Fast: Immediate rejection when rooms unavailable
 *
 * @author Ackshara
 * @version 1.0
 */
public class SharedInventory {

    private final Map<String, Integer> inventory;

    /**
     * Constructor initializes inventory with default room counts
     */
    public SharedInventory() {
        this.inventory = new HashMap<>();
        initializeInventory();
    }

    /**
     * Initialize inventory with standard room types and counts
     */
    private void initializeInventory() {
        inventory.put("SINGLE", 5);  // 5 single rooms
        inventory.put("DOUBLE", 3);  // 3 double rooms
        inventory.put("SUITE", 2);   // 2 suite rooms
    }

    /**
     * Thread-safe room allocation with atomic check-and-decrement
     *
     * Synchronized to prevent race conditions in concurrent access.
     * Ensures no double booking by checking availability before allocation.
     *
     * @param roomType Type of room to allocate (SINGLE, DOUBLE, SUITE)
     * @return true if allocation successful, false if room unavailable
     */
    public synchronized boolean allocateRoom(String roomType) {
        // Validate room type
        if (roomType == null || !inventory.containsKey(roomType.toUpperCase())) {
            System.out.println("❌ Invalid room type: " + roomType);
            return false;
        }

        String upperRoomType = roomType.toUpperCase();
        int currentCount = inventory.get(upperRoomType);

        // Check availability
        if (currentCount <= 0) {
            System.out.println("❌ No " + upperRoomType + " rooms available (count: " + currentCount + ")");
            return false;
        }

        // Atomic allocation: decrement count
        inventory.put(upperRoomType, currentCount - 1);
        System.out.println("✅ Allocated " + upperRoomType + " room (remaining: " + (currentCount - 1) + ")");
        return true;
    }

    /**
     * Get current inventory status (thread-safe copy)
     * @return Copy of inventory map for safe external access
     */
    public synchronized Map<String, Integer> getInventory() {
        return new HashMap<>(inventory);
    }

    /**
     * Get available count for specific room type
     * @param roomType Room type to check
     * @return Available count, or -1 if invalid room type
     */
    public synchronized int getAvailableCount(String roomType) {
        if (roomType == null) return -1;
        String upperRoomType = roomType.toUpperCase();
        return inventory.getOrDefault(upperRoomType, -1);
    }

    /**
     * Print current inventory status
     */
    public synchronized void printInventory() {
        System.out.println("=== Current Inventory ===");
        inventory.forEach((type, count) ->
            System.out.println("├─ " + type + ": " + count + " available"));
        System.out.println("========================\n");
    }
}