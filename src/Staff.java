public class Staff extends Person {
    private int staffID;
    int occupied;
    static int StaffIdCounter = 0;
    int capacity;

    private int numLifetimePatients;

    public Staff(String name, boolean gender) {
        super(name, gender);
        occupied = 0;
        capacity = 0;
        this.staffID = StaffIdCounter;
        this.numLifetimePatients = 0;
        StaffIdCounter++;
    }

    public Staff() {
        super();
        occupied = 0;
        capacity = 0;
        this.staffID = StaffIdCounter;
        this.numLifetimePatients = 0;
        StaffIdCounter++;
    }

    public int getNumLifetimePatients() {
        return this.numLifetimePatients;
    }

    public void setNumLifetimePatients(int num) {
        this.numLifetimePatients = num;
    }

    public void IncNumLifetimePatients() {
        this.numLifetimePatients++;
    }

    public int getStaffID() {
        return this.staffID;
    }

    public boolean CheckAvailability() {
        return occupied < capacity;
    }

    public void setOccupied() {
    if(occupied < capacity){
        this.IncNumLifetimePatients();
        occupied++;
    }

    }

    public void setStaffID(int staffID) {
        this.staffID = staffID;
        if(StaffIdCounter <=  staffID) StaffIdCounter = staffID + 1;
    }

    public void free() {
        System.out.println("Free has been Called "  + this.getStaffID() + "," + this.capacity + "," + this.occupied );
        if (occupied > 0) {
            occupied--;
        }
    }

    public int getOccupied() {
        return this.occupied;
    }
}

