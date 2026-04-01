import java.util.*;

/**
 * Use Case 5: Booking Request Queue (FIFO)
 *
 * Demonstrates handling multiple booking requests using Queue.
 */

// Reservation class (represents a booking request)
class Reservation {
    String customerName;
    String roomType;

    Reservation(String customerName, String roomType) {
        this.customerName = customerName;
        this.roomType = roomType;
    }

    void display() {
        System.out.println("Customer: " + customerName + " | Room: " + roomType);
    }
}

// Queue manager
class BookingQueue {
    private Queue<Reservation> queue;

    BookingQueue() {
        queue = new LinkedList<>();
    }

    // Add request
    void addRequest(Reservation r) {
        queue.add(r);
        System.out.println("Request added for " + r.customerName);
    }

    // Display all requests (in order)
    void showQueue() {
        System.out.println("\nBooking Requests in Queue:\n");

        for (Reservation r : queue) {
            r.display();
        }
    }
}

// Main class
public class Main {
    public static void main(String[] args) {

        BookingQueue bookingQueue = new BookingQueue();

        // Add booking requests
        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room"));

        // Show queue
        bookingQueue.showQueue();
    }
}