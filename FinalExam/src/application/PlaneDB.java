package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PlaneDB {
	public static void main(String[] args) {
		// Create a named constant for the URL.
	      // NOTE: This value is specific for Java DB.
	      final String DB_URL = "jdbc:derby:PlaneDB;create=true";
	      
	      try
	      {
	         // Create a connection to the database.
	         Connection conn =
	                DriverManager.getConnection(DB_URL);
						 
				// If the DB already exists, drop the tables.
				dropTables(conn);
				
				// Build the User table.
				buildUserTable(conn);
				

	         // Close the connection.
	         conn.close();
	      }
	      catch (Exception ex)
	      {
	         System.out.println("ERROR: " + ex.getMessage());
	      }
	}
	
	public static void dropTables(Connection conn)	{
		System.out.println("Checking for existing tables.");
		
		try
		{
			// Get a Statement object.
			Statement stmt  = conn.createStatement();;

			try
			{
	         // Drop the UnpaidOrder table.
	         stmt.execute("DROP TABLE Players");
				System.out.println("Players table dropped.");
			}
			catch(SQLException ex)
			{
				// No need to report an error.
				// The table simply did not exist.
			}

		}
  		catch(SQLException ex)
		{
	      System.out.println("ERROR: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	public static void buildUserTable(Connection conn) {
		try	{
         // Get a Statement object.
         Statement stmt = conn.createStatement();
         
			// Create the table.
			stmt.execute("CREATE TABLE Players (" +
   				       "UserName VARCHAR(25) NOT NULL PRIMARY KEY, " +
                      "Password VARCHAR(25), " +
   				       " Score INT )");
							 
			// Insert row #1.
			stmt.execute("INSERT INTO Players VALUES ( " +
                      "'Thanhpro', " +
                      "'123', " +
                      "100)");
			// Insert row #2.
			stmt.execute("INSERT INTO Players VALUES ( " +
			                      "'Thanhdeptrai', " +
			                      "'123', " +
                    "200)");
			// Insert row #3.
			stmt.execute("INSERT INTO Players VALUES ( " +
						   "'Thanh'," +
						    "'aaa', 300)");
			System.out.println("Players table created.");
		}
		catch (SQLException ex) {
			  System.out.println("ERROR: " + ex.getMessage());
		 }
	}
}
