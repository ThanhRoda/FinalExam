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

	public void setMessage(String data) {
		if (data.startsWith("Fail")) {
			
			Platform.runLater(() -> {
				message.setText(data);
				message.setTextFill(Color.RED);
			});
		}
		if (data.startsWith("Success")) {
			//this.client.send("ready");
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
