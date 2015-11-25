//package main.java.GroupsHybrid.GeneticAlgorithm.StudentGrouping;
//
//import java.util.ArrayList;
//
//import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.IChromosome;
//import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.ISolution;
//
//public class StudentGroupsSolution <T extends IChromosome<int[]>> implements ISolution<T> {
//	
//	/**
//	 * the index number of the best 'class' in a population of solutions
//	 */
//	public int bestIndex = 0;
//	
//	@Override
//	public ArrayList<T> getSolutions(ArrayList<T> population) {
//		return new ArrayList<T>();
//	}
//	/**
//	 * Looks for the highest overall heterogeneity of all groups within a population 
//	 * and selects a winner! 
//	 * 
//	 * @param population ArrayList
//	 * @return fittest ArrayList
//	 */
//	@Override
//	public T getFittestSolution(ArrayList<T> population) {
//		T fittest = population.get(0);
//				
//		for (int i = 0; i < population.size(); i++) {
//			if (population.get(i).getFitness() > fittest.getFitness()) {
//				fittest = population.get(i);
//				bestIndex = i;
//			}
//		} 
//			
//		return fittest;	
//	}
//	
//	/**
//	 * Looks for the best solution and returns value of the index. 
//	 * 
//	 * @return integer bestIndex
//	 */
//	@Override
//	public int getFittestSolutionIndex() {
//		return this.bestIndex;
//	}
//
//	/**
//	 * Must get all the member IDs of a group in an entire 'class'
//	 * 
//	 * @param population
//	 * @param index
//	 * @return 
//	 */
//	@Override
//	public String[] getMembersOfGroup(ArrayList<T> population, int index) {
//		String[] result;
//		result = population.get(index).getMembers();
//
//		return result;
//
//	}
//	
//	/**
//	 * Get the Euclidean distance from each group from a specific 'class' 
//	 * of students
//	 * 
//	 * @param population
//	 * @param index
//	 * @return 
//	 */
//	@Override
//	public double[] getEachGroupDistance(ArrayList<T> population, int index) {
//		double[] result;
//		result = population.get(index).getEachGroupMaxDistance();
//
//		return result;
//	}
//	
//	/**
//	 * Get GH of each group from a specific 'class' of students
//	 * 
//	 * @param population
//	 * @param index
//	 * @return 
//	 */
//	@Override
//	public double[] getEachGroupGH(ArrayList<T> population, int index) {
//		double[] result;
//		result = population.get(index).getEachGroupGH();
//		
//		return result;
//	}
//	
//	
//}
