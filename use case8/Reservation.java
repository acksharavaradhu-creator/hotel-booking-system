/**
 * Represents a reservation/booking record for history tracking
 * 
 * This class encapsulates all relevant booking information
 * without modifying the core booking system. It serves as
 * the data unit for the booking history.
 * 
 * @author Ackshara
 * @version 1.0
 */
public class Reservation {
    private int bookingId;
    private String customerName;
    private String roomId;
    private String roomType;
    private long bookingTime;
    private String status;  // CONFIRMED, CANCELLED, MODIFIED
    private double roomCost;

    /**
     * Constructor for Reservation
     * 
     * @param bookingId Unique booking identifier
     * @param customerName Name of the customer
     * @param roomId Assigned room ID
     * @param roomType Type of room (SINGLE, DOUBLE, SUITE)
     * @param bookingTime Timestamp of booking
     * @param status Booking status (CONFIRMED, CANCELLED, etc.)
     * @param roomCost Cost of the room
     */
    public Reservation(int bookingId, String customerName, String roomId, 
                      String roomType, long bookingTime, String status, double roomCost) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.roomId = roomId;
        this.roomType = roomType;
        this.bookingTime = bookingTime;
        this.status = status;
        this.roomCost = roomCost;
    }

    // Getters
    public int getBookingId() {
        return bookingId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomType() {
        return roomType;
    }

    public long getBookingTime() {
        return bookingTime;
    }

    public String getStatus() {
        return status;
    }

    public double getRoomCost() {
        return roomCost;
    }

    /**
     * Update the status of the reservation
     * 
     * @param newStatus New status
     */
    public void setStatus(String newStatus) {
        this.status = newStatus;
    }

    /**
     * String representation of the reservation
     * 
     * @return Formatted reservation information
     */
    @Override
    public String toString() {
        return String.format("Booking ID: %d | Customer: %-20s | Room: %-12s | Type: %-8s | Status: %-10s | Cost: $%.2f",
                bookingId, customerName, roomId, roomType, status, roomCost);
    }

    /**
     * Get formatted booking time
     * 
     * @return Formatted timestamp
     */
    public String getFormattedBookingTime() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new java.util.Date(bookingTime));
    }
}
