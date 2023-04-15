package com.cyber.GameUtils;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.cyber.Main.Main;
import com.cyber.NN.FFNN;
import com.cyber.NN.GeneticAlgorithm;

public class SettingContainer {
	//GameObject Declarations
	//Setting object
	private Setting setting;
	//Player object
	private Player controller;
	private Player[] users;
	private FFNN[] brains;
	
	//Movement colliders
	private Rectangle2D left;
	private Rectangle2D right;
	
	//Field properties
	private int fieldWidth;
	private int fieldHeight;
	
	//Counter to check user movement
	int counter = 0;
	
	//Reward system to choose optimal mates to breed
	double[] rewards;
	
	//Population size
	int populationSize = 30;
	
	//Generation stats
	int generation = 0;
	long generationStartTime;
	final int timeout = 20000;
	
	/***************************************************************************/
	
	public SettingContainer(int fieldWidth, int fieldHeight) {
		this.fieldWidth = fieldWidth;
		this.fieldHeight = fieldHeight;
		
		resetEnviornment();
	}
	
	/***************************************************************************/
	
	private void resetEnviornment() {
		//Initializing player object
		try {
			users = new Player[populationSize];
			//controller = new Player(500, 200, 200, 200, ImageIO.read(new File("res/playerSpriteSheet.png")), true);
			rewards = new double[populationSize];
			
			if(generation == 0) {
				brains = new FFNN[populationSize];
			} else {
				brains = generateNewPopulation();
			}
			
			for(int i = 0; i < users.length; i++) {
				//Initializing NN object
				int[] dimensions = {2, 10, 10, 3};
				if(generation == 0) {
					brains[i] = new FFNN(dimensions);
				}
				users[i] = new Player(150, 200, 200, 200, ImageIO.read(new File("res/playerSpriteSheet.png")), false);
			}
			//Initializing setting
			int[] field = {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,
					2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,
					2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,
					2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,
					2, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,
					2, 0, 0, 0, 1, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2,
					2, 1, 1, 1, 2, 2, 2, 0, 0, 1, 1, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 2,
					2, 2, 2, 2, 2, 2, 2, 0, 0, 2, 2, 2, 0, 0, 2, 2, 0, 2, 2, 2, 2, 0, 0, 2, 2, 2, 0, 2, 2, 0, 0, 2, 2, 2, 0, 0, 2, 2, 2, 2};
			
			setting = new Setting(field, ImageIO.read(new File("res/setting.png")), fieldWidth, fieldHeight, 40, 8, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Initializing Movement Colliders
		left = new Rectangle2D.Double(200, 0, 100, 800);
		right = new Rectangle2D.Double(600, 0, 100, 800);
		
		//Generation stats reset
		generation++;
		generationStartTime = System.currentTimeMillis();
	}
	
	/***************************************************************************/
	
	private FFNN[] generateNewPopulation() {
		int[] fittestIndividuals = GeneticAlgorithm.selectFittestIndividuals(rewards);
		
		return GeneticAlgorithm.breedFittestIndividuals(brains[fittestIndividuals[0]], brains[fittestIndividuals[1]], populationSize);
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
			if (users[j].getDead() != true) {
				//Artificially high starting values
				double frontDistance = 1000;
				double bottomDistance = 1000;
				for(int i = 0; i < setting.actualCollision.length; i++) {
					//In view frame + finding user distance
					double distanceX = Math.abs(users[j].actualX - setting.actualCollision[i].getX());
					double distanceY = Math.abs(users[j].actualY - setting.actualCollision[i].getY());
					if(setting.actualCollision[i].intersects(users[j].front) && distanceX < frontDistance) {
						frontDistance = distanceX;
					}
					if(setting.actualCollision[i].intersects(users[j].bottom) && distanceY < bottomDistance) {
						bottomDistance = distanceY;
					}
				}
				double[] arr = {frontDistance, bottomDistance};
				String userAction = brains[j].forwardPropogate(arr);
				if(userAction == "jump") {
					users[j].jump();
				} else if(userAction == "moveRight") {
					users[j].right();
				} else {
					users[j].left();
				}
				
				if(users[j].getY() > 800) {
					users[j].setIsDead(true);
				}
			}
			
			//Setting x distance covered as reward mechanism
			rewards[j] = users[j].actualX;
		}
		
		//Checking all dead to repopulate and restart simulation
		boolean allDead = true;
		for(int i = 0; i < users.length; i ++) {
			if(users[i].getDead() == false) {
				allDead = false;
			}
		}
		
		//Timeout after 20 seconds
		if(System.currentTimeMillis() - generationStartTime > timeout) {
			allDead = true;
		}
		
		if(allDead) {
			resetEnviornment();
		}
		
//		controller.render();
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
		
//		controller.paintComponent(g2);
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
					users[j].setActualX(users[j].actualX-2);
					users[j].setVelX(0);
				}
				if(users[j].borders()[2].intersects(setting.getFieldColliders()[i])) {
					users[j].setVelY(0);
					users[j].setY(users[j].getY()-0.5);
					users[j].setJumped(false);
					if(users[j].getStopped()) {
						users[j].setCurrMove(1);
					}
				}
				if(users[j].borders()[3].intersects(setting.getFieldColliders()[i])) {
					users[j].setX(users[j].getX()+2);
					users[j].setActualX(users[j].actualX+2);
					users[j].setVelX(0);
				}
			}
		}
		
		//Seperate left and right setting container collision
		boolean checkLeft = true;
		for(int i = 0; i < users.length; i++) {
			if(users[i].borders[1].intersects(right)) {
				if(setting.getParentX() > -fieldWidth+1000) {
					checkLeft = false;
					counter = 0;
					setting.setParentX(setting.getParentX()-2);
					for(int j = 0; j < users.length; j++) {
						users[j].setX(users[j].getX()-2);
					}
					break;
				}
			}
		}
		
		counter++;
		
		if(checkLeft && counter > 120) {
			for(int i = 0; i < users.length; i++) {
				if(users[i].borders[3].intersects(left)) {
					if(setting.getParentX() < 0) {
						setting.setParentX(setting.getParentX()+2);
						for(int j = 0; j < users.length; j++) {
							users[j].setX(users[j].getX()+2);
						}
					}
				}
			}
		}
		
//		if(controller.borders[3].intersects(left)) {
//			if(setting.getParentX() < 0) {
//				controller.setX(controller.getX()+2);
//				setting.setParentX(setting.getParentX()+2);
//				for(int i = 0; i < users.length; i++) {
//					users[i].setX(users[i].getX()+2);
//				}
//			}
//		}
//		else if(controller.borders[1].intersects(right)) {
//			if(setting.getParentX() > -fieldWidth+1000) {
//				controller.setX(controller.getX()-2);
//				setting.setParentX(setting.getParentX()-2);
//				for(int i = 0; i < users.length; i++) {
//					users[i].setX(users[i].getX()-2);
//				}
//			}
//		}
//		
//		for(int i = 0; i < setting.getFieldColliders().length; i++) {
//			if(controller.borders()[0].intersects(setting.getFieldColliders()[i])) {
//				controller.setVelY(4);
//				controller.setStopped(false);
//			}
//			if(controller.borders()[1].intersects(setting.getFieldColliders()[i])) {
//				controller.setX(controller.getX()-2);
//			}
//			if(controller.borders()[2].intersects(setting.getFieldColliders()[i])) {
//				controller.setVelY(0);
//				controller.setJumped(false);
//				if(controller.getStopped()) {
//					controller.setCurrMove(1);
//				}
//			}
//			if(controller.borders()[3].intersects(setting.getFieldColliders()[i])) {
//				controller.setX(controller.getX()+2);
//			}
//		}
		
		//Drawing FPS to screen
		Font fps = new Font("Ariel", 20, 20);
		g2.setFont(fps);
		g2.drawString("Generation: " + Integer.toString(generation), 830, 90);
		g2.drawString("Time left: " + Integer.toString((int)((timeout-(System.currentTimeMillis()-generationStartTime)) / 1000)), 830, 120);
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
