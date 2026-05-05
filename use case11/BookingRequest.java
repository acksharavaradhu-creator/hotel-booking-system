/**
 * Use Case 11: Concurrent Booking Requests
 *
 * Represents a booking request with guest information and room preferences.
 * Used in concurrent processing scenarios to ensure thread-safe allocation.
 *
 * Key Concepts:
 * - Immutable Data: Fields set at construction time
 * - Thread Safety: No mutable state, safe for concurrent access
 * - Data Transfer: Carries booking information between threads
 *
 * @author Ackshara
 * @version 1.0
 */
public class BookingRequest {

    private final String guestName;
    private final String roomType;

    /**
     * Constructor for booking request
     * @param guestName Name of the guest making the booking
     * @param roomType Type of room requested (SINGLE, DOUBLE, SUITE)
     */
    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    /**
     * Get the guest name
     * @return Guest name
     */
    public String getGuestName() {
        return guestName;
    }

    /**
     * Get the room type
     * @return Room type (SINGLE, DOUBLE, SUITE)
     */
    public String getRoomType() {
        return roomType;
    }

    /**
     * String representation for debugging
     * @return Formatted string showing guest and room type
     */
    @Override
    public String toString() {
        return guestName + " → " + roomType;
    }
}