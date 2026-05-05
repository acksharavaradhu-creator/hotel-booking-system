import java.util.*;

/**
 * Use Case 10: Safe Cancellation with Rollback Logic
 *
 * Implements safe cancellation of confirmed bookings with rollback capability,
 * ensuring inventory consistency and system state integrity.
 *
 * Key Concepts:
 * - Atomic Operations: Cancellation is all-or-nothing
 * - Rollback Stack: LIFO structure for undo operations
 * - State Consistency: Inventory and bookings stay synchronized
 * - Validation First: Check before acting
 * - Error Recovery: Clear error messages for debugging
 *
 * @author Ackshara
 * @version 1.0
 */
public class CancellationService {

    // Core data structures for booking management
    private Map<String, String> bookingMap;        // reservationId → roomType
    private Map<String, Integer> inventory;        // roomType → available count
    private Stack<String> rollbackStack;           // LIFO stack for cancelled reservations

    /**
     * Constructor initializes data structures for booking management
     */
    public CancellationService() {
        this.bookingMap = new HashMap<>();
        this.inventory = new HashMap<>();
        this.rollbackStack = new Stack<>();

        // Initialize inventory with default room types and counts
        initializeInventory();
    }

    /**
     * Initialize inventory with standard room types and their counts
     */
    private void initializeInventory() {
        inventory.put("SINGLE", 10);
        inventory.put("DOUBLE", 8);
        inventory.put("SUITE", 5);
    }

    /**
     * Add a booking to the system (for testing purposes)
     * @param reservationId Unique reservation identifier
     * @param roomType Type of room booked
     * @return true if booking added successfully, false if room type invalid
     */
    public boolean addBooking(String reservationId, String roomType) {
        if (reservationId == null || reservationId.trim().isEmpty()) {
            System.out.println("❌ Failed to add booking: Invalid reservation ID");
            return false;
        }

        if (roomType == null || !inventory.containsKey(roomType.toUpperCase())) {
            System.out.println("❌ Failed to add booking: Invalid room type '" + roomType + "'");
            return false;
        }

        // Check if room is available
        String upperRoomType = roomType.toUpperCase();
        int currentCount = inventory.get(upperRoomType);
        if (currentCount <= 0) {
            System.out.println("❌ Failed to add booking: No " + upperRoomType + " rooms available");
            return false;
        }

        // Add booking and decrease inventory
        bookingMap.put(reservationId, upperRoomType);
        inventory.put(upperRoomType, currentCount - 1);

        System.out.println("✅ Booking added: " + reservationId + " → " + upperRoomType);
        return true;
    }

    /**
     * Cancel a booking with rollback capability
     *
     * Atomic operation that:
     * 1. Validates reservation exists
     * 2. Records cancellation in rollback stack
     * 3. Restores inventory count
     * 4. Removes booking record
     *
     * @param reservationId The reservation to cancel
     * @return true if cancellation successful, false otherwise
     */
    public boolean cancelBooking(String reservationId) {
        // Step 1: Validate reservation exists
        if (reservationId == null || reservationId.trim().isEmpty()) {
            System.out.println("❌ Cancellation failed: Invalid reservation ID '" + reservationId + "'");
            return false;
        }

        if (!bookingMap.containsKey(reservationId)) {
            System.out.println("❌ Cancellation failed: Reservation '" + reservationId + "' not found");
            return false;
        }

        // Step 2: Get room type before removal
        String roomType = bookingMap.get(reservationId);

        // Step 3: Push to rollback stack (LIFO for potential undo)
        rollbackStack.push(reservationId);
        System.out.println("📝 Recorded cancellation in rollback stack: " + reservationId);

        // Step 4: Restore inventory count
        int currentCount = inventory.get(roomType);
        inventory.put(roomType, currentCount + 1);
        System.out.println("📦 Restored inventory: " + roomType + " count increased to " + (currentCount + 1));

        // Step 5: Remove booking from active bookings
        bookingMap.remove(reservationId);
        System.out.println("🗑️  Removed booking from active records: " + reservationId);

        System.out.println("✅ Cancellation successful: " + reservationId + " (" + roomType + ")");
        return true;
    }

    /**
     * Get current inventory status
     * @return Copy of inventory map for safe external access
     */
    public Map<String, Integer> getInventory() {
        return new HashMap<>(inventory);
    }

    /**
     * Get current active bookings
     * @return Copy of booking map for safe external access
     */
    public Map<String, String> getActiveBookings() {
        return new HashMap<>(bookingMap);
    }

    /**
     * Get rollback stack (for monitoring/debugging)
     * @return Copy of rollback stack
     */
    public Stack<String> getRollbackStack() {
        return new Stack<String>() {{
            addAll(rollbackStack);
        }};
    }

    /**
     * Print current system state for debugging
     */
    public void printSystemState() {
        System.out.println("\n=== System State ===");
        System.out.println("Active Bookings: " + bookingMap);
        System.out.println("Inventory: " + inventory);
        System.out.println("Rollback Stack: " + rollbackStack);
        System.out.println("===================\n");
    }
}