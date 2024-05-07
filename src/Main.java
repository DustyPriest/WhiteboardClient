import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    private static String address;
    private static String username;

    public static void main(String[] args) {

        if (!parseArgs(args)) {
            System.out.println("Usage: java -jar WhiteboardClient.jar <address> <username>");
            return;
        }

        try {
            //Connect to the rmiregistry that is running on localhost
            Registry registry = LocateRegistry.getRegistry(address);

            //Retrieve the stub/proxy for the remote math object from the registry
            IRemoteWhiteboard remoteMath = (IRemoteWhiteboard) registry.lookup("RemoteWhiteboard");

            new WhiteboardGUI(remoteMath, username);

        }catch(Exception e) {
            e.printStackTrace();
        }


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
