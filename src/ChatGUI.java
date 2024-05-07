import javax.swing.*;

public class ChatGUI extends JFrame {
    private JPanel mainPanel;
    private JScrollPane chatScrollPane;
    private JTextField messageField;
    private JButton sendButton;
    private JScrollPane activeUsersScrollPane;
    private JTextArea chatArea;
    private JTextArea usersArea;

    public ChatGUI(IRemoteWhiteboard remoteWhiteboardState) {
        super();

        this.setContentPane(mainPanel);
        this.setTitle("Chat & Users");
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setSize(400, 600);
        this.setVisible(true);
    }

    @Override
    public void requestFocus() {
        super.requestFocus();
        messageField.requestFocus();
    }
}
