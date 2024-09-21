package com.silverzeroes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseHandler {
	private static Connection connection;
	private static final String URI = "jdbc:mysql://" + Config.getSQLConnection() + ":" + Config.getSQLPort() + "/" + Config.getDatabaseName();
	private static final String userName = Config.getSQLUsername();
	
	public static void connect(String password) throws SQLException {
			connection = DriverManager.getConnection(URI, userName, password);
	}
	
	// Methods executing only SELECT statements. Displays Rows and Columns.
	public static ResultSet executeSelectQuery(String query) throws SQLException {
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(query);
		return resultSet;
	}

	// Performs all other CRUD operations, cannot produce result sets
	public static void executeUpdateQuery(String query) throws SQLException {
		
		if (connection == null) {
			System.out.println("Connection is closed!");
			return;
		}
		
		Statement statement = connection.createStatement();
		statement.executeUpdate(query);
		
	}
	
	// List all tables.
	public static ResultSet displayAllTables() throws SQLException {
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SHOW TABLES");
		return resultSet;
	}
	
	public static void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
			} catch(SQLException e) {
				e.printStackTrace();
			} finally {
				connection = null;
			}
		}
	}
	
	public static Connection getConnection() {
		return connection;
	}
	
}

