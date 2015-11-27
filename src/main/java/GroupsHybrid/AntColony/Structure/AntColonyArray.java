package main.java.GroupsHybrid.AntColony.Structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import main.java.GroupsHybrid.AntColony.Interfaces.AntColony;
import main.java.GroupsHybrid.Data.StudentScores;

//Implementation similar to the one of James McCaffrey
//(http://msdn.microsoft.com/de-de/magazine/hh781027.aspx)
public class AntColonyArray implements AntColony {

	private StudentScores scores = new StudentScores();

	@Override
	public List<Integer> solve(int[] studentNodes, int iterations, int antCount,
		double alpha, double beta, double rho, double Q) {

		double[][] distances = InitializerArray.computeDistances(studentNodes, scores);
		double[][] pheromones = InitializerArray.initPheromones(studentNodes);
		int[][] ants = InitializerArray.initAnts(antCount, studentNodes);
		int[] bestTrail = getBestTrail(ants);
		double bestGH = getSumGh(bestTrail);

		int count = 0;
		boolean end = false;
		while (!end) {
			updateTrails(ants, pheromones, distances, alpha, beta);
			updatePheromones(pheromones, ants, distances, rho, Q);

			int[] tmpBestTrail = getBestTrail(ants);
			double tmpBestGH = getSumGh(tmpBestTrail);
			if (tmpBestGH < bestGH) {
				bestTrail = tmpBestTrail;
				bestGH = tmpBestGH;

				System.out.println(count + " -------------------------------------");
				printTrail(bestTrail);
			}

			if (++count >= iterations) {
				end = true;
			}
		}
		System.out.println("\n-------------AntColonyArray------------------------");
		printTrail(bestTrail);
		System.out.println("ITERATIONS = " + iterations);
		System.out.println("ANTS = " + antCount);
		System.out.println("ALPHA = " + alpha);
		System.out.println("BETA = " + beta);
		System.out.println("RHO = " + rho);
		System.out.println("Q = " + Q);

		return convertToList(bestTrail);
	}

	/**
	 * TODO - make it work 
	 * 
	 * @param ants
	 * @param pheromones
	 * @param distances
	 * @param alpha
	 * @param beta 
	 */
	private void updateTrails(int[][] ants, double[][] pheromones,
		double[][] distances, double alpha, double beta) {
		Random rand = new Random();
		int numStudents = pheromones.length;
		for (int i = 0; i < ants.length; i++) {
			int start = rand.nextInt(numStudents);
			int[] trail = buildTrail(start, pheromones, distances, alpha, beta);
			ants[i] = trail;
		}
	}

	/**
	 * TODO - make it work 
	 * 
	 * @param start
	 * @param pheromones
	 * @param distances
	 * @param alpha
	 * @param beta
	 * @return 
	 */
	private int[] buildTrail(int start, double[][] pheromones,
		double[][] distances, double alpha, double beta) {
		int numStudents = pheromones.length;
		int[] trail = new int[numStudents];
		boolean[] visited = new boolean[numStudents];
		trail[0] = start;
		visited[start] = true;
		for (int i = 0; i < numStudents - 1; i++) {
			int next = nextStudent(trail[i], visited, pheromones, distances, alpha, beta);
			trail[i + 1] = next;
			visited[next] = true;
		}
		trail[numStudents] = trail[0];
		return trail;
	}

	/**
	 * TODO - make it work  
	 * 
	 * @param studentId
	 * @param visited
	 * @param pheromones
	 * @param distances
	 * @param alpha
	 * @param beta
	 * @return
	 */
	private int nextStudent(int studentId, boolean[] visited, double[][] pheromones,
		double[][] distances, double alpha, double beta) {
		double[] moveProbabilities = getMoveProbabilities(studentId, visited,
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

	/**
	 * TODO - make it work  
	 * 
	 * @param studentId
	 * @param visited
	 * @param pheromones
	 * @param distances
	 * @param alpha
	 * @param beta
	 * @return
	 */
	private double[] getMoveProbabilities(int studentId, boolean[] visited,
		double[][] pheromones, double[][] distances, double alpha, double beta) {
		int numCities = pheromones.length;
		double[] taueta = new double[numCities];
		double sum = 0.0;

		for (int i = 0; i < numCities; i++) {
			if (!visited[i]) {
				taueta[i] = Math.pow(pheromones[studentId][i], alpha)
					* Math.pow((1.0 / distances[studentId][i]), beta);
				if (taueta[i] < 0.0001) {
					taueta[i] = 0.0001;
				} else if (taueta[i] > (Double.MAX_VALUE / (numCities * 100))) {
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

	/**
	 * TODO - make it work  
	 * 
	 * @param pheromones
	 * @param ants
	 * @param distances
	 * @param rho
	 * @param Q 
	 */
	private void updatePheromones(double[][] pheromones, int[][] ants,
		double[][] distances, double rho, double Q) {
		for (int i = 0; i < pheromones.length; ++i) {
			for (int j = i + 1; j < pheromones[i].length; ++j) {
				for (int k = 0; k < ants.length; ++k) {
					double gh = getSumGh(ants[k]);
					double decrease = (1.0 - rho) * pheromones[i][j];
					double increase = 0.0;
					if (isEdgeInTrail(i, j, ants[k])) {
						increase = (Q / gh);
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

	/**
	 * A measurement of the best trail is the one with the highest GH
	 *
	 * @param ants
	 * @return
	 */
	private int[] getBestTrail(int[][] ants) {
		List<Double> gh = new ArrayList<Double>();
		for (int i = 0; i < ants.length; i++) {
			//lengths.add(getSumGh(ants[i]));
			gh.add(this.getSumGh(ants[i]));
		}
		Double maxGH = Collections.max(gh);
		return ants[gh.indexOf(maxGH)];
	}

	/**
	 * Given an ant trail, it will return the sumGH of that trail
	 * 
	 * @param trail
	 * @return
	 */
	private double getSumGh(int[] trail) {
		int sumGh = 0;

		for (int i = 0; i < scores.MAXIMUM_STUDENTS / 4; i++) {
			int s1 = trail[(i * 4)];
			int s2 = trail[(i * 4) + 1];
			int s3 = trail[(i * 4) + 2];
			int s4 = trail[(i * 4) + 3];

			if (scores.getGhValue(s1, s2, s3, s4) >= 0.5 && scores.getMaxDistance(s1, s2, s3, s4) > 2) {
				sumGh += scores.getGhValue(s1, s2, s3, s4);
			}
		}
		return (double) sumGh;
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

	private void printAnts(int[][] ants) {
		System.out.println("----Ants----");
		for (int[] trail : ants) {
			printTrail(trail);
		}
		System.out.println("--------");
	}

	private void printTrail(int[] trail) {
		StringBuilder sb = new StringBuilder(getSumGh(trail)
			+ ": ");
		for (int j : trail) {
			sb.append(j + ", ");
		}
		System.out.println(sb);
	}

}
