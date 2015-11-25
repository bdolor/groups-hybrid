package main.java.GroupsHybrid.GeneticAlgorithm.StudentGrouping;

import java.util.ArrayList;
import main.java.GroupsHybrid.GeneticAlgorithm.Common.GeneticAlgorithmException;
import main.java.GroupsHybrid.GeneticAlgorithm.Common.PermutationChromosome;
import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.IChromosome;
import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.ICrossOver;

public class StudentGroupCrossover<T extends IChromosome<int[]>> implements ICrossOver<T> {

	@SuppressWarnings("unchecked")
	@Override
	public T[] CrossOver(T[] parents) throws GeneticAlgorithmException {

		if (parents.length != this.getRequiredParentCount()) {
			throw new GeneticAlgorithmException(
					String.format("Crossover expecting 2 parents but found {0}", parents.length));
		}

		StudentGroups parent1 = (StudentGroups) parents[0];
		StudentGroups parent2 = (StudentGroups) parents[1];

		int[] parent1ValidGroups = parent1.getValidGroups();
		int[] parent2ValidGroups = parent2.getValidGroups();

		int[] validGroups = new int[parent1ValidGroups.length + parent2ValidGroups.length];
		System.arraycopy(parent1ValidGroups, 0, validGroups, 0, parent1ValidGroups.length);
		System.arraycopy(parent2ValidGroups, 0, validGroups, parent1ValidGroups.length, parent2ValidGroups.length);
		
		int[] randomValidGroups = new int[validGroups.length];
		//for (int i = 0; i < StudentGroups.MAXIMUM_STUDENTS / 4; i++) {
		//	int r = (int) (Math.random() * (StudentGroups.MAXIMUM_STUDENTS / 4));
		//	randomValidGroups[i] = validGroups[r];
		//	randomValidGroups[i+1] = validGroups[r+1];
		//	randomValidGroups[i+2] = validGroups[r+2];
		//	randomValidGroups[i+3] = validGroups[r+3];			
		//}
	
		randomValidGroups = validGroups;
		StudentGroups[] offspring = { this.getOffspring(randomValidGroups, parent1),
				this.getOffspring(randomValidGroups, parent2) };

		return (T[]) offspring;

	}

	protected StudentGroups getOffspring(int[] validGroups, StudentGroups parent) throws GeneticAlgorithmException {

		ArrayList<Integer> offspringEncoding = new ArrayList<Integer>();

		for (int i = 0; i < StudentGroups.MAXIMUM_STUDENTS / 4; i++) {
			if (!offspringEncoding.contains(validGroups[i]) && !offspringEncoding.contains(validGroups[i + 1])
					&& !offspringEncoding.contains(validGroups[i + 2])
					&& !offspringEncoding.contains(validGroups[i + 3])) {
				offspringEncoding.add(validGroups[i]);
				offspringEncoding.add(validGroups[i + 1]);
				offspringEncoding.add(validGroups[i + 2]);
				offspringEncoding.add(validGroups[i + 3]);
			}
		}

		for (int i = 0; i < StudentGroups.MAXIMUM_STUDENTS; i++) {
			int student = parent.getEncoding()[i];
			if (!offspringEncoding.contains(student)) {
				offspringEncoding.add(student);
			}
		}

		// convert ArrayList to Array
		int[] offspringEncodingArray = new int[offspringEncoding.size()];
		for (int i = 0; i < offspringEncoding.size(); i++) {
			offspringEncodingArray[i] = offspringEncoding.get(i);
		}

		PermutationChromosome offspring = PermutationChromosome.NewInstance(parent);
		offspring.setPermutationEncoding(offspringEncodingArray);

		return (StudentGroups) offspring;

	}

	@Override
	public int getRequiredParentCount() {
		return 2;
	}

	@Override
	public String toString() {
		return "Student Group Crossover";
	}
}
