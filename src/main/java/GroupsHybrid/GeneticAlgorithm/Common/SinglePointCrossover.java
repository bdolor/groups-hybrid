package main.java.GroupsHybrid.GeneticAlgorithm.Common;

import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.IChromosome;
import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.ICrossOver;

public class SinglePointCrossover<T extends IChromosome<String>> implements ICrossOver<T> {

	@SuppressWarnings("unchecked")
	@Override
	public T[] CrossOver(T[] parents) throws GeneticAlgorithmException {
	
		if (parents.length != this.getRequiredParentCount()) {
			throw new GeneticAlgorithmException(
					String.format("Crossover expecting 2 parents but found {0}", parents.length));
		}

		int r2 = (int) (1 + Math.random() * parents[0].getEncoding().length());

		String parent1 = parents[0].getEncoding();
		String parent2 = parents[1].getEncoding();

		BinaryChromosome offspring1 = BinaryChromosome.NewInstance(parents[0]);
		BinaryChromosome offspring2 = BinaryChromosome.NewInstance(parents[0]);

		offspring1.setBinaryEncoding(parent1.substring(0, r2).concat(parent2.substring(r2)));
		offspring2.setBinaryEncoding(parent2.substring(0, r2).concat(parent1.substring(r2)));

		return (T[]) new BinaryChromosome[] { offspring1, offspring2 };

	}

	@Override
	public int getRequiredParentCount() {
		return 2;
	}
}
