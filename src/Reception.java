import java.util.*;
import java.io.*;


public class Reception {
    private ArrayList<Patient> patients;
    private ArrayList<Patient> queue;
    private ArrayList<SeniorDoctor> seniorDoctors;
    private ArrayList<JuniorDoctor> juniorDoctors;
    private ArrayList<Intern> interns;
    private ArrayList<Nurse> nurses;
    private ArrayList<Staff> staffs;
    private List<String[]> diseaseParameters;
    private Random rand;
    Time NextTime;
    Room room;
    DiseaseList diseaseList;
    ArrayList<TreatmentPair> treatmentPairs;

    public Reception() {
        diseaseParameters = new ArrayList<>();
        interns = new ArrayList<Intern>();
        treatmentPairs = new ArrayList<TreatmentPair>();
        queue = new ArrayList<Patient>();
        diseaseList = new DiseaseList();
        room = new Room();
        nurses = new ArrayList<Nurse>();
        juniorDoctors = new ArrayList<JuniorDoctor>();
        seniorDoctors = new ArrayList<SeniorDoctor>();
        patients = new ArrayList<Patient>();
        staffs = new ArrayList<Staff>();
        NextTime = new Time();
        this.LoadStaff();
        rand = new Random();
        loadParameters("Database/disease_parameters.csv");

    }

    public List<Patient> getPatients() {
        return patients;
    }

    public List<SeniorDoctor> getSeniorDoctors() {
        return seniorDoctors;
    }

    public List<JuniorDoctor> getJuniorDoctors() {
        return juniorDoctors;
    }

    public List<Nurse> getNurses() {
        return nurses;
    }

    public List<Intern> getInterns() {
        return interns;
    }

    public List<Staff> getStaffs() {
        return staffs;
    }

    public List<TreatmentPair> getTreatmentPairs() {
        return treatmentPairs;
    }

    public List<Patient> getPatientQueue() {
        return queue;
    }


    public void InsertPatient(Patient patient) {
        patients.add(patient);
    }

    public void InsertSeniorDoctor(SeniorDoctor seniorDoctor) {
        seniorDoctors.add(seniorDoctor);
    }

    public void InsertJuniorDoctor(JuniorDoctor juniorDoctor) {
        juniorDoctors.add(juniorDoctor);
    }

    public void InsertIntern(Intern intern) {
        interns.add(intern);
    }

    public void InsertNurse(Nurse nurse) {
        nurses.add(nurse);
    }

    public void LoadStaff() {
        String file = "Database/Staff.csv";
        String line;
        boolean gender = false;
        String delimiter = ",";
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(delimiter);
                if (Objects.equals(data[5], "SeniorDoctor")) {
                    SeniorDoctor seniorDoctor = new SeniorDoctor();
                    seniorDoctor.setStaffID(Integer.parseInt(data[1]));
                    seniorDoctor.setPersonId(Integer.parseInt(data[0]));
                    seniorDoctor.setName(data[2]);
                    if (Objects.equals(data[3], "male")) {
                        gender = true;
                    }
                    seniorDoctor.setGender(gender);
                    seniorDoctor.setNumLifetimePatients(Integer.parseInt(data[4]));
                    InsertSeniorDoctor(seniorDoctor);
                    staffs.add(seniorDoctor);

                } else if (Objects.equals(data[5], "JuniorDoctor")) {
                    JuniorDoctor juniorDoctor = new JuniorDoctor();
                    juniorDoctor.setStaffID(Integer.parseInt(data[1]));
                    juniorDoctor.setPersonId(Integer.parseInt(data[0]));
                    juniorDoctor.setName(data[2]);
                    if (Objects.equals(data[3], "male")) {
                        gender = true;
                    }
                    juniorDoctor.setGender(gender);
                    juniorDoctor.setNumLifetimePatients(Integer.parseInt(data[4]));
                    InsertJuniorDoctor(juniorDoctor);
                    staffs.add(juniorDoctor);
                } else if (Objects.equals(data[5], "Intern")) {
                    Intern intern = new Intern();
                    intern.setPersonId(Integer.parseInt(data[0]));
                    intern.setStaffID(Integer.parseInt(data[1]));
                    intern.setName(data[2]);
                    if (Objects.equals(data[3], "male")) {
                        gender = true;
                    }
                    intern.setGender(gender);
                    intern.setNumLifetimePatients(Integer.parseInt(data[4]));
                    InsertIntern(intern);
                    staffs.add(intern);
                } else if (Objects.equals(data[5], "Nurse")) {
                    Nurse nurse = new Nurse();
                    nurse.setPersonId(Integer.parseInt(data[0]));
                    nurse.setStaffID(Integer.parseInt(data[1]));
                    nurse.setName(data[2]);
                    if (Objects.equals(data[3], "male")) {
                        gender = true;
                    }
                    nurse.setGender(gender);
                    nurse.setNumLifetimePatients(Integer.parseInt(data[4]));
                    InsertNurse(nurse);
                    staffs.add(nurse);
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadParameters(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); //To Skip header,the first line in file
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                diseaseParameters.add(parts); //Adding disease data as an array
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public Time CalculateArrivalTime(String DiseaseName, Time LastArrivalTime) {
        double alpha = 0, beta = 0;
        if (Objects.equals(DiseaseName, "empty")) {
            System.out.println("No disease parameters loaded.");
            return new Time(0, 0, 0); // Return default time
        }


        for (int i = 0; i < diseaseParameters.size(); i++) {
            String[] selectedDisease = diseaseParameters.get(i);
            if (DiseaseName.equals(selectedDisease[0])) {
                alpha = Double.parseDouble(selectedDisease[1]);
                beta = Double.parseDouble(selectedDisease[2]);
                break;
            }
        }

        // Simulate inter-arrival time using the selected disease's parameters
        Time arrivalTime = simulateArrivalTime(beta, alpha).AddTime(LastArrivalTime);
        return arrivalTime;
    }

    private Time simulateArrivalTime(double beta, double alpha) {
        double u = rand.nextDouble();
        double x = beta * Math.pow(-Math.log(1 - u), 1 / alpha);
        int minutes = (int) x;
        int hours = minutes / 60;
        int days = hours / 24;
        return new Time(days, hours % 24, minutes % 60);
    }


    public boolean simulatePatientArrival(Time currentTime, Time ArrivalTime) {
        if (currentTime.IsGreaterOrEqual(ArrivalTime)) { // Simulate arrivals based on rate
            patientArrival(currentTime);// Add new patient
            if(queue.isEmpty())NextTime = CalculateArrivalTime(patients.getLast().getDisease(), currentTime);
            else NextTime = CalculateArrivalTime(queue.getLast().getDisease(),currentTime);
            return true;
        }
        return false;
    }

    public Disease AssignDisease() {
        return diseaseList.getRandomDisease();
    }

    public void AssignTriage(Patient patient) {
        int count = 0;
        int[] segmentation = new int[5];
        Disease randomDisease = AssignDisease();
        patient.setDisease(randomDisease.getName());
        Random rand = new Random();
        int randomNumber = rand.nextInt(10000);

        count += (int) (randomDisease.getCategoryOneChance() * 100);
        segmentation[0] = count;
        count += (int) (randomDisease.getCategoryTwoChance() * 100);
        segmentation[1] = count;
        count += (int) (randomDisease.getCategoryThreeChance() * 100);
        segmentation[2] = count;
        count += (int) (randomDisease.getCategoryFourChance() * 100);
        segmentation[3] = count;
        count += (int) (randomDisease.getCategoryFiveChance() * 100);
        segmentation[4] = count;
        if (randomNumber <= segmentation[0]) {
            patient.setTriageCategory(1);
        } else if (randomNumber <= segmentation[1]) {
            patient.setTriageCategory(2);
        } else if (randomNumber <= segmentation[2]) {
            patient.setTriageCategory(3);
        } else if (randomNumber <= segmentation[3]) {
            patient.setTriageCategory(4);
        } else if (randomNumber <= segmentation[4]) {
            patient.setTriageCategory(5);
        }
    }


    public void patientArrival(Time currentTime) {
        Patient patient = new Patient();
        patient.setArrivalTime(currentTime);
        AssignTriage(patient);
        patient.AssignNameGender();
        SetTreatmentTime(patient);
        TreatmentPair pair = new TreatmentPair();
        pair.setPatientId(patient.getPatientId());
        switch (patient.getTriageCategory()) {
            case 1:
                if(AssignBedForCategoryOne(pair)) {
                    AssignDoctor(pair, patient.getTriageCategory());
                } else {
                    pair.setBedId(-1);
                    pair.setStaffId(seniorDoctors.getFirst().getStaffID());
                    writeTreatmentPair("Database/TreatmentPair.csv" , pair);
                    writePatientDataToCSV("Database/Patients.csv",patient,"Consulted");
                    return;
                }
                // TODO: if doctor is not available assign a consultant
                break;
            case 2:
                if (AssignBedForCategoryTwo(pair)) {
                    AssignDoctor(pair, patient.getTriageCategory());
                } else {
                    queue.add(patient);
                    return;
                }
                break;
            case 3, 4, 5:
                if (AssignBedForCategory345(pair)) {
                    if (AssignDoctor(pair, patient.getTriageCategory())) {
                        ;
                    }
                } else {
                    System.out.println("Patient added to waiting queue");
                    queue.add(patient);
                    return;
                }
                break;
        }
        treatmentPairs.add(pair);
        patient.setStartOfTreatment(currentTime);
        patients.add(patient);
        // TODO: patient.que() (DONE)
        // TODO: patient.assignBed() (DONE)
        // TODO: patient.getWaitTime() (DONT NEED TO DO HERE) (KIND OF DONE SOMEWHERE ELSE)
        // TODO: patient.getTreatmentTime() (DONE)
    }

    public void SetTreatmentTime(Patient patient) {
        Time TreatmentTime = calculateTreatmentTime(patient.getTriageCategory());
        patient.setDuration(TreatmentTime);
    }

    public int NextPatientToTreat() {
        //TODO:
        int priority_index = 0;
        for (int i = 0; i < queue.size(); i++) {
            if (queue.get(i).getTriageCategory() == 2) {
                priority_index = i;

                for (int j = i + 1; j < queue.size(); j++) {
                    if (queue.get(j).getTriageCategory() == 2) {
                        if (!queue.get(priority_index).getArrivalTime().islessthan(queue.get(j).getArrivalTime())) {
                            priority_index = j;
                        }
                    }
                }
                return priority_index;
            } else if (queue.get(i).getTriageCategory() == 3) {
                priority_index = i;
                for (int j = i + 1; j < queue.size(); j++) {
                    if (queue.get(j).getTriageCategory() == 2) {
                        break;
                    } else if (queue.get(j).getTriageCategory() == 3) {
                        if (!queue.get(priority_index).getArrivalTime().islessthan(queue.get(j).getArrivalTime())) {
                            priority_index = j;
                        }
                    }
                }
                return priority_index;
            } else if (queue.get(i).getTriageCategory() == 4) {
                priority_index = i;
                for (int j = i + 1; j < queue.size(); j++) {
                    if (queue.get(j).getTriageCategory() < 4) {
                        break;
                    } else if (queue.get(j).getTriageCategory() == 4) {
                        if (!queue.get(priority_index).getArrivalTime().islessthan(queue.get(j).getArrivalTime())) {
                            priority_index = j;
                        }
                    }
                }
                return priority_index;
            } else if (queue.get(i).getTriageCategory() == 5) {
                priority_index = i;
                for (int j = i + 1; j < queue.size(); j++) {
                    if (queue.get(j).getTriageCategory() < 5) {
                        break;
                    } else if (queue.get(j).getTriageCategory() == 5) {
                        if (!queue.get(priority_index).getArrivalTime().islessthan(queue.get(j).getArrivalTime())) {
                            priority_index = j;
                        }
                    }
                }
                return priority_index;
            }
        }
        return priority_index;
    }

    public boolean AssignDoctor(TreatmentPair pair, int category) {
        switch (category) {
            case 1:

                if (CheckSeniorDoctor(pair)) return true;
                if (CheckJuniorDoctor(pair)) return true;
                if (CheckNurse(pair)) return true;
                break;
            case 2:
                if (CheckJuniorDoctor(pair)) return true;
                if (CheckSeniorDoctor(pair)) return true;
                if (CheckNurse(pair)) return true;
                break;
            case 3:
                if (CheckJuniorDoctor(pair)) return true;
                if (CheckIntern(pair)) return true;
                if (CheckNurse(pair)) return true;
                if (CheckSeniorDoctor(pair)) return true;
                break;
            case 4, 5:
                if (CheckIntern(pair)) return true;
                if (CheckNurse(pair)) return true;
                if (CheckJuniorDoctor(pair)) return true;
                if (CheckSeniorDoctor(pair)) return true;
                break;

        }
        return false;
    }

    private boolean CheckNurse(TreatmentPair pair) {
        for (int i = 0; i < nurses.size(); i++) {
            if (nurses.get(i).CheckAvailability()) {
                nurses.get(i).setOccupied();
                pair.setStaffId(nurses.get(i).getStaffID());
                Collections.rotate(nurses, 1);
                return true;
            }
        }
        return false;
    }

    private boolean CheckSeniorDoctor(TreatmentPair pair) {
        for (int i = 0; i < seniorDoctors.size(); i++) {
            if (seniorDoctors.get(i).CheckAvailability()) {
                seniorDoctors.get(i).setOccupied();
                pair.setStaffId(seniorDoctors.get(i).getStaffID());
                Collections.rotate(seniorDoctors, 1);
                return true;
            }
        }
        return false;
    }

    private boolean CheckJuniorDoctor(TreatmentPair pair) {
        for (int i = 0; i < juniorDoctors.size(); i++) {
            if (juniorDoctors.get(i).CheckAvailability()) {
                juniorDoctors.get(i).setOccupied();
                pair.setStaffId(juniorDoctors.get(i).getStaffID());
                Collections.rotate(juniorDoctors, 1);
                return true;
            }
        }
        return false;
    }

    public boolean CheckIntern(TreatmentPair pair) {
        for (int i = 0; i < interns.size(); i++) {
            if (interns.get(i).CheckAvailability()) {
                interns.get(i).setOccupied();
                pair.setStaffId(interns.get(i).getStaffID());
                Collections.rotate(interns, 1);
                return true;
            }
        }
        System.out.println("No intern Is Available");
        return false;
    }

    public boolean AssignBedForCategoryOne(TreatmentPair pair) {
        if (room.IsFreeResuscitationBed()) {
            pair.setBedId(room.AssignResuscitationBed());
        } else if (room.IsFreeAcuteBed()) {
            pair.setBedId(room.AssignAcuteBed());
        } else if (room.IsFreeSubAcuteBed()) {
            pair.setBedId(room.AssignSubAcuteBed());
        } else if (room.IsFreeMinorBed()) {
            pair.setBedId(room.AssignMinorBed());
        } else {
            return false;
        }
        return true;
    }

    public boolean AssignBedForCategoryTwo(TreatmentPair pair) {
        if (room.IsFreeAcuteBed()) {
            pair.setBedId(room.AssignAcuteBed());
        } else if (room.IsFreeSubAcuteBed()) {
            pair.setBedId(room.AssignSubAcuteBed());
        } else if (room.IsFreeMinorBed()) {
            pair.setBedId(room.AssignMinorBed());
        } else if (room.IsFreeResuscitationBed()) {
            pair.setBedId(room.AssignResuscitationBed());
        } else {
            return false;
        }
        return true;
    }

    public boolean AssignBedForCategory345(TreatmentPair pair) {
        if (room.IsFreeMinorBed()) {
            pair.setBedId(room.AssignMinorBed());
        } else if (room.IsFreeSubAcuteBed()) {
            pair.setBedId(room.AssignSubAcuteBed());
        } else if (room.IsFreeAcuteBed()) {
            pair.setBedId(room.AssignAcuteBed());
        } else {
            return false;
        }
        return true;
    }

    public void writePatientDataToCSV(String fileName, Patient patient, String Status) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            String Gender;
            if (patient.getGender()) {
                Gender = "male";
            } else {
                Gender = "female";
            }

            writer.println(patient.getPersonId() + "," + patient.getPatientId() + "," + patient.getName() + "," + Gender + ","
                    + patient.getTriageCategory() + "," + patient.getDisease() + "," + patient.getArrivalTime().getDay() + ":"
                    + patient.getArrivalTime().getHour() + ":" + patient.getArrivalTime().getMinute() + "," + patient.getWaitTime().getDay()
                    + ":" + patient.getWaitTime().getHour() + ":" + patient.getWaitTime().getMinute() + "," + patient.getDuration().getDay() + ":"
                    + patient.getDuration().getHour() + ":" + patient.getDuration().getMinute() + "," + Status);
        } catch (IOException e) {
            System.out.println("Error writing patient data: " + e.getMessage());
        }
    }

    public void writeTreatmentPair(String fileName, TreatmentPair pair) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            writer.println(pair.getStaffId() + "," + pair.getPatientId() + "," + pair.getBedId());
        } catch (IOException e) {
            System.out.println("Error writing treatment pair: " + e.getMessage());
        }
    }

    public void writeStaffStatusToCSV(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            String gender = "female";
            writer.println("PersonId" + "," + "StaffId" + "," + "Name" + "," + "Gender" + "," + "LifeTimePatients" + "," + "Occupation");
            for (int i = 0; i < staffs.size(); i++) {
                Staff staff = staffs.get(i);
                if (staff.getGender()) gender = "male";
                writer.println(staff.getPersonId() + "," + staff.getStaffID() + "," + staff.getName() + "," + gender + "," + staff.getNumLifetimePatients() + "," + staff.getClass().getSimpleName());
            }
        } catch (IOException e) {
            System.out.println("Error writing staff status: " + e.getMessage());
        }
    }

    public void displayAllStaff() {
        for (int i = 0; i < staffs.size(); i++) {
            System.out.println(staffs.get(i).getStaffID() + "," + staffs.get(i).getName() + "," + staffs.get(i).getClass().getSimpleName());
        }
    }

    public void displayStaffMember(int index) {
        System.out.println(staffs.get(index).getStaffID() + "," + staffs.get(index).getName() + "," + staffs.get(index).getClass() + "," + staffs.get(index).getNumLifetimePatients() + "," + staffs.get(index).getOccupied());
    }

    public Time getNextTime() {
        return NextTime;
    }

    private  long factorial(int n) {
        if (n <= 1) return 1;
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    private  double betaFunction(int p, int q) {
        long numerator = factorial(p - 1) * factorial(q - 1);
        long denominator = factorial(p + q - 1);
        return (double) numerator / denominator;
    }

    public  Time calculateTreatmentTime(int category) {
        double beta = 0;
        int p = 0, q = 0;

        //to load parameters for the given category
        try (BufferedReader br = new BufferedReader(new FileReader("Database/treatment_parameters.csv"))) {
            String line;
            line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (Integer.parseInt(parts[0]) == category) {
                    beta = Double.parseDouble(parts[1]);
                    p = Integer.parseInt(parts[2]);
                    q = Integer.parseInt(parts[3]);
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading parameters file: " + e.getMessage());
            return new Time(0, 0, 0); // Default time for error handling
        }

        //For If parameters are not found for the category
        if (beta == 0 || p == 0 || q == 0) {
            System.out.println("Category parameters not found.");
            return new Time(0, 0, 0);       // Default time 00:00
        }

        // Generate a random x value between 0 and beta for the Pearson VI calculation
        Random random = new Random();
        double x = random.nextDouble() * beta;

        // Calculate f(x) using the Pearson VI formula
        double Bpq = betaFunction(p, q);
        double numerator = Math.pow((x / beta), (p - 1));
        double denominator = beta * Math.pow((1 + (x / beta)), (p + q)) * Bpq;
        double f_x = numerator / denominator;


        int minutes = (int) x;
        int hours = minutes / 60;
        int days = hours / 24;
        return new Time(days, hours % 24, minutes % 60);
    }

    public static void main(String[] args) {
        Reception reception = new Reception();
        reception.LoadStaff();
        Patient patient = new Patient();
        reception.displayAllStaff();
        for (int i = 0; i < reception.diseaseParameters.size(); i++) {
            String[] array = reception.diseaseParameters.get(i);
            if (array[0].equals("Environmental/Temperature/MISC")) System.out.println(array[0]);
        }
    }
}
// TODO: make the person leave when treatment time is completed and start treatment of the next patient(DONE)
// TODO: Read a csv having all Person above input attributes (DONE)
// TODO: Create Method/s for Probability of arrival of patients (DONE)


// TODO: Write data of patients after they have been discharged in a csv file(DONE)
// TODO: Delete the patient after his/her record has been added in the csv file(DONE)
// TODO: After a specific interval of time update the current status of staff members into the csv file(DONE)
// TODO: think if we need something else
// TODO: MAKE A run method in Main.java for testing before making GUI

// TODO: Map time and give option to map time faster ; (Done)
// TODO: Create Method/s for Probability of Disease (and triage category) \\ (Done)
// TODO: Read a csv having all diseases and there alpha beta values etc \\ (Done)
// TODO: Create a waiting queue (Done)
// TODO: Create Method/s for Priority Waiting Queue (DONE)
// TODO: Create Method/s for Assigning beds to patients (DONE)
// TODO: Randomly Assign those Attributes  \\ ( Done)
// TODO: assign waiting time to the person highest in the que (DONE)
// TODO: Create Method/s for Assigning Doctors,Interns,Nurses to Patients or Beds (DONE)
// TODO: Create Method/s for Assigning Arrival Time, Waiting Time And Exit time to patients (Partially Done)
