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
	static boolean isLogin;
	static String socketValue = "";
	static PrintWriter outPrinter;
	static AnimationTimer RunningGame;
	static Socket socket;
	private final String serverName = "localhost";
	private final int port = 6666;
	static GameClient client;
	static int clientNumber;
	static BaseLayout mainPlayer;

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
			if (data.startsWith("Success"))
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

		mainPlayer = new BaseLayout("/player2.png");
		mainPlayer.position.set(-100, -100);

		RunningGame = new AnimationTimer() {

			@Override
			public void handle(long now) {

				if (keyPressList.contains("UP")) {
					if (mainPlayer.position.y - 10 > 10)
						mainPlayer.position.set(mainPlayer.position.x, mainPlayer.position.y - 10);
					client.send("Cor " + String.valueOf(mainPlayer.position.y) + " " + clientNumber);
				}
				if (socketValue.startsWith("Cor")) {
					String[] data = socketValue.strip().split(" ");
					int temp = Integer.parseInt(data[2]);
					switch (temp) {
					case 0:
						plane.position.set(plane.position.x, Double.parseDouble(data[1]));
						break;
					case 1:
						plane2.position.set(plane2.position.x, Double.parseDouble(data[1]));
						break;
					case 2:
						plane3.position.set(plane3.position.x, Double.parseDouble(data[1]));
						break;
					}
				}

				if (keyPressList.contains("DOWN")) {

					if (mainPlayer.position.y + 10 < 590)
						mainPlayer.position.set(mainPlayer.position.x, mainPlayer.position.y + 10);
					client.send("Cor " + String.valueOf(mainPlayer.position.y) + " " + clientNumber);
				}
				if (socketValue.startsWith("SPACE")) {
					int temp = Integer.parseInt(socketValue.strip().split(" ")[1]);
					switch (temp) {
					case 0:
						BaseLayout gun = new BaseLayout("/rocket.png");
						gun.position.set(plane.position.x - plane.image.getWidth() / 2.0, plane.position.y);
						gun.speedMovement.setLength(300);
						gunList.add(gun);
						break;
					case 1:
						BaseLayout gun2 = new BaseLayout("/rocket2.png");
						gun2.position.set(plane2.position.x - plane2.image.getWidth() / 2.0, plane2.position.y);
						gun2.speedMovement.setLength(300);
						gunList2.add(gun2);
						break;
					case 2:
						BaseLayout gun3 = new BaseLayout("/rocket2.png");
						gun3.position.set(plane3.position.x - plane3.image.getWidth() / 2.0, plane3.position.y);
						gun3.speedMovement.setLength(300);
						gunList3.add(gun3);
						break;
					}
					socketValue = "noThing";

				}
				if (keyJustPressList.contains("SPACE")) {
					client.send(String.valueOf("SPACE ") + clientNumber);
					BaseLayout gun;
					if(clientNumber == 0 ) {
						gun = new BaseLayout("/rocket.png");
					}
						
					else 
						gun = new BaseLayout("/rocket2.png");
					gun.position.set(mainPlayer.position.x - mainPlayer.image.getWidth() / 2.0, mainPlayer.position.y);
					gun.speedMovement.setLength(300);
					switch (clientNumber) {
					case 0:
						gunList.add(gun);
						break;
					case 1:
						gunList2.add(gun);
						break;
					case 2:
						gunList3.add(gun);
						break;
					}

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
				for (int j = 0; j < gunList3.size(); j++) {
					BaseLayout gun = gunList3.get(j);
					gun.moveBackward(1 / 60.0);
					if (gun.position.x < 0)
						gunList3.remove(gun);
				}
				
				
				switch (clientNumber) {
				case 0:
					// main plane = plane1
					for (int j = 0; j < gunList3.size(); j++) {
						BaseLayout gun = gunList3.get(j);
						if (gun.isOverLap(mainPlayer)) {
							mainPlayer.exlosing = true;
							gunList3.remove(gun);
						}
					}
					for (int j = 0; j < gunList2.size(); j++) {
						BaseLayout gun = gunList2.get(j);
						if (gun.isOverLap(mainPlayer)) {
							mainPlayer.exlosing = true;
							gunList2.remove(gun);
						}
					}
					
					// plane2 checking
					for (int j = 0; j < gunList.size(); j++) {
						BaseLayout gun = gunList.get(j);
						if (gun.isOverLap(plane2)) {
							plane2.exlosing = true;
							gunList.remove(gun);
						}
					}
					// plane3 checking
					for (int j = 0; j < gunList.size(); j++) {
						BaseLayout gun = gunList.get(j);
						if (gun.isOverLap(plane3)) {
							plane3.exlosing = true;
							gunList.remove(gun);
						}
					}
					// plane2 vs plane3 checking overlap
					if (plane3.isOverLap(plane2)) {
						plane2.exlosing = true;
						plane3.exlosing = true;
					}
					break;
				case 1:
					
					// plane2 checking
					for (int j = 0; j < gunList.size(); j++) {
						BaseLayout gun = gunList.get(j);
						if (gun.isOverLap(mainPlayer)) {
							mainPlayer.exlosing = true;
							gunList.remove(gun);
						}
					}
					// plane3 checking
					for (int j = 0; j < gunList.size(); j++) {
						BaseLayout gun = gunList.get(j);
						if (gun.isOverLap(plane3)) {
							plane3.exlosing = true;
							gunList.remove(gun);
						}
					}
					//  plane1
					for (int j = 0; j < gunList3.size(); j++) {
						BaseLayout gun = gunList3.get(j);
						if (gun.isOverLap(plane)) {
							plane.exlosing = true;
							gunList3.remove(gun);
						}
					}
					for (int j = 0; j < gunList2.size(); j++) {
						BaseLayout gun = gunList2.get(j);
						if (gun.isOverLap(plane)) {
							plane.exlosing = true;
							gunList2.remove(gun);
						}
					}
					// plane2 vs plane3 checking overlap
					if (mainPlayer.isOverLap(plane3)) {
						mainPlayer.exlosing = true;
						plane3.exlosing = true;
					}
					break;
				case 2:
					// plane2 checking
					for (int j = 0; j < gunList.size(); j++) {
						BaseLayout gun = gunList.get(j);
						if (gun.isOverLap(mainPlayer)) {
							mainPlayer.exlosing = true;
							gunList.remove(gun);
						}
					}
					// plane3 checking
					for (int j = 0; j < gunList.size(); j++) {
						BaseLayout gun = gunList.get(j);
						if (gun.isOverLap(plane2)) {
							plane2.exlosing = true;
							gunList.remove(gun);
						}
					}
					//  plane1
					for (int j = 0; j < gunList3.size(); j++) {
						BaseLayout gun = gunList3.get(j);
						if (gun.isOverLap(plane)) {
							plane.exlosing = true;
							gunList3.remove(gun);
						}
					}
					for (int j = 0; j < gunList2.size(); j++) {
						BaseLayout gun = gunList2.get(j);
						if (gun.isOverLap(plane)) {
							plane.exlosing = true;
							gunList2.remove(gun);
						}
					}
					// plane2 vs plane3 checking overlap
					if (mainPlayer.isOverLap(plane2)) {
						mainPlayer.exlosing = true;
						plane2.exlosing = true;
					}
					break;
				}

				background.render(context);
				mainPlayer.update();
				mainPlayer.render(context);
				plane.update();
				plane.render(context);
				plane2.update();
				plane2.render(context);
				plane3.update();
				plane3.render(context);

				context.setFill(Color.WHITE);
				context.setStroke(Color.GREEN);
				context.setFont(new Font("Arial Black", 30));

				if (socketValue.startsWith("waiting")) {
					String textUser = "Matching ...";
					context.fillText(textUser, 450, 300);
					context.strokeText(textUser, 450, 300);
					stop();
				}
				if (socketValue.startsWith("start")) {
					socketValue = "nothing";
					String textUser = "Start Game";
					context.fillText(textUser, 450, 300);
					context.strokeText(textUser, 450, 300);
				}
				if (socketValue.startsWith("result")) {
					String result = socketValue.split(",")[1];
					context.fillText(result, 450, 300);
					context.strokeText(result, 450, 300);
					stop();
				}
				if (plane.destroy || plane2.destroy || plane3.destroy || mainPlayer.destroy) {
					if (plane2.destroy || plane3.destroy)
						client.send("dead 1");
					else if (plane.destroy) {
						client.send("dead 0" );
					} else 
						client.send("dead "+ clientNumber );
					//stop();
				}
				String textUser = String.valueOf(clientNumber);
				context.fillText(textUser, 450, 100);
				context.strokeText(textUser, 450, 100);
				for (BaseLayout gun : gunList) {
					gun.render(context);
				}
				for (BaseLayout gun : gunList2) {
					gun.render(context);
				}
				for (BaseLayout gun : gunList3) {
					gun.render(context);
				}

			}
		};
		client.seHandle(data -> {
			socketValue = data;
			Platform.runLater(() -> {
				if (data.startsWith("waiting")) {
					clientNumber = Integer.parseInt(data.strip().split(" ")[1]);
					mainPlayer = chooseMainplayer(clientNumber);
				}
				if (data.startsWith("start")) {
					RunningGame.start();
					switch (clientNumber) {
			        case 0:
			        	plane.position.set(-100, -100);
			            break;
			        case 1:
			        	plane2.position.set(-100, -100);
			            break;
			        case 2:
			        	plane3.position.set(-100, -100); 
			            break;
					}
					
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

	public static BaseLayout chooseMainplayer(int number) {
		System.out.println("NUmber: " + number);
		switch (number) {
		case 0:
			BaseLayout plane = new BaseLayout("/player.png");
			plane.position.set(100, 300);
			return plane;
		case 1:
			BaseLayout plane2 = new BaseLayout("/player2.png");
			plane2.position.set(900, 150);
			return plane2;
		case 2:
			BaseLayout plane3 = new BaseLayout("/player2.png");
			plane3.position.set(900, 450);
			return plane3;
		default:
			BaseLayout plane4 = new BaseLayout("/player2.png");
			plane4.position.set(900, 450);
			return plane4;
		}
	}
}
