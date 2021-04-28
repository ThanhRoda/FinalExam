package Server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.scene.paint.Color;

public class GameServer {
	final static String DS_URL = "jdbc:derby:PlaneDB";
	static boolean gameStatus = false;
	static int numberClients = 0;
	static boolean login = false;
	static List<Socket> listSockets;
	

	public static void main(String[] args) throws IOException {

		listSockets = new ArrayList<>();
		try (ServerSocket s = new ServerSocket(6666)) {

			while (true) {
				Socket incoming = s.accept();
				numberClients++;
				listSockets.add(incoming);
				listSockets.removeIf(x -> x.isClosed());
				System.out.println(listSockets.size());
				Runnable conn = () -> {
					try (Scanner scanner = new Scanner(incoming.getInputStream(), "UTF-8");
							PrintWriter outPrinter = new PrintWriter(
									new OutputStreamWriter(incoming.getOutputStream(), "UTF-8"), true);) {
						boolean done = false;
						 outPrinter.println("Helo");
						// check login
						 login = false;
						while(!login && scanner.hasNext() ) {
							String[] player = scanner.nextLine().strip().split(",");
							Connection connect;
							try {
								connect = DriverManager.getConnection(DS_URL);
								Statement stmt = connect.createStatement();
								String sql = "SELECT * FROM Players WHERE UserName ='" + player[0].strip() + "'";
								ResultSet res = stmt.executeQuery(sql);
								if (res.next()) {
									String passRes = res.getString("Password").strip();
									if (player[1].strip().compareTo(passRes) == 0) {
										outPrinter.println("Success");
										login = true;
									} else {
										outPrinter.println("Fail! Password incorrect");
									}

								} else
									outPrinter.println("Fail! Invalid user name");

								connect.close();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
							
						
						

							

						
						// check number players
//						listSockets.removeIf(x -> x.isClosed());
//						if (listSockets.size() == 3) {
//							listSockets.stream().forEach(x -> {
//								PrintWriter outPrint;
//								try {
//									outPrint = new PrintWriter(new OutputStreamWriter(x.getOutputStream(), "UTF-8"),
//											true);
//									outPrint.println("true");
//								} catch (IOException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
//							});
//						} else if (listSockets.size() < 3) {
//							listSockets.stream().forEach(x -> {
//								PrintWriter outPrint;
//								try {
//									outPrint = new PrintWriter(new OutputStreamWriter(x.getOutputStream(), "UTF-8"),
//											true);
//									outPrint.println("false");
//
//								} catch (IOException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
//							});
//						} else {
//							outPrinter.println("Enough Players!");
//							incoming.close();
//						}
//						if (incoming.isClosed()) {
//							listSockets.remove(incoming);
//							numberClients--;
//						}
						if (login) {
							while (!done && scanner.hasNext()) {
								String inMes = scanner.nextLine();

								listSockets.removeIf(x -> x.isClosed());
								listSockets.stream().filter(x -> x != incoming).forEach(x -> {
									PrintWriter outPrinter1;
									try {
										outPrinter1 = new PrintWriter(
												new OutputStreamWriter(x.getOutputStream(), "UTF-8"), true);
										outPrinter1.println(inMes);

									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								});
								
								if (inMes.trim().equals("BYE")) {
									done = true;
									incoming.close();
								}
							}
						}

					}

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
