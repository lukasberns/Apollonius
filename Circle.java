//
//  Circle.java
//  Apollonius
//
//  Created by Lukas Berns on 2009/5/28.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

public class Circle extends Shape {
	public Point center;
	public double radius;
	
	public Circle(Point center, double radius) {
		this.center = center;
		this.radius = radius;
	}
	
	public Circle(double x, double y, double radius) {
		this(new Point(x, y), radius);
	}
	
	public Circle(Point p1, Point p2) {
		this(p1.pointBetweenPoint(p2), p1.distanceToPoint(p2)/2);
	}
	
	public Circle(Point p, double angle, double radius) {
		this.radius = radius;
		this.setPointAndAngle(p, angle);
	}
	
	public ShapeType getShapeType() {
		return ShapeType.CIRCLE;
	}
	
	public double getX() {
		return this.center.x;
	}
	
	public double getY() {
		return this.center.y;
	}
	
	public void setPointAndAngle(Point p, double angle) {
		this.center = new Point(p.x + this.radius * Math.cos(angle), p.y + this.radius * Math.sin(angle));
	}
	
	public Shape shapeInvertedWithCircle(Circle inversionCircle) {
		Apollonius.debug("Circle.shapeInvertedWithCircle(Circle)");
		if (this.getX() == inversionCircle.getX() && this.getY() == inversionCircle.getY()) {
			// create Point on circle and invert it with the circle
			Point radiusHelperPoint = new Point(this.getX() + this.radius, this.getY());
			return new Circle(this.center, this.center.x - radiusHelperPoint.shapeInvertedWithCircle(inversionCircle).x);
		}
		else if (this.intersectsWithPoint(inversionCircle.center)) {
			Line s = new Line(this.center, inversionCircle.center);
			Point[] p_arr = s.intersectionPointsWithCircle(this);
			Point p = p_arr[0];

			if (p.isSameAs(inversionCircle.center)) {
				p = p_arr[1];
			}

			Point p_ = p.shapeInvertedWithCircle(inversionCircle);

			return new Line(p_, s.getAngle() + Math.PI/2);
		}
		else {
			Line l = new Line(this.center, inversionCircle.center);
			Point[] p_arr = l.intersectionPointsWithCircle(this);
			Point p1 = p_arr[0];
			Point p2 = p_arr[1];
			Point p1_ = p1.shapeInvertedWithCircle(inversionCircle);
			Point p2_ = p2.shapeInvertedWithCircle(inversionCircle);
			return new Circle(p1_, p2_);
		}
	}
	
	public boolean intersectsWithPoint(Point point) {
		return Apollonius.round(this.radius) == Apollonius.round(this.center.distanceToPoint(point));
	}
	
	public IntersectionCount intersectsWithLine(Line line) {
		return line.intersectsWithCircle(this);
	}
	
	public Point[] intersectionPointsWithLine(Line line) {
		return line.intersectionPointsWithCircle(this);
	}
	
	public IntersectionCount intersectsWithCircle(Circle circle) {
		// (x - xO1)^2 + (y - yO1)^2 = r1^2
		double r1 = this.radius;
		double xO1 = this.center.x;
		double yO1 = this.center.y;
		
		// (x - xO2)^2 + (y - yO2)^2 = r2^2
		double r2 = circle.radius;
		double xO2 = circle.center.x;
		double yO2 = circle.center.y;
		
		double d;
		
		if ((xO1 == xO2) && (yO1 == yO2)) {
			// equal center points
			if (r1 == r2)
				return IntersectionCount.INFINITY;
			else
				return IntersectionCount.ZERO;
		}
		else if ((yO1 == yO2) && (xO1 != xO2)) {
			// Both circle's center points have same y
			double x1 = (Math.pow(r1, 2) - Math.pow(r2, 2)) / (2*xO2 - 2*xO1) + (xO1 + xO2) / 2;
			d = Math.pow(r1, 2) - Math.pow(x1, 2) + 2*x1*xO1 - Math.pow(xO1, 2);
		}
		else if ((xO2 == xO1) && (yO2 != yO1)) {
			// Both circle's center points on same x
			double y1 = (Math.pow(r1, 2) - Math.pow(r2, 2)) / (2*yO2 - 2*yO1) + (yO1 + yO2) / 2;
			d = Math.pow(r1, 2) - Math.pow(y1, 2) + 2*y1*yO1 - Math.pow(yO1, 2);
		}
		else {
			// All other cases
			// (x - xO1)^2 + (y - yO1)^2 - r^2 = 0;
			// y = l - x*m
			double m = (xO2 - xO1) / (yO2 - yO1);
			double l = (Math.pow(r1, 2) - Math.pow(r2, 2) + Math.pow(xO2, 2) - Math.pow(xO1, 2) + Math.pow(yO2, 2) - Math.pow(yO1, 2)) / (2*(yO2 - yO1));
			
			// x^2 + (~~~/a)x + ~~~/a = 0
			// x^2 + pa + q = 0
			double a = 1 + Math.pow(m, 2);
			double p = (-2*xO1 - 2*l*m + 2*yO1*m) / a;
			double q = (Math.pow(xO1, 2) + Math.pow(l, 2) - 2*yO1*l + Math.pow(yO1, 2) - Math.pow(r1, 2)) / a;
			
			// x1/2 = -p/2 ± √(d)
			d = Math.pow(p, 2) / 4 - q;
		}
		
		if (Apollonius.round(d) > 0) {
			return IntersectionCount.TWO;
		}
		else if (Apollonius.round(d) == 0) {
			return IntersectionCount.ONE;
		}
		else {
			return IntersectionCount.ZERO;
		}
	}
	
	public Point[] intersectionPointsWithCircle(Circle circle) {
		Apollonius.debug("Circle.intersectionPointsWithCircle(Circle)");
		// when seen from this.center to circle.center:
		// [0]: left intersection point
		// [1]: right intersection point
		
		// (x - xO1)^2 + (y - yO1)^2 = r1^2
		double r1 = this.radius;
		double xO1 = this.center.x;
		double yO1 = this.center.y;
		
		// (x - xO2)^2 + (y - yO2)^2 = r2^2
		double r2 = circle.radius;
		double xO2 = circle.center.x;
		double yO2 = circle.center.y;
		
		double x1, y1; // coordinates of left intersection point (see above)
		double x2, y2; // coordinates of right intersection point
		
		if ((yO1 == yO2) && (xO1 != xO2)) {
			// Both circle's center points have same y
			x1 = x2 = (Math.pow(r1, 2) - Math.pow(r2, 2)) / (2*xO2 - 2*xO1) + (xO1 + xO2) / 2;
			double offset = Math.sqrt(Math.pow(r1, 2) - Math.pow(x1, 2) + 2*x1*xO1 - Math.pow(xO1, 2));
			
			double plusOrMinus = this.center.x < circle.center.x ? 1 : -1; // used to distinguish between left and right point
			
			y1 = yO1 + plusOrMinus * offset;
			y2 = yO1 - plusOrMinus * offset;
		}
		else if (xO1 == xO2 && yO1 != yO2) {
			// Both circle's center points have same x
			y1 = y2 = (Math.pow(r1, 2) - Math.pow(r2, 2)) / (2*yO2 - 2*yO1) + (yO1 + yO2) / 2;
			double offset = Math.sqrt(Math.pow(r1, 2) - Math.pow(y1, 2) + 2*y1*yO1 - Math.pow(yO1, 2));
			
			double plusOrMinus = this.center.y < circle.center.y ? 1 : -1; // used to distinguish between left and right point
			
			x1 = xO1 - plusOrMinus * offset;
			x2 = xO1 + plusOrMinus * offset;
		}
		else {
			// all other cases
			
			// (x - xO1)^2 + (y - yO1)^2 - r1^2 = 0;
			// (x - xO2)^2 + (y - yO2)^2 - r2^2 = 0;
			// x^2 - 2*x*xO1 + xO1^2 + y^2 - 2*y*yO1 + yO1^2 - r1^2 = x^2 - 2*x*xO2 + xO2^2 + y^2 - 2*y*yO2 + yO2^2 - r2^2;
			// 2*y*(yO2 - yO1) = r1^2 - r2^2 + xO2^2 - xO1^2 + yO2^2 - yO1^2 - 2*x*(xO2 - xO1)
			// y = (r2^2 - r1^2 + xO2^2 - xO1^2 + yO2^2 - yO1^2) / (2*(yO2 - yO1)) - x*(xO2 - xO1)/(yO2 - yO1)
			
			//   |
			//   V
			
			// y = l - x*m
			// m = (xO2 - xO1)/(yO2 - yO1)
			// l = (r1^2 - r2^2 + xO2^2 - xO1^2 + yO2^2 - yO1^2) / (2*(yO2 - yO1))
			
			double m = (xO2 - xO1) / (yO2 - yO1);
			double l = (Math.pow(r1, 2) - Math.pow(r2, 2) + Math.pow(xO2, 2) - Math.pow(xO1, 2) + Math.pow(yO2, 2) - Math.pow(yO1, 2)) / (2*(yO2 - yO1));
			
			// (x - xO1)^2 + (y - yO1)^2 - r1^2 = 0     | y = l - x*m
			// (x - xO1)^2 + ((l - x*m) - yO1)^2 - r1^2 = 0
			// x^2 - 2*xO1*x + xO1^2 + (l - x*m)^2 - 2*(l - x*m)*yO1 + yO1^2 - r1^2 = 0
			// x^2 - 2*xO1*x + xO1^2 + l^2 - 2*l*m*x + (x*m)^2 - 2*yO1*l + 2*yO1*m*x + yO1^2 - r1^2 = 0
			// (1 + m^2)*x^2 + (-2*xO1 - 2*l*m + 2*yO1*m)*x + (xO1^2 + l^2 - 2*yO1*l +yO1^2 - r1^2) = 0
			
			// x^2 + (~~~/a)x + ~~~/a = 0
			// x^2 + px + q = 0
			double a = 1 + Math.pow(m, 2);
			double p = (-2*xO1 - 2*l*m + 2*yO1*m) / a;
			double q = (Math.pow(xO1, 2) + Math.pow(l, 2) - 2*yO1*l + Math.pow(yO1, 2) - Math.pow(r1, 2)) / a;
			
			// x1/2 = -p/2 ± √(d)
			double sqrt_d = Math.sqrt(Math.pow(p, 2) / 4 - q);
			
			double plusOrMinus = this.center.y < circle.center.y ? 1 : -1; // used to distinguish between left and right point
			
			x1 = -p / 2 - plusOrMinus * sqrt_d;
			x2 = -p / 2 + plusOrMinus * sqrt_d;
			
			y1 = l - x1*m;
			y2 = l - x2*m;
		}
		
		Point[] intersectionPoints = new Point[2];
		intersectionPoints[0] = new Point(x1, y1);
		intersectionPoints[1] = new Point(x2, y2);
		return intersectionPoints;
	}
	
	public Boolean touchesCircleInternally(Circle circle) {
		Apollonius.debug("Circle.touchesCircleInternally(Circle)");
		return this.pointIsEnclosed(circle.center) || circle.pointIsEnclosed(this.center);
		// return Math.pow(circle.center.x - this.center.x, 2) + Math.pow(circle.center.y - this.center.y, 2) < Math.pow(this.radius, 2)
		//	|| Math.pow(this.center.x - circle.center.x, 2) + Math.pow(this.center.y - circle.center.y, 2) < Math.pow(circle.radius, 2);
	}
	
	public Boolean intersectsInOnePointWithCircles(Circle circle1, Circle circle2) {
		Point[] p1_arr = this.intersectionPointsWithCircle(circle1);
		Point[] p2_arr = this.intersectionPointsWithCircle(circle2);
		
		Point p1_1, p1_2, p2_1, p2_2;
		p1_1 = p1_arr[0];
		p1_2 = p1_arr[1];
		p2_1 = p2_arr[0];
		p2_2 = p2_arr[1];
		
		return p1_1.equals(p2_1) || p1_1.equals(p2_2) || p1_2.equals(p2_1) || p1_2.equals(p2_2);
	}
	
	
	public Point intersectionPointWithCircles(Circle circle1, Circle circle2) {
		Apollonius.debug("Circle.intersectionPointWithCircles(Circle, Circle)");
		Point[] p1_arr = this.intersectionPointsWithCircle(circle1);
		Point[] p2_arr = this.intersectionPointsWithCircle(circle2);
		
		Point p1_1, p1_2, p2_1, p2_2;
		p1_1 = p1_arr[0];
		p1_2 = p1_arr[1];
		p2_1 = p2_arr[0];
		p2_2 = p2_arr[1];
		
		if (p1_1.equals(p2_1) || p1_1.equals(p2_2))
			return p1_1;
		else
			return p1_2;
	}
	
	public boolean pointIsEnclosed(Point p) {
		Apollonius.debug("Circle.pointIsEnclosed(Point)");
		return Apollonius.round(p.distanceToPoint(this.center))
				< Apollonius.round(this.radius);
	}
	
	public Line[] tangentsThroughPoint(Point p) {
		Apollonius.debug("Circle.tangentsThroughPoint(Point)");
		// when seen from point p, the result tangents touch the...
		// [0]: ...left of the circle
		// [1]: ...right of the circle
		// point1 is the point p
		
		if (pointIsEnclosed(p)) { // if point is in the circle
			Apollonius.debug("The point you are trying to create tangents to is enclosed in the circle.");
			return new Line[0];
		}
		else if (this.intersectsWithPoint(p)) {
			// if the point is on the circle
			Line s = new Line(p, this.center);
			Line[] lines = new Line[2];
			lines[0] = lines[1] = new Line((Point) p.clone(), s.getAngle() + Math.PI/2);
			return lines;
		}
		else {
			Circle circle = new Circle(p, this.center);
			Point[] touchingPoints = circle.intersectionPointsWithCircle(this);
			Line[] lines = new Line[2];
			lines[0] = new Line((Point) p.clone(), touchingPoints[0]);
			lines[1] = new Line((Point) p.clone(), touchingPoints[1]);
			return lines;
		}
	}
	
	
	public Point invertedPointWithPoint(Point p) {
		return p.shapeInvertedWithCircle(this);
	}
	
	public void println() {
		System.out.println("Circle: x=" + this.center.x + ", y=" + this.center.y + ", r=" + this.radius);
	}
}


