package main.java.GroupsHybrid.GeneticAlgorithm.Common;

import java.util.ArrayList;

import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.IChromosome;
import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.ISelect;

@SuppressWarnings("rawtypes")
public class RouletteSelect<T extends IChromosome> implements ISelect<T> {

	/**
	 * Calculate the sum of all GH fitness in a population
	 * Generate a random number within a range of that number
	 * Get the first two numbers that have a GH fitness above that number.
	 * 
	 * @param population ArrayList 
	 * @param fitness double
	 * @param parentCount integer
	 * @return parents Array
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T[] GetParents(ArrayList<T> population, double[] fitness, int parentCount) {

		IChromosome[] parents = new IChromosome[parentCount];

		double sumFitness = 0;
		for (int i = 0; i < fitness.length; i++) {
			sumFitness += fitness[i];
		}

		// Random parents when sumfitness = 0, otherwise roulette will always select first chromosome.
		if (sumFitness == 0) {
			for (int i = 0; i < parentCount; i++) {
				parents[i] = population.get((int) (Math.random() * sumFitness));
			}
		} else {
			/**
			 * calculate average fitness sum, generate 
			 * a random number within a range of that average number, 
			 * select the first two numbers with a fitness score above that
			 */
			for (int i = 0; i < parentCount; i++) {
				int r = (int) (Math.random() * sumFitness);
				double sumSelection = 0;
				for (int j = 0; j < fitness.length; j++) {
					sumSelection += fitness[j];
					if (sumSelection >= r) {
						parents[i] = population.get(j);
						break;
					}
				}
			}
		}

		return (T[]) parents;
	}

	@Override
	public String toString() {
		return "Roulette Selection";
	}

}
