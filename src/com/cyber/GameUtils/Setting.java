package com.cyber.GameUtils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;

public class Setting {
	//Allows developer to quickly customize playing area
	public Rectangle2D[] fieldCollision;
	public Rectangle2D[] actualCollision;
	private BufferedImage[] images;
		//For player movement purposes
	private double parentX;
	private double parentY;
	
	/***************************************************************************/
	
	public Setting(int[] field, BufferedImage tileSheet, int fieldWidth, int fieldHeight, int arrayWidth, int arrayHeight, int collider) {
		//Initializing setting
		parentX = 0;
		parentY = 0;
		//Initializing field colliders
		int k = 0;
		int count = 0;
		for(int i = 0; i < field.length; i++) {
			if(i != 0 && i % arrayWidth == 0) {
				k +=1;
			}
			if(field[i] == collider || field[i] == collider+1) {
				if(count != 0) {
					Rectangle2D[] currFieldCollision = fieldCollision.clone();
					fieldCollision = new Rectangle2D[currFieldCollision.length+1];
					BufferedImage[] tempImages = images.clone();
					images = new BufferedImage[images.length + 1];
					for(int j = 0; j < tempImages.length; j++) {
						images[j] = tempImages[j];
					}
					
					for(int j = 0; j < fieldCollision.length; j++) {
						if(j != fieldCollision.length-1) {
							fieldCollision[j] = currFieldCollision[j];
						}
						else {
							fieldCollision[j] = new Rectangle2D.Double(parentX + (i%arrayWidth)*(fieldWidth/arrayWidth), parentY + k*(fieldHeight/arrayHeight), fieldWidth/arrayWidth, fieldHeight/arrayHeight);
						}
					}
				}
				else {
					fieldCollision = new Rectangle2D[1];
					images = new BufferedImage[1];
					fieldCollision[0] = new Rectangle2D.Double(parentX + (i%arrayWidth)*(fieldWidth/arrayWidth), parentY + k*(fieldHeight/arrayHeight), fieldWidth/arrayWidth, fieldHeight/arrayHeight);
					count += 1;
				}
				
				//For drawing images to screen
				if(field[i] == collider) {
					images[images.length-1] = tileSheet.getSubimage(96, 128, 32, 32);
				}
				else if(field[i] == collider+1) {
					images[images.length-1] = tileSheet.getSubimage(96, 160, 32, 32);
				}
			}
			
			//Actual collision rectangles
			actualCollision = fieldCollision.clone();
		}
	}
	
	/***************************************************************************/
	
	//Updates Setting properties every frame
	public void render() {
	}
	
	//Updates Setting graphics every frame
	public void paintComponent(Graphics2D g2) {
		g2.setColor(Color.DARK_GRAY);
		for(int i = 0; i < fieldCollision.length; i++) {
			actualCollision[i] = new Rectangle2D.Double(fieldCollision[i].getX()+parentX, fieldCollision[i].getY()+parentY, fieldCollision[i].getWidth(), fieldCollision[i].getHeight());
			g2.fill(actualCollision[i]);
			g2.drawImage(images[i], (int) (fieldCollision[i].getX() + parentX), (int) (fieldCollision[i].getY() + parentY), (int) fieldCollision[i].getWidth(), (int) fieldCollision[i].getHeight(), null);
		}
	}
	
	/***************************************************************************/
	
	//Getters and Setters
	public Rectangle2D[] getFieldColliders() {
		return actualCollision;
	}

	public void setParentX(double parentX) {
		this.parentX = parentX;
	}

	public void setParentY(double parentY) {
		this.parentY = parentY;
	}

	public double getParentX() {
		return parentX;
	}

	public double getParentY() {
		return parentY;
	}
}
