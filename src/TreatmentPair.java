public class TreatmentPair {
    private int bedId;
    private int patientId;
    private int staffId;

    public TreatmentPair(int bedId, int patientId, int staffId ) {
        this.bedId = bedId;
        this.patientId = patientId;
        this.staffId = staffId;
    }
    public TreatmentPair() {
        this.bedId = 0;
        this.patientId = 0;
        this.staffId = 0;
    }
    public int getBedId() {
        return bedId;
    }
    public void setBedId(int bedId) {
        this.bedId = bedId;
    }
    public int getPatientId() {
        return patientId;
    }
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
    public int getStaffId() {
        return staffId;
    }
    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }
}
