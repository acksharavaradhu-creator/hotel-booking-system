/**
 * Use Case 2: Basic Room Types & Static Availability
 *
 * Demonstrates abstraction, inheritance, and polymorphism.
 *
 * @author Ackshara
 * @version 1.0
 */

// Abstract class
abstract class Room {
    String type;
    int beds;
    double price;

    Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    abstract void displayDetails();
}

// Single Room
class SingleRoom extends Room {
    SingleRoom() {
        super("Single Room", 1, 1000);
    }

    void displayDetails() {
        System.out.println(type + " | Beds: " + beds + " | Price: " + price);
    }
}

// Double Room
class DoubleRoom extends Room {
    DoubleRoom() {
        super("Double Room", 2, 2000);
    }

    void displayDetails() {
        System.out.println(type + " | Beds: " + beds + " | Price: " + price);
    }
}

// Suite Room
class SuiteRoom extends Room {
    SuiteRoom() {
        super("Suite Room", 3, 5000);
    }

    void displayDetails() {
        System.out.println(type + " | Beds: " + beds + " | Price: " + price);
    }
}

// Main class
public class Main {
    public static void main(String[] args) {

        // Create room objects (Polymorphism)
        Room r1 = new SingleRoom();
        Room r2 = new DoubleRoom();
        Room r3 = new SuiteRoom();

        // Static availability
        boolean singleAvailable = true;
        boolean doubleAvailable = false;
        boolean suiteAvailable = true;

        System.out.println("Room Details:\n");

        r1.displayDetails();
        System.out.println("Available: " + singleAvailable);

        r2.displayDetails();
        System.out.println("Available: " + doubleAvailable);

        r3.displayDetails();
        System.out.println("Available: " + suiteAvailable);
    }
}