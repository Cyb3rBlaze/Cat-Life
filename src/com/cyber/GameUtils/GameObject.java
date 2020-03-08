package com.cyber.GameUtils;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class GameObject {
	//Basic GameObject properties that all GameObjects will inherit
	
		//Location and size properties
	private double x = 0;
	private double y = 0;
	private int width = 0;
	private int height = 0;
	
		//Sprite Properties	
	protected BufferedImage spriteSheet;
	protected BufferedImage currImage;
	protected Timer timer;
	protected int spriteIndex;
	protected int[] spriteIndexLimits;
	protected int spriteWidth;
	protected int spriteHeight;
	protected int actualWidth;
	protected int actualHeight;
	protected double actualX;
	protected double actualY;
	protected int currMove;
	protected int prevMove;
	
		//Collision Detection
	protected Rectangle2D[] borders;
	
	/***************************************************************************/
	
	//Scheduling Timer for sprite animations
	protected void scheduleTimer() {
		
		timer.scheduleAtFixedRate(new TimerTask() {
			  @Override
			  public void run() {
				  //Getting the current sprite limit to iterate through spritesheet sprites
				  int currSpriteLimit = spriteIndexLimits[currMove];
				  //Resets the sprite index if the move has changed
				  if(getMoveChanged()) {spriteIndex = 0;}
				  //Updating current sprite from current frame
				  currImage = spriteSheet.getSubimage(spriteIndex * spriteWidth, currMove * spriteHeight, spriteWidth, spriteHeight);
				  //Updating iterating vars
				  spriteIndex += 1;
				  if(spriteIndex > currSpriteLimit-1) {spriteIndex = 0;}
			  }
			}, 
		100, 100);
	}
	
	//Method used to check if the move is changed to update current sprite action
	private boolean getMoveChanged() {
		if(currMove != prevMove) {
			prevMove = currMove;
			return true;
		}
		return false;
	}
	
	/***************************************************************************/
	
	protected void initializeColliders() {
		borders = new Rectangle2D[4];
		borders[0] = new Rectangle2D.Double(actualX+20, actualY, actualWidth-40, 20);
		borders[1] = new Rectangle2D.Double(actualX+actualWidth-20, actualY+20, 20, actualHeight-40);
		borders[2] = new Rectangle2D.Double(actualX+20, actualY+actualHeight-20, actualWidth-40, 20);
		borders[3] = new Rectangle2D.Double(actualX, actualY+20, 20, actualHeight-40);
	}
	
	/***************************************************************************/
	
	//Getters and Setters
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
		//Used to make custom spritesheet limitations for each GameObject
	public void setSpriteIndexLimit(int[] limits) {
		this.spriteIndexLimits = limits;
	}
}
