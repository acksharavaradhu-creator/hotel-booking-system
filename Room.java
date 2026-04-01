class Room {
    int roomId;
    String type;
    boolean isAvailable;

    Room(int roomId, String type) {
        this.roomId = roomId;
        this.type = type;
        this.isAvailable = true;
    }
}