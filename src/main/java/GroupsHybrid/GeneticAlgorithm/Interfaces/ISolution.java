package main.java.GroupsHybrid.GeneticAlgorithm.Interfaces;

import java.util.ArrayList;

public interface ISolution<T> {
	
	ArrayList<T> getSolutions(ArrayList<T> population);
	T getFittestSolution(ArrayList<T> population);
	int getFittestSolutionIndex();
	String[] getMembersOfGroup(ArrayList<T> population, int index);
	double[] getEachGroupDistance(ArrayList<T> population, int index);
	double[] getEachGroupGH(ArrayList<T> population, int index);
}
