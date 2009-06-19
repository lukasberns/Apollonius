//
//  Circle.java
//  Apollonius
//
//  Created by Lukas Berns on 2009/5/28.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

public class Shape implements Cloneable {
	public ShapeType getShapeType() { // TODO: It's strange this doesn't work with static although it makes more sense
		return ShapeType.SHAPE;
	}
	
	public Object clone() {
		try {
		      return super.clone();
		} catch (CloneNotSupportedException ex) {
			System.out.println("Now that's a surprise!!");
			throw new InternalError(ex.toString());
		}
	}
	
	public Shape shapeInvertedWithCircle(Circle circle) {
		return new Shape();
	}
	
	public void println() {
		System.out.println("Shape");
	}
}