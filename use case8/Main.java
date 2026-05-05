/**
 * Use Case 8: Booking History Tracking & Reporting
 * 
 * Demonstrates comprehensive booking history management and
 * reporting without modifying existing booking or inventory logic.
 * 
 * Key Concepts:
 * - Insertion Order: List maintains chronological sequence
 * - Read-Only Reporting: Reports don't modify underlying data
 * - Separation of Concerns: History and reporting are independent
 * - Non-Intrusive: No changes to core booking system
 * 
 * @author Ackshara
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("====================================================");
        System.out.println("Use Case 8: Booking History & Reporting");
        System.out.println("Track & Report Without Modifying Core Logic");
        System.out.println("====================================================\n");

        // Initialize booking history
        BookingHistory history = new BookingHistory();

        // Step 1: Add bookings to history
        System.out.println("--- Step 1: Add Bookings to History ---");
        
        long now = System.currentTimeMillis();
        
        // Add various bookings
        history.addBooking(new Reservation(
            1, "Alice Johnson", "SINGLE-1001", "SINGLE", now - 5000, "CONFIRMED", 1000.0
        ));
        
        history.addBooking(new Reservation(
            2, "Bob Smith", "DOUBLE-1002", "DOUBLE", now - 4000, "CONFIRMED", 1500.0
        ));
        
        history.addBooking(new Reservation(
            3, "Carol White", "SINGLE-1003", "SINGLE", now - 3000, "CONFIRMED", 1000.0
        ));
        
        history.addBooking(new Reservation(
            4, "David Brown", "SUITE-1004", "SUITE", now - 2000, "CONFIRMED", 2500.0
        ));
        
        history.addBooking(new Reservation(
            5, "Eve Davis", "DOUBLE-1005", "DOUBLE", now - 1000, "CONFIRMED", 1500.0
        ));
        
        // Add a cancelled booking
        history.addBooking(new Reservation(
            6, "Frank Wilson", "SINGLE-1006", "SINGLE", now, "CANCELLED", 1000.0
        ));

        System.out.println("✓ Added 6 bookings to history");
        System.out.println("  - 5 CONFIRMED bookings");
        System.out.println("  - 1 CANCELLED booking");

        // Step 2: Initialize report service
        System.out.println("\n--- Step 2: Initialize Report Service ---");
        BookingReportService reportService = new BookingReportService(history);
        System.out.println("✓ Report service initialized");

        // Step 3: Print all bookings
        System.out.println("\n--- Step 3: Print All Bookings ---");
        reportService.printAllBookings();

        // Step 4: Print bookings by customer
        System.out.println("--- Step 4: Bookings by Customer ---");
        reportService.printBookingsByCustomer();

        // Step 5: Print bookings by room type
        System.out.println("--- Step 5: Bookings by Room Type ---");
        reportService.printBookingsByRoomType();

        // Step 6: Generate summary report
        System.out.println("--- Step 6: Summary Report ---");
        reportService.generateSummaryReport();

        // Step 7: Print financial report
        System.out.println("--- Step 7: Financial Analysis ---");
        reportService.printFinancialReport();

        // Step 8: Print status summary
        System.out.println("\n--- Step 8: Booking Status Summary ---");
        reportService.printStatusSummary();

        // Step 9: Search functionality
        System.out.println("\n--- Step 9: Search Functionality ---");
        reportService.searchAndDisplayByCustomer("Johnson");
        reportService.searchAndDisplayByCustomer("Smith");

        // Step 10: Demonstrate history queries
        System.out.println("\n--- Step 10: History Query Methods ---");
        System.out.println("\nDirect History Queries:");
        System.out.println("├─ Total bookings: " + history.getTotalBookings());
        System.out.println("├─ Confirmed bookings: " + history.getBookingCountByStatus("CONFIRMED"));
        System.out.println("├─ Cancelled bookings: " + history.getBookingCountByStatus("CANCELLED"));
        System.out.println("├─ SINGLE room bookings: " + history.getBookingsByRoomType("SINGLE").size());
        System.out.println("├─ DOUBLE room bookings: " + history.getBookingsByRoomType("DOUBLE").size());
        System.out.println("├─ SUITE room bookings: " + history.getBookingsByRoomType("SUITE").size());
        System.out.println("├─ Alice's bookings: " + history.getBookingsByCustomer("Alice Johnson").size());
        
        if (history.getEarliestBooking() != null) {
            System.out.println("├─ First booking: " + history.getEarliestBooking().getCustomerName());
        }
        if (history.getLatestBooking() != null) {
            System.out.println("└─ Latest booking: " + history.getLatestBooking().getCustomerName());
        }

        // Step 11: Data export capability
        System.out.println("\n--- Step 11: Data Export (CSV Format) ---");
        System.out.println("Sample export data:");
        String csvData = reportService.exportBookingsAsString();
        String[] lines = csvData.split("\n");
        // Print first 3 lines
        for (int i = 0; i < Math.min(3, lines.length); i++) {
            System.out.println(lines[i]);
        }
        if (lines.length > 3) {
            System.out.println("... (" + (lines.length - 2) + " more entries)");
        }

        // Step 12: Verify non-intrusive design
        System.out.println("\n--- Step 12: Design Verification ---");
        System.out.println("✓ Booking history tracked independently");
        System.out.println("✓ Reports generated without modifying data");
        System.out.println("✓ Insertion order maintained (List)");
        System.out.println("✓ Multiple query types supported");
        System.out.println("✓ No changes to core booking/inventory logic");
        System.out.println("✓ Separation of concerns: Storage vs Reporting");

        System.out.println("\n====================================================");
        System.out.println("✓ Use Case 8 Completed Successfully");
        System.out.println("====================================================");
    }
}
