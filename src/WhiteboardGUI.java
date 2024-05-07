import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;


public class WhiteboardGUI extends JFrame {
    private JPanel mainPanel;
    private JToolBar drawingOptionsToolbar;
    private JButton brushButton;
    private JButton eraseButton;
    private JButton lineButton;
    private JButton rectangleButton;
    private JButton circleButton;
    private JButton ovalButton;
    private JButton textButton;
    private JButton chatButton;
    private JToolBar styleOptionsToolbar;
    private JButton colorButton;
    private JSlider sizeSlider;
    private JSpinner fontSizeSpinner;
    private JComboBox<String> fontComboBox;
    private JCheckBox fillCheckBox;
    private final WhiteboardCanvas whiteboardCanvas;
    private JButton selectedDrawingButton = brushButton;
    private final ChatGUI chatGUI;

    public WhiteboardGUI(IRemoteWhiteboard remoteWhiteboardState, String username) {
        super();

        try {
            if (!remoteWhiteboardState.userExists(username)) {
                try {
                    remoteWhiteboardState.applyForConnection(username);
                    ConnectingGUI connectingGUI = new ConnectingGUI(remoteWhiteboardState, username);
                    SwingUtilities.invokeLater(connectingGUI);
                    awaitConnection(remoteWhiteboardState, username);
                    connectingGUI.close();
                } catch (RemoteException e) {
                    System.err.println("Failed to add user to remote whiteboard");
                    e.printStackTrace();
                    System.exit(1);
                }
            } else {
                System.err.println("Username already in use");
                System.exit(0);
            }
        } catch (RemoteException e) {
            System.err.println("Failed to validate username");
            e.printStackTrace();
            System.exit(1);
        }

        whiteboardCanvas = new WhiteboardCanvas(remoteWhiteboardState, username);
        mainPanel.add(whiteboardCanvas, BorderLayout.CENTER);
        for (String name : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
            fontComboBox.addItem(name);
        }
        fontComboBox.setSelectedItem("Arial");
        fontSizeSpinner.setValue(12);
        fontSizeSpinner.setModel(new SpinnerNumberModel(12, 1, 72, 1));

        addChatButton();
        setDrawingOptionsListeners();
        setStyleOptionsListeners();

        setDrawingMode(DrawingMode.BRUSH, brushButton);
        this.setTitle("Whiteboard");
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    remoteWhiteboardState.kickUser(username);
                } catch (RemoteException ex) {
                    System.err.println("Failed to remove user from server list");
                    ex.printStackTrace();
                }
            }
        });
        this.setSize(800, 600);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds((int) screenSize.getWidth() / 2 - 400, (int) screenSize.getHeight() / 2 - 300, 800, 600);
        this.setVisible(true);

        chatGUI = new ChatGUI(remoteWhiteboardState, username);
    }

    private void setDrawingOptionsListeners() {
        brushButton.addActionListener(e -> setDrawingMode(DrawingMode.BRUSH, brushButton));
        eraseButton.addActionListener(e -> setDrawingMode(DrawingMode.ERASE, eraseButton));
        lineButton.addActionListener(e -> setDrawingMode(DrawingMode.LINE, lineButton));
        rectangleButton.addActionListener(e -> setDrawingMode(DrawingMode.RECTANGLE, rectangleButton));
        circleButton.addActionListener(e -> setDrawingMode(DrawingMode.CIRCLE, circleButton));
        ovalButton.addActionListener(e -> setDrawingMode(DrawingMode.OVAL, ovalButton));
        textButton.addActionListener(e -> setDrawingMode(DrawingMode.TEXT, textButton));
        fillCheckBox.addActionListener(e -> whiteboardCanvas.setFillSelected(fillCheckBox.isSelected()));
        chatButton.addActionListener(e -> {
            chatGUI.setVisible(true);
            chatGUI.requestFocus();
        });
    }

    private void setStyleOptionsListeners() {
        sizeSlider.addChangeListener(e -> whiteboardCanvas.setDrawingStroke(sizeSlider.getValue()));
        colorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(null, "Choose a color", whiteboardCanvas.getDrawingColor());
            if (newColor != null) {
                whiteboardCanvas.setDrawingColor(newColor);
            }
        });
        fontSizeSpinner.addChangeListener(e -> {
            int newSize = (int) fontSizeSpinner.getValue();
            whiteboardCanvas.setFontSize(newSize);
        });
        fontComboBox.addActionListener(e -> {
            String fontName = (String) fontComboBox.getSelectedItem();
            whiteboardCanvas.setFontFamily(fontName);
        });
    }

    private void setDrawingMode(DrawingMode newMode, JButton button) {
        selectedDrawingButton.setEnabled(true);
        selectedDrawingButton = button;
        selectedDrawingButton.setEnabled(false);
        whiteboardCanvas.setDrawingMode(newMode);
    }

    private void addChatButton() {
        chatButton = new JButton("Chat");
        chatButton.setToolTipText("Show Chat");
        drawingOptionsToolbar.addSeparator();
        drawingOptionsToolbar.add(Box.createGlue());
        drawingOptionsToolbar.add(chatButton);
        drawingOptionsToolbar.addSeparator();
    }

    private void awaitConnection(IRemoteWhiteboard remoteWhiteboardState, String username) {
        try {
            while (remoteWhiteboardState.applicationPending(username)) {
                Thread.sleep(1000);
            }
        } catch (RemoteException | InterruptedException e) {
            System.err.println("Failed to wait for connection approval");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
