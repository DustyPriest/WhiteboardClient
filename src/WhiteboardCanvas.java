import shapes.*;

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
    private IRemoteWhiteboard remoteWhiteboardState;

    public WhiteboardCanvas(IRemoteWhiteboard remoteWhiteboardState) {
        super();
        this.remoteWhiteboardState = remoteWhiteboardState;
        this.setBackground(Color.WHITE);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        try {
            shapes = remoteWhiteboardState.getShapes();
        } catch (Exception e) {
            System.err.println("failed to get shapes from server");
            e.printStackTrace();
        }
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
        // TODO: put colour on shapes themselves && make rectangle etc take two points, so can draw in all directions
        if (drawingMode == DrawingMode.ERASE) {
            setDrawingColor(canvasColor);
        } else if (this.drawingMode == DrawingMode.ERASE) {
            setDrawingColor(Color.BLACK);
        }
        this.drawingMode = drawingMode;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        try {
            switch (drawingMode) {
                case ERASE:
                    remoteWhiteboardState.addShape(new Ellipse2D.Double(e.getX(), e.getY(), drawingStroke, drawingStroke));
                    repaint();
                    break;
                case BRUSH:
                    remoteWhiteboardState.addShape(new Ellipse2D.Double(e.getX(), e.getY(), drawingStroke, drawingStroke));
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
        } catch (Exception exc) {
            System.err.println("failed to add shape to server");
            exc.printStackTrace();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        try {
            switch (drawingMode) {
                case ERASE:
                    remoteWhiteboardState.addShape(new CustomEllipse(e.getX(), e.getY(), drawingStroke, drawingStroke, canvasColor, drawingStroke));
                    repaint();
                    break;
                case BRUSH:
                    remoteWhiteboardState.addShape(new CustomEllipse(e.getX(), e.getY(), drawingStroke, drawingStroke, drawingColor, drawingStroke));
                    repaint();
                    break;
                case LINE:
                    if (previewShape == null) {
                        previewShape = new CustomLine(e.getX(), e.getY(), e.getX(), e.getY());
                    } else {
                        CustomLine currPreview = (CustomLine) previewShape;
                        currPreview.updateBounds(e.getX(), e.getY());
                    }
                    repaint();
                    break;
                case RECTANGLE:
                    if (previewShape == null) {
                        previewShape = new CustomRectangle(e.getX(), e.getY(), 1, 1);
                    } else {
                        CustomRectangle currPreview = (CustomRectangle) previewShape;
                        currPreview.updateBounds(e.getX(), e.getY());
                    }
                    repaint();
                    break;
                case CIRCLE:
                    if (previewShape == null) {
                        previewShape = new CustomEllipse(e.getX(), e.getY(), 1, 1);
                    } else {
                        CustomEllipse currPreview = (CustomEllipse) previewShape;
                        double diameter = e.getX() - currPreview.getX();
                        currPreview.updateBounds(currPreview.getX() + diameter, currPreview.getY() + diameter);
                    }
                    repaint();
                    break;
                case OVAL:
                    if (previewShape == null) {
                        previewShape = new CustomEllipse(e.getX(), e.getY(), 1, 1);
                    } else {
                        CustomEllipse currPreview = (CustomEllipse) previewShape;
                        currPreview.updateBounds(e.getX(), e.getY());
                    }
                    repaint();
                    break;
                case TEXT:
                    break;
                default:
                    break;
            }
        } catch (Exception exc) {
            System.err.println("failed to add shape to server");
            exc.printStackTrace();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (previewShape != null) {
            try {
                remoteWhiteboardState.addShape(previewShape);
            } catch (Exception exc) {
                System.err.println("failed to add shape to server");
                exc.printStackTrace();
            }
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
