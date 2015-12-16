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
		int bestTrailIndex = getBestTrail(ants);
		int[] bestTrail = ants[bestTrailIndex];
		double bestGH = getSumGh(bestTrail);

		System.out.println("0 -------------------------------------");
				printTrail(bestTrail);
				printisValid(bestTrail);
				printEachGroupMaxDistance(bestTrail);
				printEachGroupGh(bestTrail);
				
		int count = 0;
		boolean end = false;
		while (!end) {
			updateTrails(ants, pheromones, distances, alpha, beta);

			// get the best one
			int tmpBestTrail = getBestTrail(ants);
			updatePheromones(pheromones, ants, rho, Q);

			double tmpBestGH = getSumGh(ants[tmpBestTrail]);
			if (tmpBestGH > bestGH) {
				bestTrail = ants[tmpBestTrail];
				bestGH = tmpBestGH;

				System.out.println(count + " -------------------------------------");
				printTrail(bestTrail);
				printisValid(bestTrail);
				printEachGroupMaxDistance(bestTrail);
				printEachGroupGh(bestTrail);
				//printPheromones(pheromones);
			}

			if (++count >= iterations) {
				end = true;
			}

			//printTrail(bestTrail);
			System.out.println("iteration: " + count);

		}
		System.out.println("\n-------------AntColonyArray------------------------");
		printTrail(bestTrail);
		printisValid(bestTrail);
		printEachGroupMaxDistance(bestTrail);
		printEachGroupGh(bestTrail);
		printPheromones(pheromones);

		System.out.println("ITERATIONS = " + iterations);

		return convertToList(bestTrail);
	}

	/**
	 * Move the ants from one student to the next, starting with a random
	 * number between 0 and the number of students which serves as start
	 * random/next student ID that the ant begin to visit.
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
	 * Given a start random/student ID take an ant on a tour to build a
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
		int[] allScores = scores.getAllSummedScores();
		ArrayList<Integer> groupScore = new ArrayList<>();

		// when taking an ant on a tour, choosing where to go next is a 
		// combination of how big the ED distance is, how strong the pheromone level is
		// and if the student hasn't been visited before. 
		for (int i = 0; i < numStudents - 1; i++) {
			// find out the score of the student
			int stuScore = allScores[trail[i]];
			groupScore.add(stuScore);
			int next = nextStudent(trail[i], visited, pheromones, distances, alpha, beta, groupScore);
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
		double[][] distances, double alpha, double beta, ArrayList groupScore) {
		double[] moveProbabilities = getMoveProbabilities(studentId, visited,
			pheromones, distances, alpha, beta);
		int numStudents = pheromones.length;
		
		int[] allScores = scores.getAllSummedScores();

		// 20% of the time, return the student that will ensure the best GH
		if (groupScore.size() % 4 == 3) {
			//measure the gh of the group
			int score1 = allScores[groupScore.size() - 1];
			int score2 = allScores[groupScore.size() - 2];
			int score3 = allScores[groupScore.size() - 3];

			double bestGh = 0.0;
			int bestStudentId = 0; 
			// pick the next student based solely on what will give the 
			// group the highest GH score.
			
			for ( int i = 0; i < allScores.length; i++ ){
				// get the potential GH
				if ( ! visited[i] ){
					double tmp = scores.getGhValue(score1, score2, score3, allScores[i]);
					if ( tmp > bestGh) {
						bestGh = tmp;
						bestStudentId = i;
					}
				}
				
			}
			return bestStudentId;
		}
		//
		double rand = new Random().nextDouble();

		double tot = 0;
		for (int i = 0; i < numStudents; i++) {
			tot += moveProbabilities[i];
			if (tot >= rand) {
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
		double[] combined = new double[numStudents];
		double maxED = 5.29;
		double sum = 0.0;

		// calculate that probability that a student will be visited 
		// based on a meausurement of both pheremone level and distance 
		for (int i = 0; i < numStudents; i++) {
			if (!visited[i]) {
				// a bigger distance  combined with a bigger pheremone trail
				combined[i] = Math.pow(pheromones[studentId][i], alpha)
					* Math.pow((distances[studentId][i] / maxED), beta); // ED distance represented as fraction of Maximum ED
				if (combined[i] < 0.0001) {
					combined[i] = 0.0001;
				}
			}
			// sum all these measurements
			sum += combined[i];
		}

		// divide each measurement of distance and pheromone 
		// by the sum of these measurements above
		double[] probabilities = new double[numStudents];
		for (int i = 0; i < numStudents; i++) {
			probabilities[i] = combined[i] / sum;
		}
		return probabilities;
	}

	/**
	 *
	 *
	 * @param pheromones
	 * @param ants
	 * @param rho
	 * @param Q
	 */
	private void updatePheromones(double[][] pheromones, int[][] ants, double rho, double Q) {
		// if all 128 groups had a maxGH of 14
		int maxSumGh = 1792;

		for (int i = 0; i < pheromones.length / 4; ++i) {
			for (int j = i + 1; j < pheromones[i].length / 4; ++j) {
				// would like to break it up into groups
				for (int k = 0; k < ants.length; ++k) {
					// stored value for sumGH saves repetitive computation
					double length = this.antsGh[k];
					// every trail recieves a bit of evaporation
					double evaporate1 = (1.0 - rho) * pheromones[i * 4][j * 4];
					double evaporate2 = (1.0 - rho) * pheromones[i * 4][(j * 4) + 1];
					double evaporate3 = (1.0 - rho) * pheromones[i * 4][(j * 4) + 2];
					double evaporate4 = (1.0 - rho) * pheromones[(i * 4) + 1][(j * 4) + 1];
					double evaporate5 = (1.0 - rho) * pheromones[(i * 4) + 1][(j * 4) + 2];
					double evaporate6 = (1.0 - rho) * pheromones[(i * 4) + 2][(j * 4) + 2];

					double increase1, increase2, increase3, increase4, increase5, increase6;
					increase1 = increase2 = increase3 = increase4 = increase5 = increase6 = 0.0;

					// only increase paths between students in a group
					int stu1 = ants[k][(i * 4)];
					int stu2 = ants[k][(i * 4) + 1];
					int stu3 = ants[k][(i * 4) + 2];
					int stu4 = ants[k][(i * 4) + 3];

					// only increase pheromones if it's a valid group
					if (scores.getMaxDistance(stu1, stu2, stu3, stu4) > 2 && scores.getGhValue(stu1, stu2, stu3, stu4) >= 0.5) {

						// student1 and student2
						if (isEdgeInTrail(stu1, stu2, ants[k])) {
							increase1 = (length / maxSumGh);
						}
						// student1 and student3
						if (isEdgeInTrail(stu1, stu3, ants[k])) {
							increase2 = (length / maxSumGh);
						}
						// student1 and student4
						if (isEdgeInTrail(stu1, stu4, ants[k])) {
							increase3 = (length / maxSumGh);
						}
						// student2 and student3
						if (isEdgeInTrail(stu2, stu3, ants[k])) {
							increase4 = (length / maxSumGh);
						}
						// student2 and student4
						if (isEdgeInTrail(stu2, stu4, ants[k])) {
							increase5 = (length / maxSumGh);
						}
						// student3 and student4
						if (isEdgeInTrail(stu3, stu4, ants[k])) {
							increase6 = (length / maxSumGh);
						}
					}
					//student 1 to 2
					pheromones[i * 4][j * 4] = evaporate1 + increase1;
					pheromones[j * 4][i * 4] = pheromones[i * 4][j * 4];
//					// student1 to 3
					pheromones[i * 4][(j * 4) + 1] = evaporate2 + increase2;
					pheromones[(j * 4) + 1][i * 4] = pheromones[i * 4][(j * 4) + 1];
					// student1 to 4
					pheromones[i * 4][(j * 4) + 2] = evaporate3 + increase3;
					pheromones[(j * 4) + 2][i * 4] = pheromones[i * 4][(j * 4) + 2];
					// student2 to 3
					pheromones[(i * 4) + 1][(j * 4) + 1] = evaporate4 + increase4;
					pheromones[(j * 4) + 1][(i * 4) + 1] = pheromones[(i * 4) + 1][(j * 4) + 1];
					// student2 to 4
					pheromones[(i * 4) + 1][(j * 4) + 2] = evaporate5 + increase5;
					pheromones[(j * 4) + 2][(i * 4) + 1] = pheromones[(i * 4) + 1][(j * 4) + 2];
					// student3 to 4
					pheromones[(i * 4) + 2][(j * 4) + 2] = evaporate6 + increase6;
					pheromones[(j * 4) + 2][(i * 4) + 2] = pheromones[(i * 4) + 2][(j * 4) + 2];
				}
			}
		}
	}

	/**
	 * updates trails with the best solution
	 *
	 * @param start
	 * @param stop
	 * @param trail
	 * @return
	 */
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
	private int getBestTrail(int[][] ants) {
		List<Double> gh = new ArrayList<Double>();
		for (int i = 0; i < ants.length; i++) {
			gh.add(this.getSumGh(ants[i]));
		}
		Double maxGH = Collections.max(gh);
		return gh.indexOf(maxGH);
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
		for (int j : trail) {
			sb.append(j + ", ");
		}
		System.out.println(sb);
	}

	private void printisValid(int[] bestTrail) {
		if (false == groups.isValid(bestTrail)) {
			System.out.println("All valid groups: FALSE");
		} else {
			System.out.println("All valid groups: TRUE");

		}
	}

	private void printEachGroupMaxDistance(int[] bestTrail) {
		StringBuilder md = new StringBuilder("Group Max ED: ");
		double[] g = groups.getEachGroupMaxDistance(bestTrail);
		for (double j : g) {
			md.append(j + ", ");
		}
		System.out.println(md);
	}

	private void printEachGroupGh(int[] bestTrail) {
		StringBuilder gh = new StringBuilder("Group GH: ");
		double[] g = groups.getEachGroupGH(bestTrail);
		for (double j : g) {
			gh.append(j + ", ");
		}
		System.out.println(gh);
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
}
