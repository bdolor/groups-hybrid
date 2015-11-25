package main.java.GroupsHybrid.GeneticAlgorithm.Common;

import java.util.ArrayList;

import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.IChromosome;
import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.ICrossOver;

public class PermutationCrossover <T extends IChromosome<int[]>> implements ICrossOver<T> {

	@SuppressWarnings("unchecked")
	@Override
	public T[] CrossOver(T[] parents) throws GeneticAlgorithmException {
		if (parents.length != this.getRequiredParentCount()) {
			throw new GeneticAlgorithmException(
					String.format("Crossover expecting 2 parents but found {0}", parents.length));
		}
		// copy the parents to function variables
		int[] parent1 = parents[0].getEncoding();
		int[] parent2 = parents[1].getEncoding();
		ArrayList<Integer> offspringEncoding = new ArrayList<Integer>();
		
		// generate random number within the range parent length (MAXIMUM_STUDENTS)
		int r = (int) (Math.random() * parent1.length);
		
		// copy parent1 to an 'offspring' variable, but
		// limit the amount copied to the size of the random number generated
		for (int i = 0; i <= r; i++) {
			offspringEncoding.add(parent1[i]);
		}
		
		// add everything unique (left over) from parent2, to offspring 
		for (int i = 0; i < parent1.length; i++) {
			if (!offspringEncoding.contains(parent2[i])) {
				offspringEncoding.add(parent2[i]);
			}
		}
		
		// convert ArrayList to Array
		int[] offspringEncodingArray = new int[offspringEncoding.size()];
		for (int i=0;i<offspringEncoding.size();i++) {
			offspringEncodingArray[i] = offspringEncoding.get(i);
		}
			
		PermutationChromosome offspring = PermutationChromosome.NewInstance(parents[0]);
		offspring.setPermutationEncoding(offspringEncodingArray);

		return (T[]) new PermutationChromosome[]{offspring};
	}

	@Override
	public int getRequiredParentCount() {
		return 2;
	}

}
