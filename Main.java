/**
 * Hotel Booking Application
 * Entry point demonstrating safe room allocation with double-booking prevention
 *
 * @author Ackshara
 * @version 2.0
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("====== Hotel Booking Management System v2.0 ======");
        System.out.println("Safe Room Allocation & Double-Booking Prevention");
        System.out.println("===================================================");

        Hotel hotel = new Hotel();

        // Initialize room types with inventory
        System.out.println("\n--- Initializing Hotel Inventory ---");
        hotel.addRoomType("SINGLE", 5);
        hotel.addRoomType("DOUBLE", 3);
        hotel.addRoomType("SUITE", 2);

        // Display initial room inventory
        hotel.viewRooms();

        // Queue booking requests (simulating FIFO processing)
        System.out.println("\n--- Queueing Booking Requests ---");
        hotel.queueBookingRequest("Alice Johnson", "SINGLE");
        hotel.queueBookingRequest("Bob Smith", "DOUBLE");
        hotel.queueBookingRequest("Carol White", "SINGLE");
        hotel.queueBookingRequest("David Brown", "SUITE");
        hotel.queueBookingRequest("Eve Davis", "DOUBLE");
        hotel.queueBookingRequest("Frank Wilson", "SINGLE");
        hotel.queueBookingRequest("Grace Lee", "SINGLE");
        hotel.queueBookingRequest("Henry Martinez", "SINGLE");  // This will exceed inventory

        // Process all queued bookings atomically
        hotel.processBookingQueue();

        // View final state
        hotel.viewRooms();
        hotel.viewBookings();
        hotel.viewAllocationState();

        // Demonstrate cancellation and recovery
        System.out.println("\n--- Testing Cancellation ---");
        hotel.cancelBooking(1);  // Cancel Alice's booking
        hotel.viewRooms();

        // Queue new request after cancellation
        System.out.println("\n--- Processing New Request After Cancellation ---");
        hotel.queueBookingRequest("Ivan Chen", "SINGLE");
        hotel.processBookingQueue();

        hotel.viewBookings();
        hotel.viewAllocationState();

        System.out.println("\n✓ Hotel Booking System operations completed successfully.");
    }
}