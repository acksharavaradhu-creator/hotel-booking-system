import java.util.*;

class Hotel {
    // Room management
    HashMap<String, Room> roomsByType = new HashMap<>();  // Maps room type to Room object
    HashMap<String, Room> allRoomsById = new HashMap<>();  // Maps room ID to Room object

    // Booking management
    HashMap<Integer, Booking> bookings = new HashMap<>();
    Queue<BookingRequest> bookingQueue = new LinkedList<>();  // FIFO booking request queue

    // Allocation tracking - Key concepts for preventing double-booking
    Set<String> allocatedRoomIds = new HashSet<>();  // Prevents reuse of room IDs
    HashMap<String, Set<String>> roomTypeToAllocatedIds = new HashMap<>();  // Maps room types to allocated room IDs

    int bookingCounter = 1;
    int roomIdCounter = 1000;  // Counter for generating unique room IDs

    // Add a room type with initial inventory
    void addRoomType(String type, int inventory) {
        Room room = new Room(type + "-TYPE", type, inventory);
        roomsByType.put(type, room);
        roomTypeToAllocatedIds.putIfAbsent(type, new HashSet<>());
        System.out.println("Room type '" + type + "' added with " + inventory + " available units.");
    }

    // Generate unique room ID (atomic operation)
    private synchronized String generateUniqueRoomId(String roomType) {
        String roomId;
        do {
            roomId = roomType + "-" + (++roomIdCounter);
        } while (allocatedRoomIds.contains(roomId));  // Ensure uniqueness
        return roomId;
    }

    // Process a booking request (FIFO)
    void queueBookingRequest(String customerName, String roomType) {
        bookingQueue.add(new BookingRequest(customerName, roomType));
        System.out.println("Booking request queued for " + customerName + " (" + roomType + ")");
    }

    // Process all queued booking requests
    void processBookingQueue() {
        System.out.println("\n=== Processing Booking Queue ===");
        while (!bookingQueue.isEmpty()) {
            BookingRequest request = bookingQueue.poll();
            confirmBooking(request.customerName, request.roomType);
        }
    }

    // Atomic booking confirmation with allocation and inventory update
    private synchronized boolean confirmBooking(String customerName, String roomType) {
        // Step 1: Check if room type exists
        if (!roomsByType.containsKey(roomType)) {
            System.out.println("❌ Room type '" + roomType + "' not found.");
            return false;
        }

        Room room = roomsByType.get(roomType);

        // Step 2: Check availability (atomic with inventory)
        if (!room.hasAvailability()) {
            System.out.println("❌ No rooms available for type '" + roomType + "'. Request for " + customerName + " cannot be fulfilled.");
            return false;
        }

        // Step 3: Generate unique room ID
        String uniqueRoomId = generateUniqueRoomId(roomType);

        // Step 4: Record room ID to prevent reuse (atomic)
        allocatedRoomIds.add(uniqueRoomId);
        roomTypeToAllocatedIds.get(roomType).add(uniqueRoomId);

        // Step 5: Decrement inventory immediately (atomic with allocation)
        if (!room.decrementInventory()) {
            // Rollback if inventory update fails
            allocatedRoomIds.remove(uniqueRoomId);
            roomTypeToAllocatedIds.get(roomType).remove(uniqueRoomId);
            System.out.println("❌ Inventory update failed for " + customerName + ".");
            return false;
        }

        // Step 6: Create and confirm reservation
        Booking booking = new Booking(bookingCounter++, customerName, uniqueRoomId, roomType);
        bookings.put(booking.bookingId, booking);

        // Step 7: Log confirmation
        System.out.println("✓ Booking confirmed!");
        System.out.println("  Booking ID: " + booking.bookingId);
        System.out.println("  Customer: " + customerName);
        System.out.println("  Room ID: " + uniqueRoomId);
        System.out.println("  Room Type: " + roomType);
        System.out.println("  Remaining Inventory: " + room.inventory);

        return true;
    }

    // Cancel a booking and restore inventory
    void cancelBooking(int bookingId) {
        if (!bookings.containsKey(bookingId)) {
            System.out.println("❌ Invalid Booking ID: " + bookingId);
            return;
        }

        synchronized (this) {
            Booking booking = bookings.remove(bookingId);

            // Remove from allocation tracking
            allocatedRoomIds.remove(booking.roomId);
            if (roomTypeToAllocatedIds.containsKey(booking.roomType)) {
                roomTypeToAllocatedIds.get(booking.roomType).remove(booking.roomId);
            }

            // Restore inventory
            if (roomsByType.containsKey(booking.roomType)) {
                roomsByType.get(booking.roomType).incrementInventory();
            }

            System.out.println("✓ Booking " + bookingId + " cancelled. Room " + booking.roomId + " released.");
        }
    }

    // View all room types and their availability
    void viewRooms() {
        System.out.println("\n=== Room Inventory ===");
        for (String type : roomsByType.keySet()) {
            Room room = roomsByType.get(type);
            int allocated = roomTypeToAllocatedIds.get(type).size();
            System.out.println("Type: " + type + " | Available: " + room.inventory + " | Allocated: " + allocated);
        }
    }

    // View all active bookings
    void viewBookings() {
        System.out.println("\n=== Active Bookings ===");
        if (bookings.isEmpty()) {
            System.out.println("No active bookings.");
            return;
        }
        for (Booking b : bookings.values()) {
            System.out.println(b);
        }
    }

    // View allocation state (for verification)
    void viewAllocationState() {
        System.out.println("\n=== Allocation State ===");
        System.out.println("Total Allocated Room IDs: " + allocatedRoomIds.size());
        for (String type : roomTypeToAllocatedIds.keySet()) {
            Set<String> ids = roomTypeToAllocatedIds.get(type);
            System.out.println("Type '" + type + "': " + ids);
        }
    }
}

// Helper class for booking requests
class BookingRequest {
    String customerName;
    String roomType;

    BookingRequest(String customerName, String roomType) {
        this.customerName = customerName;
        this.roomType = roomType;
    }
}