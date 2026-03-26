import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Bed {
    private int bedID;
    private static int counter = 0;
    private boolean Occupied;
    public Bed(){
        this.Occupied = false;
        this.bedID = counter;
        counter++;
    }
    public int getBedID() {
        return bedID;
    }
    public void setBedID(int bedID) {
        this.bedID = bedID;
    }
    public boolean isOccupied() {
        return Occupied;
    }
    public void setOccupied(boolean Occupied) {
        this.Occupied = Occupied;
    }

}
