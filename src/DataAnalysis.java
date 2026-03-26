import org.apache.commons.csv.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataAnalysis {
    protected String patientsFile = "Database/patients.csv";
    protected String staffFile = "Database/Staff.csv";
    protected String treatmentPairFile = "Database/TreatmentPair.csv";
    protected Connection conn;

    public static void main(String[] args) {
        DataAnalysis analysis = new DataAnalysis();

        try {
            analysis.conn = DriverManager.getConnection("jdbc:sqlite::memory:");
            // Enable foreign key support in SQLite
            try (Statement stmt = analysis.conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }

            // Load data into the database
            analysis.loadCSVToTable(analysis.patientsFile, "Patients", new String[]{"PersonId", "PatientId", "Name", "Gender", "TriageCategory", "Disease", "ArrivalTime", "WaitingTime", "TreatmentTime", "Status"});
            analysis.loadCSVToTable(analysis.staffFile, "Staff", new String[]{"PersonId", "StaffId", "Name", "Gender", "LifeTimePatients", "Occupation"});
            analysis.loadCSVToTable(analysis.treatmentPairFile, "TreatmentPair", new String[]{"StaffId", "PatientId", "BedId"});

            // Ask the user for the number of patient-doctor pairs they want to see
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the number of patient-doctor pairs you want to see: ");
            int numPairs = scanner.nextInt();

            // Display the specified number of patient-doctor information
            analysis.displayPatientDoctorInfo(numPairs);
//
//            // Example usage of the queryToArray function
//            String[][] result = analysis.queryToArray();
//            for (String[] row : result) {
//                System.out.println(String.join("\t", row));
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadCSVToTable(String filePath, String tableName, String[] columns) throws IOException, SQLException {
        try (Reader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            // Create table
            try (Statement stmt = conn.createStatement()) {
                StringBuilder createTableSQL = new StringBuilder("CREATE TABLE ").append(tableName).append(" (");
                for (String column : columns) {
                    createTableSQL.append(column).append(" TEXT, ");
                }
                createTableSQL.setLength(createTableSQL.length() - 2); // Remove last comma
                createTableSQL.append(");");
                stmt.execute(createTableSQL.toString());
            }

            // Insert data
            String insertSQL = "INSERT INTO " + tableName + " VALUES (" + "?, ".repeat(columns.length).replaceAll(", $", "") + ");";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                for (CSVRecord record : csvParser) {
                    for (int i = 0; i < columns.length; i++) {
                        pstmt.setString(i + 1, record.get(columns[i]));
                    }
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }
        }
    }

    public void displayPatientDoctorInfo(int numPairs) throws SQLException {
        String query = """
        
                SELECT 
            Patients.Name AS PatientName,
            Staff.Name AS DoctorName,
            Staff.Occupation AS DoctorOccupation,
            Patients.Disease AS DiseaseName,
            Patients.TriageCategory AS TriageCategory,
            Patients.WaitingTime AS WaitingTime,
            Patients.TreatmentTime AS TreatmentTime
        FROM 
            TreatmentPair
        JOIN 
            Patients ON TreatmentPair.PatientId = Patients.PatientId
        JOIN 
            Staff ON TreatmentPair.StaffId = Staff.StaffId
        LIMIT ?;
        """;

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, numPairs); // Set the limit based on user input

            try (ResultSet rs = pstmt.executeQuery()) {

                // Print table headers
                System.out.println(String.format("%-20s %-20s %-20s %-30s %-15s %-15s %-15s",
                        "Patient Name", "Doctor Name", "Doctor Occupation", "Disease Name", "Triage ", "Waiting Time", "Treatment Time"));
                System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------");

                // Print each row of data
                while (rs.next()) {
                    String patientName = rs.getString("PatientName");
                    String doctorName = rs.getString("DoctorName");
                    String doctorOccupation = rs.getString("DoctorOccupation");
                    String diseaseName = rs.getString("DiseaseName");
                    String triageCategory = rs.getString("TriageCategory");
                    String waitingTime = rs.getString("WaitingTime");
                    String treatmentTime = rs.getString("TreatmentTime");

                    // Format each row to fit the table with the new width for Disease Name
                    System.out.printf("%-20s %-20s %-20s %-30s %-15s %-15s %-15s%n",
                            patientName, doctorName, doctorOccupation, diseaseName, triageCategory, waitingTime, treatmentTime);
                }

            }
        }
    }

    public String[][] queryToArray() throws SQLException, IOException {
        DataAnalysis analysis = new DataAnalysis();

        // Initialize the in-memory SQLite database
        try {
            analysis.conn = DriverManager.getConnection("jdbc:sqlite::memory:");
            try (Statement stmt = analysis.conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Failed to initialize the database connection.", e);
        }

        // Load data into the database
        analysis.loadCSVToTable(analysis.patientsFile, "Patients",
                new String[]{"PersonId", "PatientId", "Name", "Gender", "TriageCategory", "Disease",
                        "ArrivalTime", "WaitingTime", "TreatmentTime", "Status"});
        analysis.loadCSVToTable(analysis.staffFile, "Staff",
                new String[]{"PersonId", "StaffId", "Name", "Gender", "LifeTimePatients", "Occupation"});
        analysis.loadCSVToTable(analysis.treatmentPairFile, "TreatmentPair",
                new String[]{"StaffId", "PatientId", "BedId"});

        // Query to fetch the data
        String query = """
        SELECT 
            Patients.Name AS PatientName,
            Staff.Name AS DoctorName,
            Staff.Occupation AS DoctorOccupation,
            Patients.Disease AS DiseaseName,
            Patients.TriageCategory AS TriageCategory,
            Patients.WaitingTime AS WaitingTime,
            Patients.TreatmentTime AS TreatmentTime
        FROM 
            TreatmentPair
        JOIN 
            Patients ON TreatmentPair.PatientId = Patients.PatientId
        JOIN 
            Staff ON TreatmentPair.StaffId = Staff.StaffId;
        """;

        List<String[]> resultList = new ArrayList<>();

        // Execute the query and process results
        try (Statement stmt = analysis.conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Get column names
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Add header row
            String[] header = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                header[i - 1] = metaData.getColumnName(i);
            }
            resultList.add(header);

            // Add data rows
            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getString(i);
                }
                resultList.add(row);
            }
        }

        // Convert list to 2D array and return
        return resultList.toArray(new String[0][]);
    }

}

