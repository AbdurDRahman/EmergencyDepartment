public class Patient extends Person {
    private Time arrivalTime;
    private Time startOfTreatment;
    private Time departureTime;
    private Time duration;
    private Time waitTime;
    private int patientId;
    private int triageCategory;
    private String disease;
    static int PatientIdCounter = 0;


    public Patient() {
        super();
        startOfTreatment = new Time();
        departureTime = new Time();
        arrivalTime = new Time();
        duration = new Time();
        waitTime = new Time();
        this.triageCategory = 0;
        this.patientId = PatientIdCounter;
        disease = null;
        PatientIdCounter++;
    }

    public Patient(String name, boolean gender, int triageCategory, String disease) {
        super(name, gender);
        this.triageCategory = triageCategory;
        arrivalTime = new Time();
        startOfTreatment = null;
        departureTime = null;
        duration = new Time();
        waitTime = new Time();
        patientId = PatientIdCounter;
        this.disease = disease;
        PatientIdCounter++;
    }

    public void setTriageCategory(int triageCategory) {
        this.triageCategory = triageCategory;
    }

    public int getTriageCategory() {
        return triageCategory;
    }

    public Time getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Time arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Time getStartOfTreatment() {
        return startOfTreatment;
    }

    public void setStartOfTreatment(Time startOfTreatment) {
        this.startOfTreatment = startOfTreatment;
    }

    public Time getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(int minutes, int hour, int day) {
        departureTime.setDay(day);
        departureTime.setHour(hour);
        departureTime.setMinute(minutes);
    }

    public Time getDuration() {
        return duration;
    }

    public void setDuration(Time duration) {
        this.duration = duration;
    }

    public Time getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Time waitTime) {
        this.waitTime = waitTime;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getDisease() {
        return disease;
    }

    public void setDepartureTime(Time departureTime) {
        this.departureTime = departureTime;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setCounter(int PatientIdCounter) {
        this.patientId = PatientIdCounter;
    }

    public int getPatientIdCounter() {
        return PatientIdCounter;
    }
    public static void main(String[] args) {
        Patient patient = new Patient("Ali", true, 5, "Diabetes");
        System.out.println(patient.getPatientId());
        System.out.println(patient.getName());
        System.out.println(patient.getGender());
        System.out.println(patient.getDuration());
        System.out.println(patient.getWaitTime());
        System.out.println(patient.getTriageCategory());
    }


}

