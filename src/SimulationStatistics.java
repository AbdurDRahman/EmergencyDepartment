
import java.util.ArrayList;

public class SimulationStatistics {
    private ArrayList<Integer> waitingTimes;
    private int totalBedsUsed;
    private int totalPatientsProcessed;
    private int[] beds = new int[4];  //0-resuscitation , 1-minor  2-acute  3-subacute bed
    private int totalPatientsArrived;

    public SimulationStatistics() {
        waitingTimes = new ArrayList<>();
        totalBedsUsed = 0;
        totalPatientsProcessed = 0;
    }

    public void addWaitingTime(int time) {
        waitingTimes.add(time);
    }

    public void incrementBedsUsed() {
        totalBedsUsed++;
    }

    public void addtoBedsTypeArr(int index, int i) {
        beds[index] = beds[index] + i;
    }

    public int getTotalBedsbyType(int index) {
        return beds[index];
    }

    public float PatientsSeenPercent() {
        // Check if totalPatients is not zero to avoid division by zero
        if (totalPatientsProcessed == 0) {
            return 0;  // Return 0% if no patients are present
        }

        return (float) (totalPatientsProcessed / totalPatientsArrived) * 100;

    }


    public void incrementPatientsProcessed() {
        totalPatientsProcessed++;
    }

    public void incrementPatientsArrived() {
        totalPatientsArrived++;
    }

    public double getAverageWaitingTime() {
        return waitingTimes.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    public int getTotalBedsUsed() {
        return totalBedsUsed;
    }

    public int getTotalPatientsProcessed() {
        return totalPatientsProcessed;
    }

}
