
package Game;

/**
 * This class is used to draw the objects's boundary, set size, positions and to check the object's collisions.
 * @param x : The parameter x to display the coordinate x
 * @param y : The parameter x to display the coordinate y
 * @param w : The parameter w to set the width of the boundary
 * @param h : The parameter h to set the height of the boundary
 */
public class Rectangle {
	double x,y,w,h;
	
	public Rectangle() {
		this.setPosision(0, 0);
		this.setSize(1, 1);
	}
	public Rectangle(double x, double y,double w, double h) {
		this.setPosision(x, y);
		this.setSize(w, h);
	}
	
	public void setPosision(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setSize(double w, double h) {
		this.w = w;
		this.h = h;
	}
	
	//this method is used to calculate the distance from one rectangle to another one basing on the formula.
	//return true if the distance is smaller the sum of a half of 2 rectangles's height.
	public boolean isOverlap(Rectangle other) {
		double distance = Math.sqrt((this.x - other.x)*(this.x - other.x) + (this.y - other.y)*(this.y - other.y) );
		return distance < this.h/2 + other.h/2;
	}
}
