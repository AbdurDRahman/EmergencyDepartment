import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

import java.io.*;

public class MainFrame {
    protected Shell shlEmergency;
    private LocalResourceManager localResourceManager;

    public static void main(String[] args) {
        try {
            MainFrame window = new MainFrame();
            window.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void open() {
        Display display = Display.getDefault();
        createContents();
        shlEmergency.open();
        shlEmergency.layout();
        while (!shlEmergency.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    protected void createContents() {
        Display display = Display.getDefault();
        shlEmergency = new Shell();
        createResourceManager();
        shlEmergency.setSize(649, 429);
        shlEmergency.setText("Emergency");

        // Add Labels and Buttons
        Label lblNewLabel = new Label(shlEmergency, SWT.NONE);
        lblNewLabel.setFont((Font) localResourceManager.create(FontDescriptor.createFrom("Segoe UI", 25, SWT.BOLD | SWT.ITALIC)));
        lblNewLabel.setBounds(393, 42, 217, 45);
        lblNewLabel.setText("EMERGENCY");

        Button btnStartSimulation = new Button(shlEmergency, SWT.NONE);
        btnStartSimulation.setFont((Font) localResourceManager.create(FontDescriptor.createFrom("Segoe UI", 12, SWT.NORMAL)));
        btnStartSimulation.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                    try {
                        DisplayOptions window = new DisplayOptions();

                        window.open();
                    }catch (Exception e1) {
                        e1.printStackTrace();
                    }
            }
        });
        btnStartSimulation.setBounds(404, 242, 146, 53);
        btnStartSimulation.setText("Start Simulation");

        // Load and Resize Image
        Label lblNewLabel_1 = new Label(shlEmergency, SWT.NONE);
        lblNewLabel_1.setBounds(10, 10, 337, 370);
        String imagePath = "Database/image.png";
        File file = new File(imagePath);
        if (file.exists()) {
            Image image = new Image(display, imagePath);
            ImageData resizedImageData = image.getImageData().scaledTo(lblNewLabel_1.getBounds().width, lblNewLabel_1.getBounds().height);
            Image resizedImage = new Image(display, resizedImageData);
            lblNewLabel_1.setImage(resizedImage);
        } else {
            System.out.println("Image not found: " + imagePath);
        }
    }

    private void createResourceManager() {
        localResourceManager = new LocalResourceManager(JFaceResources.getResources(), shlEmergency);
        shlEmergency.addDisposeListener(e -> localResourceManager.dispose());
    }
}
