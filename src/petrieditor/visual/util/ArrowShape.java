package petrieditor.visual.util;

import java.awt.*;
import java.awt.geom.*;
import static java.lang.Math.atan;
import static java.lang.Math.hypot;

public class ArrowShape implements Shape {

    protected GeneralPath head = new GeneralPath();
    protected GeneralPath arrow = new GeneralPath();
    protected Point2D.Double pointTo = new Point2D.Double();
    protected Shape transformedArrow;

    private double angle;

    public ArrowShape() {
        head.moveTo(0, 0);
        head.lineTo(5, -10);
        head.lineTo(0, -7);
        head.lineTo(-5, -10);
        head.closePath();
    }

    public void setLocation(double xTo, double yTo, double _angle) {
        pointTo.setLocation(xTo, yTo);
        angle = _angle;
    }

    public void setLocation(Point2D.Double from, Point2D.Double to) {
        this.setLocation(from.getX(), from.getY(), to.getX(), to.getY());
    }

    public void setLocation(Point from, Point to) {
        this.setLocation(from.getX(), from.getY(), to.getX(), to.getY());
    }

    public void setLocation(double xFrom, double yFrom, double xTo, double yTo) {
        pointTo.setLocation(xTo, yTo);
        arrow.reset();
        arrow.append(head, false);
        arrow.lineTo(0f, (float) -hypot((xFrom - xTo), (yFrom - yTo)));

        if (yFrom <= yTo)
            angle = atan((xFrom - xTo) / (yTo - yFrom));
        else
            angle = atan((xFrom - xTo) / (yTo - yFrom)) + Math.PI;

        if ((xFrom == xTo) && (yFrom == yTo))
            angle = 0;

        AffineTransform transform = new AffineTransform();
        transform.translate(pointTo.getX(), pointTo.getY());
        transform.rotate(angle);
        transformedArrow = arrow.createTransformedShape(transform);
    }

    public double getAngle() {
        return angle;
    }

    public Rectangle getBounds() {
        return transformedArrow.getBounds();
    }

    public Rectangle2D getBounds2D() {
        return transformedArrow.getBounds2D();
    }

    public boolean contains(double x, double y) {
        return transformedArrow.contains(x, y);
    }

    public boolean contains(Point2D p) {
        return transformedArrow.contains(p);
    }

    public boolean intersects(double x, double y, double w, double h) {
        return transformedArrow.intersects(x, y, w, h);
    }

    public boolean intersects(Rectangle2D r) {
        return transformedArrow.intersects(r);
    }

    public boolean contains(double x, double y, double w, double h) {
        return transformedArrow.contains(x, y, w, h);
    }

    public boolean contains(Rectangle2D r) {
        return transformedArrow.contains(r);
    }

    public PathIterator getPathIterator(AffineTransform at) {
        return transformedArrow.getPathIterator(at);
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return transformedArrow.getPathIterator(at, flatness);
    }
}