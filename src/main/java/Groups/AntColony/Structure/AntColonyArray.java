package main.java.Groups.AntColony.Structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import main.java.Groups.AntColony.Interfaces.AntColony;
import main.java.Groups.Data.StudentScores;
import main.java.Groups.Data.StudentGroups;

//Implementation similar to the one of James McCaffrey
//(http://msdn.microsoft.com/de-de/magazine/hh781027.aspx)
public class AntColonyArray implements AntColony {

	private StudentScores scores = new StudentScores();
	private StudentGroups groups = new StudentGroups();
	private double[] antsGh;

	@Override
	public List<Integer> solve(int[] studentNodes, int iterations, int antCount,
		double alpha, double beta, double rho, double Q) {
		this.antsGh = new double[antCount];

		double[][] distances = InitializerArray.computeDistances(studentNodes, scores);
		double[][] pheromones = InitializerArray.initPheromones(studentNodes);
		int[][] ants = InitializerArray.initAnts(antCount, studentNodes);
		int[] bestTrail = getBestTrail(ants);
		double bestGH = getSumGh(bestTrail);

		int count = 0;
		boolean end = false;
		while (!end) {
			updateTrails(ants, pheromones, distances, alpha, beta);
			updatePheromones(pheromones, ants, rho, Q);

			// get the best one
			int[] tmpBestTrail = getBestTrail(ants);
			double tmpBestGH = getSumGh(tmpBestTrail);
			System.out.println("find best trail begin");
			if (tmpBestGH > bestGH) {
				bestTrail = tmpBestTrail;
				bestGH = tmpBestGH;

				System.out.println(count + " -------------------------------------");
				printTrail(bestTrail);
			}

			if (++count >= iterations) {
				end = true;
			}
			printTrail(bestTrail);
			printisValid(bestTrail);
			//printEachGroupMaxDistance(bestTrail);

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
	 * Move the ants from one student to the next, starting with a random
	 * number between 0 and the number of students which serves as start
	 * position/next student ID that the ant begin to visit.
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
			// need the range of random numbers to be between 1 and 512
			int start = rand.nextInt(numStudents);
			int[] trail = buildTrail(start, pheromones, distances, alpha, beta);
			ants[i] = trail;
			this.antsGh[i] = this.getSumGh(ants[i]);
		}
	}

	/**
	 * Given a start position/student ID take an ant on a tour to build a
	 * trail
	 *
	 * @param start
	 * @param pheromones
	 * @param distances
	 * @param alpha
	 * @param beta
	 * @return trail - ant tour
	 */
	private int[] buildTrail(int start, double[][] pheromones,
		double[][] distances, double alpha, double beta) {
		int numStudents = pheromones.length;
		int[] trail = new int[numStudents];
		boolean[] visited = new boolean[numStudents];
		trail[0] = start;
		visited[start] = true;

		// when taking an ant on a tour, choosing where to go next is a 
		// combination of how big the ED distance is, how strong the pheromone level is
		// and if the student hasn't been visited before. 
		for (int i = 0; i < numStudents - 1; i++) {
			int next = nextStudent(trail[i], visited, pheromones, distances, alpha, beta);
			trail[i + 1] = next;
			visited[next] = true;
		}
		return trail;
	}

	/**
	 *
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

		int numStudents = pheromones.length;

		// create a sorted (ascending) list of probabilities for each 
		// student 
		// need to add  
		double[] cumul = new double[numStudents + 1];
		cumul[0] = 0.0;
		for (int i = 0; i < numStudents; i++) {
			cumul[i + 1] = cumul[i] + moveProbabilities[i];
		}
		// need the last one to be this val in case the next one is 511
		cumul[numStudents] = 1.0;

		// pick a random number, iterate through the sorted list
		// if that number is in between the current student and next student
		// return the index of that student.
		double position = new Random().nextDouble();
		for (int i = 0; i < numStudents; i++) {
			if (position >= cumul[i] && position < cumul[i + 1]) {
				return i;
			}
		}
		return -1;
	}

	/**
	 *
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
		int numStudents = pheromones.length;
		double[] taueta = new double[numStudents];
		double maxED = 14;
		double sum = 0.0;

		// calculate that probability that a student will be visited 
		// based on a meausurement of both pheremone level and distance 
		// sum all these measurements 
		for (int i = 0; i < numStudents; i++) {
			if (!visited[i]) {
				// need something where a bigger distance translates to 
				// a bigger probability
				taueta[i] = Math.pow(pheromones[studentId][i], alpha)
					* Math.pow((distances[studentId][i] / maxED), beta); // @TODO - not backed up by antying but convenience
				if (taueta[i] < 0.0001) {
					taueta[i] = 0.0001;
				} else if (taueta[i] > (Double.MAX_VALUE / (numStudents * 100))) {
					taueta[i] = Double.MAX_VALUE / (numStudents * 100);
				}
			}
			sum += taueta[i];
		}

		// divide each measurement of distance and pheromone 
		// by the sum of these measurements above
		double[] probabilities = new double[numStudents];
		for (int i = 0; i < numStudents; i++) {
			probabilities[i] = taueta[i] / sum;
		}
		return probabilities;
	}

	/**
	 * TODO - This is incredibly slow, needs to be optimized
	 *
	 * @param pheromones
	 * @param ants
	 * @param rho
	 * @param Q
	 */
	private void updatePheromones(double[][] pheromones, int[][] ants, double rho, double Q) {
		for (int i = 0; i < pheromones.length; ++i) {
			for (int j = i + 1; j < pheromones[i].length; ++j) {
				for (int k = 0; k < ants.length; ++k) {
					double length = this.antsGh[k];
					double decrease = (1.0 - rho) * pheromones[i][j];
					double increase = 0.0;
					if (isEdgeInTrail(i, j, ants[k])) {
						increase = (Q / length);
					}
					pheromones[i][j] = decrease + increase;
					pheromones[j][i] = pheromones[i][j];
				}
			}
		}
	}

	private void updatePheromonesGlobal(double[][] pheromones, int[] bestAnt) {

	}

	private boolean isEdgeInTrail(int start, int stop, int[] trail) {
		for (int i = 0; i < trail.length - 1; i++) {
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
		double sumGh = 0.0;

		for (int i = 0; i < scores.MAXIMUM_STUDENTS / 4; i++) {
			int s1 = trail[(i * 4)];
			int s2 = trail[(i * 4) + 1];
			int s3 = trail[(i * 4) + 2];
			int s4 = trail[(i * 4) + 3];

			double gh = scores.getGhValue(s1, s2, s3, s4);

			if (gh >= 0.5 && scores.getMaxDistance(s1, s2, s3, s4) > 2) {
				sumGh += gh;
			}
		}
		return sumGh;
	}

	private List<Integer> convertToList(int[] array) {
		List<Integer> result = new ArrayList<Integer>();
		for (int i : array) {
			result.add(i);
		}
		return result;
	}

	private void printTrail(int[] trail) {
		StringBuilder sb = new StringBuilder(getSumGh(trail)
			+ ": ");
		for (int i = 0; i < trail.length; i++) {

		}
//		for (int j : trail) {
//			sb.append(j + ", ");
//		}
		System.out.println(sb);
	}

	private void printisValid(int[] bestTrail) {
		if (false == groups.isValid(bestTrail)) {
			System.out.println("NOT all groups are valid");
		} else {
			System.out.println("ALL groups are valid");

		}
	}

	private void printEachGroupMaxDistance(int[] bestTrail) {
		StringBuilder bt = new StringBuilder(groups.getEachGroupMaxDistance(bestTrail)
			+ ": ");
		for (int j : bestTrail) {
			bt.append(j + ", ");
		}
		System.out.println(bt);
	}

}
