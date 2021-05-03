package Game;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import java.io.IOException;

import Server.GameClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;


/** 
 * The Login Controller class displays login scene and used to control login action and components. 
 * It validates, sends username, password from client to server and receives message from server to client. 
 * @param client: The parameter is to send data from client to server.
 * @param loginBtn: The parameter is to do login action.
 */
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

	@FXML
	void dogister(ActionEvent event) throws IOException {
		Main.moveScene(getClass(), event, "Register.fxml");
	}

	// Event Listener on Button[#loginBtn].onAction
	// send data(username, password) to server
	@FXML
	public void doLogin(ActionEvent event) {
		String name = userNameTf.getText();
		String pass = passTxt.getText();
		if (name.isEmpty() || pass.isEmpty()) {
			message.setTextFill(Color.RED);
			if (name.isEmpty())
				message.setText("Enter UserName!");
			else
				message.setText("Enter Password!");
		} else {
			client.send(name+","+pass);
		}

	}
	
	//when clients wrongly access, server will send a notification to client stored in variable "data" and display a message on login scene.
	public void setMessage(String data) {
		if (data.startsWith("Fail")) {
			
			Platform.runLater(() -> {
				message.setText(data);
				message.setTextFill(Color.RED);
			});
		}
		if (data.startsWith("Success")) {
			 Platform.runLater(() -> {
					message.setText(data);
					message.setTextFill(Color.web("#13ee1a"));
				});
		}
	}
	public void setClient(GameClient c) {
		this.client = c;
	}

}
