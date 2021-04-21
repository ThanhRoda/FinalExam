package application;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class ResultLayoutController {
	@FXML
	private Label userName;
	@FXML
	private Label socer;
	@FXML
	private ListView<Player> top10;
	@FXML
	private Button playGainBtn;
	@FXML
	private Button exitBtn;
	
	@FXML
	public void playAgain(ActionEvent event) {
		Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
		stage.close();
		Player p = PlaySingleton.getInstance().getPlayer();
		//p.setScore(0);
		SupperPlaneGame.gameScene(stage, p);
	}
	// Event Listener on Button[#exitBtn].onAction
	@FXML
	public void doExit(ActionEvent event) {
		Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
		stage.close();
	}
	
	
	public void setUserName(String userName) {
		this.userName.setText(userName);
	}
	public void setSocer(int socer) {
		this.socer.setText(String.valueOf(socer));;
	}
	public void getTopTen() throws SQLException {
		ObservableList<Player> players =  FXCollections.observableArrayList();
		
		//FXCollections.observableArrayList(Roles.values());
		Connection conn = PlaySingleton.getInstance().getConnection();
		System.out.println("Connected");
		Statement stmt = conn.createStatement();
		String sql = "SELECT  * FROM Players ORDER BY Score DESC" ;
		ResultSet res = stmt.executeQuery(sql);
		while(res.next()) {
			Player  p = new Player(res.getString(1),res.getString(2), res.getInt(3));
			players.add(p);
			if(players.size() == 10) {
				break;
			}
		}
		top10.setItems(players);
		conn.close();
	}
	public void savePlayer(Player p) throws SQLException {
		Connection conn = PlaySingleton.getInstance().getConnection();
		System.out.println("Connected");
		Statement stmt = conn.createStatement();
		String sql = "UPDATE Players "
				+ "SET Score= " + p.getScore() + "WHERE UserName = '"+ p.getUserName()+"'" ;
		stmt.executeUpdate(sql);
		conn.close();
	}
}
