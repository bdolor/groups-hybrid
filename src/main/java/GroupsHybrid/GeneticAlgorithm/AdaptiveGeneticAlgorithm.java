package main.java.GroupsHybrid.GeneticAlgorithm;

import main.java.GroupsHybrid.GeneticAlgorithm.Common.GeneticAlgorithmException;
import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.IChromosome;


public class AdaptiveGeneticAlgorithm<T extends IChromosome<?>> extends GeneticAlgorithm<T>{

	/**
	 * Performs Adaptive Probability Crossover
	 * 
	 * @param parents
	 * @param avgFitness
	 * @param maxFitness
	 * @param parentFitness
	 * @return 
	 */
	@Override
	protected T[] doCrossover(T[] parents, double avgFitness, double maxFitness, double parentFitness) {
		T[] offspring = null;
		
		// set probability
		this.Config.setAdaptiveCrossoverProbability(avgFitness, maxFitness, parentFitness / 2);
		
		if (Math.random() < this.Config.getCrossoverProbability()) {

			try {
				offspring = this.CrossOver.CrossOver(parents);
			} catch (GeneticAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.CrossoverCount++;
		} else {
			offspring = parents;
		}

		return offspring;
	}

	/**
	 * Performs Adaptive Probability Mutation 
	 * 
	 * @param offspring
	 * @param avgFitness
	 * @param maxFitness
	 * @param parentFitness
	 * @return 
	 */
	@Override
	protected T[] doMutation(T[] offspring, double avgFitness, double maxFitness, double parentFitness ) {
		T[] mutatedOffspring = null;
		
		// set probability
		this.Config.setAdaptiveMutationProbability(avgFitness, maxFitness, parentFitness / 2);
		
		if (Math.random() < this.Config.getMutationProbability()) {
			try {
				mutatedOffspring = this.Mutation.Mutate(offspring);
			} catch (GeneticAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.MutationCount++;
		} else {
			mutatedOffspring = offspring;
		}

		return mutatedOffspring;
	}

}
