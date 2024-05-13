import javax.swing.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class Main {

    private static String address;
    private static String username;
    private static Timer timer;

    public static void main(String[] args) {

        if (!parseArgs(args)) {
            System.out.println("Usage: java -jar WhiteboardClient.jar <address> <username>");
            return;
        }

        try {
            //Connect to the rmi registry that is running on localhost
            Registry registry = LocateRegistry.getRegistry(address);

            //Retrieve the stub/proxy for the remote whiteboard object from the registry
            IRemoteWhiteboard remoteWhiteboard = (IRemoteWhiteboard) registry.lookup("RemoteWhiteboard");

            new WhiteboardGUI(remoteWhiteboard, username);

        }catch(Exception e) {
            handleConnectionFailure(e);
        }


    }

    public static void handleConnectionFailure(Exception e) {
        timer.stop();
        JOptionPane.showMessageDialog(null, "Connection to whiteboard failed.\nProgram will exit...", "Connection Failed", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
        System.exit(0);
    }

    public static void setTimer(Timer newTimer) {
        timer = timer;
    }

    private static boolean parseArgs(String[] args) {
        if (args.length != 2) {
            return false;
        }
        String ipRegex = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$";
        if (!args[0].matches(ipRegex) && !args[0].equals("localhost")) {
            return false;
        }
        address = args[0];
        username = args[1];
        return true;
    }

}
