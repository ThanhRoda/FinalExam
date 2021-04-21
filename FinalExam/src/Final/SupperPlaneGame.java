package Final;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import application.BaseLayout;
import application.Player;
import application.ResultLayoutController;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.StringProperty;
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
	static String socketValue = "";
	PrintWriter outPrinter;
	public static void main(String[] args) {
		try {
			launch(args);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}

	}

//	@Override
//	public void start(Stage primaryStage) throws Exception {
//		// Login screen
//		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/Login.fxml"));
//		Parent root = loader.load();
//		Scene sc = new Scene(root);
//		primaryStage.setScene(sc); // set the scene
//		primaryStage.setTitle("TooPin");
//		primaryStage.show();
//	}
	@Override
	public void start(Stage primaryStage) throws IOException {
		Runnable task1 = () -> {
			Socket s = new Socket();
			try {
				s.connect(new InetSocketAddress("localhost", 6666), 1500);
		        OutputStream output = s.getOutputStream();
				try (Scanner in = new Scanner(s.getInputStream(), "UTF-8")) {
					 outPrinter = new PrintWriter(new OutputStreamWriter(output, "UTF-8"),true);
					while (in.hasNext()) {
						socketValue = in.nextLine();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		};
		

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
		ArrayList<BaseLayout> gunList2 = new ArrayList<>();
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
		
		BaseLayout plane2 = new BaseLayout("/player2.png");
		plane2.position.set(900, 300);
		
		AnimationTimer RunningGame = new AnimationTimer() {

			@Override
			public void handle(long now) {
				if (keyPressList.contains("UP")) {
					outPrinter.println("up");
					if (plane.position.y - 10 > 10)
						plane.position.set(plane.position.x, plane.position.y - 10);
					
				}
				if (socketValue.compareToIgnoreCase("up") == 0) {
					socketValue = "noThing";
					if (plane2.position.y - 10 > 10)
						plane2.position.set(plane2.position.x, plane2.position.y - 10);
				}
				if (socketValue.compareToIgnoreCase("down") == 0) {
					socketValue = "noThing";
					if (plane2.position.y + 10 < 590)
						plane2.position.set(plane2.position.x, plane2.position.y + 10);
				}
				
				if (keyPressList.contains("DOWN")) {
					outPrinter.println("down");
					if (plane.position.y + 10 < 590)
						plane.position.set(plane.position.x, plane.position.y + 10);
				}
				if (socketValue.compareToIgnoreCase("SPACE") == 0) {
					socketValue = "noThing";
					BaseLayout gun = new BaseLayout("/rocket2.png");
					gun.position.set(plane2.position.x - plane2.image.getWidth() / 2.0, plane2.position.y);
					gun.speedMovement.setLength(300);
					gunList2.add(gun);
				}
				if (keyJustPressList.contains("SPACE")) {
					outPrinter.println("SPACE");
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
				for (int j = 0; j < gunList2.size(); j++) {
					BaseLayout gun = gunList2.get(j);
					gun.moveBackward(1 / 60.0);
					if (gun.position.x < 0)
						gunList2.remove(gun);
				}

				for (BaseLayout obs : obstacles)
					obs.moveBackward(1 / 60.0);

				for (int j = 0; j < gunList.size(); j++) {
					BaseLayout gun = gunList.get(j);
					if(gun.isOverLap(plane2)) {
						plane2.exlosing = true;
						gunList.remove(gun);
					}
				}
				for (int j = 0; j < gunList2.size(); j++) {
					BaseLayout gun = gunList2.get(j);
					if(gun.isOverLap(plane)) {
						plane.exlosing = true;
						gunList2.remove(gun);
					}
				}

				background.render(context);
				plane.update();
				plane.render(context);
				plane2.update();
				plane2.render(context);
				if (plane.destroy) {
					gameover = true;
				}
				for (BaseLayout gun : gunList) {
					gun.render(context);
				}
				for (BaseLayout gun : gunList2) {
					gun.render(context);
				}
				if (plane.destroy || plane2.destroy) {
					stop();
					if (plane2.destroy)
						context.fillText("You Win", 300, 450);
					else 
						context.fillText("You Lose", 450, 300);
						
				}
				context.setFill(Color.WHITE);
				context.setStroke(Color.GREEN);
				context.setFont(new Font("Arial Black", 30));
				String textScore = "Score: " + score;
				context.fillText(textScore, 750, 40);
				context.strokeText(textScore, 750, 40);
				// String textUser = "Name: " + player.getUserName();
//				context.fillText(textUser, 400, 40);
//				context.strokeText(textUser, 400, 40);
				
			}
		};
		primaryStage.setOnShowing(e -> new Thread(task1).start());
		RunningGame.start();
		primaryStage.show();
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
