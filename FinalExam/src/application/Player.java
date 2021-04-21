package application;

public class Player {
	private String userName;
	private String password;
	private int score;
	
	public Player(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
		this.score = 0;
	}
	
	public Player(String userName, String password, int score) {
		super();
		this.userName = userName;
		this.password = password;
		this.score = score;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return userName + "\t \t Score: " + score ;
	}
	
	
}