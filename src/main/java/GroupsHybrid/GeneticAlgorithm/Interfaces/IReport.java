package main.java.GroupsHybrid.GeneticAlgorithm.Interfaces;

import main.java.GroupsHybrid.GeneticAlgorithm.GeneticAlgorithm;

public interface IReport<T extends IChromosome<?>> {
	public void updateReport(double averageFitness, double maxFitness, T fittest, boolean isConverged, long startTime);
	public void initializeReport(GeneticAlgorithm<?> algorithm);	
}
