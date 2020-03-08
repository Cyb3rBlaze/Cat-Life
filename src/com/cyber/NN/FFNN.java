package com.cyber.NN;

public class FFNN {
	//Creating the network object
	private Layer[] network;
	
	/***************************************************************************/
	
	//Constructor
	public FFNN(int[] map) {
		//Initializing layers given a map of the NN architecture
		network = new Layer[map.length];
		for(int i = 0; i < network.length; i++) {
			if(i == network.length-1) {
				network[i] = new Layer(map[i], "sigmoid");
			}
			else {
				network[i] = new Layer(map[i], "relu");
			}
		}
	}
	
	/***************************************************************************/
	
	//Forward Propogation function
	public String forwardPropogate(double[] inputs) {
		for(int i = 0; i < network.length; i++) {
			inputs = network[i].forwardPropogateOneLayer(inputs);
		}
		
		//Map must have the last layer contain one neuron for accurate propogation
		if(inputs[0] > 0.5) {
			return "jump";
		}
		else {
			return "move";
		}
	}
	
	/***************************************************************************/
	
	//For genetic algorithm purposes
	public double[][][] getMap(){
		double[][][] map = new double[network.length][][];
		
		for(int i = 0; i < network.length; i++) {
			map[i] = network[i].getMap();
		}
		
		return map;
	}
	
		//Sets map based off of genetic algorithm output
	public void setMap(double[][][] map) {
		for(int i = 0; i < network.length; i++) {
			network[i].setMap(map[i]);
		}
	}
}
