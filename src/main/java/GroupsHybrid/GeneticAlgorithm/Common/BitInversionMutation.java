package main.java.GroupsHybrid.GeneticAlgorithm.Common;

import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.IChromosome;
import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.IMutation;

public class BitInversionMutation<T extends IChromosome<String>> implements IMutation<T> {

	@SuppressWarnings("unchecked")
	@Override
	public T[] Mutate(T[] offspring) throws GeneticAlgorithmException {
		BinaryChromosome[] mutatedOffspring = new BinaryChromosome[offspring.length];

		for (int i = 0; i < offspring.length; i++) {

			int r = (int) (Math.random() * offspring[i].getEncoding().length());

			String encoding = offspring[i].getEncoding();
			String mutatedEncoding = this.replaceCharAt(encoding, r, encoding.charAt(r) == '0' ? '1' : '0');

			
			mutatedOffspring[i] = BinaryChromosome.NewInstance(offspring[i]);
			mutatedOffspring[i].setBinaryEncoding(mutatedEncoding);
		}

		return (T[]) mutatedOffspring;
	}

	protected String replaceCharAt(String s, int pos, char c) {
		return s.substring(0, pos) + c + s.substring(pos + 1);
	}

}
