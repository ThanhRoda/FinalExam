package Server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import Game.BaseLayout;
import Game.LoginController;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SupperPlaneGame extends Application {
	static int SPEED_OBS = 50;
	static int MAXOBSTACLES = 10;
	static int score = 0;
	static boolean isLogin;
	static String socketValue = "";
	static PrintWriter outPrinter;
	static AnimationTimer RunningGame;
	static Socket socket;
	private final String serverName = "localhost";
	private final int port = 6666;
	static GameClient client;

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
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Game/Login.fxml"));
		Parent root = loader.load();
		LoginController con = (LoginController) loader.getController();
		client = new GameClient(serverName, port, data -> {
			con.setMessage(data);
			if(data.startsWith("Success"))
				Platform.runLater(() -> {
					try {
					primaryStage.close();
					SupperPlaneGame.gameScene(primaryStage);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            });
		});
		con.setClient(client);
		client.connectSocket();
		Scene sc = new Scene(root);
		primaryStage.setScene(sc); 
		
		primaryStage.setTitle("TooPin");
		primaryStage.show();
	}


	public static void gameScene(Stage primaryStage) throws IOException {
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
		ArrayList<BaseLayout> gunList3 = new ArrayList<>();
		
		BaseLayout background = new BaseLayout("/background.jpg");
		background.position.set(500, 300);

		BaseLayout plane = new BaseLayout("/player.png");
		plane.position.set(100, 300);

		BaseLayout plane2 = new BaseLayout("/player2.png");
		plane2.position.set(900, 150);
		
		BaseLayout plane3 = new BaseLayout("/player2.png");
		plane3.position.set(900, 450);

		RunningGame = new AnimationTimer() {

			@Override
			public void handle(long now) {
				if (keyPressList.contains("UP")) {
					if (plane.position.y - 10 > 10)
						plane.position.set(plane.position.x, plane.position.y - 10);
					client.send(String.valueOf(plane.position.y));
				}

				try {
					if (Double.parseDouble(socketValue) > 0) {
						plane2.position.set(plane2.position.x, Double.parseDouble(socketValue));
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

				if (keyPressList.contains("DOWN")) {

					if (plane.position.y + 10 < 590)
						plane.position.set(plane.position.x, plane.position.y + 10);
					client.send(String.valueOf(plane.position.y));
				}
				if (socketValue.compareToIgnoreCase("SPACE") == 0) {
					socketValue = "noThing";
					BaseLayout gun = new BaseLayout("/rocket2.png");
					gun.position.set(plane2.position.x - plane2.image.getWidth() / 2.0, plane2.position.y);
					gun.speedMovement.setLength(300);
					gunList2.add(gun);
				} 
				if (keyJustPressList.contains("SPACE")) {
					client.send(String.valueOf("SPACE"));
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


				for (int j = 0; j < gunList.size(); j++) {
					BaseLayout gun = gunList.get(j);
					if (gun.isOverLap(plane2)) {
						plane2.exlosing = true;
						gunList.remove(gun);
					}
				}
				for (int j = 0; j < gunList2.size(); j++) {
					BaseLayout gun = gunList2.get(j);
					if (gun.isOverLap(plane)) {
						plane.exlosing = true;
						gunList2.remove(gun);
					}
				}
				if (plane2.isOverLap(plane3)) {
					plane2.exlosing = true;
					plane3.exlosing = true;
				}
				for (int j = 0; j < gunList3.size(); j++) {
					BaseLayout gun = gunList3.get(j);
					if (gun.isOverLap(plane)) {
						plane.exlosing = true;
						gunList3.remove(gun);
					}
				}

				background.render(context);
				plane.update();
				plane.render(context);
				plane2.update();
				plane2.render(context);
				plane3.update();
				plane3.render(context);

				if (plane.destroy || plane2.destroy || plane3.destroy ) {
					stop();
					if (plane2.destroy || plane3.destroy)
						context.fillText("You Win", 450, 300);
					else
						context.fillText("You Lose", 450, 300);

				}
				context.setFill(Color.WHITE);
				context.setStroke(Color.GREEN);
				context.setFont(new Font("Arial Black", 30));

				if (socketValue.compareToIgnoreCase("waiting") == 0) {
					String textUser = "Matching ...";
					context.fillText(textUser, 450, 300);
					context.strokeText(textUser, 450, 300);
					stop();
				}
				if (socketValue.compareToIgnoreCase("start") == 0) {
					socketValue ="nothing";
					String textUser = "Start Game";
					context.fillText(textUser, 450, 300);
					context.strokeText(textUser, 450, 300);
				}
				for (BaseLayout gun : gunList) {
					gun.render(context);
				}
				for (BaseLayout gun : gunList2) {
					gun.render(context);
				}
				
			}
		};
		
		client.seHandle(data -> {
				socketValue = data;
				System.out.println(socketValue);
				Platform.runLater(() -> {
					if (socketValue.compareToIgnoreCase("start") == 0) {
						gunList.clear();
						gunList2.clear();
						RunningGame.start();
					}
						
				});
			});
		client.send("ready");
		RunningGame.start();
		primaryStage.show();
	}

	public static void showAlertInform(String mess) throws Exception {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Inform Server");
		alert.setHeaderText("Server");
		alert.setContentText(mess);
		alert.show();
	}
}
