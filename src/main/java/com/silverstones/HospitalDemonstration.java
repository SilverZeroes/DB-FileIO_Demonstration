package com.silverstones;
// Author: Abdullah Al-Hasani
// This code can be found on my github: [not available yet] https://github.com/SilverZeroes?tab=repositories

public class HospitalDemonstration {

	public static void main(String[] args) {
		Config.init(); // Initializes some settings, only runs once.
		
		if (Config.isUsingGUI()) {
			System.out.println("Entering GUI Mode...");
			// Implement a Graphical User Interface
		} else {
			System.out.println("Entering CLI Mode...");
			if (Config.isUsingSQL()) {
				DatabaseHandler.connect();
			}
			
		}

	}

}
