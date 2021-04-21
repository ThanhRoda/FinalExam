package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class PlaySingleton {
	final String DS_URL = "jdbc:derby:PlaneDB";
	private Boolean play;
	private Player player;
	
	  private final static PlaySingleton INSTANCE = new PlaySingleton();
	  
	  private PlaySingleton() {
		 
	  }
	  
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DS_URL);
	}
	
	public Player getPlayer() {
		return player;
	}


	public void setPlayer(Player player) {
		this.player = player;
	}


	public Boolean getPlay() {
		return play;
	}

	public void setPlay(Boolean play) {
		this.play = play;
	}

	public static PlaySingleton getInstance() {
		return INSTANCE;
	}
	  
}
