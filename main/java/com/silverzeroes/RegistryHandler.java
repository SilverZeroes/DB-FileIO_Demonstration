package com.silverzeroes;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RegistryHandler {
	
	// Write data to file
	public static void writeData(List<List<String>> data) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(".data.txt" , true))) {
			for (List<String> row: data) {
				writer.write(String.join(",", row));	// Join columns with a comma.
				writer.newLine();						// Add a new line after each row.
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	// Read data from the file.
	public static List<List<String>> readData() {
		File file = new File(".data.txt");
		if (!file.exists()) {
			System.out.println("File does not exist!");
			return new ArrayList<>();
		}
		List<List<String>> dataList = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(".data.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] columns = line.split(","); 	// Split by comma
				List<String> row = new ArrayList<>();
				for (String column : columns) {
					row.add(column.trim());				// Trim Whitespace
				}
				dataList.add(row);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dataList;
		
	}
	
}
