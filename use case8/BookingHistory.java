import java.util.*;

/**
 * Maintains a history of all confirmed bookings
 * 
 * Uses a List<Reservation> to store bookings in insertion order,
 * enabling chronological tracking and reporting. This class is
 * independent from the core booking system.
 * 
 * Key Concepts:
 * - List: Maintains insertion order (important for history)
 * - Immutability: External modifications don't affect history
 * - Chronological: Bookings recorded in time sequence
 * - Non-Intrusive: No modification to existing booking logic
 * 
 * @author Ackshara
 * @version 1.0
 */
public class BookingHistory {
    
    // List maintains insertion order for chronological tracking
    // Each element represents a confirmed booking
    private List<Reservation> bookingList;

    /**
     * Constructor initializes empty booking history
     */
    public BookingHistory() {
        this.bookingList = new ArrayList<>();
    }

    /**
     * Add a booking to the history
     * 
     * Maintains insertion order. New bookings are appended to the end.
     * 
     * @param reservation Reservation to add
     * @return true if booking added successfully
     */
    public boolean addBooking(Reservation reservation) {
        if (reservation == null) {
            System.err.println("Error: Cannot add null reservation to history");
            return false;
        }
        
        // Add to the end, maintaining insertion order
        bookingList.add(reservation);
        return true;
    }

    /**
     * Get all bookings from history
     * 
     * Returns a copy to prevent external modification of history
     * 
     * @return List of all reservations in insertion order
     */
    public List<Reservation> getAllBookings() {
        // Return copy to protect internal list
        return new ArrayList<>(bookingList);
    }

    /**
     * Get a specific booking by ID
     * 
     * @param bookingId Booking ID to search for
     * @return Reservation if found, null otherwise
     */
    public Reservation getBookingById(int bookingId) {
        for (Reservation r : bookingList) {
            if (r.getBookingId() == bookingId) {
                return r;
            }
        }
        return null;
    }

    /**
     * Get all bookings for a specific customer
     * 
     * @param customerName Name of customer to search for
     * @return List of reservations for this customer
     */
    public List<Reservation> getBookingsByCustomer(String customerName) {
        List<Reservation> results = new ArrayList<>();
        for (Reservation r : bookingList) {
            if (r.getCustomerName().equalsIgnoreCase(customerName)) {
                results.add(r);
            }
        }
        return results;
    }

    /**
     * Get all bookings for a specific room type
     * 
     * @param roomType Room type to search for
     * @return List of reservations for this room type
     */
    public List<Reservation> getBookingsByRoomType(String roomType) {
        List<Reservation> results = new ArrayList<>();
        for (Reservation r : bookingList) {
            if (r.getRoomType().equalsIgnoreCase(roomType)) {
                results.add(r);
            }
        }
        return results;
    }

    /**
     * Get all bookings with a specific status
     * 
     * @param status Status to search for (CONFIRMED, CANCELLED, etc.)
     * @return List of reservations with this status
     */
    public List<Reservation> getBookingsByStatus(String status) {
        List<Reservation> results = new ArrayList<>();
        for (Reservation r : bookingList) {
            if (r.getStatus().equalsIgnoreCase(status)) {
                results.add(r);
            }
        }
        return results;
    }

    /**
     * Get total number of bookings in history
     * 
     * @return Count of all bookings
     */
    public int getTotalBookings() {
        return bookingList.size();
    }

    /**
     * Get number of bookings with a specific status
     * 
     * @param status Status to count
     * @return Count of bookings with this status
     */
    public int getBookingCountByStatus(String status) {
        int count = 0;
        for (Reservation r : bookingList) {
            if (r.getStatus().equalsIgnoreCase(status)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Calculate total revenue from all bookings
     * 
     * @return Sum of all room costs
     */
    public double calculateTotalRevenue() {
        double total = 0.0;
        for (Reservation r : bookingList) {
            if (r.getStatus().equalsIgnoreCase("CONFIRMED")) {
                total += r.getRoomCost();
            }
        }
        return total;
    }

    /**
     * Check if booking history is empty
     * 
     * @return true if no bookings recorded
     */
    public boolean isEmpty() {
        return bookingList.isEmpty();
    }

    /**
     * Clear all booking history
     * 
     * Warning: This removes all records. Use with caution.
     */
    public void clearHistory() {
        bookingList.clear();
    }

    /**
     * Get the earliest (first) booking in history
     * 
     * @return First reservation, or null if empty
     */
    public Reservation getEarliestBooking() {
        if (bookingList.isEmpty()) {
            return null;
        }
        return bookingList.get(0);
    }

    /**
     * Get the latest (most recent) booking in history
     * 
     * @return Last reservation, or null if empty
     */
    public Reservation getLatestBooking() {
        if (bookingList.isEmpty()) {
            return null;
        }
        return bookingList.get(bookingList.size() - 1);
    }

    /**
     * Search bookings by partial customer name
     * 
     * @param namePattern Partial name to search
     * @return List of matching reservations
     */
    public List<Reservation> searchByCustomerName(String namePattern) {
        List<Reservation> results = new ArrayList<>();
        String pattern = namePattern.toLowerCase();
        for (Reservation r : bookingList) {
            if (r.getCustomerName().toLowerCase().contains(pattern)) {
                results.add(r);
            }
        }
        return results;
    }
}
