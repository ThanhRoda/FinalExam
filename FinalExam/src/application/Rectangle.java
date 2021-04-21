
package application;

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
	
	public boolean isOverlap(Rectangle other) {
		double distance = Math.sqrt((this.x - other.x)*(this.x - other.x) + (this.y - other.y)*(this.y - other.y) );
		return distance < this.h/2 + other.h/2;
	}
}
