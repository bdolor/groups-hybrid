package main.java.GroupsHybrid.GeneticAlgorithm.Common;

import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.IChromosome;
import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.IMutation;

public class PermutationMutation <T extends IChromosome<int[]>> implements IMutation<T>  {

	/**
	 * Randomly swap one student position in the class with another
	 * 
	 * @param offspring 
	 * @return
	 * @throws GeneticAlgorithmException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T[] Mutate(T[] offspring) throws GeneticAlgorithmException {
		PermutationChromosome[] mutatedOffspring = new PermutationChromosome[offspring.length];

		for (int i = 0; i < offspring.length; i++) {
			
			int[] childEncoding = offspring[i].getEncoding();
			
			int r1 = (int) (Math.random() * childEncoding.length);
			int r2 = (int) (Math.random() * childEncoding.length);
			
			// randomly swap only student position in the class with another
			int temp = childEncoding[r1];
			childEncoding[r1] = childEncoding[r2];
			childEncoding[r2] = temp;
			
			PermutationChromosome mutatedChild = PermutationChromosome.NewInstance(offspring[i]);
			mutatedChild.setPermutationEncoding(childEncoding);
			
			mutatedOffspring[i] = mutatedChild;

		}
		return (T[]) mutatedOffspring;
	}

	@Override
	public String toString() {
		return "Permutation Mutation";
	}

}
