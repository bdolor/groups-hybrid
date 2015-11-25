package main.java.GroupsHybrid.GeneticAlgorithm.Interfaces;

import main.java.GroupsHybrid.GeneticAlgorithm.Common.GeneticAlgorithmException;

public interface IMutation <T> {
	T[] Mutate(T[] offspring) throws GeneticAlgorithmException;
}
