import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

enum DrawingMode {
    BRUSH,
    ERASE,
    LINE,
    RECTANGLE,
    CIRCLE,
    OVAL,
    TEXT
}
public class WhiteboardCanvas extends JPanel implements MouseInputListener {

    private boolean fillSelected = false;
    private DrawingMode drawingMode = DrawingMode.BRUSH;
    private Color canvasColor = Color.WHITE;

    private Color drawingColor = Color.BLACK;
    private int drawingStroke = 1;
    private ArrayList<Shape> shapes = new ArrayList<>();
    private Shape previewShape;

    public WhiteboardCanvas() {
        super();
        this.setBackground(Color.WHITE);
        shapes.add(new Rectangle(50, 50, 50, 50));
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(drawingColor);
        for (Shape shape : shapes) {
            g2.draw(shape);
        }
    }

    protected void setDrawingColor(Color color) {
        this.drawingColor = color;
    }

    protected void setDrawingStroke(int width) {
        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.setStroke(new BasicStroke(width));
        drawingStroke = width;
    }


    protected void setDrawingMode(DrawingMode drawingMode) {
        this.drawingMode = drawingMode;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (drawingMode) {
            case BRUSH:
                break;
            case LINE:
                break;
            case RECTANGLE:
                shapes.add(new Rectangle(e.getX(), e.getY(), 50, 50));
                this.repaint();
                break;
            case CIRCLE:
                break;
            case OVAL:
                break;
            case TEXT:
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        switch (drawingMode) {
            case BRUSH:
                shapes.add(new Ellipse2D.Double(e.getX(), e.getY(), drawingStroke, drawingStroke));
                this.repaint();
                break;
            case LINE:
                break;
            case RECTANGLE:
                break;
            case CIRCLE:
                break;
            case OVAL:
                break;
            case TEXT:
                break;
            default:
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
