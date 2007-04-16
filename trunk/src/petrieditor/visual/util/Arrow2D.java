/**
 * Copyright (C) 2004 C. Schalkwijk, Response B.V.
 *
 * info@response.nl
 * Response B.V
 * Jagersbosstraat 26
 * 5241 JT Rosmalen
 * THE NETHERLANDS
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the
 *
 * Free Software Foundation, Inc., 
 * 59 Temple Place,
 * Suite 330,
 * Boston,
 * MA 02111-1307 USA 
 *
 * Response B.V., hereby disclaims all copyright interest in the library 'DeSL'
 * (a developer support library) written by Coen Schalkwijk.
 *
 * H. Houet, Director of Response B.V.
 * 27th April 2004
 * (hard copy of the disclaimer in possession of C. Schalkwijk)
 *
 */
package petrieditor.visual.util;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Arrow2D implements Shape {

    private double x1 = 0;
    private double y1 = 0;
    private double x2 = 0;
    private double y2 = 0;
    private Shape arrow = null;

    public Arrow2D() {
    }

    public Arrow2D(Point2D begin, Point2D end) {
        this.x1 = begin.getX();
        this.y1 = begin.getY();
        this.x2 = end.getX();
        this.y2 = end.getY();
        this.initArrow();
    }

    public void setPoints(Point2D begin, Point2D end) {
        this.x1 = begin.getX();
        this.y1 = begin.getY();
        this.x2 = end.getX();
        this.y2 = end.getY();
        this.initArrow();
    }

    private double calcAngle(float x1, float y1, float x2, float y2) {
        double rad;
        float dx = x2 - x1;
        float dy = y2 - y1;

        if (dx == 0.0) {
            if (dy == 0.0) {
                rad = 0.0;
            } else if (dy > 0.0) {
                rad = Math.PI / 2.0;
            } else {
                rad = Math.PI * 3.0 / 2.0;
            }
        } else if (dy == 0.0) {
            if (dx > 0.0) {
                rad = 0.0;
            } else {
                rad = Math.PI;
            }
        } else {
            if (dx < 0.0) {
                rad = Math.atan(dy / dx) + Math.PI;
            } else if (dy < 0.0) {
                rad = Math.atan(dy / dx) + (2 * Math.PI);
            } else {
                rad = Math.atan(dy / dx);
            }
        }

        return rad;
    }

    private void initArrow() {
        int length = (int) Math.sqrt(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2), 2));

        Polygon poly = new Polygon();
        poly.addPoint((int) x1, (int) y1);
        poly.addPoint((int) x1 + length, (int) y1);
        poly.addPoint((int) x1 + length - 10, (int) y1 - 5);
        poly.addPoint((int) x1 + length, (int) y1);
        poly.addPoint((int) x1 + length - 10, (int) y1 + 5);

        double rad = this.calcAngle((float) this.x1, (float) this.y1, (float) this.x2, (float) this.y2);
        AffineTransform tx = AffineTransform.getRotateInstance(rad, x1, y1);

        this.arrow = tx.createTransformedShape(poly);
    }

    public Shape getArrow() {
        return this.arrow;
    }

    public Rectangle2D getBounds2D() {
        double x, y, w, h;

        if (x1 < x2) {
            x = x1;
            w = x2 - x1;
        } else {
            x = x2;
            w = x1 - x2;
        }
        if (y1 < y2) {
            y = y1;
            h = y2 - y1;
        } else {
            y = y2;
            h = y1 - y2;
        }
        return new Rectangle2D.Double(x, y, w, h);
    }

    public Rectangle getBounds() {
        return this.getBounds2D().getBounds();
    }

    public boolean contains(double x, double y) {
        return this.arrow.contains(x, y);
    }

    public boolean contains(double x, double y, double w, double h) {
        return this.arrow.contains(x, y, w, h);
    }

    public boolean intersects(double x, double y, double w, double h) {
        return this.arrow.intersects(x, y, w, h);
    }

    public boolean contains(Point2D p) {
        return this.arrow.contains(p);
    }

    public boolean contains(Rectangle2D r) {
        return this.arrow.contains(r);
    }

    public boolean intersects(Rectangle2D r) {
        return this.arrow.intersects(r);
    }

    public PathIterator getPathIterator(AffineTransform at) {
        return new ArrowIterator(this, at);
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return new ArrowIterator(this, at);
    }
}
