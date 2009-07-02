//
//  CanvasPanel.java
//  CanvasPanel
//
//  Created by Lukas Berns on 2009/6/29.
//  Copyright (c) 2009 __MyCompanyName__. All rights reserved.
//

package apollonius;

import javax.swing.*;        
import java.awt.*;

class CanvasPanel extends JPanel {
	public double scale = 6.0;
	public Shape[] blackShapes;
	public Shape[] redShapes;
	
	public static final int pointRadius = 2;
	
	public CanvasPanel(Shape[] black, Shape[] red) {
		blackShapes = black;
		redShapes = red;
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(640,480);
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);       
		
		Graphics2D g = (Graphics2D) graphics;
		
		g.translate(0, 480);
		g.scale(1, -1); // flip vertically - by default greater y means going down
		
		paintShapes(g, blackShapes);
		
		for (Shape s : redShapes) {
			if (s != null) {
				s.color = Color.RED;
			}
		}
		paintShapes(g, redShapes);
	}
	
	public void paintShapes(Graphics2D g, Shape[] shapes) {
		for (Shape s : shapes) {
			if (s == null) {
				continue;
			}
			g.setColor(s.color);
			ShapeType type = s.getShapeType();
			if (type == ShapeType.POINT) {
				paintPoint(g, (Point) s);
			}
			else if (type == ShapeType.LINE) {
				paintLine(g, (Line) s);
			}
			else if(type == ShapeType.CIRCLE) {
				paintCircle(g, (Circle) s);
			}
		}
	}
	
	public void paintPoint(Graphics2D g, Point p) {
		int x = (int) Math.round(scale * p.x) - pointRadius;
		int y = (int) Math.round(scale * p.y) - pointRadius;
		int pointDiameter = 2 * pointRadius;
		g.fillOval(x, y, pointDiameter, pointDiameter);
	}
	
	public void paintLine(Graphics2D g, Line l) {
		Rectangle bounds = g.getClipBounds();
		
		if (Apollonius.round(l.point1.x) == Apollonius.round(l.point2.x)) {
			int x = (int) Math.round(l.point1.x * scale);
			g.drawLine(x, 0, x, bounds.height);
		}
		else {
			double yI = l.getYIntersept() * scale;
			double grad = l.getGradient();
			
			int x1, y1, x2, y2;
			if (yI >= 0) {
				x1 = 0;
				y1 = (int) Math.round(yI);
			}
			else {
				x1 = (int) Math.round(- yI / grad);
				y1 = 0;
			}
			
			y2 = (int) Math.round((grad * (double) bounds.width) + yI);
			if (y2 > bounds.height) {
				x2 = (int) Math.round(((double) bounds.height - yI) / grad);
				y2 = bounds.height;
			}
			else {
				x2 = bounds.width;
			}
			
			g.drawLine(x1, y1, x2, y2);
		}
	}
	
	public void paintCircle(Graphics2D g, Circle c) {
		int x = (int) Math.round(scale * (c.center.x - c.radius));
		int y = (int) Math.round(scale * (c.center.y - c.radius));
		int diameter = (int) Math.round(scale * 2 * c.radius);
		g.drawOval(x, y, diameter, diameter);
	}
}