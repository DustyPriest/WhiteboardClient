import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    public static void main(String[] args) {

        try {
            //Connect to the rmiregistry that is running on localhost
            Registry registry = LocateRegistry.getRegistry("localhost");

            //Retrieve the stub/proxy for the remote math object from the registry
            IRemoteWhiteboard remoteMath = (IRemoteWhiteboard) registry.lookup("RemoteWhiteboard");

            new WhiteboardGUI(remoteMath);

        }catch(Exception e) {
            e.printStackTrace();
        }


    }
}
