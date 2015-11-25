package main.java.GroupsHybrid.GeneticAlgorithm.GroupEncoding;

import java.util.ArrayList;

import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.IChromosome;
import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.ISolution;

public class GroupEncodingSolution <T extends IChromosome<int[]>> implements ISolution<T>{

	@Override
	public ArrayList<T> getSolutions(ArrayList<T> population) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T getFittestSolution(ArrayList<T> population) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFittestSolutionIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String[] getMembersOfGroup(ArrayList<T> population, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] getEachGroupDistance(ArrayList<T> population, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[] getEachGroupGH(ArrayList<T> population, int index) {
		// TODO Auto-generated method stub
		return null;
	}
	

}

