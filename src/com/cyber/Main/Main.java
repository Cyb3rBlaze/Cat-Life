package com.cyber.Main;

import javax.swing.JFrame;

public class Main {
	//Initializing global window attributes
	public static int WIDTH = 1000;
	public static int HEIGHT = 800;
	
	/***************************************************************************/
	
	//Main Method
	public static void main(String[] args) {
		//Creating window object
		JFrame window = new JFrame();
		
		/***********************************************************************/
		
		//Initializing window attributes
		window.setSize(1000, 800);
		window.setLocationRelativeTo(null);
		window.setTitle("AP CSP Exam Create Task");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
			//Adding a panel object to the window
		window.add(new Panel());
		window.setVisible(true);
	}
}
