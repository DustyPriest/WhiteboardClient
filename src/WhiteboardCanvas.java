import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
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
        if (previewShape != null) {
            g2.draw(previewShape);
        }
    }

    protected void setDrawingColor(Color color) {
        System.out.println("Colour set to: " + color.toString());
        this.drawingColor = color;
    }

    protected void setDrawingStroke(int width) {
        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.setStroke(new BasicStroke(width));
        drawingStroke = width;
    }


    protected void setDrawingMode(DrawingMode drawingMode) {
        // TODO: put colour on shapes themselves
        if (drawingMode == DrawingMode.ERASE) {
            setDrawingColor(canvasColor);
        } else if (this.drawingMode == DrawingMode.ERASE) {
            setDrawingColor(Color.BLACK);
        }
        this.drawingMode = drawingMode;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (drawingMode) {
            case ERASE:
                shapes.add(new Ellipse2D.Double(e.getX(), e.getY(), drawingStroke, drawingStroke));
                repaint();
                break;
            case BRUSH:
                shapes.add(new Ellipse2D.Double(e.getX(), e.getY(), drawingStroke, drawingStroke));
                repaint();
                break;
            case TEXT:
                // TODO: start text input
                break;
            case RECTANGLE, LINE, CIRCLE, OVAL:
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        switch (drawingMode) {
            case ERASE:
                shapes.add(new Ellipse2D.Double(e.getX(), e.getY(), drawingStroke, drawingStroke));
                repaint();
                break;
            case BRUSH:
                shapes.add(new Ellipse2D.Double(e.getX(), e.getY(), drawingStroke, drawingStroke));
                repaint();
                break;
            case LINE:
                if (previewShape == null ) {
                    previewShape = new Line2D.Double(e.getX(), e.getY(), e.getX(), e.getY());
                } else {
                    previewShape = new Line2D.Double(((Line2D) previewShape).getX1(), ((Line2D) previewShape).getY1(), e.getX(), e.getY());
                }
                repaint();
                break;
            case RECTANGLE:
                // TODO: allow drawing in all directions
                if (previewShape == null ) {
                    previewShape = new Rectangle(e.getX(), e.getY(), 1, 1);
                } else {
                    int mouseX = e.getX();
                    int mouseY = e.getY();
                    Rectangle currPreview = (Rectangle) previewShape;
                    previewShape = new Rectangle(currPreview.x, currPreview.y, mouseX - currPreview.x, mouseY - currPreview.y);

                }
                repaint();
                break;
            case CIRCLE:
                // TODO: allow drawing in all directions
                if (previewShape == null ) {
                    previewShape = new Ellipse2D.Double(e.getX(), e.getY(), 1, 1);
                } else {
                    Ellipse2D currPreview = (Ellipse2D) previewShape;
                    double diameter = e.getX() - currPreview.getX();
                    previewShape = new Ellipse2D.Double(currPreview.getX(), currPreview.getY(), diameter, diameter);
                }
                repaint();
                break;
            case OVAL:
                // TODO: allow drawing in all directions
                if (previewShape == null ) {
                    previewShape = new Ellipse2D.Double(e.getX(), e.getY(), 1, 1);
                } else {
                    int mouseX = e.getX();
                    int mouseY = e.getY();
                    Ellipse2D currPreview = (Ellipse2D) previewShape;
                    previewShape = new Ellipse2D.Double(currPreview.getX(), currPreview.getY(), mouseX - currPreview.getX(), mouseY - currPreview.getY());
                }
                repaint();
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
        if (previewShape != null) {
            shapes.add(previewShape);
            previewShape = null;
            this.repaint();
        }
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
