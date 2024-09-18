package com.silverstones;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
public class DatabaseHandler {
	
	public static void connect() {
		Connection connection = null;
		String uname = Config.getSQLUsername();
		String address = Config.getSQLConnection();
		int port = Config.getSQLPort();
		String databaseName = Config.getDatabaseName();
		
		final String URL = "jdbc:mysql://" + address + ":" + port + "/" + databaseName;
		System.out.println(URL);
		
		try 
		{
			try
			{
				Class.forName("com.mysql.cj.jdbc.Driver");		// Check the pom.xml file: line 9-14.
			} catch (ClassNotFoundException e) { e.printStackTrace(); }
			String passwd;
			Scanner input = new Scanner(System.in);
			System.out.print("Enter the database's password (this will not be saved): ");
			passwd = input.next();
			connection = DriverManager.getConnection(URL, uname, passwd);
			System.out.println("Connection Successful! You can execute any queries here now. Type 'exit' to log out.");
			String prompt = "";
			while (!prompt.equals("exit")) {
				System.out.println("SQL> ");
				prompt = input.next();
				
				executeQuery(connection, prompt);
			}
			
		} catch (SQLException f) { 
			f.printStackTrace(); System.exit(1);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException g) {
					g.printStackTrace();
				}
			}
		}
	}
	
	private static void executeQuery(Connection connection, String query) throws SQLException {
		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {
			System.out.println(query);
			int columns = resultSet.getMetaData().getColumnCount();
			for (int i = 1; i <= columns; i++) {
				System.out.println(resultSet.getString(i) + '\t');
			}
			System.out.println();
		}
		
	}
	
}

