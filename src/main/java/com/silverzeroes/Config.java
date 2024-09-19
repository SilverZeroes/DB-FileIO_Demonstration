package com.silverzeroes;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;

public class Config {
	private static boolean useGUI = false;
	private static boolean useSQL = false;
	private static String SQLConnection = "localhost";
	private static int SQLPort = 3306;
	private static String SQLUsername = "root";
	private static String databaseName = "Records";
	private Config() {
		// Prevent instantiation.
	}
	
	public static String getSQLUsername() {
		return SQLUsername;
	}
	public static String getSQLConnection() {
		return SQLConnection;
	}
	public static int getSQLPort() {
		return SQLPort;
	}
	public static String getDatabaseName() {
		return databaseName;
	}
	public static boolean isUsingSQL() {
		return useSQL;
	}
	public static boolean isUsingGUI() {
		return useGUI;
	}
	
	// Check if the configuration file exists.
	// If the file does not exist, take input from the user to save the configurations.
	public static void init() {
		File configFile = new File(".config.txt");
		if (configFile.exists()) {
			readConfigs(configFile);
			return;
		}
		// Take the input, use SQL or GUI, or neither.
		System.out.println("This is the configuration stage, this step will only run once.");
		System.out.println("If you are having trouble with saving the configurations," 
		+ "you can override this process and procede into a CLI. Press '1' to override.");
		
		// Give the user the option to opt-out.
		Scanner input = new Scanner(System.in);
		if (input.nextInt() == 1) {
			input.close();
			return;
		}
		System.out.println("Would you like to use an SQL server to store your data, or just a file?" + 
				"\n1: Use an SQL server." + 
				"\n0: Do not use an SQL server.");
		
		useSQL = input.nextInt() == 1;
		if (useSQL) {
			System.out.print("Please enter the server's ip or type 0 to leave it at default (localhost): ");
			String prompt = input.next();
			if (!prompt.equals("0")) SQLConnection = prompt;
			
			System.out.print("Please enter the server's port (default 3306): ");
			prompt = input.next();
			if (!prompt.equals("0")) SQLPort = Integer.parseInt(prompt);
			
			System.out.print("Please enter your username (default root): ");
			prompt = input.next();
			if (!prompt.equals("0")) SQLUsername = prompt;
			

			System.out.print("Please enter the database's name: (default is 'Records'): ");
			prompt = input.next();
			if (!prompt.equals("0")) databaseName = prompt;
			
		}
		System.out.println("Would you like to have a graphical user interface?" + 
				"\n1: Use a GUI." + 
				"\n0: Do not use a GUI");
		
		useGUI = input.nextInt() == 1;
		
		System.out.println("Saving current settings. If you wish to change the settings, delete the config.txt file at: " 
		+ configFile.getAbsolutePath());
		writeConfigs(configFile);
		input.close();
	}
	private static void writeConfigs(File configFile) {
		try {
			FileWriter writer = new FileWriter(configFile);
			writer.write("SQL: " + useSQL 
			+ "\nConnection: " + SQLConnection 
			+ "\nPort: " + SQLPort
			+ "\nUsername: " + SQLUsername
			+ "\nDatabase: " + databaseName
			+ "\nGUI: " + useGUI);
			writer.close();
			System.out.println("Configurations saved.");
		} catch (IOException e) {
			System.out.println("An error occured! Assuming all options are unchecked.");
			useSQL = false;
			useGUI = false;
			e.printStackTrace();
		}
	}
	private static void readConfigs(File configFile) {
		try (BufferedReader configReader = new BufferedReader(new FileReader(configFile))) {
			for (String line; (line = configReader.readLine()) != null;) {
				String[] parts = line.trim().split("\\s+"); // Split by whitespace.
				if (parts.length == 2) {
					String key = parts[0];
					String value = parts[1];
					//System.out.println(key);
					switch(key) {
					case "SQL:":
						useSQL = value.equals("true");
						break;
					case "Connection:":
						SQLConnection = value;
						break;
					case "Username:":
						SQLUsername = value;
						break;
					case "Database:":
						databaseName = value;
						break;
					case "Port:":
						SQLPort = Integer.parseInt(value);
						break;
					case "GUI:":
						useGUI = value.equals("true");
						break;
					}
					/*
					if (key.equals("SQL:")) {
						useSQL = value.equals("true");
					} else if (key.equals("GUI:")) {
						useGUI = value.equals("true");
					} else if (key.equals("Connection:")) {
						SQLConnection = value;
					}
					*/
					
				}
			}
			
			
		} catch (IOException e) {
			System.out.println("Error reading configurations! defaulting to CLI");
			useSQL = false;
			useGUI = false;
			e.printStackTrace();
		}
		
		//System.out.println("SQL: " + useSQL);
		//System.out.println("GUI: " + useGUI);
		//System.out.println("Connection: " + SQLConnection);
	}
	
}
