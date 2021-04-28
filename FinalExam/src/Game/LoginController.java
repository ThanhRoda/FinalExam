package Game;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.sun.tools.attach.AgentInitializationException;

import Server.GameClient;
import Server.SupperPlaneGame;
import javafx.application.Platform;
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

	private GameClient client;

	private final String serverName = "localhost";
	private final int port = 6666;

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
			loginSql(name, pass, getClass(), event);
		}

	}

	public void loginSql(String userName, String passwor, Class<?> c, ActionEvent event) {
		client = new GameClient(serverName, port, data -> {
			if (data.startsWith("Fail")) {
				
				Platform.runLater(() -> {
					message.setText(data);
					message.setTextFill(Color.RED);
				});
			}
			else if (data.startsWith("Success")) {
				 Platform.runLater(() -> {
						message.setText(data);
						message.setTextFill(Color.web("#13ee1a"));
					});
				
				//join game
					Platform.runLater(() -> {
						try {
						Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
						stage.close();
						SupperPlaneGame.gameScene(stage, client.getSocket());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            });
					
			}
		});
		client.connectSocket();
		client.send(userName+","+passwor);

}

}
