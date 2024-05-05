import java.awt.*;
import java.rmi.Remote;
import java.util.ArrayList;

public interface IRemoteWhiteboard extends Remote {

    public ArrayList<Shape> getShapes();
    public void addShape(Shape shape);
    public void clearShapes();

}
