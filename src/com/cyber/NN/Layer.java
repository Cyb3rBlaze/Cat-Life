package com.cyber.NN;

public class Layer {
	private Neuron[] layer;
	private String type;
	
	/***************************************************************************/
	
	//Constructor
	public Layer(int inputSize, int numberOfNeurons, String type) {
		layer = new Neuron[numberOfNeurons];
		
		//Initializing parameters for the given layer
		layer = new Neuron[numberOfNeurons];
		for(int i = 0; i < layer.length; i++) {
			layer[i] = new Neuron(inputSize);
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
	public void replaceWeightsAndBiases(double[][] weights, double[] biases) {
		for(int i = 0; i < layer.length; i++) {
			layer[i].replaceWeightAndBias(weights[i], biases[i]);
		}
	}
	
	/***************************************************************************/
	
	//For the genetic algorithm
	public double[][] getWeights() {
		double[][] weights = new double[layer.length][];
		
		for(int i = 0; i < weights.length; i++) {
			weights[i] = layer[i].getWeights();
		}
		
		return weights;
	}
	
	public double[] getBiases() {
		double[] biases = new double[layer.length];
		
		for(int i = 0; i < biases.length; i++) {
			biases[i] = layer[i].getBias();
		}
		
		return biases;
	}
		
	//Sets preferred map based off of genetic algorithm output
	public void setMap(double[][] weights, double[] biases) {
		for(int i = 0; i < layer.length; i++) {
			layer[i].setMap(weights[i], biases[i]);
		}
	}
}
