package Server;

import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.Consumer;

public class GameClient {
	private String IPAddress;
	private int port;
	private Socket socket;
	private Consumer<String> handle;
	private PrintWriter out;
	
	public GameClient(String iPAddress, int port, Consumer<String> handle) {
		super();
		this.IPAddress = iPAddress;
		this.port = port;
		this.handle = handle;
	}
	
	public void seHandle(Consumer<String> han) {
		this.handle = han;
	}
	
	public void connectSocket() {
		try {
			this.socket = new Socket();
			this.socket.connect(new InetSocketAddress(this.IPAddress, this.port), 1500);
			Scanner sc = new Scanner(this.socket.getInputStream());
			this.out = new PrintWriter(this.socket.getOutputStream(), true);
			new Thread(() -> {
				while(sc.hasNext()) {
					String data = sc.nextLine();
					this.handle.accept(data);
				}
					
			}).start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void send(String mes) {
		this.out.println(mes);
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
}
