import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Text;

public class Statistics {

    protected Shell shell;
    private LocalResourceManager localResourceManager;
    private Text text;
    private Text text_2;
    private Text text_1;
    private Text text_3;
    private Text text_4;
    private Text text_5;

    private SimulationStatistics statistics = new SimulationStatistics();
    private Label lblAverageWaitingTime;
    private Text text_6;
    /**
     * Launch the application.
     * @param args
     */
    public static void main(String[] args) {
        try {
            Statistics window = new Statistics();
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open the window.
     */
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

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        shell = new Shell();
        createResourceManager();
        shell.setSize(560, 424);
        shell.setText("SWT Application");

        Label lblOverallStatisticsOf = new Label(shell, SWT.BORDER);
        lblOverallStatisticsOf.setBackground((Color) localResourceManager.create(ColorDescriptor.createFrom(new RGB(219, 219, 219))));
        lblOverallStatisticsOf.setBounds(82, 21, 365, 33);
        lblOverallStatisticsOf.setText("  Overall Statistics of Emergency Department");

        Label lblTotalBedsUsed = new Label(shell, SWT.NONE);
        lblTotalBedsUsed.setBounds(10, 88, 145, 25);
        lblTotalBedsUsed.setText("Total Beds Used:");

        text = new Text(shell, SWT.BORDER);
        text.setBounds(166, 85, 281, 31);
        int beds = (statistics.getTotalBedsUsed());
        text.setText(String.valueOf(beds));

        Label lblNewLabel = new Label(shell, SWT.NONE);
        lblNewLabel.setBounds(10, 129, 156, 25);
        lblNewLabel.setText("Resuscitation Beds");

        Label lblAcuteBeds = new Label(shell, SWT.NONE);
        lblAcuteBeds.setBounds(10, 162, 177, 25);
        lblAcuteBeds.setText("Minor Procedure Beds");

        Label lblSubacuteBeds = new Label(shell, SWT.NONE);
        lblSubacuteBeds.setBounds(313, 162, 124, 25);
        lblSubacuteBeds.setText("Sub-Acute Beds");

        Label lblAcuteBeds_1 = new Label(shell, SWT.NONE);
        lblAcuteBeds_1.setText("Acute Beds");
        lblAcuteBeds_1.setBounds(313, 122, 102, 25);

        text_2 = new Text(shell, SWT.BORDER);
        text_2.setBounds(200, 162, 80, 31);
        int mb = (statistics.getTotalBedsbyType(1));
        text_2.setText(String.valueOf(mb));

        text_1 = new Text(shell, SWT.BORDER);
        text_1.setBounds(200, 129, 80, 31);
        int rb = (statistics.getTotalBedsbyType(0));
        text_1.setText(String.valueOf(rb));

        text_3 = new Text(shell, SWT.BORDER);
        text_3.setBounds(448, 123, 80, 31);
        int ab = (statistics.getTotalBedsbyType(2));
        text_3.setText(String.valueOf(ab));

        text_4 = new Text(shell, SWT.BORDER);
        text_4.setBounds(448, 162, 80, 31);
        int sab =(statistics.getTotalBedsbyType(3));
        text_4.setText(String.valueOf(sab));

        Label lblNewLabel_1 = new Label(shell, SWT.NONE);
        lblNewLabel_1.setBounds(10, 215, 244, 25);
        lblNewLabel_1.setText("Percentage of Patients Seen");

        text_5 = new Text(shell, SWT.BORDER);
        text_5.setBounds(265, 209, 177, 31);
        float seen = (statistics.PatientsSeenPercent());
        text_5.setText(String.valueOf(seen));


        lblAverageWaitingTime = new Label(shell, SWT.NONE);
        lblAverageWaitingTime.setBounds(10, 274, 365, 25);
        lblAverageWaitingTime.setText("Average Waiting Time for Patients Treatement");

        text_6 = new Text(shell, SWT.BORDER);
        text_6.setBounds(405, 268, 92, 31);
        double  avg = (statistics.getAverageWaitingTime());
        text_6.setText(String.valueOf(avg));

    }
    private void createResourceManager() {
        localResourceManager = new LocalResourceManager(JFaceResources.getResources(),shell);
    }
}
