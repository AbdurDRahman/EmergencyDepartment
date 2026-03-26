import java.util.ArrayList;

public class Room {
    private ArrayList<AcuteBed> acuteBeds;
    private ArrayList<SubAcuteBed> subAcuteBeds;
    private ArrayList<ResuscitationBed> resuscitationBeds;
    private ArrayList<MinorBed> minorBeds;
    private SimulationStatistics statistics = new SimulationStatistics();
    public Room() {
        acuteBeds = new ArrayList<AcuteBed>();
        subAcuteBeds = new ArrayList<SubAcuteBed>();
        resuscitationBeds = new ArrayList<ResuscitationBed>();
        minorBeds = new ArrayList<MinorBed>();
        for (int i = 0; i < 6; i++) {
            AcuteBed acuteBed = new AcuteBed();
            acuteBeds.add(acuteBed);

        }
        for (int i = 0; i < 6; i++) {
            SubAcuteBed subAcuteBed = new SubAcuteBed();
            subAcuteBeds.add(subAcuteBed);
        }
        for (int i = 0; i < 6; i++) {
            ResuscitationBed ResuscitationBed = new ResuscitationBed();
            resuscitationBeds.add(ResuscitationBed);
        }
        for (int i = 0; i < 6; i++) {
            MinorBed minorBed = new MinorBed();
            minorBeds.add(minorBed);
        }
    }

    public boolean IsFreeResuscitationBed() {
        for (int i = 0; i < 6; i++) {
            if (!(resuscitationBeds.get(i).isOccupied())) {
                return true;
            }
        }
        return false;
    }

    public boolean IsFreeMinorBed() {
        for (int i = 0; i < 6; i++) {
            if (!(minorBeds.get(i).isOccupied())) {
                return true;
            }
        }
        return false;
    }

    public boolean IsFreeAcuteBed() {
        for (int i = 0; i < 6; i++) {
            if (!(acuteBeds.get(i).isOccupied())) {
                return true;
            }
        }
        return false;
    }

    public boolean IsFreeSubAcuteBed() {
        for (int i = 0; i < 6; i++) {
            if (!(subAcuteBeds.get(i).isOccupied())) {
                return true;
            }
        }
        return false;
    }

    public int AssignResuscitationBed() {
        for (int i = 0; i < 6; i++) {
            if (!(resuscitationBeds.get(i).isOccupied())) {
                resuscitationBeds.get(i).setOccupied(true);
                statistics.addtoBedsTypeArr(0, 1);
                return resuscitationBeds.get(i).getBedID();
            }
        }
        return -1;
    }

    public int AssignMinorBed() {
        for (int i = 0; i < 6; i++) {
            if (!(minorBeds.get(i).isOccupied())) {
                minorBeds.get(i).setOccupied(true);
                statistics.addtoBedsTypeArr(1, 1);
                return minorBeds.get(i).getBedID();
            }

        }
        return -1;
    }

    public int AssignAcuteBed() {
        for (int i = 0; i < 6; i++) {
            if (!(acuteBeds.get(i).isOccupied())) {
                acuteBeds.get(i).setOccupied(true);
                statistics.addtoBedsTypeArr(2, 1);
                return acuteBeds.get(i).getBedID();
            }

        }
        return -1;
    }

    public int AssignSubAcuteBed() {
        for (int i = 0; i < 6; i++) {
            if (!(subAcuteBeds.get(i).isOccupied())) {
                subAcuteBeds.get(i).setOccupied(true);
                statistics.addtoBedsTypeArr(3, 1);
                return subAcuteBeds.get(i).getBedID();
            }

        }
        return -1;
    }

    public void freeBed(int bedID) {
        for (int i = 0; i < 6; i++) {
            if (bedID == acuteBeds.get(i).getBedID()) {
                acuteBeds.get(i).setOccupied(false);
                statistics.addtoBedsTypeArr(2, -1);
            } else if ( bedID == subAcuteBeds.get(i).getBedID()) {
                subAcuteBeds.get(i).setOccupied(false);
                statistics.addtoBedsTypeArr(3, -1);
            } else if ( bedID == resuscitationBeds.get(i).getBedID()) {
                resuscitationBeds.get(i).setOccupied(false);
                statistics.addtoBedsTypeArr(0, -1);
            } else if ( bedID == minorBeds.get(i).getBedID()) {
                minorBeds.get(i).setOccupied(false);
                statistics.addtoBedsTypeArr(1, -1);
            }

 }
    }
}