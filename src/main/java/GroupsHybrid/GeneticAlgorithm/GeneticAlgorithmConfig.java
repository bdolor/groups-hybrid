package main.java.GroupsHybrid.GeneticAlgorithm;

public class GeneticAlgorithmConfig {
	
	private int PopulationSize = 100;
	private int MaximumEvolutions = 20000;
	private int RequiredParentCount = 2;
	private double CrossoverProbability = 0.9;
	private double MutationProbability = 0.2;
	private int EliteChromosomeCount = 14;
	private int ConvergenceMaximum = 50;
	private boolean AdapativeEnabled;
	
	
	public boolean isAdapativeEnabled() {
		return AdapativeEnabled;
	}
	public void setAdapativeEnabled(boolean adapativeEnabled) {
		AdapativeEnabled = adapativeEnabled;
	}
	public int getPopulationSize() {
		return PopulationSize;
	}
	public int getConvergenceMaximum() {
		return ConvergenceMaximum;
	}

	public void setConvergenceMaximum(int convergenceMaximum) {
		ConvergenceMaximum = convergenceMaximum;
	}

	public int getEliteChromosomeCount() {
		return EliteChromosomeCount;
	}

	public void setEliteChromosomeCount(int eliteChromosomeCount) {
		EliteChromosomeCount = eliteChromosomeCount;
	}	
	
	public double getCrossoverProbability() {
		return CrossoverProbability;
	}
	
	public void setCrossoverProbability(double crossoverProbability) {
		CrossoverProbability = crossoverProbability;
	}
	
	public void setMutationProbability(double mutationProbability) {
		MutationProbability = mutationProbability;
	}
	
	/**
	 * Adjusts the Crossover probability based on how fit the parents are.
	 *
	 * If parent fitness is low, CP is increased ( promote the extensive
	 * recombination )
	 *
	 *
	 * @param avgFitness
	 * @param maxFitness
	 * @param parentFitness
	 */
	public void setAdaptiveCrossoverProbability(double avgFitness, double maxFitness, double parentFitness) {
		// adaptive probability
		double fallback = 0.99;
		double crossoverProbability;
		double upperLimit = 1.0;
		double lowerLimit = 0.5;

		// CP = (max fitness - parent fitness)/ (max fitness - avg fitness), CP <= 1.0
		crossoverProbability = upperLimit * ((maxFitness - parentFitness) / (maxFitness - avgFitness));

		/**
		 * safety, must be less than 1.0 A value of greater than 1
		 * occurs when parentFitness is less than average Fitness
		 */
		if (crossoverProbability >= upperLimit) {
			crossoverProbability = fallback;
		}
		if (crossoverProbability < lowerLimit) {
			crossoverProbability = lowerLimit;
		}
		CrossoverProbability = crossoverProbability;

	}
	
	public double getMutationProbability() {
		return MutationProbability;
	}
	/**
	 * Adjusts the Mutation probability based on how fit the parents are.
	 *
	 * If parent fitness is high, MP is decreased ( prevent disruption of
	 * the solution )
	 *
	 * @param avgFitness
	 * @param maxFitness
	 * @param parentFitness
	 */
	public void setAdaptiveMutationProbability(double avgFitness, double maxFitness, double parentFitness) {
		// adaptive probability
		double fallback = 0.049;
		double mutationProbability;
		double upperLimit = 0.5;
		double lowerLimit = 0.01;

		// MP <= 0.05
		mutationProbability = upperLimit * ((maxFitness - parentFitness) / (maxFitness - avgFitness));

		/**
		 * safety, must be less than 0.05 A value of greater than 0.05
		 * occurs when parentFitness is less than average Fitness
		 */
		if (mutationProbability >= upperLimit) {
			mutationProbability = fallback;
		}
		if (mutationProbability < lowerLimit) {
			mutationProbability = lowerLimit;
		}

		MutationProbability = mutationProbability;
	}
	
	public void setPopulationSize(int populationSize) {
		PopulationSize = populationSize;
	}
	public int getMaximumEvolutions() {
		return MaximumEvolutions;
	}
	public void setMaximumEvolutions(int maximumEvolutions) {
		MaximumEvolutions = maximumEvolutions;
	}
	public int getRequiredParentCount() {
		return RequiredParentCount;
	}
	public void setRequiredParentCount(int requiredParentCount) {
		RequiredParentCount = requiredParentCount;
	}
	
	@Override
	public String toString() {
		return String.format(
				"{PopulationSize:%d}, {MaximumEvolutions:%d}, {Crossover:%.3f}, {Mutation %.3f}, {Elitists:%d}, {ConvergenceCount:%d}",
				this.getPopulationSize(), this.getMaximumEvolutions(), this.getCrossoverProbability(),
				this.getMutationProbability(), this.getEliteChromosomeCount(), this.getConvergenceMaximum());
	}

}
