package com.cyber.NN;

import java.util.Random;

public class Neuron {
	//Creating weight and bias object for individual neuron
	private double[] weights;
	private double bias;
	
	/***************************************************************************/
	
	//Constructor
	public Neuron(int inputSize) {
		//Randomly initialize weights and biases 
		Random rg = new Random();
		
		weights = new double[inputSize];
		
		for(int i = 0; i < inputSize; i++) {
			weights[i] = rg.nextFloat();
			if(rg.nextInt()%2 ==0) {weights[i] = -weights[i];}
		}
		
		bias = rg.nextFloat();
		
		if(rg.nextInt()%2 ==0) {bias = -bias;}
	}
	
	/***************************************************************************/
	
	//Activation function to feed data through the network
	public double activationFunction(String type, double[] inputs) {
		double output = 0;
		
		//Linear combining
		for(int i = 0; i < inputs.length; i++) {
			output += weights[i]*inputs[i];
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
	public void replaceWeightAndBias(double weights[], double bias) {
		this.weights = weights;
		this.bias = bias;
	}
	
	/***************************************************************************/
	
	//For genetic algorithm purposes
	public double[] getWeights() {
		return weights;
	}
	
	public double getBias() {
		return bias;
	}
	
	//Sets the weight and biases the genetic algorithm prefers
	public void setMap(double[] weights, double bias) {
		this.weights = weights;
		this.bias = bias;
	}
}