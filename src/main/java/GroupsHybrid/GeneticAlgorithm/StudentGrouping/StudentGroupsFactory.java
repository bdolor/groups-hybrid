package main.java.GroupsHybrid.GeneticAlgorithm.StudentGrouping;

import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.IFactory;

public class StudentGroupsFactory <T extends StudentGroups> implements IFactory<T> {

	@SuppressWarnings("unchecked")
	@Override
	public T CreateChromosome() {
		return (T) new StudentGroups();
	}
	

}
