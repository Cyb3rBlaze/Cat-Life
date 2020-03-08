package com.cyber.GameUtils;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.cyber.Main.Main;
import com.cyber.NN.FFNN;

public class SettingContainer {
	//GameObject Declarations
		//Setting object
	private Setting setting;
		//Player object
	private Player controller;
	private Player[] users;
	private FFNN[] brain1;
	
	//Movement colliders
	private Rectangle2D left;
	private Rectangle2D right;
	
	//Field properties
	private int fieldWidth;
	private int fieldHeight;
	
	/***************************************************************************/
	
	public SettingContainer(int fieldWidth, int fieldHeight) {
		//Initializing player object
		try {
			this.fieldWidth = fieldWidth;
			this.fieldHeight = fieldHeight;
			
			users = new Player[20];
			controller = new Player(500, 200, 200, 200, ImageIO.read(new File("res/playerSpriteSheet.png")), true);
			brain1 = new FFNN[20];
			
			for(int i = 0; i < users.length; i++) {
				//Initializing NN object
				int[] dimensions = {5, 5, 5};
				brain1[i] = new FFNN(dimensions);
				users[i] = new Player(100, 200, 200, 200, ImageIO.read(new File("res/playerSpriteSheet.png")), false);
			}
			//Initializing setting
			int[] field = {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,
					2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,
					2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,
					2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,
					2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,
					2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,
					2, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 2,
					2, 2, 2, 2, 2, 2, 2, 0, 0, 2, 2, 2, 0, 0, 2, 2, 0, 2, 2, 2, 2, 0, 0, 2, 2, 2, 0, 2, 2, 0, 0, 2, 2, 2, 0, 0, 2, 2, 2, 2};
			
			setting = new Setting(field, ImageIO.read(new File("res/setting.png")), fieldWidth, fieldHeight, 40, 8, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Initializing Movement Colliders
		left = new Rectangle2D.Double(200, 0, 100, 800);
		right = new Rectangle2D.Double(600, 0, 100, 800);
	}
	
	/***************************************************************************/
	
	//Updates Setting properties every frame
	public void render() {
		//Rendering player properties
		for(int i = 0; i < users.length; i++) {
			users[i].render();
		}
		//Rendering setting properties
		setting.render();
		
		//Brain Movement
		for(int j = 0; j < users.length; j++) {
			double x = 100;
			for(int i = 0; i < setting.actualCollision.length; i++) {
				if(setting.actualCollision[i].getX() < x && Math.abs(users[j].actualY - setting.actualCollision[i].getY()) < 100) {
					x = setting.actualCollision[i].getX();
				}
			}
			double[] arr = {x};
			if(brain1[j].forwardPropogate(arr) == "jump") {
				users[j].jump();
			}
			else {
				users[j].right();
			}
		}
		controller.render();
	}
	
	//Updates Setting graphics every frame
	public void paintComponent(Graphics2D g2) {
		//Painting background image
		try {
			g2.drawImage(ImageIO.read(new File("res/background.png")), 0, 0, Main.WIDTH, Main.HEIGHT, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Painting player to window
		for(int i = 0; i < users.length; i++) {
			users[i].paintComponent(g2);
		}
		
		controller.paintComponent(g2);
		//Painting setting to window
		setting.paintComponent(g2);
		
		//Collision detection
		for(int j = 0; j < users.length; j++) {
			for(int i = 0; i < setting.getFieldColliders().length; i++) {
				if(users[j].borders()[0].intersects(setting.getFieldColliders()[i])) {
					users[j].setVelY(4);
					users[j].setStopped(false);
				}
				if(users[j].borders()[1].intersects(setting.getFieldColliders()[i])) {
					users[j].setX(users[j].getX()-2);
				}
				if(users[j].borders()[2].intersects(setting.getFieldColliders()[i])) {
					users[j].setVelY(0);
					users[j].setJumped(false);
					if(users[j].getStopped()) {
						users[j].setCurrMove(1);
					}
				}
				if(users[j].borders()[3].intersects(setting.getFieldColliders()[i])) {
					users[j].setX(users[j].getX()+2);
				}
			}
		}
		
		if(controller.borders[3].intersects(left)) {
			if(setting.getParentX() < 0) {
				controller.setX(controller.getX()+2);
				setting.setParentX(setting.getParentX()+2);
				for(int i = 0; i < users.length; i++) {
					users[i].setX(users[i].getX()+2);
				}
			}
		}
		else if(controller.borders[1].intersects(right)) {
			if(setting.getParentX() > -fieldWidth+1000) {
				controller.setX(controller.getX()-2);
				setting.setParentX(setting.getParentX()-2);
				for(int i = 0; i < users.length; i++) {
					users[i].setX(users[i].getX()-2);
				}
			}
		}
		
		for(int i = 0; i < setting.getFieldColliders().length; i++) {
			if(controller.borders()[0].intersects(setting.getFieldColliders()[i])) {
				controller.setVelY(4);
				controller.setStopped(false);
			}
			if(controller.borders()[1].intersects(setting.getFieldColliders()[i])) {
				controller.setX(controller.getX()-2);
			}
			if(controller.borders()[2].intersects(setting.getFieldColliders()[i])) {
				controller.setVelY(0);
				controller.setJumped(false);
				if(controller.getStopped()) {
					controller.setCurrMove(1);
				}
			}
			if(controller.borders()[3].intersects(setting.getFieldColliders()[i])) {
				controller.setX(controller.getX()+2);
			}
		}
	}
	
	/***************************************************************************/
	
	//KeyListener methods
	public void keyPressed(KeyEvent e) {
		for(int i = 0; i < users.length; i++) {
			users[i].keyPressed(e);
		}
		controller.keyPressed(e);
	}
	
	public void keyReleased(KeyEvent e) {
		for(int i = 0; i < users.length; i++) {
			users[i].keyReleased(e);
		}
		controller.keyReleased(e);
	}
}
