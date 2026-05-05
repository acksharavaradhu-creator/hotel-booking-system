import java.util.*;

/**
 * Use Case 11: Concurrent Booking Requests
 *
 * Runnable processor that handles booking requests from a shared queue.
 * Demonstrates thread-safe processing in concurrent environments.
 *
 * Key Concepts:
 * - Runnable Interface: Enables execution in separate threads
 * - Shared Queue: Thread-safe access to booking requests
 * - Synchronization: Protects shared data structures
 * - Worker Pattern: Continuous processing until queue empty
 * - Thread Identity: Each processor has unique identifier
 *
 * @author Ackshara
 * @version 1.0
 */
public class BookingProcessor implements Runnable {

    private final Queue<BookingRequest> requestQueue;
    private final SharedInventory inventory;
    private final String processorId;

    /**
     * Constructor for booking processor
     * @param requestQueue Shared queue of booking requests
     * @param inventory Shared inventory for room allocation
     * @param processorId Unique identifier for this processor
     */
    public BookingProcessor(Queue<BookingRequest> requestQueue, SharedInventory inventory, String processorId) {
        this.requestQueue = requestQueue;
        this.inventory = inventory;
        this.processorId = processorId;
    }

    /**
     * Main processing loop - runs in separate thread
     *
     * Continuously processes booking requests from shared queue until empty.
     * Synchronizes on queue to prevent concurrent access issues.
     */
    @Override
    public void run() {
        System.out.println("🚀 " + processorId + " started processing requests");

        while (true) {
            BookingRequest request = null;

            // Synchronize on queue to safely fetch next request
            synchronized (requestQueue) {
                if (requestQueue.isEmpty()) {
                    break; // No more requests to process
                }
                request = requestQueue.poll(); // Remove and return head of queue
            }

            if (request != null) {
                processBookingRequest(request);
            }

            // Small delay to simulate processing time and allow thread interleaving
            try {
                Thread.sleep(50); // 50ms delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("❌ " + processorId + " interrupted");
                break;
            }
        }

        System.out.println("✅ " + processorId + " finished processing");
    }

    /**
     * Process a single booking request
     *
     * Attempts to allocate room for the request and reports result.
     * Thread-safe through SharedInventory's synchronized methods.
     *
     * @param request The booking request to process
     */
    private void processBookingRequest(BookingRequest request) {
        String guestName = request.getGuestName();
        String roomType = request.getRoomType();

        System.out.println("📋 " + processorId + " processing: " + request);

        // Attempt room allocation (thread-safe through SharedInventory)
        boolean success = inventory.allocateRoom(roomType);

        if (success) {
            System.out.println("🎉 " + processorId + " SUCCESS: " + guestName + " allocated " + roomType + " room");
        } else {
            System.out.println("💔 " + processorId + " FAILED: " + guestName + " could not get " + roomType + " room");
        }
    }
}