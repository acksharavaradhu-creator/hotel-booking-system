class Booking {
    int bookingId;
    String customerName;
    String roomId;  // Changed to String for unique ID generation
    String roomType;
    long bookingTime;

    Booking(int bookingId, String customerName, String roomId, String roomType) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.roomId = roomId;
        this.roomType = roomType;
        this.bookingTime = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Booking{" +
                "ID=" + bookingId +
                ", Customer='" + customerName + '\'' +
                ", RoomID='" + roomId + '\'' +
                ", Type='" + roomType + '\'' +
                '}';
    }
}