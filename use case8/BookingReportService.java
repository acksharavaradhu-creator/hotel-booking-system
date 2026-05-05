import java.util.*;

/**
 * Generates reports from booking history without modifying stored data
 * 
 * This service provides various reporting methods to analyze and
 * visualize booking data. All operations are read-only and do not
 * affect the underlying booking history.
 * 
 * Key Concepts:
 * - Read-Only: Reporting does not modify data
 * - Analysis: Multiple perspectives on booking data
 * - Formatting: Clear, organized report output
 * - Independence: Separate from booking and inventory systems
 * 
 * @author Ackshara
 * @version 1.0
 */
public class BookingReportService {
    
    private BookingHistory history;

    /**
     * Constructor initializes report service with booking history
     * 
     * @param bookingHistory BookingHistory instance to report on
     */
    public BookingReportService(BookingHistory bookingHistory) {
        this.history = bookingHistory;
    }

    /**
     * Print all bookings in a formatted table
     */
    public void printAllBookings() {
        List<Reservation> bookings = history.getAllBookings();
        
        if (bookings.isEmpty()) {
            System.out.println("\n=== Booking Report ===");
            System.out.println("No bookings in history.");
            return;
        }
        
        System.out.println("\n=== All Bookings ===");
        System.out.println("Total Bookings: " + bookings.size());
        System.out.println("────────────────────────────────────────────────────────────────────────────────");
        
        for (Reservation r : bookings) {
            System.out.println(r);
        }
        
        System.out.println("────────────────────────────────────────────────────────────────────────────────");
    }

    /**
     * Print bookings by customer with detailed information
     */
    public void printBookingsByCustomer() {
        List<Reservation> bookings = history.getAllBookings();
        
        if (bookings.isEmpty()) {
            System.out.println("\nNo bookings to display.");
            return;
        }
        
        System.out.println("\n=== Bookings by Customer ===");
        
        // Group by customer
        Map<String, List<Reservation>> byCustomer = new LinkedHashMap<>();
        for (Reservation r : bookings) {
            String customer = r.getCustomerName();
            byCustomer.putIfAbsent(customer, new ArrayList<>());
            byCustomer.get(customer).add(r);
        }
        
        // Display grouped results
        int customerCount = 1;
        for (String customer : byCustomer.keySet()) {
            List<Reservation> customerBookings = byCustomer.get(customer);
            System.out.println("\n" + customerCount + ". " + customer);
            System.out.println("   Total Bookings: " + customerBookings.size());
            
            for (Reservation r : customerBookings) {
                System.out.println("   ├─ " + r.getRoomId() + " (" + r.getRoomType() + 
                                 ") - Status: " + r.getStatus() + " - $" + 
                                 String.format("%.2f", r.getRoomCost()));
            }
            customerCount++;
        }
    }

    /**
     * Print bookings by room type with statistics
     */
    public void printBookingsByRoomType() {
        List<Reservation> bookings = history.getAllBookings();
        
        if (bookings.isEmpty()) {
            System.out.println("\nNo bookings to display.");
            return;
        }
        
        System.out.println("\n=== Bookings by Room Type ===");
        
        // Group by room type
        Map<String, List<Reservation>> byType = new LinkedHashMap<>();
        for (Reservation r : bookings) {
            String type = r.getRoomType();
            byType.putIfAbsent(type, new ArrayList<>());
            byType.get(type).add(r);
        }
        
        // Display with statistics
        for (String type : byType.keySet()) {
            List<Reservation> typeBookings = byType.get(type);
            int confirmed = 0;
            double revenue = 0.0;
            
            for (Reservation r : typeBookings) {
                if (r.getStatus().equalsIgnoreCase("CONFIRMED")) {
                    confirmed++;
                    revenue += r.getRoomCost();
                }
            }
            
            System.out.println("\n" + type + " Rooms");
            System.out.println("├─ Total Bookings: " + typeBookings.size());
            System.out.println("├─ Confirmed: " + confirmed);
            System.out.println("├─ Cancelled: " + (typeBookings.size() - confirmed));
            System.out.println("└─ Revenue: $" + String.format("%.2f", revenue));
        }
    }

    /**
     * Generate comprehensive summary report
     */
    public void generateSummaryReport() {
        List<Reservation> bookings = history.getAllBookings();
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println(" ".repeat(15) + "BOOKING SUMMARY REPORT");
        System.out.println("=".repeat(70));
        
        if (bookings.isEmpty()) {
            System.out.println("No bookings in history.");
            System.out.println("=".repeat(70) + "\n");
            return;
        }
        
        // Calculate statistics
        int totalBookings = bookings.size();
        int confirmedBookings = history.getBookingCountByStatus("CONFIRMED");
        int cancelledBookings = history.getBookingCountByStatus("CANCELLED");
        double totalRevenue = history.calculateTotalRevenue();
        
        // Count by room type
        Map<String, Integer> roomTypeCounts = new HashMap<>();
        for (Reservation r : bookings) {
            String type = r.getRoomType();
            roomTypeCounts.put(type, roomTypeCounts.getOrDefault(type, 0) + 1);
        }
        
        // Print summary
        System.out.println("\nOVERALL STATISTICS:");
        System.out.println("├─ Total Bookings: " + totalBookings);
        System.out.println("├─ Confirmed Bookings: " + confirmedBookings);
        System.out.println("├─ Cancelled Bookings: " + cancelledBookings);
        System.out.println("├─ Confirmation Rate: " + 
                         String.format("%.1f%%", (confirmedBookings * 100.0 / totalBookings)));
        System.out.println("└─ Total Revenue: $" + String.format("%.2f", totalRevenue));
        
        System.out.println("\nBOOKINGS BY ROOM TYPE:");
        for (String type : roomTypeCounts.keySet()) {
            int count = roomTypeCounts.get(type);
            System.out.println("├─ " + type + ": " + count + " booking(s)");
        }
        
        System.out.println("\nTIMELINE:");
        if (!bookings.isEmpty()) {
            Reservation first = history.getEarliestBooking();
            Reservation last = history.getLatestBooking();
            System.out.println("├─ First Booking: " + first.getCustomerName() + 
                             " (" + first.getFormattedBookingTime() + ")");
            System.out.println("└─ Latest Booking: " + last.getCustomerName() + 
                             " (" + last.getFormattedBookingTime() + ")");
        }
        
        System.out.println("\n" + "=".repeat(70) + "\n");
    }

    /**
     * Print detailed financial report
     */
    public void printFinancialReport() {
        List<Reservation> bookings = history.getAllBookings();
        
        if (bookings.isEmpty()) {
            System.out.println("\n=== Financial Report ===");
            System.out.println("No bookings to report.");
            return;
        }
        
        System.out.println("\n=== Financial Report ===");
        
        double totalRevenue = 0.0;
        double confirmedRevenue = 0.0;
        
        for (Reservation r : bookings) {
            totalRevenue += r.getRoomCost();
            if (r.getStatus().equalsIgnoreCase("CONFIRMED")) {
                confirmedRevenue += r.getRoomCost();
            }
        }
        
        double cancelledRevenue = totalRevenue - confirmedRevenue;
        
        System.out.println("Total Revenue (All Bookings): $" + String.format("%.2f", totalRevenue));
        System.out.println("Confirmed Revenue: $" + String.format("%.2f", confirmedRevenue));
        System.out.println("Lost Revenue (Cancelled): $" + String.format("%.2f", cancelledRevenue));
        System.out.println("\nAverage Booking Cost: $" + 
                         String.format("%.2f", totalRevenue / bookings.size()));
    }

    /**
     * Print status summary showing booking states
     */
    public void printStatusSummary() {
        System.out.println("\n=== Booking Status Summary ===");
        
        List<Reservation> confirmed = history.getBookingsByStatus("CONFIRMED");
        List<Reservation> cancelled = history.getBookingsByStatus("CANCELLED");
        
        System.out.println("\nCONFIRMED BOOKINGS (" + confirmed.size() + "):");
        if (confirmed.isEmpty()) {
            System.out.println("None");
        } else {
            int count = 1;
            for (Reservation r : confirmed) {
                System.out.println(count + ". " + r.getCustomerName() + 
                                 " - " + r.getRoomId() + " ($" + 
                                 String.format("%.2f", r.getRoomCost()) + ")");
                count++;
            }
        }
        
        System.out.println("\nCANCELLED BOOKINGS (" + cancelled.size() + "):");
        if (cancelled.isEmpty()) {
            System.out.println("None");
        } else {
            int count = 1;
            for (Reservation r : cancelled) {
                System.out.println(count + ". " + r.getCustomerName() + 
                                 " - " + r.getRoomId());
                count++;
            }
        }
    }

    /**
     * Search and display bookings by customer name
     * 
     * @param customerName Name to search for
     */
    public void searchAndDisplayByCustomer(String customerName) {
        List<Reservation> results = history.searchByCustomerName(customerName);
        
        System.out.println("\n=== Search Results for '" + customerName + "' ===");
        System.out.println("Found: " + results.size() + " booking(s)");
        
        if (!results.isEmpty()) {
            for (Reservation r : results) {
                System.out.println("├─ " + r);
            }
        }
    }

    /**
     * Export booking data as formatted string (for file export potential)
     * 
     * @return Formatted booking data
     */
    public String exportBookingsAsString() {
        StringBuilder sb = new StringBuilder();
        List<Reservation> bookings = history.getAllBookings();
        
        sb.append("BOOKING_ID,CUSTOMER_NAME,ROOM_ID,ROOM_TYPE,STATUS,ROOM_COST\n");
        
        for (Reservation r : bookings) {
            sb.append(r.getBookingId()).append(",")
              .append(r.getCustomerName()).append(",")
              .append(r.getRoomId()).append(",")
              .append(r.getRoomType()).append(",")
              .append(r.getStatus()).append(",")
              .append(String.format("%.2f", r.getRoomCost())).append("\n");
        }
        
        return sb.toString();
    }
}
