import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import java.io.IOException;
import java.sql.SQLException;

public class OverallStatistics {

    protected Shell shell;

    public static void main(String[] args) {
        try {
            OverallStatistics window = new OverallStatistics();
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void open() throws SQLException, IOException {
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

    protected void createContents() throws SQLException, IOException {
        shell = new Shell();
        shell.setSize(800, 400); // Increased size to fit the table
        shell.setText("Overall Statistics");

        // Create a table widget
        Table table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
        table.setBounds(10, 10, 760, 340); // Position and size of the table
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        // Define columns
        String[] columnHeaders = {
                "Patient Name", "Doctor Name", "Doctor Occupation",
                "Disease Name", "Triage", "Waiting Time", "Treatment Time"
        };
        for (String header : columnHeaders) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText(header);
            column.setWidth(120); // Adjust column width
        }

        // Fetch data and populate the table
        String[][] data = fetchData();
        int i = 0 ;
        for (String[] row : data) {
            if(!(i == 0)){
                TableItem item = new TableItem(table, SWT.NONE);
                item.setText(row);
            }
            i++;

        }
    }

    /**
     * Mock method to fetch data from the DataAnalysis class.
     * Replace this with an actual call to the DataAnalysis queryToArray method.
     */
    private String[][] fetchData() throws SQLException, IOException {
        DataAnalysis analysis = new DataAnalysis();
        return analysis.queryToArray();
    }
}
