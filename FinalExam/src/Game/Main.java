package Game;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

/**
 * <h1>Space Battle Game</h1>
 * The Space Battle program implements an JavaFX application simply displays applications
 *  which can simulate a battle game in which its users can play in pairs or a group 
 *  of three and use the keyboard to control fighting planes to fire at each other. 
 * @author RodaThanh, ETanTai, KhangLai
 * @version Closed-Beta
 * @since 2021-05-04
 *
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root, 400, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static void moveScene(Class<?> c, ActionEvent e, String path) throws IOException {
		Parent root = FXMLLoader.load(c.getResource(path));
		Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.show();
	}

	public void moveScene1(Stage stage) {
	
	}
}
