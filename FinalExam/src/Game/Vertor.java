package Game;

/**The Vector class is used to tracking the objects's coordinate 
 * @param x The parameter is to display the coordinate x.
 * @param y The parameter is to display the coordinate y.
 */
public class Vertor {
	public double x, y;

	public Vertor() {
		this.set(0, 0);
	}

	public Vertor(double x, double y) {
		this.set(x, y);
	}

	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void add(double dx, double dy) {
		this.x += dx;
		this.y += dy;
	}
	public void minus(double dx, double dy) {
		this.x -= dx;
		this.y -= dy;
	}
	public void multiply(double m) {
		this.x *= m;
		this.y *= m;
	}
	public double getLength() {
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}
	public void setLength(double l) {
		double currentLength = this.getLength();
		if (currentLength == 0) {
			this.set(l, 0);
		}
		else {
			this.multiply(1/currentLength); //length = 1
			this.multiply(l); //length = L
		}
	}
}
