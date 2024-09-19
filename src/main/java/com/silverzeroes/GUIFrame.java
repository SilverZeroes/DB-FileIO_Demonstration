package com.silverzeroes;
import javax.swing.JFrame;

public class GUIFrame {
	public static void start() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setTitle("Database Authentication");
		frame.setSize(600, 400);
		frame.setResizable(false);
		
	
		frame.setVisible(true);
	}
}
