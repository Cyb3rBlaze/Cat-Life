package com.cyber.Main;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.cyber.GameUtils.SettingContainer;

public class Panel extends JPanel implements Runnable, KeyListener {
	//Creating vars used by panel to render graphics to the screen
	
		//Thread vars used to manage frame rate and game loop
	private Thread thread;
	private boolean isRunning;
	private int targetFPS;
			//For displaying purposes
	public static int displayFPS;
	
	//Game setting declaration
	private SettingContainer enviornment;
	
	/***************************************************************************/
	
	//Constructor to initialize vars
	public Panel() {
		//Initializing thread vars for game loop
		thread = new Thread(this);
		isRunning = true;
		targetFPS = 60;
			//Initializing display FPS
		displayFPS = 0;
		
		//Initializing game setting
		enviornment = new SettingContainer(4000, 800);
		
		//Beginning game loop
		thread.start();
		
		//Enabling Keylistener properties
		this.addKeyListener(this);
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
	}
	
	/***************************************************************************/
	
	//Run method part of Runnable class. Controls frame rate and renders screen
	@Override
	public void run() {
		//To displaying current frame rate
		long lastTime = System.currentTimeMillis();
		int currFPS = 0;
		
		//Game loop
		while(isRunning) {
			currFPS++;
			
			//Prints current FPS every second
			if(System.currentTimeMillis() - lastTime >= 1000) {
				System.out.println(currFPS);
				displayFPS = currFPS;
				lastTime = System.currentTimeMillis();
				currFPS = 0;
			}
			
			//Method used to control graphics rendering
			render();
			
			//Controls frame rate
			try {
				thread.sleep(1000 / targetFPS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	//Controls individual object rendering and paints to the screen every frame
	public void render() {
		//Rendering setting
		enviornment.render();
		
		repaint();
	}
	
	
	//Paint method paints graphics to screen
	public void paintComponent(Graphics g) {
		//Initializing graphics object used to paint to the screen
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		
		//Painting setting
		enviornment.paintComponent(g2);
		
		//Drawing FPS to screen
		Font fps = new Font("Ariel", 50, 50);
		g2.setFont(fps);
		g2.drawString("FPS: " + Integer.toString(displayFPS), 750, 70);
	}
	
	/***************************************************************************/
	
	//KeyListener methods gets user input to execute specific tasks
	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) {
		enviornment.keyPressed(e);
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		enviornment.keyReleased(e);
	}
}
