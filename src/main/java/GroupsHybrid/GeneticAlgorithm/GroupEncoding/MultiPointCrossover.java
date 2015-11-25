package main.java.GroupsHybrid.GeneticAlgorithm.GroupEncoding;

import java.util.ArrayList;
import java.util.Collections;

import main.java.GroupsHybrid.GeneticAlgorithm.Common.GeneticAlgorithmException;
import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.IChromosome;
import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.ICrossOver;
import main.java.GroupsHybrid.GeneticAlgorithm.GroupEncoding.GroupEncodingChromosome;

public class MultiPointCrossover<T extends IChromosome<int[]>> implements ICrossOver<T> {

	private int RequiredParentCount;

	public MultiPointCrossover(int parentCount) {
		this.RequiredParentCount = parentCount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T[] CrossOver(T[] parents) throws GeneticAlgorithmException {

		if (parents.length != this.getRequiredParentCount()) {
			throw new GeneticAlgorithmException(String.format("Crossover expecting %d parents but found %d",
					this.getRequiredParentCount(), parents.length));
		}

		ArrayList<Integer> r = this.getRandomInterval();

		int[] offspringEncoding = new int[GroupEncodingChromosome.MAXIMUM_STUDENTS];

		for (int i = 0; i < GroupEncodingChromosome.MAXIMUM_STUDENTS; i++) {
			for (int j = 0; j < r.size() - 1; j++) {
				if (i >= r.get(j) && i < r.get(j + 1)) {
					offspringEncoding[i] = parents[j].getEncoding()[i];
				}
			}
		}

		GroupEncodingChromosome offspring = new GroupEncodingChromosome();
		offspring.setEncoding(offspringEncoding);
		offspring.applyCorrection();

		GroupEncodingChromosome[] ret = { offspring };

		return (T[]) ret;
	}

	@Override
	public int getRequiredParentCount() {
		return this.RequiredParentCount;
	}
	
	@Override
	public String toString() { 
		return String.format("MultiPointCrossover with %d parents", this.getRequiredParentCount());
	}
	
	

	private ArrayList<Integer> getRandomInterval() {
		ArrayList<Integer> r = new ArrayList<Integer>();
		r.add(0);
		r.add(GroupEncodingChromosome.MAXIMUM_STUDENTS);
		for (int i = 0; i < this.getRequiredParentCount() - 1; i++) {
			r.add((int) (1 + Math.random() * GroupEncodingChromosome.MAXIMUM_STUDENTS));
		}
		Collections.sort(r);
		return r;
	}



}
