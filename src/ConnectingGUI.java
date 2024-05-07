// ConnectingGUI
// Pop-up window while connection to server is pending
// Shows while waiting for streams from server

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

public class ConnectingGUI implements Runnable {

    private final JOptionPane optionPane;
    private final JDialog dialog;

    public ConnectingGUI(IRemoteWhiteboard remoteWhiteboardState, String username) {

        JFrame frame = new JFrame();
        String[] options = {"Quit"};

        optionPane = new JOptionPane(
                "Application to join whiteboard pending...",
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.YES_NO_OPTION, null, options, options[0]);

        dialog = new JDialog(frame,
                "Connecting",
                true);
        dialog.setContentPane(optionPane);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setBounds((int) screenSize.getWidth() / 2 - 135, (int) screenSize.getHeight() / 2 - 200, frame.getWidth(), frame.getHeight());

        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                try {
                    remoteWhiteboardState.retractApplication(username);
                } catch (RemoteException e) {
                    System.err.println("Failed to retract application");
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });

        optionPane.addPropertyChangeListener(
                (e) -> {
                    String prop = e.getPropertyName();

                    if (dialog.isVisible()
                            && (e.getSource() == optionPane)
                            && (prop.equals(JOptionPane.VALUE_PROPERTY))) {
                        System.exit(0);
                    }
                });

        dialog.pack();
    }

    @Override
    public void run() {
        dialog.setVisible(true);
    }

    public void close() {
        dialog.setVisible(false);
        dialog.dispose();
    }


}
