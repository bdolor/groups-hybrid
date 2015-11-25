package main.java.GroupsHybrid.GeneticAlgorithm.GroupEncoding;

import main.java.GroupsHybrid.GeneticAlgorithm.Common.GeneticAlgorithmException;
import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.IChromosome;
import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.ICrossOver;

public class GroupEncodingCrossover <T extends IChromosome<int[]>> implements ICrossOver<T> {

	@SuppressWarnings("unchecked")
	@Override
	public T[] CrossOver(T[] parents) throws GeneticAlgorithmException {
		
		if (parents.length != this.getRequiredParentCount()) {
			throw new GeneticAlgorithmException(
					String.format("Crossover expecting 2 parents but found {0}", parents.length));
		}
		
		
		int r = (int) (1 + Math.random() * GroupEncodingChromosome.MAXIMUM_STUDENTS);
		
		int[] parent1Encoding = parents[0].getEncoding();
		int[] parent2Encoding = parents[1].getEncoding();
		
		int[] offspringEncoding = new int[GroupEncodingChromosome.MAXIMUM_STUDENTS];
		
		for (int i=0; i<GroupEncodingChromosome.MAXIMUM_STUDENTS;i++ ) {
			offspringEncoding[i] = i < r ? parent1Encoding[i] : parent2Encoding[i];
		}
								
		
		GroupEncodingChromosome offspring = new GroupEncodingChromosome();
		offspring.setEncoding(offspringEncoding);
		offspring.applyCorrection();
		
		GroupEncodingChromosome[] ret = {offspring}; 
		
		
		return (T[]) ret;
	}

	@Override
	public int getRequiredParentCount() {
		return 2;
	}

	@Override
	public String toString() {
		return "Group Encoding Crossover";
	}
	

}
