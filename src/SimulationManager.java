import java.io.*;
import java.util.List;
import java.util.Random;

public class SimulationManager {
    private Reception reception;
    private Timer timer;
    protected SimulationStatistics statistics;

    public SimulationManager() {
        reception = new Reception();
        timer = new Timer();
        statistics = new SimulationStatistics();
        timer.start();
        readCurrentPersonCounter();
        readCurrentPatientCounter();
        System.out.println("Person id counter : " + Person.PersonIdCounter);
        System.out.println("Patient id counter: " + Patient.PatientIdCounter);
    }

    public void runSimulation(int durationMinutes) {
        long startTime = System.currentTimeMillis();
        String lastDisease = "empty";
        Time ArrivalTime = new Time();
        while ((System.currentTimeMillis() - startTime) / 60000 < durationMinutes) {
            Time currentTime = timer.getTimeElapsed();

            if (reception.simulatePatientArrival(currentTime, ArrivalTime)) {
                ArrivalTime = reception.getNextTime();
                System.out.print("Arrival time : "); ArrivalTime.displayTime();
                System.out.print("Patient Has Arrived: ");
                currentTime.displayTime();
            }

            handlePatientFlow(currentTime);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        endSimulation();
    }

    private void handlePatientFlow(Time currentTime) {
        int numPatients = reception.getPatients().size();
        int numPair = reception.getTreatmentPairs().size();
        for (int i = 0; i < numPatients; i++) {
            Time treatmentTime = reception.getPatients().get(i).getDuration();
            Time startOfTreatment = reception.getPatients().get(i).getStartOfTreatment();
            if (currentTime.calcDuration(startOfTreatment).IsGreaterOrEqual(treatmentTime)) {

                Patient temp = reception.getPatients().get(i);

                String Status = "Discharged";
                Random rand  = new Random();
                if(rand.nextFloat(1) < 0.10) Status = "PDDT";
                reception.writePatientDataToCSV("Database/patients.csv", reception.getPatients().get(i), Status);
                for (int j = 0; j < numPair; j++) {
                    if (reception.getTreatmentPairs().get(j).getPatientId() == reception.getPatients().get(i).getPatientId()) {
                        reception.room.freeBed(reception.getTreatmentPairs().get(j).getBedId());
                        reception.getStaffs().get(reception.treatmentPairs.get(j).getStaffId()).free();
                        int id = reception.getTreatmentPairs().get(j).getStaffId();
                        List<Staff> Staffs = reception.getStaffs();
                        reception.displayStaffMember(id);
                        reception.writeTreatmentPair("Database/TreatmentPair.csv", reception.getTreatmentPairs().get(j));
                        reception.treatmentPairs.remove(j);
                        statistics.incrementPatientsProcessed();
                        statistics.incrementBedsUsed();
                        break;
                    }
                }
                reception.getPatients().remove(i);
                numPatients--;
                if (!reception.getPatientQueue().isEmpty()) {
                    Patient patient;
                    TreatmentPair pair = new TreatmentPair();
                    int index = reception.NextPatientToTreat();
                    patient = reception.getPatientQueue().get(index);
                    reception.InsertPatient(patient);
                    pair.setPatientId(patient.getPatientId());
                    reception.AssignDoctor(pair, patient.getTriageCategory());
                    switch (patient.getTriageCategory()) {
                        case 2:
                            reception.AssignBedForCategoryTwo(pair);
                        default:
                            reception.AssignBedForCategory345(pair);
                    }
                    patient.setWaitTime(currentTime.calcDuration(patient.getArrivalTime()));
                    patient.setStartOfTreatment(currentTime);
                    reception.getPatientQueue().remove(index);
                    reception.treatmentPairs.add(pair);
                    Time[] arr = new Time[4];
                    arr[0].setMinute(10);
                    arr[1].setMinute(30);
                    arr[2].setMinute(60);
                    arr[3].setMinute(120);
                    for (int k = 0; k < reception.getPatientQueue().size(); k++) {
                        Patient patientInCheck = reception.getPatientQueue().get(k);
                        if (patientInCheck.getWaitTime().IsGreaterOrEqual(arr[patientInCheck.getTriageCategory() - 2])) {
                            patientInCheck.setTriageCategory(patientInCheck.getTriageCategory() - 1);
                        }
                        if (patientInCheck.getTriageCategory() == 1) {
                            TreatmentPair tempPair = new TreatmentPair();
                            tempPair.setPatientId(patientInCheck.getPatientId());
                            tempPair.setStaffId(reception.getSeniorDoctors().getFirst().getStaffID());
                            tempPair.setBedId(-1);
                            reception.writeTreatmentPair("Database/TreatmentPair.csv",tempPair);
                            reception.writePatientDataToCSV("Database/Patients.csv",patientInCheck,"Consulted");
                            reception.getPatientQueue().remove(k);
                            }
                        }
                    }
                }
                break;
            }
        }


    public void writeCurrentPersonCounter() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Database/personId.txt"))) {
            int value = Person.PersonIdCounter;
            writer.write(String.valueOf(value));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void writeCurrentPatientCounter() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Database/patientId.txt"))) {
            writer.write(String.valueOf(Patient.PatientIdCounter));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void readCurrentPersonCounter() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Database/personId.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                Person.PersonIdCounter = Integer.parseInt(line);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println(e);
        }
    }


    public void readCurrentPatientCounter() {
        try (BufferedReader reader = new BufferedReader(new FileReader("Database/patientId.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                Patient.PatientIdCounter = Integer.parseInt(line);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading patient counter: " + e);
        }
    }

    public void endSimulation() {

        System.out.println("end of simulation started");

//        while (!reception.getPatients().isEmpty()) {
//            handlePatientFlow(timer.getTimeElapsed());
//            System.out.println(reception.getPatients());
//        }

        reception.writeStaffStatusToCSV("Database/staff.csv");
        writeCurrentPatientCounter();
        writeCurrentPersonCounter();
        System.out.println("Simulation complete. Check CSV files for results.");
        timer.stopTimer();

        timer = null;
        return;
    }

    public static void main(String[] args) {
        SimulationManager manager = new SimulationManager();
        manager.runSimulation(10);

    }

    public Timer getTimer() {
        return timer;
    }

    public Reception getReception() {
        return reception;
    }
}