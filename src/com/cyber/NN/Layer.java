package com.cyber.NN;

public class Layer {
	private Neuron[] layer;
	private String type;
	
	/***************************************************************************/
	
	//Constructor
	public Layer(int numberOfNeurons, String type) {
		//Initializing parameters for the given layer
		layer = new Neuron[numberOfNeurons];
		for(int i = 0; i < layer.length; i++) {
			layer[i] = new Neuron();
		}
		
		//Specifying activation function
		this.type = type;
	}
	
	/***************************************************************************/
	
	//Function forward propogates through one layer of the NN 
	public double[] forwardPropogateOneLayer(double[] inputs) {
		double[] outputs = new double[layer.length];
		
		for(int i = 0; i < layer.length; i++) {
			outputs[i] = layer[i].activationFunction(type, inputs);
		}
		
		return outputs;
	}
	
	/***************************************************************************/
	
	//Replaces weights and biases during network training
	public void replaceWeightsAndBiases(double[] weights, double[] biases) {
		for(int i = 0; i < layer.length; i++) {
			layer[i].replaceWeightAndBias(weights[i], biases[i]);
		}
	}
	
	/***************************************************************************/
	
	//For the genetic algorithm
	public double[][] getMap() {
		double[][] map = new double[layer.length][];
		
		for(int i = 0; i < map.length; i++) {
			map[i] = layer[i].getMap();
		}
		
		return map;
	}
		
		//Sets preferred map based off of genetic algorithm output
	public void setMap(double[][] map) {
		for(int i = 0; i < map.length; i++) {
			layer[i].setMap(map[i]);
		}
	}
}
