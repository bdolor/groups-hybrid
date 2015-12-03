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
	private int whatisit;

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
	 * Move the ants from one student to the next, starting with a 
	 * random number between 0 and the number of students which serves 
	 * as start position/next student ID that the ant begin to visit.
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
	 * Given a start position/student ID take an ant on a tour to build a trail 
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
		for (int i = 0; i < numStudents; i++) {
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

		int numStudents = pheromones.length;

		// create a sorted (ascending) list of probabilities for each 
		// student 
		// need to add  
		double[] cumul = new double[numStudents + 1];
		cumul[0] = 0.0;
		for (int i = 0; i < numStudents; i++) {
			cumul[i+1] = cumul[i] + moveProbabilities[i];
		}
		// need the last one to be this val in case the next one is 511
		cumul[numStudents] = 1.0;
		
		// pick a random number, iterate through the sorted list
		// if that number is in between the current student and next student
		// return the index of that student.
		double position = new Random().nextDouble();
		for (int i = 0; i < numStudents; i++) {
			if (position >= cumul[i] && position < cumul[i + 1]) {
				// @TODO - DOES THIS NEED TO BE I-1 TO RETURN STUDENT ID?
				return i;
			}
		this.whatisit = i;	
		}
		return -1;
	}

	/**
	 * TODO - LOOK AT THIS MEASUREMENT, LIKELY DIFFERENT FOR ED distance.
	 * NEEDS TO CHANGE
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
		double sum = 0.0;

		// calculate that probability that a student will be visited 
		// based on a meausurement of both pheremone level
		// and distance 
		// sum all these measurements 
		for (int i = 0; i < numStudents; i++) {
			if (!visited[i]) {
				taueta[i] = Math.pow(pheromones[studentId][i], alpha)
					* Math.pow((1.0 / distances[studentId][i]), beta);
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
