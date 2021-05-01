package Game;

public  class GameSingleton {
	private int clientNumber;
	
	private final static GameSingleton INSTANCE = new GameSingleton();
	
	private GameSingleton() {
		
	}

	public int getClientNumber() {
		return clientNumber;
	}

	public void setClientNumber(int clientNumber) {
		this.clientNumber = clientNumber;
	}

	public static GameSingleton getInstance() {
		return INSTANCE;
	}
	
	
	
}
