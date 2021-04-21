package Final;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.application.Platform;

public class GameServer {
	static boolean gameStatus = false;
	static int numberClients = 0;
	static List<Socket> listSockets;
	 

	public static void main(String[] args) throws IOException {
		
		listSockets = new ArrayList<>();
		try (ServerSocket s = new ServerSocket(6666)) {

			while (true) {
				Socket incoming = s.accept();
				listSockets.add(incoming);
				Runnable conn = () -> {
					try (Scanner scanner = new Scanner(incoming.getInputStream(), "UTF-8");
							PrintWriter outPrinter = new PrintWriter(
									new OutputStreamWriter(incoming.getOutputStream(), "UTF-8"), true);) {
						boolean done = false;
						outPrinter.println("Helo");
						while (!done && scanner.hasNext()) {
							String inMes = scanner.nextLine();
							listSockets.removeIf(x -> x.isClosed());
							listSockets.stream().filter(x -> x != incoming).forEach(x -> {
								PrintWriter outPrinter1;
								try {
									outPrinter1 = new PrintWriter(new OutputStreamWriter(x.getOutputStream(), "UTF-8"),
											true);
									outPrinter1.println(inMes);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							});
							;
							if (inMes.trim().equals("BYE")) {
								done = true;
							}
						}
					}
//						numberClients ++;
//						if (numberClients == 2)
//							gameStatus = true;
//						
//						InputStream in = incoming.getInputStream();
//						OutputStream out = incoming.getOutputStream();
//						DataOutputStream dataOutputStream = new DataOutputStream(out);
//				         	dataOutputStream.writeBoolean(gameStatus);
//				         	dataOutputStream.flush();
//				         DataInputStream input = new DataInputStream(in);
					// System.out.println(input.readInt());
					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				};
				new Thread(conn).start();
			}
		} catch (

		IOException e1) {
			e1.printStackTrace();
		}

	}
}
