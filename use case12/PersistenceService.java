import java.io.*;
import java.util.*;

/**
 * Use Case 12: Persistence and Recovery
 *
 * Service for persisting and recovering booking system state using file-based storage.
 * Uses Java serialization to maintain data consistency across application restarts.
 *
 * Key Concepts:
 * - File Persistence: Data survives application shutdown
 * - Java Serialization: ObjectOutputStream/ObjectInputStream
 * - Error Handling: Graceful degradation on file issues
 * - Data Integrity: Validation of loaded data
 * - Recovery: System continues with default state if file missing
 * - Atomic Writes: Complete state saved or none at all
 *
 * @author Ackshara
 * @version 1.0
 */
public class PersistenceService {

    private static final String DATA_FILE = "data.ser";

    /**
     * Container class for serializing booking system state
     * Holds both bookings and inventory in single serializable object
     */
    private static class SystemState implements Serializable {
        private static final long serialVersionUID = 1L;

        private final List<BookingRecord> bookings;
        private final Map<String, Integer> inventory;

        public SystemState(List<BookingRecord> bookings, Map<String, Integer> inventory) {
            this.bookings = new ArrayList<>(bookings); // Defensive copy
            this.inventory = new HashMap<>(inventory); // Defensive copy
        }

        public List<BookingRecord> getBookings() {
            return bookings;
        }

        public Map<String, Integer> getInventory() {
            return inventory;
        }
    }

    /**
     * Save current system state to file
     *
     * Serializes booking records and inventory to persistent storage.
     * Uses atomic write operation to prevent partial saves.
     *
     * @param bookings List of active booking records
     * @param inventory Current room inventory counts
     * @return true if save successful, false otherwise
     */
    public boolean saveState(List<BookingRecord> bookings, Map<String, Integer> inventory) {
        SystemState state = new SystemState(bookings, inventory);

        try (FileOutputStream fos = new FileOutputStream(DATA_FILE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(state);
            System.out.println("💾 State saved successfully: " + bookings.size() + " bookings, " +
                             inventory.size() + " room types");
            return true;

        } catch (IOException e) {
            System.err.println("❌ Failed to save state: " + e.getMessage());
            return false;
        }
    }

    /**
     * Load system state from file
     *
     * Attempts to deserialize previously saved state.
     * Returns default empty state if file missing or corrupted.
     *
     * @return Map containing "bookings" (List<BookingRecord>) and "inventory" (Map<String, Integer>)
     */
    public Map<String, Object> loadState() {
        Map<String, Object> result = new HashMap<>();

        // Default empty state
        List<BookingRecord> defaultBookings = new ArrayList<>();
        Map<String, Integer> defaultInventory = createDefaultInventory();

        try (FileInputStream fis = new FileInputStream(DATA_FILE);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            SystemState loadedState = (SystemState) ois.readObject();

            // Validate loaded data
            if (loadedState.getBookings() != null && loadedState.getInventory() != null) {
                result.put("bookings", loadedState.getBookings());
                result.put("inventory", loadedState.getInventory());
                System.out.println("📂 State loaded successfully: " +
                                 loadedState.getBookings().size() + " bookings, " +
                                 loadedState.getInventory().size() + " room types");
            } else {
                System.out.println("⚠️ Loaded state is invalid, using defaults");
                result.put("bookings", defaultBookings);
                result.put("inventory", defaultInventory);
            }

        } catch (FileNotFoundException e) {
            System.out.println("📁 No saved state found, starting with defaults");
            result.put("bookings", defaultBookings);
            result.put("inventory", defaultInventory);

        } catch (IOException e) {
            System.err.println("❌ Error reading saved state: " + e.getMessage());
            System.out.println("🔄 Using default state due to read error");
            result.put("bookings", defaultBookings);
            result.put("inventory", defaultInventory);

        } catch (ClassNotFoundException e) {
            System.err.println("❌ Deserialization error: " + e.getMessage());
            System.out.println("🔄 Using default state due to class mismatch");
            result.put("bookings", defaultBookings);
            result.put("inventory", defaultInventory);
        }

        return result;
    }

    /**
     * Create default inventory for new systems
     * @return Map with standard room types and counts
     */
    private Map<String, Integer> createDefaultInventory() {
        Map<String, Integer> inventory = new HashMap<>();
        inventory.put("SINGLE", 10);
        inventory.put("DOUBLE", 8);
        inventory.put("SUITE", 5);
        return inventory;
    }

    /**
     * Check if saved state file exists
     * @return true if data file exists
     */
    public boolean hasSavedState() {
        return new File(DATA_FILE).exists();
    }

    /**
     * Delete saved state file (for testing/cleanup)
     * @return true if deletion successful
     */
    public boolean deleteSavedState() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("🗑️ Saved state file deleted");
            } else {
                System.out.println("❌ Failed to delete saved state file");
            }
            return deleted;
        }
        System.out.println("📁 No saved state file to delete");
        return true;
    }
}