package com.silverzeroes;
// Author: Abdullah Al-Hasani
// This code can be found on my github: https://github.com/SilverZeroes/HospitalDemonstration

public class Main {

	public static void main(String[] args) {
		Config.init(); // Initializes some settings, only runs once.
		
		if (Config.isUsingGUI()) {
			System.out.println("Entering GUI Mode...");
			// Implement a Graphical User Interface
			GUIFrame.start();
		} else {
			System.out.println("Entering CLI Mode...");
			if (Config.isUsingSQL()) {
				DatabaseHandler.connect();
			}
			
		}

	}

}
