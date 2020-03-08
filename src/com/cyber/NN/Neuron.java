package com.cyber.NN;

import java.util.Random;

public class Neuron {
	//Creating weight and bias object for individual neuron
	private double weight;
	private double bias;
	
	/***************************************************************************/
	
	//Constructor
	public Neuron() {
		//Randomly initialize weights and biases 
		Random rg = new Random();
		weight = rg.nextFloat();
		bias = rg.nextFloat();
		
		if(rg.nextInt()%2 ==0) {weight = -weight;}
		if(rg.nextInt()%2 ==0) {bias = -bias;}
	}
	
	/***************************************************************************/
	
	//Activation function to feed data through the network
	public double activationFunction(String type, double[] inputs) {
		double output = 0;
		
		//Linear combining
		for(int i = 0; i < inputs.length; i++) {
			output += weight*inputs[i];
		}
		
		output += bias;
		
		//Activation function that the user can choose from
		if(type=="relu") {
			if(output < 0) {
				output = 0;
			}
		}
		else if(type =="sigmoid") {
			output = (1/( 1 + Math.pow(Math.E,(-1*output))));
		}
		
		return output;
	}
	
	/***************************************************************************/
	
	//Genetic algorithim trains the neural network
	public void replaceWeightAndBias(double weight, double bias) {
		this.weight = weight;
		this.bias = bias;
	}
	
	/***************************************************************************/
	
	//For genetic algorithm purposes
	public double[] getMap() {
		double[] output = {weight, bias};
		
		return output;
	}
	
		//Sets the weight and biases the genetic algorithm prefers
	public void setMap(double[] map) {
		weight = map[0];
		bias = map[1];
	}
}
