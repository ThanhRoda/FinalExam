package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class RegisterController {
	@FXML
	private Label loginID;
	@FXML
	private Label userLb;
	@FXML
	private TextField userNameTf;
	@FXML
	private Label passLb;
	@FXML
    private PasswordField passTxt;;
	@FXML
	private Button loginBtn;
	@FXML
	private Label message;

	// Event Listener on Button[#loginBtn].onAction
	@FXML
	public void doRegister(ActionEvent event) {
		 String name = userNameTf.getText();
			String pass = passTxt.getText();
			String rs = "";
			if( name.isEmpty() || pass.isEmpty() ) {
				message.setTextFill(Color.RED);
				if(name.isEmpty())
					rs = "Enter UserName!";
				else 
					rs = "Enter Password!";
			}
			else {
				insertSql(name, pass, event);
			}
			message.setText(rs);
	}
	public void insertSql(String userName, String passwor, ActionEvent event) {
		try {
			Connection conn = PlaySingleton.getInstance().getConnection();
			System.out.println("Connected");
			Statement stmt = conn.createStatement();
			String testUsersql = "SELECT Password FROM Players WHERE UserName ='"+userName+"'" ;
			ResultSet ress = stmt.executeQuery(testUsersql);
			if(ress.next()) {
				showAlertError( "User already exist");
			}
			String sql = "INSERT INTO Players VALUES ( '" +
					   userName + "','" + passwor +
					    "', 0)";
			int res = stmt.executeUpdate(sql);
			if(res == 1) {
				showAlertInform("Registered success", event);
			}
			else 
				showAlertError("Registered False");
			conn.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	public void showAlertInform(String mess, ActionEvent event) throws Exception {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Inform");
		alert.setHeaderText("Inform");
		alert.setContentText(mess);
		Optional<ButtonType> option = alert.showAndWait();
		if(option.get() == alert.getButtonTypes().get(0)) {
			openLogin(event);
		}
			
	}
	public void showAlertError(String mess) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("Error");
		alert.setContentText(mess);
		 alert.showAndWait();
	}
	public void openLogin(ActionEvent e) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Stage secondStage = new Stage();
        secondStage.setScene(new Scene(root)); // set the scene
        secondStage.setTitle("TooPin");
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        stage.close();
        secondStage.show();
	}
}
