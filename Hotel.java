import java.util.*;

class Hotel {
    ArrayList<Room> rooms = new ArrayList<>();
    HashMap<Integer, Booking> bookings = new HashMap<>();
    Queue<String> waitingList = new LinkedList<>();

    int bookingCounter = 1;
    void addRoom(int id, String type) {
    rooms.add(new Room(id, type));
}
    void viewRooms() {
    for (Room r : rooms) {
        System.out.println("Room " + r.roomId +
                " | Type: " + r.type +
                " | Available: " + r.isAvailable);
    }
}
    void bookRoom(String customerName) {
    for (Room r : rooms) {
        if (r.isAvailable) {
            r.isAvailable = false;

            Booking b = new Booking(bookingCounter++, customerName, r.roomId);
            bookings.put(b.bookingId, b);

            System.out.println("Room booked successfully!");
            System.out.println("Booking ID: " + b.bookingId);
            return;
        }
    }

    // No rooms available
    waitingList.add(customerName);
    System.out.println("No rooms available. Added to waiting list.");
}
    void cancelBooking(int bookingId) {
       if (!bookings.containsKey(bookingId)) {
          System.out.println("Invalid Booking ID");
          return;
    }

       Booking b = bookings.remove(bookingId);

    // Make room available again
       for (Room r : rooms) {
          if (r.roomId == b.roomId) {
            r.isAvailable = true;
            break;
        }
    }

        System.out.println("Booking cancelled!");

    // Assign to waiting list (FIFO)
        if (!waitingList.isEmpty()) {
            String nextCustomer = waitingList.poll();
            bookRoom(nextCustomer);
    }
}
}