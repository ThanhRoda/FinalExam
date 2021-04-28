package Game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BaseLayout {
	int explosionStep = 0;
	final int EXPLOSION_W = 128;
	final int EXPLOSION_ROWS = 4;
	final int EXPLOSION_COL = 4;
	final int EXPLOSION_H = 128;
	final int EXPLOSION_STEPS = 15;

	public Vertor position;
	public Vertor speedMovement;
	public Rectangle boudary;
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
		this.boudary = new Rectangle();
	}

	public BaseLayout(String imgUrl) {
		this();
		this.setImage(imgUrl);
	}

	public void setImage(String imgUrl) {
		this.image = new Image(imgUrl);
		this.boudary.setSize(this.image.getWidth(), this.image.getHeight());
	}

	public Rectangle getBoudary() {
		this.boudary.setPosision(this.position.x, this.position.y);
		return this.boudary;
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

	public void render(GraphicsContext context) {
		context.save();
		if (exlosing) {
			context.drawImage(new Image("/explosion.png"), explosionStep % EXPLOSION_COL * EXPLOSION_W,
					(explosionStep / EXPLOSION_ROWS) * EXPLOSION_H + 1, EXPLOSION_W, EXPLOSION_H, this.position.x - 40,
					this.position.y - 40, 100, 100);
			explosionStep++;
		} else {
			context.translate(this.position.x, this.position.y);
			context.translate(-this.image.getWidth() / 2, -this.image.getHeight() / 2);
			context.drawImage(this.image, 0, 0);
		}
		context.restore();
	}
}
