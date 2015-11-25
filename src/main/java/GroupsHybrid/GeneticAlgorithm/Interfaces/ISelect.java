package main.java.GroupsHybrid.GeneticAlgorithm.Interfaces;

import java.util.ArrayList;

public interface ISelect<T> {
	T[] GetParents(ArrayList<T> population, double[] fitness, int count);
}
