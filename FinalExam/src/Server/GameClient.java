package Server;

import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * The GameClient class is to receive IP address, port of the server to connect via sockets,
 * and handle method is to accept the data from the server and do things in orders.
 * @param IPaddress: The parameter is to receive the server's IP address to connect.
 * @param port: The parameter is to receive the server's using port.
 * @param handle: The parameter is a string array to handle actions by strings.
 * @param out: The parameter is to write data to send back to the server.
 */
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
	public GameClient(String iPAddress, int port) {
		super();
		this.IPAddress = iPAddress;
		this.port = port;
	}
	
	public void setHandle(Consumer<String> han) {
		this.handle = han;
	}
	
	// connect to the server via sockets with the IP address and port.
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
