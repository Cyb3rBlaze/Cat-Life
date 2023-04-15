package com.cyber.NN;

public class FFNN {
	//Creating the network object
	private Layer[] network;
	
	private int numParamsWeights;
	private int numParamsBiases;
	
	private int[] dimensions;
	
	/***************************************************************************/
	
	//Constructor
	public FFNN(int[] map) {
		//Initializing layers given a map of the NN architecture
		//Map is input, layer dims
		
		this.dimensions = map;
		
		//Just weights for now
		for(int i = 1; i < map.length; i++) {
			numParamsWeights += map[i-1]*map[i];
			numParamsBiases += map[i];
		}
		
		network = new Layer[map.length-1];
		for(int i = 0; i < network.length; i++) {
			if(i == network.length-1) {
				network[i] = new Layer(map[i], map[i+1], "sigmoid");
			}
			else {
				network[i] = new Layer(map[i], map[i+1], "relu");
			}
		}
	}
	
	/***************************************************************************/
	
	//Forward Propogation function
	public String forwardPropogate(double[] inputs) {
		for(int i = 0; i < network.length; i++) {
			inputs = network[i].forwardPropogateOneLayer(inputs);
		}
		
		//Producing softmax outputs from log counts
		double exponentiatedSum = 0;
		for(double input : inputs) {
			exponentiatedSum += Math.exp(input);
		}
		
		double[] probabilities = new double[inputs.length];
		for(int i = 0; i < probabilities.length; i++) {
			probabilities[i] = Math.exp(inputs[i]) / exponentiatedSum;
		}
		
		int largestValueIndex = 0;
		double currLargestVal = 0;
		for(int i = 0; i < probabilities.length; i++) {
			if(probabilities[i] > currLargestVal) {
				currLargestVal = probabilities[i];
				largestValueIndex = i;
			}
		}
		
		String[] actionSpace = {"jump", "moveRight", "moveLeft"};
		
		
		return actionSpace[largestValueIndex];
	}
	
	/***************************************************************************/
	
	//For genetic algorithm purposes
	public double[][][] getWeights(){
		double[][][] weights = new double[network.length][][];
		
		for(int i = 0; i < network.length; i++) {
			weights[i] = network[i].getWeights();
		}
		
		return weights;
	}
	
	public double[][] getBiases(){
		double[][] biases = new double[network.length][];
		
		for(int i = 0; i < network.length; i++) {
			biases[i] = network[i].getBiases();
		}
		
		return biases;
	}
	
	//Sets map based off of genetic algorithm output
	public void setMap(double[][][] weights, double[][] biases) {
		for(int i = 0; i < network.length; i++) {
			network[i].setMap(weights[i], biases[i]);
		}
	}
	
	public int getNumParamsWeights() {
		return numParamsWeights;
	}
	
	public int getNumParamsBiases() {
		return numParamsBiases;
	}
	
	public int[] getDimensions() {
		return dimensions;
	}
}
