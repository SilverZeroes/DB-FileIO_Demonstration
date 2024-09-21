package com.silverzeroes;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.SQLException;
import java.sql.ResultSet;


public class CLIPrompt {
	public static void startFileRegistry() {
		List<List<String>> data = new ArrayList<>();
		
		Scanner input = new Scanner(System.in);
		
		String prompt;
		while (true) {
			System.out.print("FS> ");
			prompt = input.nextLine();
			if (prompt.equalsIgnoreCase("exit")) break;
			else if (prompt.equalsIgnoreCase("add")) {
				System.out.print("Enter first name: ");
				String firstName = input.nextLine();
				System.out.print("Enter last name: ");
				String lastName = input.nextLine();
				System.out.print("Enter role: ");
				String role = input.nextLine();
				
				
			}
			else if (prompt.equalsIgnoreCase("read")) {
				// Read from the file
			}
		}
		
		input.close();
		
	}
	
	public static void startSQLServer() {
			String password;
			Scanner input = new Scanner(System.in);
			System.out.print("Enter the database's password: ");
			password = input.nextLine();
			try {
				DatabaseHandler.connect(password);
				System.out.println("Connection Successful! You can execute any queries here now. Type 'exit' to log out.");
				String prompt;
				while (true) {
					System.out.print("SQL> ");
					prompt = input.nextLine();
					if (prompt.equalsIgnoreCase("exit")) break;
					
					// If the command is a get command, use the get function, else use the update function.
					if (prompt.trim().toUpperCase().startsWith("SELECT")) {
						ResultSet selection = DatabaseHandler.executeSelectQuery(prompt);
						displaySelection(selection);
					}
					else if (prompt.trim().toUpperCase().startsWith("LIST")) {
						ResultSet tables = DatabaseHandler.displayAllTables();
						while (tables.next()) {
							String tableName = tables.getString(1);
							System.out.println(tableName);
							ResultSet selection = DatabaseHandler.executeSelectQuery( "SELECT * FROM " + tableName);
							displaySelection(selection);
							System.out.println();
						}
					}
					else  {
						DatabaseHandler.executeUpdateQuery(prompt);
					}
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DatabaseHandler.closeConnection();
			}
			input.close();
		}
	
	private static void displaySelection(ResultSet selection) throws SQLException {
		if (!selection.isBeforeFirst()) {
			System.out.println("No results found!");
		}
		int columns = selection.getMetaData().getColumnCount();
		for (int i = 1; i <= columns; i++) {
			System.out.printf("%-20s", selection.getMetaData().getColumnName(i));
		}
		System.out.println();
		while (selection.next()) {
			for (int i = 1; i <= columns; i++) {
				System.out.printf("%-20s", selection.getString(i));
			}
			System.out.println();
		}
	}
}
