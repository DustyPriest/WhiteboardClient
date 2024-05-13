import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;

public class ChatGUI extends JFrame implements KeyListener {
    private JPanel mainPanel;
    private JScrollPane chatScrollPane;
    private JTextField messageField;
    private JButton sendButton;
    private JScrollPane activeUsersScrollPane;
    private JTextArea chatArea;
    private JTextArea usersArea;
    private final IRemoteWhiteboard remoteWhiteboard;
    private final String username;

    public ChatGUI(IRemoteWhiteboard remoteWhiteboard, String username) {
        super();
        this.remoteWhiteboard = remoteWhiteboard;
        this.username = username;

        this.setContentPane(mainPanel);
        this.setTitle("Chat & Users");
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setSize(400, 600);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds((int) screenSize.getWidth() / 2 + 405, (int) screenSize.getHeight() / 2 - 300, 400, 600);

        sendButton.addActionListener(e -> sendMessage());
        messageField.addKeyListener(this);

        this.setVisible(true);
    }

    public void updateChat() {
        try {
            chatArea.setText(String.join("\n", remoteWhiteboard.getChatMessages()));
            usersArea.setText(String.join("\n", remoteWhiteboard.getCurrentUsers()));
        } catch (RemoteException ex) {
            System.err.println("Failed to get chat messages or users");
            Main.handleConnectionFailure(ex);
        }
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            try {
                remoteWhiteboard.addChatMessage(message, username);
                messageField.setText("");
            } catch (RemoteException ex) {
                System.err.println("Failed to send chat message");
                Main.handleConnectionFailure(ex);
            }
        }
    }

    @Override
    public void requestFocus() {
        super.requestFocus();
        messageField.requestFocus();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            sendMessage();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
