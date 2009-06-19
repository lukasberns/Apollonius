//
//  Apollonius.java
//  Apollonius
//
//  Created by Lukas Berns on 2009/5/28.
//  Copyright (c) 2009 __MyCompanyName__. All rights reserved.
//
import java.util.*;

public class Apollonius {
	public static int precision = 100000;
	public static int inversionCircleSize = 100; // use a number greater than those you use for input

    public static void main (String args[]) {
		// insert code here...
		Circle[] circles = new Circle[3];
		circles[0] = new Circle(50, 30, 12);
		circles[1] = new Circle(25, 30, 9);
		circles[2] = new Circle(35, 15, 8);
		
		Shape[] shapes = solutionForShapes(circles[0], circles[1], circles[2]); //solutionForShapes(circles);
		Circle[] solCircles = getCircles(shapes);
		for (Circle circle : solCircles) {
			System.out.println("x: " + circle.center.x + ", y:" + circle.center.y + ", r: " + circle.radius);
		}
		Line[] solLines = getLines(shapes);
		for (Line line : solLines) {
			System.out.println("m: " + line.getGradient() + ", c:" + line.getYIntersept());
		}
	}
	
	public static double round(double input) {
		return Math.round(input * precision) / precision;
	}
	
	public static Shape[] solutionForShapes(Shape[] shapes) {	
		if (shapes.length != 3) {
			System.out.println("Apollonius.solutionForShapes(): Array must contain 3 elements."); // TODO: Throw an error
			return new Shape[0];
		}
		
		int numberOfPoints = numberOfPoints(shapes);
		int numberOfLines = numberOfLines(shapes);
		int numberOfCircles = numberOfCircles(shapes);
		
		if (numberOfCircles == 3) {
			// 3 circles
			Circle[] circles = getCircles(shapes);
			return solutionForShapes(circles[0], circles[1], circles[2]);
		}
	/*	else if (numberOfLines == 3) {
			// 3 lines

			NSUInteger iCount = 0;
			IVSMutableArray *angleBisectors1, *angleBisectors2, *angleBisectors3;
			if ([shapes[0] intersectsWithLine:shapes[1]] == IVSDoesIntersectLine) {
				iCount+=1;
			}
			if ([shapes[0] intersectsWithLine:shapes[2]] == IVSDoesIntersectLine) {
				iCount+=2;
			}
			if ([shapes[1] intersectsWithLine:shapes[2]] == IVSDoesIntersectLine) {
				iCount+=4;
			}

			if (iCount == 3 || iCount == 5 || iCount == 7) {
				angleBisectors1 = [shapes[0] angleBisectorsWithLine:shapes[1]];
				angleBisectors1.paintConstructionsIfWanted;
			}
			if (iCount == 3 || iCount == 6 || iCount == 7) {
				angleBisectors2 = [shapes[0] angleBisectorsWithLine:shapes[2]];
				angleBisectors2.paintConstructionsIfWanted;
			}
			if (iCount == 5 || iCount == 6) {
				angleBisectors3 = [shapes[1] angleBisectorsWithLine:shapes[2]];
				angleBisectors3.paintConstructionsIfWanted;
			}

			/* iCount info:
				0: all lines parallel
				3: 1 and 2 are parallel
				5: 0 and 2 are parallel
				6: 0 and 1 are parallel
				7: none of the lines are parallel
			*//*


			NSMutableArray* centers = NSMutableArray.array;
			if (iCount == 3 || iCount == 7) {
				if ([angleBisectors1[0] intersectsWithLine:angleBisectors2[0]] == IVSDoesIntersectLine)
					[centers addShape:[angleBisectors1[0] intersectionPointWithLine:angleBisectors2[0]]];
				if ([angleBisectors1[0] intersectsWithLine:angleBisectors2[1]] == IVSDoesIntersectLine)
					[centers addShape:[angleBisectors1[0] intersectionPointWithLine:angleBisectors2[1]]];
				if ([angleBisectors1[1] intersectsWithLine:angleBisectors2[0]] == IVSDoesIntersectLine)
					[centers addShape:[angleBisectors1[1] intersectionPointWithLine:angleBisectors2[0]]];
				if ([angleBisectors1[1] intersectsWithLine:angleBisectors2[1]] == IVSDoesIntersectLine)
					[centers addShape:[angleBisectors1[1] intersectionPointWithLine:angleBisectors2[1]]];
			}
			else if (iCount == 5) {
				if ([angleBisectors1[0] intersectsWithLine:angleBisectors3[0]] == IVSDoesIntersectLine)
					[centers addShape:[angleBisectors1[0] intersectionPointWithLine:angleBisectors3[0]]];
				if ([angleBisectors1[0] intersectsWithLine:angleBisectors3[1]] == IVSDoesIntersectLine)
					[centers addShape:[angleBisectors1[0] intersectionPointWithLine:angleBisectors3[1]]];
				if ([angleBisectors1[1] intersectsWithLine:angleBisectors3[0]] == IVSDoesIntersectLine)
					[centers addShape:[angleBisectors1[1] intersectionPointWithLine:angleBisectors3[0]]];
				if ([angleBisectors1[1] intersectsWithLine:angleBisectors3[1]] == IVSDoesIntersectLine)
					[centers addShape:[angleBisectors1[1] intersectionPointWithLine:angleBisectors3[1]]];
			}
			else if (iCount == 6) {
				if ([angleBisectors2[0] intersectsWithLine:angleBisectors3[0]] == IVSDoesIntersectLine)
					[centers addShape:[angleBisectors2[0] intersectionPointWithLine:angleBisectors3[0]]];
				if ([angleBisectors2[0] intersectsWithLine:angleBisectors3[1]] == IVSDoesIntersectLine)
					[centers addShape:[angleBisectors2[0] intersectionPointWithLine:angleBisectors3[1]]];
				if ([angleBisectors2[1] intersectsWithLine:angleBisectors3[0]] == IVSDoesIntersectLine)
					[centers addShape:[angleBisectors2[1] intersectionPointWithLine:angleBisectors3[0]]];
				if ([angleBisectors2[1] intersectsWithLine:angleBisectors3[1]] == IVSDoesIntersectLine)
					[centers addShape:[angleBisectors2[1] intersectionPointWithLine:angleBisectors3[1]]];
			}

			IVSMutableArray* circlesTouching = IVSMutableArray.array;
			double radius;
			Circle c;
			for (Point cp in centers) {
				radius = [shapes[0] distanceToPoint:cp];
				c = Circle.circleWithPointradius(cp, radius);
				circlesTouching.addShape(c);
			}
			return circlesTouching;

		}
		else if (this.numberOfPoints == 3) {
			Circle inversionCircle = [Circle circleWithPoint:this[0] radius:Apollonius.inversionCircleSize];
			if (IVSShowConstructions) inversionCircle.paintInversionCircleStyle;
			IVSMutableArray* constructionsArray = IVSMutableArray.array;

			Point point1_ = [this[1] shapeInvertedWithCircle:inversionCircle];
			Point point2_ = [this[2] shapeInvertedWithCircle:inversionCircle];
			Line line = Line.lineWithPoint1point2(point1_, point2_);

			constructionsArray.addShape(point1_);
			constructionsArray.addShape(point2_);
			constructionsArray.addShape(line);
			constructionsArray.paintConstructionsIfWanted;

			IVSMutableArray* rArr = IVSMutableArray.array;
			rArr.addShape(line);
			return rArr.arrayInvertedWithCircle(inversionCircle);
		}*/
		else if (numberOfCircles == 2 && numberOfPoints == 1) {
			// 2 circles, 1 point
			Point point = getPoints(shapes)[0];
			Circle[] circles = getCircles(shapes);
			
			return solutionForShapes(point, circles[0], circles[1]);
		}/*
		else if (numberOfCircles == 1 && numberOfPoints == 2) {
			// 1 circles, 2 points
			Pointpoint1, *point2; Circlecircle, *inversionCircle;
			point1 = this.getPoints.shapeAtIndex(0);
			point2 = this.getPoints.shapeAtIndex(1);
			circle = this.getCircles.shapeAtIndex(0);
			if (circle.intersectsWithPoint(point1)) {
				Point p = point1;
				point1 = point2;
				point2 = p;
				if (circle.intersectsWithPoint(point1))
					return IVSMutableArray.array;
			}

			inversionCircle = Circle.circleWithPointradius(point1, Apollonius.inversionCircleSize);
			if (IVSShowConstructions) inversionCircle.paintInversionCircleStyle;
			IVSMutableArray* constructionsArray = IVSMutableArray.array;

			Circle circle_ = circle.shapeInvertedWithCircle(inversionCircle);
			Point point2_ = point2.shapeInvertedWithCircle(inversionCircle);
			constructionsArray.addShape(circle_);
			constructionsArray.addShape(point2_);

			IVSMutableArray* tArr = circle_.tangentsToPoint(point2_);
			constructionsArray.addShapesFromArray(tArr);

			constructionsArray.paintConstructionsIfWanted;
			return tArr.arrayInvertedWithCircle(inversionCircle);
		}/*
		else if (numberOfLines == 2 && numberOfPoints == 1) {
			Circle inversionCircle = [Circle circleWithPoint:this.getPoints.shapeAtIndex(0) radius:Apollonius.inversionCircleSize];
			if (IVSShowConstructions) inversionCircle.paintInversionCircleStyle;
			IVSMutableArray* constructionsArray = IVSMutableArray.array;

			IVSMutableArray* iArr = this.getLines.arrayInvertedWithCircle(inversionCircle);
			constructionsArray.addShapesFromArray(iArr);
			IVSMutableArray* tArr = iArr.arrayOfCommonTangents;
			constructionsArray.addShapesFromArray(tArr);
			IVSMutableArray* rArr_ = tArr.arrayInvertedWithCircle(inversionCircle);
			IVSMutableArray* rArr = IVSMutableArray.array;
			for (id obj in rArr_._array) {
				if (obj.getShapeType() == CircleShape)
					rArr.addShape(obj);
			}
			constructionsArray.paintConstructionsIfWanted;
			return rArr;
		}
		else if (numberOfLines == 1 && numberOfPoints == 2) {
			IVSMutableArray* pointsArr = this.getPoints;
			Point center = pointsArr[0];
			Point point = pointsArr[1];

			Line line = this.getLines.shapeAtIndex(0);
			if (line.intersectsWithPoint(center)) {
				center = point;
				point = pointsArr[0];
				if (line.intersectsWithPoint(center))
					return IVSMutableArray.array;
			}

			Circle inversionCircle = Circle.circleWithPointradius(center, Apollonius.inversionCircleSize);
			if (IVSShowConstructions) inversionCircle.paintInversionCircleStyle;
			IVSMutableArray* constructionsArray = IVSMutableArray.array;

			Circle line_ = line.shapeInvertedWithCircle(inversionCircle);
			Point point_ = point.shapeInvertedWithCircle(inversionCircle);
			IVSMutableArray* tArr = line_.tangentsToPoint(point_);

			constructionsArray.addShape(line_);
			constructionsArray.addShape(point_);
			constructionsArray.addShapesFromArray(tArr);

			IVSMutableArray* rArr_ = tArr.arrayInvertedWithCircle(inversionCircle);
			IVSMutableArray* rArr = IVSMutableArray.array;
			for (id obj in rArr_._array) {
				if (obj.getShapeType() == CircleShape)
					rArr.addShape(obj);
			}
			constructionsArray.paintConstructionsIfWanted;
			return rArr;
		}
		else if (numberOfCircles == 2 && numberOfLines == 1) {
			Circle[] cArr = this.getCircles;
			Circle oldCircle = cArr[0];
			Circle circle = cArr[1];
			Line line = this.getLines.shapeAtIndex(0);

			if (oldCircle.radius > circle.radius) {
				Circle cTemp = oldCircle;
				oldCircle = circle;
				circle = cTemp;
			}

			IVSMutableArray* constructionsArray = IVSMutableArray.array;

			IVSMutableArray* arr1 = IVSMutableArray.array;
			IVSMutableArray* arr2 = IVSMutableArray.array;
			IVSMutableArray* arr3 = IVSMutableArray.array;
			IVSMutableArray* arr4 = IVSMutableArray.array;

			arr1.addShape(oldCircle.center);
			arr2.addShape(oldCircle.center);
			arr3.addShape(oldCircle.center);
			arr4.addShape(oldCircle.center);

			Circle circle_1 = circle.clone();
			circle_1.radius += oldCircle.radius;
			arr1.addShape(circle_1);
			arr3.addShape(circle_1);
			constructionsArray.addShape(circle_1);

			if (oldCircle.radius == circle.radius) {
				arr2.addShape(circle.center);
				arr4.addShape(circle.center);
				constructionsArray.addShape(circle.center);
			}
			else {
				Circle circle_2 = circle.clone();
				circle_2.radius -= oldCircle.radius;
				arr2.addShape(circle_2);
				arr4.addShape(circle_2);
				constructionsArray.addShape(circle_2);
			}

			Line vertical_ = line.verticalToPoint(oldCircle.center);
			Line vertical = [Line lineWithPoint1:oldCircle.center point2:vertical_.intersectionPointWithLine(line)];
			Line vertical1 = vertical.clone(); vertical1.length += oldCircle.radius;
			Line vertical2 = vertical.clone(); vertical2.length -= oldCircle.radius;
			Line line_1 = Line.lineWithPointangle(vertical1.point2, line.angle);
			Line line_2 = Line.lineWithPointangle(vertical2.point2, line.angle);
			constructionsArray.addShape(line_1);
			constructionsArray.addShape(line_2);

			arr1.addShape(line_1);
			arr2.addShape(line_1);
			arr3.addShape(line_2);
			arr4.addShape(line_2);

			IVSMutableArray* cArr1 = arr1.getCirclesTouching;
			IVSMutableArray* cArr2 = arr2.getCirclesTouching;
			IVSMutableArray* cArr3 = arr3.getCirclesTouching;
			IVSMutableArray* cArr4 = arr4.getCirclesTouching;

			IVSMutableArray* rArr = IVSMutableArray.array;
			CircletmpCircle, *obj1, *obj2;
			NSArray* cArrs = [NSArray arrayWithShapes:cArr1, cArr2, cArr3, cArr4, nil];
			for (id cArr in cArrs) {
				for (id obj in cArr._array) {
					if (obj.getShapeType() == CircleShape) {
						tmpCircle = obj.clone();
						obj1 = obj.clone();
						obj2 = obj.clone();
						obj1.radius += oldCircle.radius;
						obj2.radius -= oldCircle.radius;
						if (obj1.intersectsWithCircle(oldCircle) == IVSDoesTouchCircle && obj1.intersectsWithCircle(circle) == IVSDoesTouchCircle && obj1.intersectsWithLine(line) == IVSDoesTouchCircle) {
							constructionsArray.addShape(tmpCircle);
							rArr.addShape(obj1);
						}
						if (obj2.intersectsWithCircle(oldCircle) == IVSDoesTouchCircle && obj2.intersectsWithCircle(circle) == IVSDoesTouchCircle && obj2.intersectsWithLine(line) == IVSDoesTouchCircle) {
							constructionsArray.addShape(tmpCircle);
							rArr.addShape(obj2);
						}
					}
				}
			}
/*			for (id obj in cArr2._array) {
				if (obj.getShapeType() == CircleShape) {
					tmpCircle = obj.clone();
					[obj setRadius:obj.radius - oldCircle.radius];
					if (obj.intersectsWithCircle(oldCircle) == IVSDoesTouchCircle && obj.intersectsWithCircle(circle) == IVSDoesTouchCircle && obj.intersectsWithLine(line) == IVSDoesTouchCircle) {
					//if (!obj.touchesCircleInternally(oldCircle) && !obj.touchesCircleInternally(circle))
						constructionsArray.addShape(tmpCircle);
						rArr.addShape(obj);
					}
				}
			}
			for (id obj in cArr3._array) {
				if (obj.getShapeType() == CircleShape) {
					tmpCircle = obj.clone();
					[obj setRadius:obj.radius + oldCircle.radius];
					if (obj.intersectsWithCircle(oldCircle) == IVSDoesTouchCircle && obj.intersectsWithCircle(circle) == IVSDoesTouchCircle && obj.intersectsWithLine(line) == IVSDoesTouchCircle) {
					//if (obj.touchesCircleInternally(oldCircle) && !obj.touchesCircleInternally(circle))
						constructionsArray.addShape(tmpCircle);
						rArr.addShape(obj);
					}
				}
			}
			for (id obj in cArr4._array) {
				if (obj.getShapeType() == CircleShape) {
					tmpCircle = obj.clone();
					[obj setRadius:obj.radius + oldCircle.radius];
					if (obj.intersectsWithCircle(oldCircle) == IVSDoesTouchCircle && obj.intersectsWithCircle(circle) == IVSDoesTouchCircle && obj.intersectsWithLine(line) == IVSDoesTouchCircle) {
					//if (obj.touchesCircleInternally(oldCircle) && obj.touchesCircleInternally(circle))
						constructionsArray.addShape(tmpCircle);
						rArr.addShape(obj);
					}
				}
			}*//*
			return rArr;
		}
		else if (numberOfCircles == 1 && numberOfLines == 2) {
			IVSMutableArray* lArr = this.getLines;
			Circle oldCircle = this.getCircles.shapeAtIndex(0);
			Line line1 = lArr[0];
			Line line2 = lArr[1];

			IVSMutableArray* constructionsArray = IVSMutableArray.array;

			Line l1_vertical_ = line1.verticalToPoint(oldCircle.center);
			Line l1_vertical = [Line lineWithPoint1:oldCircle.center point2:l1_vertical_.intersectionPointWithLine(line1)];
			Line l1_vertical1 = l1_vertical.clone(); l1_vertical1.length += oldCircle.radius;
			Line l1_vertical2 = l1_vertical.clone(); l1_vertical2.length -= oldCircle.radius;
			Line l1_line_1 = Line.lineWithPointangle(l1_vertical1.point2, line1.angle);
			Line l1_line_2 = Line.lineWithPointangle(l1_vertical2.point2, line1.angle);

			Line l2_vertical_ = line2.verticalToPoint(oldCircle.center);
			Line l2_vertical = [Line lineWithPoint1:oldCircle.center point2:l2_vertical_.intersectionPointWithLine(line2)];
			Line l2_vertical1 = l2_vertical.clone(); l2_vertical1.length += oldCircle.radius;
			Line l2_vertical2 = l2_vertical.clone(); l2_vertical2.length -= oldCircle.radius;
			Line l2_line_1 = Line.lineWithPointangle(l2_vertical1.point2, line2.angle);
			Line l2_line_2 = Line.lineWithPointangle(l2_vertical2.point2, line2.angle);

			IVSMutableArray* arr1 = IVSMutableArray.array;
			IVSMutableArray* arr2 = IVSMutableArray.array;
			IVSMutableArray* arr3 = IVSMutableArray.array;
			IVSMutableArray* arr4 = IVSMutableArray.array;

			arr1.addShape(oldCircle.center);
			arr2.addShape(oldCircle.center);
			arr3.addShape(oldCircle.center);
			arr4.addShape(oldCircle.center);

			arr1.addShape(l1_line_1);
			arr2.addShape(l1_line_1);
			arr3.addShape(l1_line_2);
			arr4.addShape(l1_line_2);

			arr1.addShape(l2_line_1);
			arr2.addShape(l2_line_2);
			arr3.addShape(l2_line_1);
			arr4.addShape(l2_line_2);

			constructionsArray.addShapesFromArray(arr1);
			constructionsArray.addShapesFromArray(arr4);

			IVSMutableArray* cArr1 = arr1.getCirclesTouching;
			IVSMutableArray* cArr2 = arr2.getCirclesTouching;
			IVSMutableArray* cArr3 = arr3.getCirclesTouching;
			IVSMutableArray* cArr4 = arr4.getCirclesTouching;

			NSArray* cArrs = [NSArray arrayWithShapes:cArr1, cArr2, cArr3, cArr4, nil];
			IVSMutableArray* rArr = IVSMutableArray.array;
			CircletmpCircle, *obj1, *obj2;
			for (id cArr in cArrs) {
				for (id obj in cArr._array) {
					if (obj.getShapeType() == CircleShape) {
						tmpCircle = obj.clone();
						obj1 = obj.clone();
						obj2 = obj.clone();
						obj1.radius += oldCircle.radius;
						obj2.radius -= oldCircle.radius;
						if (obj1.intersectsWithCircle(oldCircle) == IVSDoesTouchCircle && obj1.intersectsWithLine(line1) == IVSDoesTouchCircle && obj1.intersectsWithLine(line2) == IVSDoesTouchCircle) {
							constructionsArray.addShape(tmpCircle);
							rArr.addShape(obj1);
						}
						if (obj2.intersectsWithCircle(oldCircle) == IVSDoesTouchCircle && obj2.intersectsWithLine(line1) == IVSDoesTouchCircle && obj2.intersectsWithLine(line2) == IVSDoesTouchCircle) {
							constructionsArray.addShape(tmpCircle);
							rArr.addShape(obj2);
						}
					}
				}
			}
			constructionsArray.paintConstructionsIfWanted;
			return rArr;
		}
		else if (numberOfCircles == 1 && numberOfLines == 1 && this.numberOfPoints == 1) {
			Point center = this.getPoints.shapeAtIndex(0);
			Circle inversionCircle = Circle.circleWithPointradius(center, Apollonius.inversionCircleSize);
			if (IVSShowConstructions) inversionCircle.paintInversionCircleStyle;
			IVSMutableArray* constructionsArray = IVSMutableArray.array;

			Circle circle = this.getCircles.shapeAtIndex(0);
			Line line = this.getLines.shapeAtIndex(0);
			if (line.intersectsWithPoint(center) || circle.intersectsWithPoint(center))
				return IVSMutableArray.array;

			IVSMutableArray* clArr = IVSMutableArray.array;
			clArr.addShape(circle); clArr.addShape(line);
			IVSMutableArray* iArr = clArr.arrayInvertedWithCircle(inversionCircle);
			IVSMutableArray* tArr = iArr.arrayOfCommonTangents;

			constructionsArray.addShapesFromArray(iArr);
			constructionsArray.addShapesFromArray(tArr);
			constructionsArray.paintConstructionsIfWanted;
			return tArr.arrayInvertedWithCircle(inversionCircle);
		}
		return IVSMutableArray.array;*/
		return new Shape[0];
	}
	
	public static Shape[] solutionForShapes(Circle circle1, Circle circle2, Circle circle3) {
		/*ALTERNATIVE APPROACH WHEN ALL CIRCLES INTERSECT IN ONE POINT
		-----------------------------------------------------------
		if (((Circle) shapes[0]).intersectsInOnePointWithCircles((Circle) shapes[1], (Circle) shapes[2])) {
			// all circles intersect in one point

			Point intersection = ((Circle) shapes[0]).intersectionPointWithCircles((Circle) shapes[1], (Circle) shapes[2]);

			Circle inversionCircle = new Circle(intersection, Apollonius.inversionCircleSize);
			Shape[] invertedArray = shapesInvertedWithCircle(shapes, inversionCircle);

			Shape[] circlesTouching = solutionForShapes(invertedArray);
			return shapesInvertedWithCircle(circlesTouching, inversionCircle);
		}
		else {
			// the circles don't intersect in one point
			-------------------*/

			/* result is defined as following:
			   e: touches externally
			   i: touches internally

					c1	c2	c3
			[0]		e	e	e
			[1]		e	e	i
			[2]		e	i	e
			[3]		e	i	i
			[4]		i	e	e
			[5]		i	e	i
			[6]		i	i	e
			[7]		i	i	i
			
			*/
			
			// this approach shrinks or enlarges all circles by the radius of the smallest circle
			// then it uses a selected result of solutionForShapes(Point, Circle, Circle)
			
			int whichCircle = 1;
			if (circle1.radius > circle2.radius) {
				whichCircle = 2;
			}
			if (circle2.radius > circle3.radius) {
				whichCircle = 3;
			}
			
			Circle c1, c2, c3; // NOTE: these are not in the same order as circle1, circle2, circle3 – c1 is smallest
			
			double radius = circle1.radius;
			
			Circle c2s = (Circle) circle2.clone();
			c2s.radius -= radius;
			
			Circle c2l = (Circle) circle2.clone();
			c2l.radius += radius;
			
			Circle c3s = (Circle) circle3.clone();
			c3s.radius -= radius;
			
			Circle c3l = (Circle) circle3.clone();
			c3l.radius += radius;
			
			Shape[] ss = solutionForShapes(circle1.center, c2s, c3s);
			Shape[] sl = solutionForShapes(circle1.center, c2s, c3l);
			Shape[] ls = solutionForShapes(circle1.center, c2l, c3s);
			Shape[] ll = solutionForShapes(circle1.center, c2l, c3l);
			
			Circle[] solutions = new Circle[8];
			solutions[0] = (Circle) ss[0];
			if (solutions[0] != null) {
				System.out.print("0 -- ");
				solutions[0].println();
				solutions[0].radius -= radius;
			}
			
			solutions[1] = (Circle) sl[1];
			if (solutions[1] != null) {
				System.out.print("1 -- ");
				solutions[1].println();
				solutions[1].radius -= radius;
			}
			
			solutions[2] = (Circle) ls[2];
			if (solutions[2] != null) {
				System.out.print("2 -- ");
				solutions[2].println();
				solutions[2].radius -= radius;
			}
			
			solutions[3] = (Circle) ll[3];
			if (solutions[3] != null) {
				System.out.print("3 -- ");
				solutions[3].println();
				solutions[3].radius -= radius;
			}
			
			solutions[4] = (Circle) ll[0];
			if (solutions[4] != null) {
				System.out.print("4 -- ");
				solutions[4].println();
				solutions[4].radius += radius;
			}
			
			solutions[5] = (Circle) ls[1];
			if (solutions[5] != null) {
				System.out.print("5 -- ");
				solutions[5].println();
				solutions[5].radius += radius;
			}
			
			solutions[6] = (Circle) sl[2];
			if (solutions[6] != null) {
				System.out.print("6 -- ");
				solutions[6].println();
				solutions[6].radius += radius;
			}
			
			solutions[7] = (Circle) ss[3];
			if (solutions[7] != null) {
				System.out.print("7 -- ");
				solutions[7].println();
				solutions[7].radius += radius;
			}
			
			return solutions;
			
			/*
			BUGGY ALTERNATIVE APPROACH
			-------------------------
			double r_1 = ((Circle) shapes[0]).radius;
			double r_2 = ((Circle) shapes[1]).radius;
			double r_3 = ((Circle) shapes[2]).radius;
			Circle circle1, circle2, circle3; // circle1 has the smallest radius
			if (r_1 <= r_2) {
				circle2 = (Circle) shapes[1];
				if (r_1 <= r_3) {
					circle1 = (Circle) shapes[0];
					circle3 = (Circle) shapes[2];
				}
				else {
					circle1 = (Circle) shapes[2];
					circle3 = (Circle) shapes[0];
				}
			}
			else {
				circle2 = (Circle) shapes[0];
				if (r_2 <= r_3) {
					circle1 = (Circle) shapes[1];
					circle3 = (Circle) shapes[2];
				}
				else {
					circle1 = (Circle) shapes[2];
					circle3 = (Circle) shapes[1];
				}
			}
			r_1 = circle1.radius;
			r_2 = circle2.radius;
			r_3 = circle3.radius;

			// (circles touching from inside | from outside... )
			Shape[] arr_1 = new Shape[3]; // (123|-) (-|123)	- 2-, 3-	- ii, aa
			Shape[] arr_2 = new Shape[3]; // (12|3) (3|12)		- 2-, 3+	- ia
			Shape[] arr_3 = new Shape[3]; // (13|2) (2|13)		- 2+, 3-	- ia 
			Shape[] arr_4 = new Shape[3]; // (1|23) (23|1)		- 2+, 3+	- ii, aa

			arr_1[0] = (Point) circle1.center.clone();
			arr_2[0] = (Point) circle1.center.clone();
			arr_3[0] = (Point) circle1.center.clone();
			arr_4[0] = (Point) circle1.center.clone();

			Circle circle2_1 = (Circle) circle2.clone(); // 2-
			Circle circle2_2 = (Circle) circle2.clone(); // 2+
			circle2_1.radius -= r_1;
			circle2_2.radius += r_1;
			if (r_1 == r_2) {
				arr_1[1] = (Point) circle2.center.clone();
				arr_2[1] = (Point) circle2.center.clone();
			}
			else {
				arr_1[1] = circle2_1; // 2-, 3-
				arr_2[1] = circle2_1; // 2-, 3+
			}
			arr_3[1] = circle2_2; // 2+, 3-
			arr_4[1] = circle2_2; // 2+, 3+

			Circle circle3_1 = (Circle) circle3.clone(); // 3-
			Circle circle3_2 = (Circle) circle3.clone(); // 3+
			circle3_1.radius -= r_1;
			circle3_2.radius += r_1;
			if (r_1 == r_3) {
				arr_1[2] = (Point) circle3.center.clone(); // 2-, 3-
				arr_3[2] = (Point) circle3.center.clone(); // 2-, 3+
			}
			else {
				arr_1[2] = circle3_1; // 2-, 3-
				arr_3[2] = circle3_1; // 2+, 3-
			}
			arr_2[2] = circle3_2; // 2-, 3+
			arr_4[2] = circle3_2; // 2+, 3+

			Shape[] rArr1 = solutionForShapes(arr_1);
			Shape[] rArr2 = solutionForShapes(arr_2);
			Shape[] rArr3 = solutionForShapes(arr_3);
			Shape[] rArr4 = solutionForShapes(arr_4);
			Shape[] rArr = new Shape[8];

			Circle tmpCircle;
			boolean makeBigger;
			Line[] parallelLines;
			for (Shape obj : rArr1) {
				if (obj.getShapeType() == ShapeType.LINE) {
					parallelLines = ((Line) obj).parallelLinesWithDistance(r_1);
					rArr[0] = parallelLines[0];
					rArr[1] = parallelLines[1];
				}
				else if ((((Circle) obj).touchesCircleInternally(circle2_1) == ((Circle) obj).touchesCircleInternally(circle3_1)) || r_1 == r_2 || r_1 == r_3) {
					rArr[0] = obj;
					if (r_1 == r_2 && r_1 == r_3) {
						tmpCircle = (Circle) ((Circle) obj).clone();
						((Circle) obj).radius += r_1;
						rArr[1] = tmpCircle;
						tmpCircle.radius -= r_1;
					}
					else {
						if (r_1 == r_2)
							makeBigger = ((Circle) obj).touchesCircleInternally(circle3_1);
						else
							makeBigger = ((Circle) obj).touchesCircleInternally(circle2_1);

						if (makeBigger)
							((Circle) obj).radius += r_1;
						else
							((Circle) obj).radius -= r_1;
					}
				}
			}
			for (Shape obj : rArr2) {
				if (obj.getShapeType() == ShapeType.LINE) {
					parallelLines = ((Line) obj).parallelLinesWithDistance(r_1);
					rArr[2] = parallelLines[0];
					rArr[3] = parallelLines[1];
				}
				else if ((((Circle) obj).touchesCircleInternally(circle2_1) != ((Circle) obj).touchesCircleInternally(circle3_2)) || r_1 == r_2) {
					rArr[2] = obj;
					if (r_1 == r_2)
						makeBigger = !((Circle) obj).touchesCircleInternally(circle3_2);
					else
						makeBigger = ((Circle) obj).touchesCircleInternally(circle2_1);

					if (makeBigger)
						((Circle) obj).radius += r_1;
					else
						((Circle) obj).radius -= r_1;
				}
			}
			for (Shape obj : rArr3) {
				if (obj.getShapeType() == ShapeType.LINE) {
					parallelLines = ((Line) obj).parallelLinesWithDistance(r_1);
					rArr[4] = parallelLines[0];
					rArr[5] = parallelLines[1];
				}
				else if ((((Circle) obj).touchesCircleInternally(circle2_2) != ((Circle) obj).touchesCircleInternally(circle3_1)) || r_1 == r_3) {
					rArr[4] = obj;
					if (r_1 == r_3)
						makeBigger = !((Circle) obj).touchesCircleInternally(circle2_2);
					else
						makeBigger = ((Circle) obj).touchesCircleInternally(circle3_1);

					if (makeBigger)
						((Circle) obj).radius += r_1;
					else
						((Circle) obj).radius -= r_1;
				}
			}
			for (Shape obj : rArr4) {
				if (obj.getShapeType() == ShapeType.LINE) {
					parallelLines = ((Line) obj).parallelLinesWithDistance(r_1);
					rArr[6] = parallelLines[0];
					rArr[7] = parallelLines[1];
				}
				else if (((Circle) obj).touchesCircleInternally(circle2_2) == ((Circle) obj).touchesCircleInternally(circle3_2)) {
					rArr[6] = obj;
					makeBigger = !((Circle) obj).touchesCircleInternally(circle2_2);
					if (makeBigger) {
						((Circle) obj).radius += r_1;
					}
					else {
						((Circle) obj).radius -= r_1;
					}
				}
			}
			return rArr; // TODO: remove empty fields*/
	/*	} */
		
	}
	
	public static Shape[] solutionForShapes(Point point, Circle circle1, Circle circle2) {
		/* result is defined as following:
		   e: touches externally
		   i: touches internally

				c1	c2
		[0]		e	e
		[1]		e	i
		[2]		i	e
		[3]		i	i
		
		*/
		if (!circle1.intersectsWithPoint(point) && !circle2.intersectsWithPoint(point)) {
			Circle inversionCircle = new Circle(point, Apollonius.inversionCircleSize);
			Circle c1_ = (Circle) circle1.shapeInvertedWithCircle(inversionCircle);
			Circle c2_ = (Circle) circle2.shapeInvertedWithCircle(inversionCircle);
			Line[] tArr = commonTangentsOfCircles(c1_, c2_);
			
			Line s = new Line(c1_.center, c2_.center);
			Boolean pointIsOnLeftSideOfS = s.pointIsOnLeftSide(point);
			
			Shape[] iArr = shapesInvertedWithCircle(tArr, inversionCircle);
			Shape[] rArr = new Shape[4];
			
			if (pointIsOnLeftSideOfS) {
				rArr = iArr;
			}
			else {
				rArr[3] = iArr[0];
				rArr[2] = iArr[1];
				rArr[1] = iArr[2];
				rArr[0] = iArr[3];
			}
			
			return rArr;
		}
		else {
			System.out.println("Point is on either circle"); // TODO: Throw error
			return new Shape[4];
		}
	}
	
	public static Shape[] shapesInvertedWithCircle(Shape[] shapes, Circle circle) {
		Shape iObj;
		int length = shapes.length;
		Shape[] invertedArray = new Shape[length];
		for (int i = 0; i < length; i++) {
			if (shapes[i] == null) {
				continue;
			}
			iObj = shapes[i].shapeInvertedWithCircle(circle);
			invertedArray[i] = iObj;
		}
		return invertedArray;
	}
	
	public static Line[] commonTangentsOfCircles(Circle circle1, Circle circle2) {
		/* when seen from circle1.center to circle2.center,
		 * l: touches left side of circle1
		 * r: touches right side of circle1
		 * L: touches left side of circle2
		 * R: touches right side of circle2
		 *
		 * [0]: l L
		 * [1]: l R
		 * [2]: r L
		 * [3]: r R
		 *
		 * if lines do not exist, their values will be null
		 * if either radius is 0, the tangents are numbered as Circle.tangentsThroughPoint(Point), but always seen from circle1
		 */
		
		if (circle1.center.equals(circle2.center)) {
			System.out.println("Apollonius.commonTangentsOfCircles: circles are concentric");
			return new Line[0];
		}
		
		if (circle1.radius == 0) {
			return circle2.tangentsThroughPoint(circle1.center);
		}
		if (circle2.radius == 0) {
			Line[] tangentsInWrongOrder = circle1.tangentsThroughPoint(circle2.center);
			Line[] tangents = new Line[2];
			tangents[0] = tangentsInWrongOrder[1];
			tangents[1] = tangentsInWrongOrder[0];
			return tangents;
		}
		
		Boolean circle1IsSmaller = circle1.radius < circle2.radius;
		
		Circle circle2S = (Circle) circle2.clone();
		circle2S.radius -= circle1.radius;
		
		Circle circle2L = (Circle) circle2.clone();
		circle2L.radius += circle1.radius;
		
		Line[] sArr = circle2S.tangentsThroughPoint(circle1.center);
		Line[] lArr = circle2L.tangentsThroughPoint(circle1.center);
		
		Line[] tangents = new Line[4];
		
		tangents[0] = sArr[circle1IsSmaller ? 0 : 1].parallelLinesWithDistance(circle1.radius)[0];
		tangents[3] = sArr[circle1IsSmaller ? 1 : 0].parallelLinesWithDistance(circle1.radius)[1];
		
		System.out.println("lArr.length = " + lArr.length);
		if (lArr.length > 0) {
			tangents[1] = lArr[1].parallelLinesWithDistance(circle1.radius)[0];
			tangents[2] = lArr[0].parallelLinesWithDistance(circle1.radius)[1];
		}
		
		return tangents;
/*
		ALTERNATIVE APROACH:
		--------------------
		Line centersLine = new Line(circle1.center, circle2.center);
		double angle = centersLine.getAngle() + Math.PI/2;

		Line rLine1 = new Line(circle1.center, angle);
		Line rLine2 = new Line(circle2.center, angle);

		Point[] pArr1 = rLine1.intersectionPointsWithCircle(circle1);
		Point[] pArr2 = rLine2.intersectionPointsWithCircle(circle2);
		Point p1_1 = pArr1[0];
		Point p1_2 = pArr1[1];
		Point p2_1 = pArr2[0];
		Point p2_2 = pArr2[1];

		Line[] tArr = new Line[4];
		int tCount = 0;

		Line _tLine1 = new Line(p1_1, p2_1);
		if (_tLine1.intersectsWithLine(centersLine) == IntersectionCount.ONE) {
			Point tP1 = _tLine1.intersectionPointWithLine(centersLine);
			Line[] tArr1 = circle1.tangentsThroughPoint(tP1);
			if (tArr1.length > 0) {
				tArr[tCount] = tArr1[0];
				tCount++;
			}
			if (tArr1.length > 1) {
				tArr[tCount] = tArr1[1];
				tCount++;
			}
		}
		else {
			tArr[tCount] = new Line(p1_1, p2_1);
			tCount++;
			tArr[tCount] = new Line(p1_2, p2_2);
			tCount++;
		}

		Line _tLine2 = new Line(p1_1, p2_2);
		if (_tLine2.intersectsWithLine(centersLine) == IntersectionCount.ONE) {
			Point tP2 = _tLine2.intersectionPointWithLine(centersLine);
			Line[] tArr2 = circle1.tangentsThroughPoint(tP2);
			if (tArr2.length > 0) {
				tArr[tCount] = tArr2[0];
				tCount++;
			}
			if (tArr2.length > 1) {
				tArr[tCount] = tArr2[1];
				tCount++;
			}
		}
		else {
			tArr[tCount] = new Line(p1_1, p2_2);
			tCount++;
			tArr[tCount] = new Line(p1_2, p2_1);
			tCount++;
		}

		
		// copy all tangents into one array to return
		Line[] rTArr = new Line[tCount];
		for (int i = 0; i < tCount; i++) {
			rTArr[i] = tArr[i];
		}

		return rTArr;*/
	}
	
	public static int numberOfPoints(Shape[] shapes) {
		int count = 0;
		for (Shape shape : shapes) {
			if (shape == null) {
				continue;
			}
			if (shape.getShapeType() == ShapeType.POINT) {
				count++;
			}
		}
		return count;
	}
	
	public static int numberOfCircles(Shape[] shapes) {
		int count = 0;
		for (Shape shape : shapes) {
			if (shape == null) {
				continue;
			}
			if (shape.getShapeType() == ShapeType.CIRCLE) {
				count++;
			}
		}
		return count;
	}
	
	public static int numberOfLines(Shape[] shapes) {
		int count = 0;
		for (Shape shape : shapes) {
			if (shape == null) {
				continue;
			}
			if (shape.getShapeType() == ShapeType.LINE) {
				count++;
			}
		}
		return count;
	}
	
	public static Point[] getPoints(Shape[] shapes) {
		Point[] arr = new Point[numberOfPoints(shapes)];
		int i = 0;
		for (Shape shape : shapes) {
			if (shape == null) {
				continue;
			}
			if (shape.getShapeType() == ShapeType.POINT) {
				arr[i] = (Point) shape;
				i++;
			}
		}
		return arr;
	}
	
	public static Circle[] getCircles(Shape[] shapes) {
		Circle[] arr = new Circle[numberOfCircles(shapes)];
		int i = 0;
		for (Shape shape : shapes) {
			if (shape == null) {
				continue;
			}
			if (shape.getShapeType() == ShapeType.CIRCLE) {
				arr[i] = (Circle) shape;
				i++;
			}
		}
		return arr;
	}
	
	public static Line[] getLines(Shape[] shapes) {
		Line[] arr = new Line[numberOfLines(shapes)];
		int i = 0;
		for (Shape shape : shapes) {
			if (shape == null) {
				continue;
			}
			if (shape.getShapeType() == ShapeType.LINE) {
				arr[i] = (Line) shape;
				i++;
			}
		}
		return arr;
	}
}
