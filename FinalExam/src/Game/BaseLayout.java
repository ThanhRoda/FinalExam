package Game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * The BaseLayout class implements objects (planes, rockets) using canvas method to render.
 * @param explosionStep: This is the parameter to display explosion effect (run from image 0-15) when objects are collapsed.
 * @param position: This is the parameter to track the position of objects.
 * @param boundary: This is the parameter display the border of the objects to define collisions.
 * @param image: This used to link to image URI.
 * @param speedMovement: This is parameter to check objects's speed.
 */
public class BaseLayout {
	int explosionStep = 0;
	final int EXPLOSION_W = 128;
	final int EXPLOSION_ROWS = 4;
	final int EXPLOSION_COL = 4;
	final int EXPLOSION_H = 128;
	final int EXPLOSION_STEPS = 15;

	public Vertor position;
	public Vertor speedMovement;
	public Rectangle boundary;
	public Image image;
	public Boolean exlosing = false, destroy = false;

	public Boolean getExlosing() {
		return exlosing;
	}

	public void setExlosing(Boolean exlosing) {
		this.exlosing = exlosing;
	}

	public Boolean getDestroy() {
		return destroy;
	}

	public void setDestroy(Boolean destroy) {
		this.destroy = destroy;
	}

	public BaseLayout() {
		this.position = new Vertor();
		this.speedMovement = new Vertor();
		this.boundary = new Rectangle();
	}

	public BaseLayout(String imgUrl) {
		this();
		this.setImage(imgUrl);
	}

	public void setImage(String imgUrl) {
		this.image = new Image(imgUrl);
		this.boundary.setSize(this.image.getWidth(), this.image.getHeight());
	}

	public Rectangle getBoudary() {
		this.boundary.setPosision(this.position.x, this.position.y);
		return this.boundary;
	}

	public boolean isOverLap(BaseLayout other) {
		return this.getBoudary().isOverlap(other.getBoudary());
	}

	public void moveForward(double time) {
		this.position.add(this.speedMovement.x * time, this.speedMovement.y * time);
	}

	public void moveBackward(double time) {
		this.position.minus(this.speedMovement.x * time, this.speedMovement.y * time);
	}

	public void update() {
		if (exlosing)
			explosionStep += 0.02;
		destroy = explosionStep > EXPLOSION_STEPS;
	}
	public void remove() {
		this.position.set(-100, -100);
	}
	
	public void render(GraphicsContext context) {
		context.save();
		if (exlosing) {
			context.drawImage(new Image("/explosion.png"), explosionStep % EXPLOSION_COL * EXPLOSION_W,
					(explosionStep / EXPLOSION_ROWS) * EXPLOSION_H + 1, EXPLOSION_W, EXPLOSION_H, this.position.x - 40,
					this.position.y - 40, 100, 100);
			explosionStep++;
		} else 
		{
			context.translate(this.position.x, this.position.y);
			context.translate(-this.image.getWidth() / 2, -this.image.getHeight() / 2);
			context.drawImage(this.image, 0, 0);
		}
		context.restore();
	}
}
