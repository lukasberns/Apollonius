//
//  Point.java
//  Apollonius
//
//  Created by Lukas Berns on 2009/5/28.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

package apollonius;

public class Point extends Shape {
	public double x;
	public double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public ShapeType getShapeType() {
		return ShapeType.POINT;
	}
	
	public double distanceToPoint(Point otherPoint) {
		return Math.sqrt(Math.pow((this.x - otherPoint.x), 2) + Math.pow((this.y - otherPoint.y), 2));
	}
	
	public boolean equals(Point p) {
		return Apollonius.eq(this.x, p.x) && Apollonius.eq(this.y, p.y);
	}
	
	public Point pointBetweenPoint(Point p) {
		return new Point((this.x + p.x)/2, (this.y + p.y)/2);
	}

	public Point shapeInvertedWithCircle(Circle circle) {
		double r = circle.radius;
		Point center = circle.center;
		double d = this.distanceToPoint(center);
		
		// d_ = r^2 / d
		double d_ = Math.pow(r, 2) / d;
		
		double x = center.x + (this.x - center.x) * d_ / d;
		double y = center.y + (this.y - center.y) * d_ / d;
		
		return new Point(x, y);
	}
	
	public void println() {
		System.out.println("Point: x=" + this.x + ", y=" + this.y);
	}
}
