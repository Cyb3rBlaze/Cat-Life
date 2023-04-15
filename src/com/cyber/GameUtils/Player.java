package com.cyber.GameUtils;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Timer;

public class Player extends GameObject {
	//Custom GameObject properties
	private double velX;
	private double velY;
	private boolean jumped = false;
	private boolean stopped = false;
	private boolean left = false;
	private boolean dead = false;
	
	public boolean GAenabled = false;
	
	private boolean isPlayer;
	
	protected Rectangle2D front;
	protected Rectangle2D bottom;
	
	/***************************************************************************/
	
	public Player(int x, int y, int width, int height, BufferedImage spriteSheet, boolean isPlayer) {
		this.setX(x);
		this.setY(y);
		this.setWidth(width);
		this.setHeight(height);
		
		//Initializing player sprite properties
		this.spriteSheet = spriteSheet;
		timer = new Timer();
		spriteIndex = 0;
		int[] tempSpriteIndexLimits = {3, 7, 7, 9, 8, 6, 5, 7, 12, 9, 11, 5, 7, 7, 7, 5};
		spriteIndexLimits = tempSpriteIndexLimits;
		spriteIndex = 0;
		spriteWidth = 64;
		spriteHeight = 64;
		currMove = 0;
		prevMove = 0;
		actualWidth = 60;
		actualHeight = 90;
		actualX = x;
		actualY = y;
		currImage = spriteSheet.getSubimage(spriteIndex * spriteWidth, currMove * spriteHeight, spriteWidth, spriteHeight);
		
		scheduleTimer();
		
		//Initializing colliders
		initializeColliders();
		
		this.isPlayer = isPlayer;
		
		front = new Rectangle2D.Double(x+90, y+78, 200, 20);
		bottom = new Rectangle2D.Double(x+90, y+78, 20, 200);
	}
	
	/***************************************************************************/
	
	//Updates Player properties every frame
	public void render() {
		if(!dead) {
			this.setX(this.getX()+velX);
			this.setY(this.getY()+velY);
			actualX = actualX + velX;
			actualY = actualY + velY;
			if(velY <= 4) {
				velY += 0.1;
			}
			initializeColliders();
			initializeInputDetectors();
		}
		else {
			velY = 0;
		}
	}
	
	//Updates Player graphics every frame
	public void paintComponent(Graphics2D g2) {
		if(this.left) {
			BufferedImage rotated = flipImage(currImage);
			g2.drawImage(rotated, (int) this.getX(), (int) this.getY(), (int) this.getWidth(), (int) this.getHeight(), null);
		}
		else {
			g2.drawImage(currImage, (int) this.getX(), (int) this.getY(), (int) this.getWidth(), (int) this.getHeight(), null);
		}
	}
	
	private void initializeInputDetectors() {
		front = new Rectangle2D.Double(x+120, y+95, 200, 20);
		bottom = new Rectangle2D.Double(x+120, y+95, 20, 200);
	}
	
	/***************************************************************************/
	
	//Collision detection
	public void setVelX(double velX) {
		this.velX = velX;
	}
	
	public void setVelY(double velY) {
		this.velY = velY;
	}
	
	public void setJumped(boolean jumped) {
		this.jumped = jumped;
	}
	
	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}
	
	public boolean getStopped() {
		return stopped;
	}
	
	public void setCurrMove(int move) {
		this.currMove = move;
	}
	
	public void setIsDead(boolean dead) {
		this.dead = dead;
	}
	
	public boolean getDead() {
		return dead;
	}
	
	/***************************************************************************/
	
	//Returns borders for collision detection
	public Rectangle2D[] borders() {
		return borders;
	}
	
	/***************************************************************************/
	
	//KeyListener methods
	public void keyPressed(KeyEvent e) {
		if(isPlayer) {
			if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				this.left();
			}
			else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				this.right();
			}
			else if(e.getKeyCode() == KeyEvent.VK_UP) {
				this.jump();
			}
			else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				
			}
		}
	}
	
	public void keyReleased(KeyEvent e) {
		if(isPlayer) {
			if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				velX = 0;
				currMove = 0;
				setStopped(false);
				left = false;
			}
			else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				velX = 0;
				currMove = 0;
				setStopped(false);
			}
			else if(e.getKeyCode() == KeyEvent.VK_UP) {
				
			}
			else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				
			}
		}
	}
	
	/***************************************************************************/
	
	//Flips image
	public static BufferedImage flipImage(BufferedImage img) {
        BufferedImage rotated = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for(int x = 0; x < img.getWidth(); x++) {
        	for(int y = 0; y < img.getHeight(); y++) {
        		rotated.setRGB(img.getWidth()-1-x, y, img.getRGB(x, y));
        	}
        }

        return rotated;
    }
	
	/***************************************************************************/
	
	//Genetic algorithm functions
	public void right() {
		velX = 2;
		if(jumped == false) {
			currMove = 1;
			setStopped(true);
		}
		left = false;
	}
	
	public void left() {
		velX = -2;
		if(jumped == false) {
			currMove = 1;
			setStopped(true);
		}
		left = true;
	}
	
	public void jump() {
		if(!jumped) {
			velY = -6;
			jumped = true;
			currMove = 0;
		}
	}
}
