package com.silverzeroes;

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
			/*
			try
			{
				Class.forName("com.mysql.cj.jdbc.Driver");		// Check the pom.xml file: line 9-14.
			} catch (ClassNotFoundException e) { e.printStackTrace(); }
			*/
			String passwd;
			Scanner input = new Scanner(System.in);
			System.out.print("Enter the database's password (this will not be saved): ");
			passwd = input.nextLine();
			connection = DriverManager.getConnection(URL, uname, passwd);
			System.out.println("Connection Successful! You can execute any queries here now. Type 'exit' to log out.");
			String prompt = "";
			while (true) {
				System.out.print("SQL> ");
				prompt = input.nextLine();
				if (prompt.equalsIgnoreCase("exit")) break;
				
				// If the command is a get command, use the get function, else use the update function.
				if (prompt.trim().toUpperCase().startsWith("SELECT")) executeSelectQuery(connection, prompt);
				else if (prompt.trim().toUpperCase().startsWith("LIST")) displayAllTables(connection);
				else executeUpdateQuery(connection, prompt);
			}
			input.close();
			
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
	
	// Methods executing only SELECT statements. Displays Rows and Columns.
	private static void executeSelectQuery(Connection connection, String query) throws SQLException {
		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {
			
			if (!resultSet.isBeforeFirst()) {
				System.out.println("No results found.");
				return;
			}
			
			int columns = resultSet.getMetaData().getColumnCount();
			for (int i = 1; i <= columns; i++) {
				System.out.printf("%-20s", resultSet.getMetaData().getColumnName(i));
			}
			System.out.println();
			while (resultSet.next()) {
				for (int i = 1; i <= columns; i++) {
					System.out.printf("%-20s", resultSet.getString(i));
				}
				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Performs all other CRUD operations, cannot produce result sets
	private static void executeUpdateQuery(Connection connection, String query) throws SQLException {
		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(query);
		} catch (SQLException e) {e.printStackTrace();}
	}
	
	// List all tables.
	private static void displayAllTables(Connection connection) {
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SHOW TABLES");
			
			while (resultSet.next()) {
				String tableName = resultSet.getString(1);
				System.out.println(tableName);
				executeSelectQuery(connection, "SELECT * FROM " + tableName);
				System.out.println();
			}
			
		} catch (SQLException e) {e.printStackTrace();}
	}
	
}

