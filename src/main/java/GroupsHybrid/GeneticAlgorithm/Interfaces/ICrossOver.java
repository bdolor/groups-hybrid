package main.java.GroupsHybrid.GeneticAlgorithm.Interfaces;

import main.java.GroupsHybrid.GeneticAlgorithm.Common.GeneticAlgorithmException;

public interface ICrossOver<T> {
	T[] CrossOver(T[] parents) throws GeneticAlgorithmException;
	int getRequiredParentCount();
}
