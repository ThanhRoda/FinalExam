package application;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SupperPlaneGame extends Application {
	static int SPEED_OBS = 50;
	static int MAXOBSTACLES = 10;
	static int score = 0;
	static boolean gameover;

	public static void main(String[] args) {
		try {
			launch(args);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Login screen
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/Login.fxml"));
		Parent root = loader.load();
		Scene sc = new Scene(root);
		primaryStage.setScene(sc); // set the scene
		primaryStage.setTitle("TooPin");
		primaryStage.show();
	}

	public static void gameScene(Stage primaryStage, Player player) {
		gameover = false;
		SPEED_OBS = 50;
		score = 0;
		primaryStage.setTitle("Supper Plane");

		BorderPane root = new BorderPane();
		Scene sc = new Scene(root);
		primaryStage.setScene(sc);

		Canvas canvas = new Canvas(1000, 600);
		GraphicsContext context = canvas.getGraphicsContext2D();
		root.setCenter(canvas);

		ArrayList<String> keyPressList = new ArrayList<>();
		ArrayList<String> keyJustPressList = new ArrayList<>();

		sc.setOnKeyPressed((KeyEvent event) -> {
			String keyname = event.getCode().toString();
			if (!keyPressList.contains(keyname))
				keyPressList.add(keyname);
			if (!keyJustPressList.contains(keyname))
				keyJustPressList.add(keyname);
		});
		sc.setOnKeyReleased((KeyEvent event) -> {
			String keyname = event.getCode().toString();
			if (keyPressList.contains(keyname))
				keyPressList.remove(keyname);
		});
		
		
		ArrayList<BaseLayout> gunList = new ArrayList<>();
		ArrayList<BaseLayout> obstacles = new ArrayList<>();

		for (int i = 1; i <= 3; i++) {
			BaseLayout ob = new BaseLayout("/12.png");
			double y = 500 * Math.random() + 100;
			ob.position.set(1000, y);
			ob.speedMovement.setLength(SPEED_OBS);
			obstacles.add(ob);
		}
		BaseLayout background = new BaseLayout("/background.jpg");
		background.position.set(500, 300);

		BaseLayout plane = new BaseLayout("/player.png");
		plane.position.set(100, 300);
		AnimationTimer RunningGame = new AnimationTimer() {

			@Override
			public void handle(long now) {
				if (keyPressList.contains("UP")) {
					if (plane.position.y - 10 > 10)
						plane.position.set(plane.position.x, plane.position.y - 10);
				}
				if (keyPressList.contains("DOWN")) {
					if (plane.position.y + 10 < 590)
						plane.position.set(plane.position.x, plane.position.y + 10);
				}
				if (keyJustPressList.contains("SPACE")) {
					BaseLayout gun = new BaseLayout("/rocket.png");
					gun.position.set(plane.position.x + plane.image.getWidth() / 2.0, plane.position.y);
					gun.speedMovement.setLength(300);
					gunList.add(gun);
				}
				keyJustPressList.clear();
				for (int j = 0; j < gunList.size(); j++) {
					BaseLayout gun = gunList.get(j);
					gun.moveForward(1 / 60.0);
					if (gun.position.x > 1020)
						gunList.remove(gun);
				}

				for (BaseLayout obs : obstacles)
					obs.moveBackward(1 / 60.0);

				for (int j = 0; j < gunList.size(); j++) {
					BaseLayout gun = gunList.get(j);
					for (int i = 0; i < obstacles.size(); i++) {
						BaseLayout obs = obstacles.get(i);
						if (gun.isOverLap(obs)) {
							obs.setExlosing(true);
							gunList.remove(gun);
							SPEED_OBS += 1;
							score += 10;
							NewObstacle(obstacles);
						}
					}
				}

				for (int i = 0; i < obstacles.size(); i++) {
					BaseLayout obs = obstacles.get(i);
					if (plane.isOverLap(obs)) {
						obstacles.remove(obs);
						plane.setExlosing(true);

					} else {
						if (obs.position.x < -5) {
							SPEED_OBS += 2;
							double y = 500 * Math.random() + 100;
							obs.position.set(1100, y);
							obs.speedMovement.setLength(SPEED_OBS);
						}
					}
				}

				background.render(context);
				plane.update();
				plane.render(context);
				if (plane.destroy) {
					gameover = true;
				}
				for (BaseLayout gun : gunList) {
					gun.render(context);
				}
				for (int i = 0; i < obstacles.size(); i++) {
					BaseLayout obs = obstacles.get(i);

					if (obs.destroy)
						obstacles.remove(obs);
					else {
						obs.update();
						obs.render(context);
					}
				}
				context.setFill(Color.WHITE);
				context.setStroke(Color.GREEN);
				context.setFont(new Font("Arial Black", 30));
				String textScore = "Score: " + score;
				context.fillText(textScore, 750, 40);
				context.strokeText(textScore, 750, 40);
				String textUser = "Name: " + player.getUserName();
				context.fillText(textUser, 400, 40);
				context.strokeText(textUser, 400, 40);
				if (gameover) {
					stop();
					try {
						SupperPlaneGame a = new SupperPlaneGame();
								a.showResult(player);
								primaryStage.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};

		RunningGame.start();
		primaryStage.show();
	}
	public void showResult(Player player) throws Exception  {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/ResultLayout.fxml"));
		Parent root = loader.load();
		ResultLayoutController controller = loader.getController();
		controller.setUserName(player.getUserName());
		controller.setSocer(score);
		// top ten vs save to database
		if (player.getScore() < score) {
			player.setScore(score);
			controller.savePlayer(player);
		}
		controller.getTopTen();
		Scene secondScene = new Scene(root);
		Stage secondStage = new Stage();
		secondStage.setScene(secondScene); // set the scene
		secondStage.setTitle("Result");
		secondStage.show();
		
	}

	public static void NewObstacle(ArrayList<BaseLayout> obstacles) {
		for (int i = 1; i <= 2; i++) {
			BaseLayout ob = new BaseLayout("/12.png");
			double y = 500 * Math.random() + 100;
			ob.position.set(1150, y);
			ob.speedMovement.setLength(SPEED_OBS);
			if (obstacles.size() < MAXOBSTACLES)
				obstacles.add(ob);
		}
	}
}
