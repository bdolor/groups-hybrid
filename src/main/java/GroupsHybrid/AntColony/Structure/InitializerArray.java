package main.java.GroupsHybrid.AntColony.Structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class InitializerArray {

	/**
	 * Instead of calculating the distance between all cities from the perspective
	 * of a snapshot of all the cities, this should return the Euclidean Distances between students
	 * from the perspective of a snapshot of all the students.
	 * 
	 * @param studentNodes
	 * @return 
	 */
	public static double[][] computeDistances(int[] studentNodes) {
		double[][] globalDistances = new double[studentNodes.length][studentNodes.length];
//		for (int i = 0; i < studentNodes.size(); i++) {
//			for (int j = i + 1; j < studentNodes.size(); j++) {
//				double distance = Math
//						.sqrt(Math.pow(studentNodes.get(i).getX()
//								- studentNodes.get(j).getX(), 2)
//								+ Math.pow(studentNodes.get(i).getY()
//										- studentNodes.get(j).getY(), 2));
//				globalDistances[i][j] = distance;
//				globalDistances[j][i] = distance;
//	}
//		}
		return globalDistances;
	}

	public static double[][] initPheromones(int[] studentNodes) {
		double[][] globalPheromones = new double[studentNodes.length][studentNodes.length];
//		for (int i = 0; i < studentNodes.size(); i++) {
//			for (int j = 0; j < studentNodes.size(); j++) {
//				globalPheromones[i][j] = 0.01;
//			}
//		}
		return globalPheromones;
	}

	public static int[][] initAnts(int antCount, int[] studentNodes) {
		Random rand = new Random();
		// trail has length studentNodes.size() + 1, because we include the start city
		// at the end
		int[][] ants = new int[antCount][studentNodes.length + 1];
		for (int i = 0; i < antCount; i++) {
			ants[i] = getRandomTrail(rand.nextInt(studentNodes.length), studentNodes.length);
		}
		return ants;
	}

	private static int[] getRandomTrail(int start, int size) {
		List<Integer> trail = new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {
			trail.add(i);
		}
		Collections.shuffle(trail);
		Collections.swap(trail, 0, trail.indexOf(start));
		trail.add(trail.get(0));

		int[] result = new int[trail.size()];
		for (int i = 0; i < trail.size(); i++) {
			result[i] = trail.get(i);
		}
		return result;
	}
}
