package main.java.GroupsHybrid.GeneticAlgorithm.GroupEncoding;

import main.java.GroupsHybrid.GeneticAlgorithm.Common.GeneticAlgorithmException;
import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.IChromosome;
import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.IMutation;

public class GroupEncodingMutation  <T extends IChromosome<int[]>> implements IMutation<T>  {

	public final static int MUTATION_SWAP_COUNT = 1;
	
	@SuppressWarnings("unchecked")
	@Override
	public T[] Mutate(T[] offspring) throws GeneticAlgorithmException {		
		GroupEncodingChromosome[] mutatedOffspring = new GroupEncodingChromosome[offspring.length];
		
		for (int i=0; i< offspring.length;i++) {									
			int[] offspringEncoding = offspring[i].getEncoding();
			
			for (int j=0;j<GroupEncodingMutation.MUTATION_SWAP_COUNT;j++) {								
				int r1 = (int) (Math.random() * GroupEncodingChromosome.MAXIMUM_STUDENTS);
				int r2 = (int) (Math.random() * GroupEncodingChromosome.MAXIMUM_STUDENTS);
				
				int temp = offspringEncoding[r1];
				offspringEncoding[r1] = offspringEncoding[r2];
				offspringEncoding[r1] = temp;				
			}

			mutatedOffspring[i] = new GroupEncodingChromosome();
			mutatedOffspring[i].setEncoding(offspringEncoding);
			mutatedOffspring[i].applyCorrection();				
		}
		
		return (T[]) mutatedOffspring;
	}

	@Override
	public String toString() {
		return "GroupEncodingMutation";
	}	
}
