package main.java.GroupsHybrid.AntColony.Structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import main.java.GroupsHybrid.AntColony.Interfaces.AntColony;
import main.java.GroupsHybrid.AntColony.Node;

//Implementation similar to the one of James McCaffrey
//(http://msdn.microsoft.com/de-de/magazine/hh781027.aspx)
public class AntColonyArray implements AntColony {

	@Override
	public List<Integer> solve(int[] studentNodes, int iterations, int antCount,
			double alpha, double beta, double rho, double Q) {
		
		double[][] distances = InitializerArray.computeDistances(studentNodes);
		double[][] pheromones = InitializerArray.initPheromones(studentNodes);
		int[][] ants = InitializerArray.initAnts(antCount, studentNodes);
		int[] bestTrail = getBestTrail(ants, distances);
		double bestLength = getLength(bestTrail, distances);

		int count = 0;
		boolean end = false;
		while (!end) {
			updateTrails(ants, pheromones, distances, alpha, beta);
			updatePheromones(pheromones, ants, distances, rho, Q);

			int[] tmpBestTrail = getBestTrail(ants, distances);
			double tmpBestLength = getLength(tmpBestTrail, distances);
			if (tmpBestLength < bestLength) {
				bestTrail = tmpBestTrail;
				bestLength = tmpBestLength;
				
				System.out.println(count + " -------------------------------------");
				printTrail(bestTrail, distances);
			}
			
			if (++count >= iterations) {
				end = true;
			}
		}
		System.out.println("\n-------------AntColonyArray------------------------");
		printTrail(bestTrail, distances);
		System.out.println("ITERATIONS = " + iterations);
		System.out.println("ANTS = " + antCount);
		System.out.println("ALPHA = " + alpha);
		System.out.println("BETA = " + beta);
		System.out.println("RHO = " + rho);
		System.out.println("Q = " + Q);
		
		return convertToList(bestTrail);
	}

	private void updateTrails(int[][] ants, double[][] pheromones,
			double[][] distances, double alpha, double beta) {
		Random rand = new Random();
		int numCities = pheromones.length;
		for (int i = 0; i < ants.length; i++) {
			int start = rand.nextInt(numCities);
			int[] trail = buildTrail(start, pheromones, distances, alpha, beta);
			ants[i] = trail;
		}
	}

	private int[] buildTrail(int start, double[][] pheromones,
			double[][] distances, double alpha, double beta) {
		int numCities = pheromones.length;
		int[] trail = new int[numCities + 1];
		boolean[] visited = new boolean[numCities];
		trail[0] = start;
		visited[start] = true;
		for (int i = 0; i < numCities - 1; i++) {
			int next = nextCity(trail[i], visited, pheromones, distances, alpha, beta);
			trail[i + 1] = next;
			visited[next] = true;
		}
		trail[numCities] = trail[0];
		return trail;
	}

	private int nextCity(int city, boolean[] visited, double[][] pheromones,
			double[][] distances, double alpha, double beta) {
		double[] moveProbabilities = getMoveProbabilities(city, visited,
				pheromones, distances, alpha, beta);

		int numCities = pheromones.length;
		double[] cumul = new double[numCities + 1];
		cumul[0] = 0.0;
		for (int i = 0; i < numCities; i++) {
			cumul[i + 1] = cumul[i] + moveProbabilities[i];
		}
		cumul[numCities] = 1.0;

		double position = new Random().nextDouble();
		for (int i = 0; i < numCities; i++) {
			if (position >= cumul[i] && position < cumul[i + 1]) {
				return i;
			}
		}

		return -1;
	}

	private double[] getMoveProbabilities(int city, boolean[] visited,
			double[][] pheromones, double[][] distances, double alpha, double beta) {
		int numCities = pheromones.length;
		double[] taueta = new double[numCities];
		double sum = 0.0;

		for (int i = 0; i < numCities; i++) {
			if (!visited[i]) {
				taueta[i] = Math.pow(pheromones[city][i], alpha)
						* Math.pow((1.0 / distances[city][i]), beta);
				if (taueta[i] < 0.0001) {
		            taueta[i] = 0.0001;
				}
				else if (taueta[i] > (Double.MAX_VALUE / (numCities * 100))) {
		            taueta[i] = Double.MAX_VALUE / (numCities * 100);
				}
			}
			sum += taueta[i];
		}

		double[] probabilities = new double[numCities];
		for (int i = 0; i < numCities; i++) {
			probabilities[i] = taueta[i] / sum;
		}
		return probabilities;
	}

	private void updatePheromones(double[][] pheromones, int[][] ants,
			double[][] distances, double rho, double Q) {
		for (int i = 0; i < pheromones.length; ++i) {
			for (int j = i + 1; j < pheromones[i].length; ++j) {
				for (int k = 0; k < ants.length; ++k) {
					double length = getLength(ants[k], distances);
					double decrease = (1.0 - rho) * pheromones[i][j];
					double increase = 0.0;
					if (isEdgeInTrail(i, j, ants[k])) {
						increase =(Q / length);
					}
					pheromones[i][j] = decrease + increase;
					pheromones[j][i] = pheromones[i][j];
				}
			}
		}
	}

	private boolean isEdgeInTrail(int start, int stop, int[] trail) {
		for (int i = 0; i < trail.length - 2; i++) {
			if (trail[i] == start && trail[i + 1] == stop) {
				return true;
			}
		}
		return false;
	}

	private int[] getBestTrail(int[][] ants, double[][] distances) {
		List<Double> lengths = new ArrayList<Double>();
		for (int i = 0; i < ants.length; i++) {
			lengths.add(getLength(ants[i], distances));
		}
		Double minLength = Collections.min(lengths);
		return ants[lengths.indexOf(minLength)];
	}

	private double getLength(int[] trail, double[][] distances) {
		double length = 0.0;
		for (int i = 0; i < trail.length - 1; i++) {
			int start = trail[i];
			int stop = trail[i + 1];
			length += distances[start][stop];
		}
		return length;
	}

	private List<Integer> convertToList(int[] array) {
		List<Integer> result = new ArrayList<Integer>();
		for (int i : array) {
			result.add(i);
		}
		return result;
	}

	private void printDistances(double[][] distances) {
		System.out.println("----Distances----");
		for (double[] i : distances) {
			StringBuilder sb = new StringBuilder();
			for (double j : i) {
				sb.append(j + ", ");
			}
			System.out.println(sb);
		}
		System.out.println("--------");
	}

	private void printPheromones(double[][] pheromones) {
		System.out.println("----Pheromones----");
		for (double[] i : pheromones) {
			StringBuilder sb = new StringBuilder();
			for (double j : i) {
				sb.append(j + ", ");
			}
			System.out.println(sb);
		}
		System.out.println("--------");
	}

	private void printAnts(int[][] ants, double[][] distances) {
		System.out.println("----Ants----");
		for (int[] trail : ants) {
			printTrail(trail, distances);
		}
		System.out.println("--------");
	}
	
	private void printTrail(int[] trail, double[][] distances) {
		StringBuilder sb = new StringBuilder(getLength(trail, distances)
				+ ": ");
		for (int j : trail) {
			sb.append(j + ", ");
		}
		System.out.println(sb);
	}

}