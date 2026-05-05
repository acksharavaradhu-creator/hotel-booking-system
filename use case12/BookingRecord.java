import java.io.Serializable;

/**
 * Use Case 12: Persistence and Recovery
 *
 * Serializable representation of a booking record for file-based storage.
 * Enables persistence of booking state across application restarts.
 *
 * Key Concepts:
 * - Serializable: Enables object serialization for file storage
 * - Immutable Data: Fields set at construction, no setters
 * - Data Persistence: Survives application shutdown/restart
 * - Thread Safety: Immutable objects are inherently thread-safe
 * - Data Integrity: Consistent state representation
 *
 * @author Ackshara
 * @version 1.0
 */
public class BookingRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String reservationId;
    private final String roomType;

    /**
     * Constructor for booking record
     * @param reservationId Unique reservation identifier
     * @param roomType Type of room booked (SINGLE, DOUBLE, SUITE)
     */
    public BookingRecord(String reservationId, String roomType) {
        this.reservationId = reservationId;
        this.roomType = roomType;
    }

    /**
     * Get the reservation ID
     * @return Unique reservation identifier
     */
    public String getReservationId() {
        return reservationId;
    }

    /**
     * Get the room type
     * @return Room type (SINGLE, DOUBLE, SUITE)
     */
    public String getRoomType() {
        return roomType;
    }

    /**
     * String representation for debugging and logging
     * @return Formatted string showing reservation details
     */
    @Override
    public String toString() {
        return "BookingRecord{" +
               "reservationId='" + reservationId + '\'' +
               ", roomType='" + roomType + '\'' +
               '}';
    }

    /**
     * Equality check based on reservation ID
     * @param obj Object to compare
     * @return true if objects represent same reservation
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BookingRecord that = (BookingRecord) obj;
        return reservationId != null ? reservationId.equals(that.reservationId) : that.reservationId == null;
    }

    /**
     * Hash code based on reservation ID
     * @return Hash code for this booking record
     */
    @Override
    public int hashCode() {
        return reservationId != null ? reservationId.hashCode() : 0;
    }
}