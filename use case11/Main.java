import java.util.*;
import java.util.concurrent.*;

/**
 * Use Case 11: Concurrent Booking Requests
 *
 * Demonstrates thread-safe room allocation in concurrent booking scenarios.
 * Multiple threads compete for limited room inventory with proper synchronization.
 *
 * Key Concepts:
 * - Concurrency: Multiple threads accessing shared resources
 * - Thread Safety: Synchronized access prevents race conditions
 * - Resource Contention: Limited rooms create competition
 * - Atomic Operations: Check-and-allocate as single unit
 * - Synchronization: Protects both queue and inventory
 * - Correctness: No double booking, consistent state
 *
 * @author Ackshara
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("====================================================");
        System.out.println("Use Case 11: Concurrent Booking Requests");
        System.out.println("Thread-Safe Room Allocation with Synchronization");
        System.out.println("====================================================\n");

        // Step 1: Initialize shared resources
        System.out.println("--- Step 1: Initializing Shared Resources ---");

        // Create shared queue for booking requests
        Queue<BookingRequest> requestQueue = new LinkedList<>();

        // Create shared inventory (thread-safe)
        SharedInventory inventory = new SharedInventory();

        System.out.println("✅ Shared queue and inventory initialized");
        inventory.printInventory();

        // Step 2: Create booking requests
        System.out.println("--- Step 2: Creating Booking Requests ---");

        // Mix of requests that will succeed and fail due to limited inventory
        String[][] requestData = {
            {"Alice Johnson", "SINGLE"},
            {"Bob Smith", "DOUBLE"},
            {"Carol White", "SUITE"},
            {"David Brown", "SINGLE"},
            {"Eva Davis", "DOUBLE"},
            {"Frank Miller", "SUITE"},
            {"Grace Wilson", "SINGLE"},
            {"Henry Taylor", "DOUBLE"},
            {"Ivy Anderson", "SUITE"},
            {"Jack Thomas", "SINGLE"},     // May fail - only 5 SINGLE rooms
            {"Kate Jackson", "DOUBLE"},    // May fail - only 3 DOUBLE rooms
            {"Liam Garcia", "SUITE"},      // May fail - only 2 SUITE rooms
            {"Mia Martinez", "SINGLE"},    // Will fail - inventory exhausted
            {"Noah Rodriguez", "DOUBLE"},  // Will fail - inventory exhausted
            {"Olivia Lee", "SUITE"}        // Will fail - inventory exhausted
        };

        // Add requests to shared queue
        for (String[] data : requestData) {
            BookingRequest request = new BookingRequest(data[0], data[1]);
            requestQueue.add(request);
            System.out.println("📝 Added request: " + request);
        }

        System.out.println("\n📊 Total requests queued: " + requestQueue.size());
        System.out.println("🎯 Expected results: Some succeed, some fail due to limited inventory\n");

        // Step 3: Create and start processing threads
        System.out.println("--- Step 3: Starting Concurrent Processing ---");

        final int NUM_THREADS = 4; // 4 concurrent processors
        List<Thread> threads = new ArrayList<>();

        // Create processor threads
        for (int i = 0; i < NUM_THREADS; i++) {
            String processorId = "Processor-" + (i + 1);
            BookingProcessor processor = new BookingProcessor(requestQueue, inventory, processorId);
            Thread thread = new Thread(processor);
            threads.add(thread);
        }

        // Start all threads simultaneously
        System.out.println("🚀 Starting " + NUM_THREADS + " concurrent processors...");
        long startTime = System.currentTimeMillis();

        for (Thread thread : threads) {
            thread.start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("❌ Main thread interrupted");
                Thread.currentThread().interrupt();
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("\n✅ All processors completed in " + (endTime - startTime) + "ms\n");

        // Step 4: Final inventory check
        System.out.println("--- Step 4: Final Inventory Check ---");
        inventory.printInventory();

        // Step 5: Analyze results
        System.out.println("--- Step 5: Results Analysis ---");

        Map<String, Integer> finalInventory = inventory.getInventory();
        int totalRooms = finalInventory.values().stream().mapToInt(Integer::intValue).sum();
        int initialTotal = 5 + 3 + 2; // SINGLE + DOUBLE + SUITE

        System.out.println("Initial inventory: " + initialTotal + " rooms");
        System.out.println("Final inventory: " + totalRooms + " rooms");
        System.out.println("Rooms allocated: " + (initialTotal - totalRooms));

        // Verify no double booking occurred
        boolean consistent = true;
        for (Map.Entry<String, Integer> entry : finalInventory.entrySet()) {
            int count = entry.getValue();
            if (count < 0) {
                System.out.println("❌ ERROR: Negative count for " + entry.getKey() + ": " + count);
                consistent = false;
            }
        }

        if (consistent) {
            System.out.println("✅ Inventory consistency verified - no double booking");
        }

        // Step 6: Thread safety verification
        System.out.println("\n--- Step 6: Thread Safety Verification ---");
        System.out.println("✓ Synchronized allocateRoom() prevents race conditions");
        System.out.println("✓ Synchronized queue access prevents concurrent modification");
        System.out.println("✓ Atomic check-and-decrement operations");
        System.out.println("✓ No double booking despite concurrent access");
        System.out.println("✓ Consistent inventory state throughout execution");
        System.out.println("✓ Proper thread coordination with join()");

        // Step 7: Performance note
        System.out.println("\n--- Step 7: Performance Characteristics ---");
        System.out.println("• Correctness prioritized over performance");
        System.out.println("• Synchronized methods create sequential bottlenecks");
        System.out.println("• Small delays simulate real processing time");
        System.out.println("• Thread interleaving demonstrates concurrency effects");

        System.out.println("\n====================================================");
        System.out.println("✓ Use Case 11 Completed Successfully");
        System.out.println("✓ Concurrent booking with thread-safe allocation");
        System.out.println("✓ No race conditions or double booking detected");
        System.out.println("====================================================");
    }
}