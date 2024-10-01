package com.silverzeroes;
// Author: Abdullah Al-Hasani
// This code can be found on my github: https://github.com/SilverZeroes/DB-Frontend-Demo/tree/main

public class Main {

	public static void main(String[] args) {
		Config.init(); // Initializes some settings, only runs once.
		
		if (Config.isUsingGUI()) {
			System.out.println("Entering GUI Mode...");
			GUIFrame.start();
		} else {
			System.out.println("Entering CLI Mode...");
			if (Config.isUsingSQL())
				CLIPrompt.startSQLServer();
			else
				CLIPrompt.startFileRegistry();

		}

	}

}
