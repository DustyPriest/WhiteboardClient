import javax.swing.*;
import java.awt.event.ActionEvent;
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
    private final Timer chatUpdateTimer;
    private final IRemoteWhiteboard remoteWhiteboardState;
    private final String username;

    public ChatGUI(IRemoteWhiteboard remoteWhiteboardState, String username) {
        super();
        this.remoteWhiteboardState = remoteWhiteboardState;
        this.username = username;
        this.setContentPane(mainPanel);
        this.setTitle("Chat & Users");
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setSize(400, 600);
        this.setVisible(true);

        sendButton.addActionListener(e -> sendMessage());
        messageField.addKeyListener(this);

        chatUpdateTimer = new Timer(1000, e -> {
            try {
                chatArea.setText(String.join("\n", remoteWhiteboardState.getChatMessages()));
                usersArea.setText(String.join("\n", remoteWhiteboardState.getCurrentUsers()));
            } catch (RemoteException ex) {
                System.err.println("Failed to get chat messages or users");
                ex.printStackTrace();
            }
        });
        chatUpdateTimer.start();
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            try {
                remoteWhiteboardState.addChatMessage(message, username);
                messageField.setText("");
            } catch (RemoteException ex) {
                System.err.println("Failed to send chat message");
                ex.printStackTrace();
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