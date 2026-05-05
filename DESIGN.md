# Technical Design: Safe Room Allocation System

## Problem Statement

**Challenge**: Design a booking system that prevents double-booking while maintaining inventory consistency and processing requests fairly (FIFO).

**Constraints**:
- No room can be assigned to multiple guests
- Inventory must always reflect reality
- State must remain consistent during concurrent operations
- Requests must be processed fairly (FIFO)

---

## Design Pattern: Atomic Allocation

### Core Insight
Treat room allocation as an **indivisible atomic operation**:
- Check availability
- Generate unique ID
- Record allocation
- Update inventory

All steps must complete together or none at all.

### Implementation
```
┌─────────────────────────────────────────────┐
│  Synchronized confirmBooking() Block        │
├─────────────────────────────────────────────┤
│  1. Room type exists? ─────────────────┐    │
│  2. Inventory > 0? ─────────────────┐  │    │
│  3. Generate unique ID ────────┐    │  │    │
│  4. Add to Sets ───────────┐    │    │  │    │
│  5. Decrement inventory ───┤    │    │  │    │
│  6. Create booking ────────┤ NO room ID collision
│  7. Confirm ───────────────┘ All happen together
└─────────────────────────────────────────────┘
```

---

## Preventing Double-Booking: Set-Based Uniqueness

### Traditional Approach (Unsafe)
```java
// PROBLEM: Manual duplicate checking is error-prone
for (String allocated : allocatedRooms) {
    if (allocated.equals(newRoomId)) {
        // Double-booking possible if check isn't complete
    }
}
```

### Set-Based Approach (Safe)
```java
// SOLUTION: Set enforces uniqueness by design
if (allocatedRoomIds.contains(roomId)) {
    // Generate new ID
}
allocatedRoomIds.add(roomId);  // Cannot add duplicate

// GUARANTEE: No duplicate in Set possible
```

**Why Sets?**
- Set interface guarantees: "no duplicate elements"
- O(1) lookup for collision detection
- Impossible to add duplicate (add returns false if already present)
- Clear intent: this is a uniqueness constraint

---

## Inventory Synchronization Strategy

### Requirement: Availability Must Reflect Reality
```
Initial State:
┌──────────────┬──────────────┬──────────────┐
│   SINGLE     │   DOUBLE     │    SUITE     │
│ Available: 5 │ Available: 3 │ Available: 2 │
└──────────────┴──────────────┴──────────────┘

After Booking 1 (SINGLE):
┌──────────────┬──────────────┬──────────────┐
│   SINGLE     │   DOUBLE     │    SUITE     │
│ Available: 4 │ Available: 3 │ Available: 2 │
└──────────────┴──────────────┴──────────────┘
(Immediately decremented)

After Cancellation (SINGLE):
┌──────────────┬──────────────┬──────────────┐
│   SINGLE     │   DOUBLE     │    SUITE     │
│ Available: 5 │ Available: 3 │ Available: 2 │
└──────────────┴──────────────┴──────────────┘
(Immediately restored)
```

### Implementation
```java
// Step 5: Decrement immediately (atomic with allocation)
if (!room.decrementInventory()) {
    // Rollback if fails
    allocatedRoomIds.remove(uniqueRoomId);
    roomTypeToAllocatedIds.get(roomType).remove(uniqueRoomId);
    return false;
}
```

---

## FIFO Queue Processing

### Design
```
┌──────────────────────────────────────────────────┐
│  Booking Request Queue (FIFO)                    │
├──────────────────────────────────────────────────┤
│  [1] Alice: SINGLE  ← First (Dequeued 1st)      │
│  [2] Bob: DOUBLE    ← Second (Dequeued 2nd)     │
│  [3] Carol: SINGLE  ← Third (Dequeued 3rd)      │
│  ...                                             │
└──────────────────────────────────────────────────┘
```

### Guarantee
```java
// FIFO by design
while (!bookingQueue.isEmpty()) {
    BookingRequest req = bookingQueue.poll();  // First in, first out
    confirmBooking(req.customerName, req.roomType);
}
```

---

## Data Structures & Their Roles

### 1. `Set<String> allocatedRoomIds`
```
Purpose: UNIQUENESS
Contains: All allocated room IDs across all types
Property: Cannot contain duplicates
Operation: Add → Success | Add duplicate → Ignored
Query: O(1) contains() check

Example:
{SINGLE-1001, SINGLE-1003, SINGLE-1006, DOUBLE-1002, SUITE-1004}
```

### 2. `HashMap<String, Set<String>> roomTypeToAllocatedIds`
```
Purpose: GROUPING & VALIDATION
Structure: roomType → Set of allocated IDs
Property: Enables per-type queries and reconciliation

Example:
{
  SINGLE → {1001, 1003, 1006, 1007, 1008},
  DOUBLE → {1002, 1005},
  SUITE → {1004}
}
```

### 3. `HashMap<String, Room> roomsByType`
```
Purpose: INVENTORY MANAGEMENT
Structure: roomType → Room object (tracks available count)
Property: Synchronized updates, O(1) lookup

Example:
{
  SINGLE → Room{inventory: 0},
  DOUBLE → Room{inventory: 1},
  SUITE → Room{inventory: 1}
}
```

### 4. `Queue<BookingRequest> bookingQueue`
```
Purpose: FIFO PROCESSING
Structure: First in, first out request queue
Property: Ensures fairness, prevents starvation

Example:
[Alice:SINGLE] → [Bob:DOUBLE] → [Carol:SINGLE] → ...
```

---

## Atomic Operation Guarantee

### Synchronized Block
```java
private synchronized boolean confirmBooking(String customerName, String roomType) {
    // Only one thread can execute this at a time
    // All steps happen together
    // No interleaving possible
    
    // Step 1: Check existence
    // Step 2: Check availability  
    // Step 3: Generate ID
    // Step 4: Record in Sets
    // Step 5: Update inventory
    // Step 6: Create booking
    
    // All complete together or none execute
}
```

### Why Synchronized?
- **Without**: Two threads could both see inventory=1, both allocate same room
- **With**: Only one thread proceeds at a time, guarantees consistency

### Sequence Without Synchronization (Unsafe)
```
Thread 1                          Thread 2
─────────────────────────────────────────────
Check: inventory=5 ✓              
                                  Check: inventory=5 ✓
Allocate: SINGLE-1001             
                                  Allocate: SINGLE-1001 (COLLISION!)
Decrement: inventory=4
                                  Decrement: inventory=4 (Wrong! Should be 3)
```

### Sequence With Synchronization (Safe)
```
Thread 1                          Thread 2
─────────────────────────────────────────────
[LOCK acquired]
Check: inventory=5 ✓              
Allocate: SINGLE-1001             
Decrement: inventory=4            [WAITING for lock]
[LOCK released]
                                  [LOCK acquired]
                                  Check: inventory=4 ✓
                                  Allocate: SINGLE-1002 (Different ID)
                                  Decrement: inventory=3
                                  [LOCK released]
```

---

## Collision Detection Strategy

### Problem
Multiple room IDs could theoretically collide:
- SINGLE-1001, SINGLE-1001 (duplicate)
- SUITE-1004, SINGLE-1004 (same counter)

### Solution: Multi-Level Check
```java
private synchronized String generateUniqueRoomId(String roomType) {
    String roomId;
    do {
        roomId = roomType + "-" + (++roomIdCounter);
    } while (allocatedRoomIds.contains(roomId));  // Loop until unique
    return roomId;
}
```

### Guarantee
- Counter increments per generation
- Before using any ID, check against **global Set**
- Global Set is the source of truth for uniqueness
- Loop continues until unique ID found

---

## Failure & Recovery

### Partial Failure Handling
```java
// Scenario: Allocation succeeds, inventory update fails

// Step 1-4: SUCCESS
allocatedRoomIds.add(uniqueRoomId);
roomTypeToAllocatedIds.get(roomType).add(uniqueRoomId);

// Step 5: FAIL
if (!room.decrementInventory()) {
    // ROLLBACK: Remove from allocation
    allocatedRoomIds.remove(uniqueRoomId);
    roomTypeToAllocatedIds.get(roomType).remove(uniqueRoomId);
    return false;  // Operation failed
}
```

**Result**: No partial state. Either fully allocated or fully rolled back.

---

## Cancellation & Recovery

### Cancellation Process
```java
void cancelBooking(int bookingId) {
    Booking booking = bookings.remove(bookingId);
    
    // Step 1: Remove from allocation tracking
    allocatedRoomIds.remove(booking.roomId);
    roomTypeToAllocatedIds.get(booking.roomType).remove(booking.roomId);
    
    // Step 2: Restore inventory
    roomsByType.get(booking.roomType).incrementInventory();
}
```

### Reconciliation
```
Before Cancel: allocatedRoomIds = 8, inventory = 0
After Cancel:  allocatedRoomIds = 7, inventory = 1
Math Check:    8 - 1 = 7 ✓
```

---

## System Consistency Properties

### Invariant 1: Inventory ≥ 0
```
Property: availableCount cannot go negative
Guarantee: Check before decrement
Result: Never oversell
```

### Invariant 2: No Duplicate Allocations
```
Property: No room ID appears twice
Guarantee: Set<String> by design
Result: No double-booking possible
```

### Invariant 3: Allocation ↔ Inventory Balance
```
Property: #allocated + available = initial_inventory
Guarantee: Atomic increment/decrement
Result: Always balanced
```

### Invariant 4: All Bookings Valid
```
Property: Each booking has valid room ID and type
Guarantee: Created during atomic allocation
Result: No orphaned or invalid bookings
```

---

## Performance Characteristics

| Operation | Time | Space | Note |
|-----------|------|-------|------|
| Generate Unique ID | O(1) amortized | O(1) | Counter increment + Set lookup |
| Check Availability | O(1) | O(1) | Direct inventory query |
| Record Allocation | O(1) | O(1) | Set add operation |
| Process Booking | O(1) | O(1) | All atomic ops are O(1) |
| View Allocations | O(n) | O(1) | n = number of allocated rooms |

---

## Conclusion

This design achieves:

1. **Safety**: Double-booking impossible (Set enforces uniqueness)
2. **Consistency**: Inventory always reflects reality (atomic + immediate updates)
3. **Fairness**: FIFO queue ensures no starvation
4. **Correctness**: Synchronized blocks prevent race conditions
5. **Recoverability**: Cancellation properly restores state
6. **Verifiability**: Tracking enables audit and reconciliation
