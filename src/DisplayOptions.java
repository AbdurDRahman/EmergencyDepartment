import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DisplayOptions {

    protected Shell shell;
    private LocalResourceManager localResourceManager;
    private Text currentTimeText;
    private Table patientDetailsTable;
    private Table waitingPatientsTable;
    private SimulationManager simulationManager;

    public static void main(String[] args) {
        try {
            DisplayOptions window = new DisplayOptions();
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DisplayOptions() {
        simulationManager = new SimulationManager(); // Initialize the simulation manager
        new Thread(() -> simulationManager.runSimulation(10)).start(); // Run simulation in a separate thread
    }

    public void open() {
        Display display = Display.getDefault();
        createContents();
        shell.open();
        shell.layout();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    protected void createContents() {
        shell = new Shell();
        createResourceManager();
        shell.setSize(800, 600);
        shell.setText("Hospital Management System");

        // Labels
        Label lblCurrentTime = new Label(shell, SWT.NONE);
        lblCurrentTime.setFont((Font) localResourceManager.create(FontDescriptor.createFrom("Segoe UI", 10, SWT.BOLD)));
        lblCurrentTime.setBounds(10, 10, 100, 20);
        lblCurrentTime.setText("Current Time");

        currentTimeText = new Text(shell, SWT.BORDER);
        currentTimeText.setEditable(false);
        currentTimeText.setBounds(120, 10, 150, 20);

        Label lblPatientDetails = new Label(shell, SWT.NONE);
        lblPatientDetails.setFont((Font) localResourceManager.create(FontDescriptor.createFrom("Segoe UI", 10, SWT.BOLD)));
        lblPatientDetails.setBounds(10, 50, 200, 20);
        lblPatientDetails.setText("Patient Details");

        patientDetailsTable = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
        patientDetailsTable.setBounds(10, 80, 360, 417);
        patientDetailsTable.setHeaderVisible(true);
        patientDetailsTable.setLinesVisible(true);

        TableColumn column1 = new TableColumn(patientDetailsTable, SWT.NONE);
        column1.setText("Name");
        column1.setWidth(180);

        TableColumn column2 = new TableColumn(patientDetailsTable, SWT.NONE);
        column2.setText("Disease");
        column2.setWidth(180);

        Label lblWaitingPatients = new Label(shell, SWT.NONE);
        lblWaitingPatients.setFont((Font) localResourceManager.create(FontDescriptor.createFrom("Segoe UI", 10, SWT.BOLD)));
        lblWaitingPatients.setBounds(400, 50, 200, 20);
        lblWaitingPatients.setText("Waiting Patients");

        waitingPatientsTable = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
        waitingPatientsTable.setBounds(400, 80, 360, 417);
        waitingPatientsTable.setHeaderVisible(true);
        waitingPatientsTable.setLinesVisible(true);

        TableColumn wColumn1 = new TableColumn(waitingPatientsTable, SWT.NONE);
        wColumn1.setText("Name");
        wColumn1.setWidth(120);

        TableColumn wColumn2 = new TableColumn(waitingPatientsTable, SWT.NONE);
        wColumn2.setText("Disease");
        wColumn2.setWidth(120);

        TableColumn wColumn3 = new TableColumn(waitingPatientsTable, SWT.NONE);
        wColumn3.setText("Waiting Time");
        wColumn3.setWidth(120);

        Button btnTerminateProgram = new Button(shell, SWT.NONE);
        btnTerminateProgram.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDoubleClick(MouseEvent e) {
                try {
                    simulationManager.endSimulation();
                    OverallStatistics  overallStatistics = new OverallStatistics();
                    overallStatistics.open();

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);

                }

                // Terminate the program
            }

        });


        btnTerminateProgram.setBounds(62, 521, 150, 30);
        btnTerminateProgram.setText("Terminate Program");

        // Update simulation data periodically
        updateSimulationTime();
        updatePatientDetails();
        updateWaitingPatients();
    }

    private void createResourceManager() {
        localResourceManager = new LocalResourceManager(JFaceResources.getResources(), shell);
    }

    private void updateSimulationTime() {
        Display.getDefault().timerExec(1000, new Runnable() {
            @Override
            public void run() {
                Time currentTime = simulationManager.getTimer().getTimeElapsed();
                currentTimeText.setText(currentTime.toString());
                if (!shell.isDisposed()) {
                    updateSimulationTime();
                }
            }
        });
    }

    private void updatePatientDetails() {
        Display.getDefault().timerExec(2000, new Runnable() {
            @Override
            public void run() {
                List<Patient> patients = simulationManager.getReception().getPatients();
                System.out.println("Patient Details: " + patients); // Debugging output
                patientDetailsTable.removeAll();
                for (Patient patient : patients) {
                    TableItem item = new TableItem(patientDetailsTable, SWT.NONE);
                    item.setText(new String[] { patient.getName(), patient.getDisease() });
                }
                if (!shell.isDisposed()) {
                    updatePatientDetails();
                }
            }
        });
    }

    private void updateWaitingPatients() {
        Display.getDefault().timerExec(2000, new Runnable() {
            @Override
            public void run() {
                List<Patient> waitingPatients = simulationManager.getReception().getPatientQueue();
                System.out.println("Waiting Patients: " + waitingPatients); // Debugging output
                waitingPatientsTable.removeAll();
                for (Patient patient : waitingPatients) {
                    Time waitTime = simulationManager.getTimer().getTimeElapsed().calcDuration(patient.getArrivalTime());
                    TableItem item = new TableItem(waitingPatientsTable, SWT.NONE);
                    item.setText(new String[] { patient.getName(), patient.getDisease(), waitTime.toString() });
                }
                if (!shell.isDisposed()) {
                    updateWaitingPatients();
                }
            }
        });
    }

    }
