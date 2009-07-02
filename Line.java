//
//  Line.java
//  Apollonius
//
//  Created by Lukas Berns on 2009/6/5.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

package apollonius;

public class Line extends Shape {
	public Point point1;
	public Point point2;
	
	public Line(Point point1, Point point2) {
		this.point1 = point1;
		this.point2 = point2;
	}
	
	public Line(double x1, double y1, double x2, double y2) {
		this(new Point(x1, y2), new Point(x2, y2));
	}
	
	public Line(double x, double y, double angle) {
		this(new Point(x, x), angle);
	}

	public Line(double gradient, double yIntersept) {
		this(new Point(0, yIntersept), 0);
		setGradient(gradient);
	}

	public Line(Point p, double angle) {
		this(p, angle, 100);
	}

	public Line(Point p, double angle, double length) {
		this.point1 = p;
		this.point2 = new Point(p.x + (length * Math.cos(angle)), p.y + (length * Math.sin(angle)));
	}
	
	public ShapeType getShapeType() {
		return ShapeType.LINE;
	}
	
	public double getX1() {
		return this.point1.x;
	}
	
	public double getY1() {
		return this.point1.y;
	}
	
	public double getX2() {
		return this.point2.x;
	}
	
	public double getY2() {
		return this.point2.y;
	}
	
	public double getLength() {
		return this.point1.distanceToPoint(this.point2);
	}
	
	public void setLength(double length) {
		this.point2 = new Point(this.point1.x + (length * Math.cos(this.getAngle())), this.point1.y + (length * Math.sin(this.getAngle())));
	}
	
	public double getGradient() {
		return (this.point2.y - this.point1.y) / (this.point2.x - this.point1.x);
	}
	
	public void setGradient(double gradient) {
		// point1 is the origin
		this.setAngle(Math.atan(gradient));
	}

	public double getYIntersept() {
		return this.point1.y - this.getGradient() * this.point1.x;
	}

	public void setYIntersept(double yIntersept) {
		double diff = yIntersept - this.getYIntersept();
		this.point1.y += diff;
		this.point2.y += diff;
	}
	
	public double getAngle() {
		Apollonius.debug("Line.getAngle()");
		// relative to point1
		if (this.point1.x == this.point2.x) {
			if (this.point1.y < this.point2.y)
				return Math.PI / 2;
			else if (this.point1.y > this.point2.y)
				return Math.PI * 3/2;
			else {
				System.out.println("The line has no length."); // TODO: Throw error
				return 0;
			}
		}
		else {
			if (this.point1.x > this.point2.x)
				return Math.PI + Math.atan(this.getGradient());
			else if (this.point1.y > this.point2.y)
				return 2*Math.PI + Math.atan(this.getGradient());
			else
				return Math.atan(this.getGradient());
		}
	}

	public void setAngle(double angle) {
		this.point2 = new Point(this.point1.x + (this.getLength() * Math.cos(angle)), this.point1.y + (this.getLength() * Math.sin(angle)));
	}
	
	public boolean intersectsWithPoint(Point p) {
		Apollonius.debug("Line.intersectsWithPoint(Point)");
		if (this.point1.x == this.point2.x) {
			return Apollonius.round(p.x) == Apollonius.round(this.point1.x);
		}
		else {
			double y = this.getGradient() * p.x + this.getYIntersept();		// f(x) = mx + b
			return Apollonius.round(p.y) == Apollonius.round(y);		// ob f(x) = p.y
		}
	}

	public IntersectionCount intersectsWithLine(Line line) {
		if (this.point1.x  == this.point2.x && line.point1.x == line.point2.x) { // if both are a vertical line
			return this.point1.x == line.point1.x ? IntersectionCount.INFINITY : IntersectionCount.ZERO;
		}
		else if (this.point1.x == this.point2.x || line.point1.x == line.point2.x) { // if only one is a vertical line
			return IntersectionCount.ONE;
		}
		else {
			double m1 = this.getGradient();	//Steigung
			double b1 = this.getYIntersept();	// y-Achsenabschnitt

			double m2 = line.getGradient();
			double b2 = line.getGradient();

			if (Apollonius.round(m1) == Apollonius.round(m2) && Apollonius.round(b1) == Apollonius.round(b2)) { // invariant
				return IntersectionCount.INFINITY;
			}
			else if (Apollonius.round(m1) == Apollonius.round(m2)) { // parallel
				return IntersectionCount.ZERO;
			}
			else { // schneiden sich
				return IntersectionCount.ONE;
			}
		}
	}

	public Point intersectionPointWithLine(Line line) {
		Apollonius.debug("Line.intersectionPointWithLine(Line)");
		if (this.point1.x == this.point2.x) {
			
			if (line.point1.x == line.point2.x) {
				System.out.println("Lines don't intersect or are invariant");
				return new Point(0,0);
			}
			
			return new Point(this.point1.x, (line.getGradient() * this.point1.x) + line.getYIntersept());
		}
		else if (line.point1.x == line.point2.x) {		
			return new Point(line.point1.x, (this.getGradient() * line.point1.x) + this.getYIntersept());
		}
		else {
			double m1 = this.getGradient();
			double b1 = this.getYIntersept();

			double m2 = line.getGradient();
			double b2 = line.getYIntersept();

			if (Apollonius.round(m1) == Apollonius.round(m2)) { // parallel or invariant
				System.out.println("Lines don't intersect or are invariant");
				return new Point(0,0);
			}
			else {
				// m1 * x + b1 = m2 * x + b2
				double x = (b2 - b1) / (m1 - m2);
				return new Point(x, (m1 * x) + b1);
			}
		}
	}

	public IntersectionCount intersectsWithCircle(Circle circle) {
		double d;
		if (this.point1.x == this.point2.x) {
			// d = yO - yO^2 - (this.x - xO)^2 + r^2
			d = circle.center.y - Math.pow(circle.center.y, 2) - Math.pow(this.point1.x - circle.center.x, 2) + Math.pow(circle.radius, 2);
		}
		else {
			// y = m1 * x + b1
			double m1 = this.getGradient();	//Steigung
			double b1 = this.getYIntersept();	// y-Achsenabschnitt

			// (x - xO2)^2 + (y - yO2)^2 = r^2
			double r = circle.radius;
			double xO2 = circle.center.x;
			double yO2 = circle.center.y;

			// (x - xO2)^2 + (m1 * x + b1 - yO2)^2 = r^2
			// x^2 + p*x + q = 0
			double a = 1 + Math.pow(m1, 2);
			double p = (2*m1*b1 - 2*xO2 - 2*yO2*m1) / a;
			double q = (Math.pow(xO2, 2) + Math.pow(b1, 2) - 2*yO2*b1 + Math.pow(yO2, 2) - Math.pow(r, 2)) / a;

			// x1/2 = -p/2 ± √(d); d = p^2/4 - q
			d = Math.pow(p, 2)/4 - q;
		}

		if (Apollonius.round(d) > 0) { // d > 0
			return IntersectionCount.TWO;
		}
		else if (Apollonius.round(d) == 0) { // d == 0
			return IntersectionCount.ONE;
		}
		else {
			return IntersectionCount.ZERO;
		}
	}

	public Point[] intersectionPointsWithCircle(Circle circle) {
		Apollonius.debug("Line.intersectionPointsWithCircle(Circle)");
		// when seen from the center of circle onto the line,
		// [0]: left intersection point
		// [1]: right intersection point
		
		double x1, y1; // coordinates of [0]
		double x2, y2; // coordinates of [1]

		if (this.point1.x == this.point2.x) {
			// vertical line
			
			x1 = x2 = this.point1.x;
			double sqrt_d = Math.sqrt(Math.pow(circle.radius, 2) - Math.pow(x1 - circle.center.x, 2));
			double plusOrMinus = circle.center.x < x1 ? 1 : -1;
			
			y1 = circle.center.y + plusOrMinus * sqrt_d;
			y2 = circle.center.y - plusOrMinus * sqrt_d;
		}
		else {
			// y = m1 * x + b1
			double m1 = this.getGradient();
			double b1 = this.getYIntersept();

			// (x - xO2)^2 + (y - yO2)^2 = r^2
			double r = circle.radius;
			double xO2 = circle.center.x;
			double yO2 = circle.center.y;

			// (x - xO2)^2 + (m1 * x + b1 - yO2)^2 = r^2
			// x^2 + p*x + q = 0
			double a = 1 + Math.pow(m1, 2);
			double p = (2*m1*b1 - 2*xO2 - 2*yO2*m1) / a;
			double q = (Math.pow(xO2, 2) + Math.pow(b1, 2) - 2*yO2*b1 + Math.pow(yO2, 2) - Math.pow(r, 2)) / a;

			// x1/2 = -p/2 ± √(d); d = p^2/4 - q
			double sqrt_d = Math.sqrt(Math.pow(p, 2)/4 - q);
			double plusOrMinus = circle.center.x > ((m1 * circle.center.x) + b1) ? 1 : -1; // used to distinguish between left and right point
			
			x1 = - p/2 + plusOrMinus * sqrt_d;
			x2 = - p/2 - plusOrMinus * sqrt_d;

			y1 = m1*x1 + b1;
			y2 = m1*x2 + b1;
		}

		Point[] points = new Point[2];
		points[0] = new Point(x1, y1);
		points[1] = new Point(x2, y2);
		return points;
	}


	public double distanceToPoint(Point p) {
		Apollonius.debug("Line.distanceToPoint(Point)");
		Line line = this.verticalToPoint(p);
		Point intersection = this.intersectionPointWithLine(line);
		return p.distanceToPoint(intersection);
	}

	public Line verticalToPoint(Point p) {
		Apollonius.debug("Line.verticalToPoint(Point)");
		Circle c1 = new Circle(p, p.distanceToPoint(this.point1));
		
		if (this.intersectsWithCircle(c1) == IntersectionCount.ONE) {
			return new Line(p, this.point1);
		}
		else {
			Point[] c1p_arr = this.intersectionPointsWithCircle(c1);
			Point c1p1 = c1p_arr[0];
			Point c1p2 = c1p_arr[1];
			double radius = c1p1.distanceToPoint(c1p2) * 2/3;
			Circle c2 = new Circle(c1p1, radius);
			Circle c3 = new Circle(c1p2, radius);
			Point[] p_arr = c2.intersectionPointsWithCircle(c3);
			Point p1 = p_arr[0];
			Point p2 = p_arr[1];

			return new Line(p1, p2);
		}
	}

	public Line[] angleBisectorsWithLine(Line line) {
		double angle1 = (this.getAngle() + line.getAngle()) / 2;
		double angle2 = angle1 + Math.PI/2;
		Point point = this.intersectionPointWithLine(line);

		Line[] arr = new Line[2];
		arr[0] = new Line(point, angle1);
		arr[1] = new Line(point, angle2);
		return arr;
	}

	public Line[] parallelLinesWithDistance(double distance) {
		// when seen from point1 to point2
		// [0]: left line
		// [1]: right line
		
		Point point1 = (new Line(this.point1, this.getAngle() + Math.PI/2, distance)).point2;
		Point point2 = (new Line(this.point1, this.getAngle() - Math.PI/2, distance)).point2;
		Line line1 = new Line(point1, this.getAngle());
		Line line2 = new Line(point2, this.getAngle());
		Line[] arr = new Line[2];
		arr[0] = line1;
		arr[1] = line2;
		return arr;
	}


	public Shape shapeInvertedWithCircle(Circle inversionCircle) {
		Apollonius.debug("Line.shapeInvertedWithCircle(Circle)");
		Point center = inversionCircle.center;
		
		if (this.intersectsWithPoint(center)) {
			return this;
		}
		else {
			Line s = this.verticalToPoint(center);
			Point p = this.intersectionPointWithLine(s);
			Point p_ = p.shapeInvertedWithCircle(inversionCircle);

			return new Circle(center, p_);
		}
	}
	
	public Boolean pointIsOnLeftSide(Point point) {
		Apollonius.debug("Line.pointIsOnLeftSide(Point)");
		// seen from point1 to point2
		if (this.point1.x == this.point2.x) {
			// vertical line
			return ((point.x > this.point1.x) ^ (this.point1.y < this.point2.y)); // XOR
		}
		else if(this.point1.y == this.point2.y) {
			// horizontal line
			return ((point.y < this.point1.y) ^ (this.point1.x < this.point2.x)); // XOR
		}
		else {
			double m = this.getGradient();
			double c = this.getYIntersept();
			return ((m > 0) ^ (point.y < (m * point.x + c)));
		}
	}
	
	public void println() {
		System.out.println("Line: y = " + this.getGradient() + " * x + " + this.getYIntersept());
	}
}