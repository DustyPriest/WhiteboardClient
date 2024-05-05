import shapes.ICustomShape;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IRemoteWhiteboard extends Remote {

    ArrayList<ICustomShape> getShapes() throws RemoteException;
    void addShape(ICustomShape shape) throws RemoteException;
    void clearShapes() throws RemoteException;

}
