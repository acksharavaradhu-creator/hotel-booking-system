class Room {
    String roomId;  // Changed to String for unique ID generation
    String type;
    int inventory;  // Track inventory count

    Room(String roomId, String type, int initialInventory) {
        this.roomId = roomId;
        this.type = type;
        this.inventory = initialInventory;
    }

    boolean hasAvailability() {
        return inventory > 0;
    }

    synchronized boolean decrementInventory() {
        if (inventory > 0) {
            inventory--;
            return true;
        }
        return false;
    }

    synchronized void incrementInventory() {
        inventory++;
    }

    @Override
    public String toString() {
        return "Room{" +
                "ID='" + roomId + '\'' +
                ", Type='" + type + '\'' +
                ", Available=" + inventory +
                '}';
    }
}