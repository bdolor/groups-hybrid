package main.java.Groups.AntColony.Structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import main.java.Groups.Data.StudentScores;

public class InitializerArray {

	/**
	 * Instead of calculating the distance between all cities from the
	 * perspective of a snapshot of all the cities, this should return the
	 * Euclidean Distances between students from the perspective of a
	 * snapshot of all the students.
	 *
	 * @param studentNodes
	 * @return
	 */
	public static double[][] computeDistances(int[] studentNodes, StudentScores scores) {

		double[][] globalDistances = new double[studentNodes.length][studentNodes.length];
		for (int i = 0; i < studentNodes.length; i++) {
			for (int j = i + 1; j < studentNodes.length; j++) {

				double distance = scores.getDistance(i, j);

				globalDistances[i][j] = distance;
				globalDistances[j][i] = distance;
			}
		}
		return globalDistances;
	}

	public static double[][] initPheromones(int[] studentNodes) {
		double[][] globalPheromones = new double[studentNodes.length][studentNodes.length];
		for (int i = 0; i < studentNodes.length; i++) {
			for (int j = 0; j < studentNodes.length; j++) {
				globalPheromones[i][j] = 0.01;
			}
		}
		return globalPheromones;
	}

	public static int[][] initAnts(int antCount, int[] studentNodes) {
		int[][] ants = new int[antCount][studentNodes.length];

		for (int i = 0; i < antCount; i++) {
			ants[i] = getRandomTrail(studentNodes.length);
		}
		return ants;
	}

	private static int[] getRandomTrail(int size) {
		List<Integer> trail = new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {
			trail.add(i);
		}
		Collections.shuffle(trail);

		int[] result = new int[trail.size()];
		for (int i = 0; i < trail.size(); i++) {
			result[i] = trail.get(i);
		}
		return result;
	}
}
