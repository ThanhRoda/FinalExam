package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import Server.SupperPlaneGame;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class LoginController {
	@FXML
	private Label loginID;
	@FXML
	private Label userLb;
	@FXML
	private TextField userNameTf;
	@FXML
	private Label passLb;
	@FXML
	private PasswordField passTxt;
	@FXML
	private Button loginBtn;
	@FXML
	private Label message;
	@FXML
	private Button register;

	@FXML
	void dogister(ActionEvent event) throws IOException {
		Main.moveScene(getClass(), event, "Register.fxml");
	}

	// Event Listener on Button[#loginBtn].onAction
	@FXML
	public void doLogin(ActionEvent event) {
		String name = userNameTf.getText();
		String pass = passTxt.getText();
		String rs = "";
		if (name.isEmpty() || pass.isEmpty()) {
			message.setTextFill(Color.RED);
			if (name.isEmpty())
				rs = "Enter UserName!";
			else
				rs = "Enter Password!";
		} else {
			rs = loginSql(name, pass, getClass(), event);
		}
		message.setText(rs);
		System.out.println(rs);
	}

	public String loginSql(String userName, String passwor, Class<?> c, ActionEvent event) {
		try {
			Connection conn = PlaySingleton.getInstance().getConnection();
			Statement stmt = conn.createStatement();
			String sql = "SELECT * FROM Players WHERE UserName ='" + userName + "'";
			ResultSet res = stmt.executeQuery(sql);
			String finalRes = "";
			if (res.next()) {
				String passRes = res.getString("Password").strip();
				if (passwor.compareTo(passRes) == 0) {
					message.setTextFill(Color.web("#13ee1a"));
					finalRes = "Login succeess";

					Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					stage.close();
					int score = res.getInt(3);
					PlaySingleton p = PlaySingleton.getInstance();
					p.setPlayer(new Player(userName, passRes, score));

					// join game
					SupperPlaneGame.gameScene(stage);;

				} else {
					finalRes = "Password incorrect";
					message.setTextFill(Color.RED);
				}

			} else {
				finalRes = "Invalid user name";
				message.setTextFill(Color.RED);

			}

			conn.close();
			return finalRes;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public void openRegister() throws Exception {
		// Create the FXMLLoader
		FXMLLoader loader = new FXMLLoader();
		// Path to the FXML File
		String fxmlDocPath = "src//application//Register.fxml";
		FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);
		// Create the Pane and all Details
		AnchorPane root = (AnchorPane) loader.load(fxmlStream);
		Scene secondScene = new Scene(root);
		Stage secondStage = new Stage();
		secondStage.setScene(secondScene); // set the scene
		secondStage.setTitle("Second Form");
		secondStage.show();

	}

}
