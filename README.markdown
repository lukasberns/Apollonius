# Apollonius — An Apollonius’ problem-solver written in Java

This is a port of the core of my “[Inversion][]” app to Java.

A quick introduction to the problem of Apollonius:
> The task of this geometrical problem is to construct all circles (or lines) that are tangent to (“touch”) three circles (or a point / line).
Now this may sound easy, but try to do this with three points, which is the kind of the most easy one. You'll soon find out it isn't that easy. But if you use something called “inversion” (not the app), it's very easy. Inversion is sometimes also called “mirroring in a circle” (at least in German), which explains the concept very well.

[Inversion]: http://rand1-365.blogspot.com/search/label/Inversion

## How to compile / run

Compile the program from one directory higher with the following command:

	$ javac apollonius/Apollonius.java

Usage:

	$ java apollonius.Apollonius [options] [input shapes]

### Available options:  
`--debug`		Enable debug mode.  
`--help`		Show help.  
`--render`		Render the solutions in a window.

### How to specify input shapes:

Currently, only circles are supported.
Please input exactly three circles separated with spaces.
The syntax is like this:

	circle[x,y,r]

Where x and y are the coordinates of the center point and r is the radius.
(Don't insert spaces after the comma!)


## To use it in other projects

If you only want calculations, you should include the code in you project. Proceed like this:

	import apollonius; // or change the package of the classes
	
	Circle c1 = new Circle(15, 10, 8);
	Circle c2 = new Circle(28, 30, 8);
	Circle c3 = new Circle(35, 15, 8);
	
	// this array will hold the solutions
	Shape[] solutions = Apollonius.solutionForShapes(c1, c2, c3);
	
	// you can use the array just as you like, e.g. to print the data of the shapes into STDOUT
	for (Shape shape : solutions) {
		if (shape != null) {
			// whatever you do with the solutions, always check for this
			
			shape.println();
		}
	}

Browse the source code for more info. Please read the notes at the end of this file, too.

## Note

To get correct results, you might need to adjust some class variables of the `Apollonius` class:

`precision`:  
I recommend something like 1000000.0 (10^6)

`inversionCircleSize`:  
Use a number greater than the radii of the circles you give as input (e.g. 1000)

When rendering the solutions, adjust the `scale` class variable of `CanvasPanel` to scale the output.


