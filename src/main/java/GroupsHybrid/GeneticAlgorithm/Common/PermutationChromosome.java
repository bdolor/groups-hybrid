package main.java.GroupsHybrid.GeneticAlgorithm.Common;

import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.IChromosome;

public abstract class PermutationChromosome implements IChromosome<int[]>{

	private int PermutationLength;

	private int[] PermutationEncoding = null;
	
	public PermutationChromosome(int length) {
		this.PermutationLength = length;
		this.setPermutationEncoding(this.getRandomGroup());
	}
	
	public PermutationChromosome(int[] groups) {
		this.setPermutationEncoding(groups);
	}

	/**
	 * Creates an array with length = PermutationLength
	 * with random values within that range.
	 * 
	 * @return Array randomOrder
	 */
	protected int[] getRandomGroup() {
		int[] randomOrder = new int[this.PermutationLength];

		for (int i = 0; i < randomOrder.length; i++) {
			randomOrder[i] = i + 1;
		}

		for (int i = 0; i < randomOrder.length; i++) {
			int r = (int) (Math.random() * this.PermutationLength);
			int temp = randomOrder[r];
			randomOrder[r] = randomOrder[i];
			randomOrder[i] = temp;
		}

		return randomOrder;
	}
	
	@SuppressWarnings("unchecked")
	public static PermutationChromosome NewInstance(IChromosome<int[]> o) throws GeneticAlgorithmException {
		IChromosome<int[]> newInstance;
		try {
			
			newInstance = o.getClass().newInstance();
			
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new GeneticAlgorithmException(e.getMessage());			
		}
		
		return (PermutationChromosome) newInstance;				
	}

	public int[] getPermutationEncoding() {
		return PermutationEncoding;
	}

	public void setPermutationEncoding(int[] permutationEncoding) {
		PermutationEncoding = permutationEncoding;
	}


}
