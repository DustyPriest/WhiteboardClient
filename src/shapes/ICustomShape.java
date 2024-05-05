package shapes;

import java.awt.*;

public interface ICustomShape {

    Color getColor();

    int getStrokeWidth();

    void updateBounds(int x, int y);
}
