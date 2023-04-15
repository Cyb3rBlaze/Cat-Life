package com.cyber.NN;

import java.util.Random;

public class GeneticAlgorithm {
	static Random rg = new Random();
	
	/***************************************************************************/
	
	public static int[] selectFittestIndividuals(double[] rewards) {
		//Indices of fittest individuals
		int individual1 = 1;
		int individual2 = 0;
		double individual1Reward = 0;
		double individual2Reward = 0;
		
		for(int i = 0; i < rewards.length; i++) {
			if(rewards[i] > individual1Reward) {
				individual2Reward = individual1Reward;
				individual1Reward = rewards[i];
				
				individual2 = individual1;
				individual1 = i;
			}
		}
		
		int[] fittestIndividuals = {individual1, individual2};
		
		return fittestIndividuals;
	}
	
	/***************************************************************************/
	
	public static FFNN[] breedFittestIndividuals(FFNN fittest, FFNN secondFittest, int populationSize) {
		double[][][] weights1 = fittest.getWeights();
		double[][][] weights2 = secondFittest.getWeights();
		double[][] biases1 = fittest.getBiases();
		double[][] biases2 = secondFittest.getBiases();
		
		FFNN[] population = new FFNN[populationSize];
		
		population[0] = fittest;
		population[1] = secondFittest;
		
		for(int i = 2; i < population.length; i++) {
			population[i] = breedOneChild(fittest.getNumParamsWeights(), fittest.getNumParamsBiases(), weights1, weights2, biases1, biases2, fittest.getDimensions());
		}
		
		return population;
	}
	
	/***************************************************************************/
	
	private static FFNN breedOneChild(int numParamsWeights, int numParamsBiases, double[][][] weights1, double[][][] weights2, double[][] biases1, double[][] biases2, int[] dimensions) {
		double[][][] childWeights = weights1.clone();
		double[][] childBiases = biases1.clone();
		
		int splitIndexWeights = rg.nextInt(numParamsWeights);
		int splitIndexBiases = rg.nextInt(numParamsBiases);
		
		int paramCounter = 0;
		
		//Sexual reproduction
		for(int i = 0; i < weights1.length; i++) {
			for(int j = 0; j < weights1[i].length; j++) {
				for(int k = 0; k < weights1[i][j].length; k++) {
					if(paramCounter >= splitIndexWeights) {
						childWeights[i][j][k] = weights1[i][j][k];
					} else {
						childWeights[i][j][k] = weights2[i][j][k];
					}
					
					paramCounter++;
				}
			}
		}
		
		paramCounter = 0;
		
		for(int i = 0; i < biases1.length; i++) {
			for(int j = 0; j < biases1[i].length; j++) {
				if(paramCounter >= splitIndexBiases) {
					childBiases[i][j] = biases1[i][j];
				} else {
					childBiases[i][j] = biases2[i][j];
				}
				
				paramCounter++;
			}
			
		}
		
		//Random perturbations to expand action space - 5% random perturbations
		for(int a = 0; a < Math.ceil(numParamsWeights * 0.05); a++) {
			paramCounter = 0;
			
			int indexToChange = rg.nextInt(numParamsWeights);
			
			for(int i = 0; i < weights1.length; i++) {
				for(int j = 0; j < weights1[i].length; j++) {
					for(int k = 0; k < weights1[i][j].length; k++) {
						double newParamValue = rg.nextFloat();
						if(rg.nextInt()%2 ==0) {newParamValue = -newParamValue;}
						
						if(paramCounter == indexToChange) {
							childWeights[i][j][k] = newParamValue;
						}
						
						paramCounter++;
					}
				}
			}
			
			paramCounter = 0;
			indexToChange = rg.nextInt(numParamsBiases);
			
			for(int i = 0; i < biases1.length; i++) {
				for(int j = 0; j < biases1[i].length; j++) {
					double newParamValue = rg.nextFloat();
					if(rg.nextInt()%2 ==0) {newParamValue = -newParamValue;}
					
					if(paramCounter == indexToChange) {
						childBiases[i][j] = newParamValue;
					}
					
					paramCounter++;
				}
				
			}
		}
		
		
		FFNN childBrain = new FFNN(dimensions);
		
		childBrain.setMap(childWeights, childBiases);
		
		return childBrain;
	}
}
