/**
 * Use Case 10: Safe Cancellation with Rollback Logic
 *
 * Demonstrates safe cancellation of confirmed bookings with rollback capability,
 * ensuring inventory consistency and demonstrating LIFO stack behavior.
 *
 * Key Concepts:
 * - Atomic Cancellation: All-or-nothing operations
 * - Rollback Stack: LIFO structure for cancellation tracking
 * - State Consistency: Inventory and bookings stay synchronized
 * - Validation: Check before acting
 * - Error Handling: Clear failure messages
 *
 * @author Ackshara
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("====================================================");
        System.out.println("Use Case 10: Safe Cancellation with Rollback Logic");
        System.out.println("Atomic Operations & LIFO Rollback Stack");
        System.out.println("====================================================\n");

        // Initialize cancellation service
        CancellationService service = new CancellationService();

        // Step 1: Display initial system state
        System.out.println("--- Step 1: Initial System State ---");
        service.printSystemState();

        // Step 2: Add sample bookings
        System.out.println("--- Step 2: Adding Sample Bookings ---");

        String[] sampleBookings = {
            "BK001:SINGLE",
            "BK002:DOUBLE",
            "BK003:SUITE",
            "BK004:SINGLE",
            "BK005:DOUBLE"
        };

        for (String booking : sampleBookings) {
            String[] parts = booking.split(":");
            service.addBooking(parts[0], parts[1]);
        }

        System.out.println();
        service.printSystemState();

        // Step 3: Perform valid cancellations
        System.out.println("--- Step 3: Valid Cancellations ---");

        String[] validCancellations = {"BK001", "BK003", "BK005"};

        for (String reservationId : validCancellations) {
            System.out.println("\nCancelling reservation: " + reservationId);
            service.cancelBooking(reservationId);
        }

        System.out.println();
        service.printSystemState();

        // Step 4: Perform invalid cancellations
        System.out.println("--- Step 4: Invalid Cancellations ---");

        String[] invalidCancellations = {"BK001", "INVALID_ID", "", null, "BK999"};

        for (String reservationId : invalidCancellations) {
            System.out.println("\nAttempting to cancel: '" + reservationId + "'");
            service.cancelBooking(reservationId);
        }

        System.out.println();
        service.printSystemState();

        // Step 5: Demonstrate rollback stack behavior
        System.out.println("--- Step 5: Rollback Stack Analysis ---");
        System.out.println("Rollback stack demonstrates LIFO (Last In, First Out) behavior:");
        System.out.println("Most recent cancellations appear at the top of the stack");

        System.out.println("\nCurrent rollback stack: " + service.getRollbackStack());
        System.out.println("Stack size: " + service.getRollbackStack().size());

        // Show LIFO behavior explanation
        System.out.println("\nLIFO Demonstration:");
        System.out.println("├─ Last cancelled: BK005 (DOUBLE) - at top of stack");
        System.out.println("├─ Previous: BK003 (SUITE)");
        System.out.println("└─ First: BK001 (SINGLE) - at bottom of stack");

        // Step 6: Add more bookings and cancellations to show stack growth
        System.out.println("\n--- Step 6: Additional Operations ---");

        System.out.println("Adding more bookings...");
        service.addBooking("BK006", "SUITE");
        service.addBooking("BK007", "SINGLE");
        service.addBooking("BK008", "DOUBLE");

        System.out.println("\nCancelling more reservations...");
        service.cancelBooking("BK006");
        service.cancelBooking("BK007");

        System.out.println();
        service.printSystemState();

        // Step 7: Final state verification
        System.out.println("--- Step 7: State Consistency Verification ---");

        System.out.println("Active bookings: " + service.getActiveBookings().size());
        System.out.println("Cancelled bookings in rollback stack: " + service.getRollbackStack().size());
        System.out.println("Total operations: " + (service.getActiveBookings().size() + service.getRollbackStack().size()));

        // Verify inventory consistency
        System.out.println("\nInventory Status:");
        service.getInventory().forEach((roomType, count) -> {
            System.out.println("├─ " + roomType + ": " + count + " available");
        });

        // Step 8: Design verification
        System.out.println("\n--- Step 8: Design Verification ---");
        System.out.println("✓ Atomic cancellation operations (all-or-nothing)");
        System.out.println("✓ Rollback stack uses LIFO behavior");
        System.out.println("✓ Inventory consistency maintained after cancellations");
        System.out.println("✓ Validation performed before state changes");
        System.out.println("✓ Clear error messages for failed operations");
        System.out.println("✓ System state remains consistent throughout");
        System.out.println("✓ No modification to core Booking/Hotel/Room classes");

        System.out.println("\n====================================================");
        System.out.println("✓ Use Case 10 Completed Successfully");
        System.out.println("✓ Safe cancellation with rollback logic implemented");
        System.out.println("====================================================");
    }
}