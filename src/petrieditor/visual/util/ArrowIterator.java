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

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.NoSuchElementException;

public class ArrowIterator implements PathIterator {

    // Attributes
    private int index = 0;
    private Arrow2D arrow = null;
    private AffineTransform affine = null;
    private PathIterator itArrow = null;
    
    public ArrowIterator(Arrow2D arrow, AffineTransform affine) {
        this.arrow = arrow;
        this.affine = affine;
    }

    public int getWindingRule() {
        return WIND_NON_ZERO;
    }

    public void next() {
        this.index++;
    }
    
    public boolean isDone() {
        return (this.index > 4);
    }
    
    public int currentSegment(double[] coords) {
        if (this.isDone()) {
		    throw new NoSuchElementException("Arrow iterator out of bounds");
		}
		
		int type;
		
		if(this.index == 0){
		    this.itArrow = this.arrow.getArrow().getPathIterator(this.affine);
		    this.itArrow.currentSegment(coords);
		    this.itArrow.next();
		    type = SEG_MOVETO;
		} else if(this.index == 1){
		    this.itArrow.currentSegment(coords);
		    this.itArrow.next();
		    type = SEG_LINETO;
		} else if(this.index == 2){
		    this.itArrow.currentSegment(coords);
		    this.itArrow.next();
			type = SEG_LINETO;
		} else if(this.index == 3){
		    this.itArrow.currentSegment(coords);
		    this.itArrow.next();
			type = SEG_MOVETO;
		} else {
		    this.itArrow.currentSegment(coords);
		    this.itArrow.next();
		    this.itArrow= null;
			type = SEG_LINETO;
		}
		
		return type;
	}

    public int currentSegment(float[] coords) {
        if (this.isDone()) {
		    throw new NoSuchElementException("Arrow iterator out of bounds");
		}
		
		int type;
		
		if(this.index == 0){
		    this.itArrow = this.arrow.getArrow().getPathIterator(this.affine);
		    this.itArrow.currentSegment(coords);
		    this.itArrow.next();
		    type = SEG_MOVETO;
		} else if(this.index == 1){
		    this.itArrow.currentSegment(coords);
		    this.itArrow.next();
		    type = SEG_LINETO;
		} else if(this.index == 2){
		    this.itArrow.currentSegment(coords);
		    this.itArrow.next();
			type = SEG_LINETO;
		} else if(this.index == 3){
		    this.itArrow.currentSegment(coords);
		    this.itArrow.next();
			type = SEG_MOVETO;
		} else {
		    this.itArrow.currentSegment(coords);
		    this.itArrow.next();
		    this.itArrow= null;
			type = SEG_LINETO;
		}
		
		if (this.affine != null) {
		    this.affine.transform(coords, 0, coords, 0, 1);
		}
		
		return type;
    }

}
