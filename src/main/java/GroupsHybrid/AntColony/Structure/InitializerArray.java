package main.java.GroupsHybrid.AntColony.Structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import main.java.GroupsHybrid.AntColony.Node;

public class InitializerArray {

	public static double[][] computeDistances(List<Node> nodes) {
		double[][] globalDistances = new double[nodes.size()][nodes.size()];
		for (int i = 0; i < nodes.size(); i++) {
			for (int j = i + 1; j < nodes.size(); j++) {
				double distance = Math
						.sqrt(Math.pow(nodes.get(i).getX()
								- nodes.get(j).getX(), 2)
								+ Math.pow(nodes.get(i).getY()
										- nodes.get(j).getY(), 2));
				globalDistances[i][j] = distance;
				globalDistances[j][i] = distance;
			}
		}
		return globalDistances;
	}

	public static double[][] initPheromones(List<Node> nodes) {
		double[][] globalPheromones = new double[nodes.size()][nodes.size()];
		for (int i = 0; i < nodes.size(); i++) {
			for (int j = 0; j < nodes.size(); j++) {
				globalPheromones[i][j] = 0.01;
			}
		}
		return globalPheromones;
	}

	public static int[][] initAnts(int antCount, List<Node> nodes) {
		Random rand = new Random();
		// trail has length nodes.size() + 1, because we include the start city
		// at the end
		int[][] ants = new int[antCount][nodes.size() + 1];
		for (int i = 0; i < antCount; i++) {
			ants[i] = getRandomTrail(rand.nextInt(nodes.size()), nodes.size());
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
