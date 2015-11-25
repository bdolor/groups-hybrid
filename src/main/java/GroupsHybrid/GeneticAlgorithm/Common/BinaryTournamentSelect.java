package main.java.GroupsHybrid.GeneticAlgorithm.Common;

import java.util.ArrayList;
import java.util.Collections;

import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.IChromosome;
import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.ISelect;

public class BinaryTournamentSelect<T extends IChromosome> implements ISelect<T> {

	@Override
	public T[] GetParents(ArrayList<T> population, double[] fitness, int parentCount) {
		IChromosome[] parents = new IChromosome[parentCount];
		int pool = fitness.length / 4; // increasing the size of pool decreases the liklihood that weak parents will be chosen
		int contenders;
		double champ1fitness = 0;
		double champ2fitness = 0;
		int redCorner = 0;
		int blueCorner = 0;
		
		// ensure that pool tournament selelction is large 
		// and that it is divisible by 2
		contenders = ( pool % 2 == 0 ) ? pool : pool - 1;
		int halfContenders = contenders / 2;
		
		// create a list of numbers
		ArrayList<Integer> randomList = new ArrayList<>();
		for (int i = 0; i < pool; i++) {
			randomList.add(i);
		}

		// give that list a shake
		Collections.shuffle(randomList);
		// choose a number of individuals randomly from a population
		// repeat until the mating pool is filled
		for (int i = 0; i < halfContenders; i++) {

			// select the best individuals from the first half of the pool
			if (fitness[randomList.get(i)] > champ1fitness) {
				champ1fitness = fitness[randomList.get(i)];
				redCorner = randomList.get(i);
			}
			// select the best individuals from the last half of the pool
			if (fitness[randomList.get(i + halfContenders)] > champ2fitness) {
				champ2fitness = fitness[randomList.get(i + halfContenders)];
				blueCorner = randomList.get(i + halfContenders);

			}
		}

		parents[0] = population.get(redCorner);
		parents[1] = population.get(blueCorner);

		return (T[]) parents;
	}
	@Override
	public String toString() {
		return "Binary Tournament Selection";
	}
}
