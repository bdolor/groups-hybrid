package main.java.Groups.AntColony.Structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.Groups.AntColony.Interfaces.AntColony;
import main.java.Groups.Data.StudentScores;
import main.java.Groups.Data.StudentGroups;

public class AntColonyArray implements AntColony {

	private StudentScores scores = new StudentScores();
	private StudentGroups groups = new StudentGroups();
	private double[] antsGh;
	private int validGroups;
	private double avgGh;
	private double sumEd;

	@Override
	public boolean solve(int[] studentNodes, int iterations, int antCount,
		double alpha, double beta, double rho) {
		this.antsGh = new double[antCount];

		double[][] distances = InitializerArray.computeDistances(studentNodes, scores);
		double[][] pheromones = InitializerArray.initPheromones(studentNodes);
		int[][] ants = InitializerArray.initAnts(antCount, studentNodes);
		int bestTrailIndex = getBestTrail(ants);
		int[] bestTrail = ants[bestTrailIndex];
		int[][] bestValidTrail = new int [1][bestTrail.length];
		boolean validSolutionFound = false;
		double bestGH = getSumGh(bestTrail);

		System.out.println("START ********************************************* | number of ants: " + antCount);
		System.out.println("ALPHA: " + alpha + " BETA: " + beta + " RHO: " + rho);

		int count = 0;
		boolean end = false;
		int stale = 0;
		while (!end) {
			this.validGroups = 0;
			try {
				updateTrails(ants, pheromones, distances, alpha, beta);
			} catch (Exception ex) {
				ex.getMessage();
				Logger.getLogger(AntColonyArray.class.getName()).log(Level.SEVERE, null, ex);
			}

			// get the best one
			int tmpBestTrail = getBestTrail(ants);
			// evaporate
			evaporatePheromones(pheromones, rho);
			// update edges of all valid groups for all ant trails
			increasePheromones(pheromones, ants, rho);

			double tmpBestGH = getSumGh(ants[tmpBestTrail]);

			// record if it's the best
			if (tmpBestGH > bestGH) {
				stale = 0;
				bestTrail = ants[tmpBestTrail];
				bestGH = tmpBestGH;
				System.out.println("sumGH = " + bestGH);

				// only print it out if it's valid
				if (groups.isValid(bestTrail)) {
					System.out.println("\nVALID SOLUTION at " + count);
					System.out.println("ALL VALID GROUPS: TRUE");
					printTrail(bestTrail);
					System.out.println("GROUP MAX ED SUM: " + this.sumEd);
					printEachGroupMaxDistance(bestTrail);
					printEachGroupGh(bestTrail);
					System.out.println("------------------------------------- | Continuing to  search -");
					// save for later
					bestValidTrail[0] = bestTrail;
					validSolutionFound = true;
					// re-inforce the best solution 
					increasePheromones( pheromones, bestValidTrail, rho );
					
				}
			}
			// check on the terminating condition
			if (++count >= iterations || (stale > 50 && stale > (count * 0.66))) {
				end = true;
			}

			System.out.print(".");
			if (stale != 0 && stale % 75 == 0) {
				System.out.print("\n");
			}
			//System.out.println("iteration: " + count + " | valid groups: " + validGroups + "/" + (antCount * 128) + " | avg sumGH " + this.avgGh );
			stale++;

		}
		if ( validSolutionFound ){
			bestTrail = bestValidTrail[0];
		}
		
		System.out.println("\n ------------------------ Final Ant Colony Array ------------------------");
		printisValid(bestTrail);
		System.out.println("ALPHA: " + alpha + " BETA: " + beta + " RHO: " + rho);
		
		if (validSolutionFound) {
			printValidPretty(bestTrail);
		} else {
			printTrail(bestTrail);
			System.out.println("GROUP MAX ED SUM: " + this.sumEd);
			printEachGroupMaxDistance(bestTrail);
			printEachGroupGh(bestTrail);
			//printPheromones(pheromones);
		}

		System.out.println("ITERATIONS = " + count);
		System.out.println("");
		System.out.println("********************************************* | END");
		System.out.println("");
		System.out.println("");
		
		return validSolutionFound;
		
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
		double[][] distances, double alpha, double beta) throws Exception {
		Random rand = new Random();
		int numStudents = pheromones.length;
		for (int i = 0; i < ants.length; i++) {
			// need the range of random numbers to be between 0 and 511
			int start = rand.nextInt(numStudents - 1);
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
		double[][] distances, double alpha, double beta) throws Exception {
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
			int next = nextStudent(trail[i], visited, pheromones, distances, alpha, beta, groupScore, trail);
			trail[i + 1] = next;
			visited[next] = true;
			
		}
		return trail;
	}

	/**
	 *
	 * @param studentId
	 * @param visited
	 * @param pheromones
	 * @param distances
	 * @param alpha
	 * @param beta
	 * @param groupScore
	 * @param trail
	 * @return
	 */
	private int nextStudent(int studentId, boolean[] visited, double[][] pheromones,
		double[][] distances, double alpha, double beta, ArrayList groupScore, int[] trail) throws Exception {

		double rand = new Random().nextDouble();
		int numStudents = pheromones.length;
		int[] allScores = scores.getAllSummedScores();
		double[] moveProbabilities = getMoveProbabilities(studentId, visited,
			pheromones, distances, alpha, beta, allScores);

		// randomly chosen edge, for new group formation
		if (groupScore.size() % 4 == 0) {
			// pick a number between 0 and 511
			for (int i = 0; i < numStudents; i++) {
				int randomStudent = (int) (Math.random() * (numStudents - 1));
				if (!visited[randomStudent]) {
					return randomStudent;
				}
			}

		}

		// Exploit! 20% of the time, return the student that will ensure the best GH
		if (groupScore.size() % 4 == 3) {
			//measure the gh of the group
			int score1 = allScores[groupScore.size() - 1];
			int score2 = allScores[groupScore.size() - 2];
			int score3 = allScores[groupScore.size() - 3];

			double bestGh = 0.0;
			int bestStudentId = 0;
			int validGroupStudentId = allScores.length + 100; //something out of range

			// pick the next student based on what will give the 
			// group the highest GH score.
			for (int i = 0; i < numStudents; i++) {
				// get the potential GH
				if (!visited[i]) {
					double tmpGh = scores.getGhValue(score1, score2, score3, allScores[i]);
					double maxEd = scores.getMaxDistance(trail[trail.length - 1], trail[trail.length - 2], trail[trail.length - 3], i);
					// look for the best, there's always one winner 
					if (tmpGh > bestGh) {
						bestGh = tmpGh;
						bestStudentId = i;
					}
					// even better if it's valid
					if (tmpGh > 0.5 && maxEd > 2.0) {
						validGroupStudentId = i;
					}
				}
				// take the valid over the best if it's available
				if (validGroupStudentId != allScores.length + 100) {
					bestStudentId = validGroupStudentId;
				}

			}
			// return best student
			return bestStudentId;
		}

		// Explore!
		double tot = 0;
		for (int i = 0; i < numStudents; i++) {
			tot += moveProbabilities[i];
			if (tot >= rand) {
				return i;
			}
		}
		// if you get here, that's not good
		throw new Exception("evaporation rate likely too strong");
	}

	/**
	 *
	 * @param studentId
	 * @param visited
	 * @param pheromones
	 * @param distances
	 * @param alpha
	 * @param beta
	 * @param allScores
	 * @return
	 */
	private double[] getMoveProbabilities(int studentId, boolean[] visited,
		double[][] pheromones, double[][] distances, double alpha, double beta, int[] allScores) {
		int numStudents = pheromones.length;
		double[] combined = new double[numStudents];
		double maxED = 5.29;
		double sum = 0.0;

		// calculate that probability that a student will be visited 
		// based on a meausurement of both pheremone level and distance 
		for (int i = 0; i < numStudents; i++) {
			if (!visited[i]) {
				// a bigger distance combined with a bigger pheremone trail
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
	 * @param pheromones
	 * @param ants
	 */
	private void increasePheromones(double[][] pheromones, int[][] ants, double rho) {

		// set up for increase routine
		double increase1, increase2, increase3, increase4, increase5, increase6, phere1, phere2,
			phere3, phere4, phere5, phere6;
		increase1 = increase2 = increase3 = increase4 = increase5 = increase6 = phere1 = phere2
			= phere3 = phere4 = phere5 = phere6 = 0.0;

		// loop through each ant trail and update edges only if a valid group has been formed.
		for (int k = 0; k < ants.length; ++k) {
			for (int s = 0; s < pheromones.length / 4; s++) {
				// stored value for sumGH saves repetitive computation
				double sumGh = this.antsGh[k];
				double maxGh = 14;

				// only increase paths between students in a group
				int stu1 = ants[k][(s * 4)];
				int stu2 = ants[k][(s * 4) + 1];
				int stu3 = ants[k][(s * 4) + 2];
				int stu4 = ants[k][(s * 4) + 3];

				// ensures an increase in pheromones only if it's a valid group
				double strength = scores.getGhValue(stu1, stu2, stu3, stu4);
				if (scores.getMaxDistance(stu1, stu2, stu3, stu4) > 2 && strength >= 0.5) {
					this.validGroups++;
					// increases pheremone levels as a function of how strong the GH level is
					// student1 and student2
					if (isEdgeInTrail(stu1, stu2, ants[k])) {
						phere1 = pheromones[stu1][stu2];
						increase1 = (strength / maxGh);
					}
					// student1 and student3
					if (isEdgeInTrail(stu1, stu3, ants[k])) {
						phere2 = pheromones[stu1][stu3];
						increase2 = (strength / maxGh);
					}
					// student1 and student4
					if (isEdgeInTrail(stu1, stu4, ants[k])) {
						phere3 = pheromones[stu1][stu4];
						increase3 = (strength / maxGh);
					}
					// student2 and student3
					if (isEdgeInTrail(stu2, stu3, ants[k])) {
						phere4 = pheromones[stu2][stu3];
						increase4 = (strength / maxGh);
					}
					// student2 and student4
					if (isEdgeInTrail(stu2, stu4, ants[k])) {
						phere5 = pheromones[stu2][stu4];
						increase5 = (strength / maxGh);
					}
					// student3 and student4
					if (isEdgeInTrail(stu3, stu4, ants[k])) {
						phere6 = pheromones[stu1][stu4];
						increase6 = (strength / maxGh);
					}
					//student 1 to 2
					pheromones[stu1][stu2] = phere1 + increase1;
					pheromones[stu2][stu1] = pheromones[stu1][stu2];
					// student1 to 3
					pheromones[stu1][stu3] = phere2 + increase2;
					pheromones[stu3][stu1] = pheromones[stu1][stu3];
					// student1 to 4
					pheromones[stu1][stu4] = phere3 + increase3;
					pheromones[stu4][stu1] = pheromones[stu1][stu4];
					// student2 to 3
					pheromones[stu2][stu3] = phere4 + increase4;
					pheromones[stu3][stu2] = pheromones[stu2][stu3];
					// student2 to 4
					pheromones[stu2][stu4] = phere5 + increase5;
					pheromones[stu4][stu2] = pheromones[stu2][stu4];
					// student3 to 4
					pheromones[stu3][stu4] = phere6 + increase6;
					pheromones[stu4][stu3] = pheromones[stu3][stu4];

				} else { // discourage invalid groups
					//student 1 to 2
					pheromones[stu1][stu2] = (1.0 - rho) * pheromones[stu1][(stu2)];
					pheromones[stu2][stu1] = pheromones[stu1][stu2];
					// student1 to 3
					pheromones[stu1][stu3] = (1.0 - rho) * pheromones[stu1][(stu3)];
					pheromones[stu3][stu1] = pheromones[stu1][stu3];
					// student1 to 4
					pheromones[stu1][stu4] = (1.0 - rho) * pheromones[stu1][(stu4)];
					pheromones[stu4][stu1] = pheromones[stu1][stu4];
					// student2 to 3
					pheromones[stu2][stu3] = (1.0 - rho) * pheromones[stu2][(stu3)];
					pheromones[stu3][stu2] = pheromones[stu2][stu3];
					// student2 to 4
					pheromones[stu2][stu4] = (1.0 - rho) * pheromones[stu2][(stu4)];
					pheromones[stu4][stu2] = pheromones[stu2][stu4];
					// student3 to 4
					pheromones[stu3][stu4] = (1.0 - rho) * pheromones[stu3][(stu4)];
					pheromones[stu4][stu3] = pheromones[stu3][stu4];
				}
			}
		}
	}

	/**
	 *
	 * @param pheromones
	 * @param ants
	 * @param rho
	 */
	private void evaporatePheromones(double[][] pheromones, double rho) {

		for (int i = 0; i < pheromones.length; ++i) {
			// break it up into groups
			for (int j = 0; j < pheromones[i].length / 4; ++j) {
				int s = 0;
				// ensures that every trail recieves a bit of evaporation
				double evaporateAll1 = (1.0 - rho) * pheromones[i][(j * 4)];
				double evaporateAll2 = (1.0 - rho) * pheromones[i][(j * 4) + 1];
				double evaporateAll3 = (1.0 - rho) * pheromones[i][(j * 4) + 2];
				double evaporateAll4 = (1.0 - rho) * pheromones[i][(j * 4) + 3];

				// evaporate these
				pheromones[i][(j * 4)] = evaporateAll1;
				pheromones[(j * 4)][i] = pheromones[i][(j * 4)];
					
				pheromones[i][(j * 4) + 1] = evaporateAll2;
				pheromones[(j * 4) + 1][i] = pheromones[i][(j * 4) + 1];

				pheromones[i][(j * 4) + 2] = evaporateAll3;
				pheromones[(j * 4) + 2][i] = pheromones[i][(j * 4) + 2];

				pheromones[(i)][(j * 4) + 3] = evaporateAll4;
				pheromones[(j * 4) + 3][i] = pheromones[i][(j * 4) + 3];

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
		List<Double> gh = new ArrayList<>();
		double sumGh = 0;

		for (int[] ant : ants) {
			gh.add(this.getSumGh(ant));
			sumGh += this.getSumGh(ant);
		}
		Double maxGH = Collections.max(gh);
		this.avgGh = sumGh / ants.length;
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

			sumGh += gh;

		}
		return sumGh;
	}

	/**
	 *
	 * @param trail
	 */
	private void printTrail(int[] trail) {
		StringBuilder sb = new StringBuilder("SUM GH: " + getSumGh(trail)
			+ "\nMEMBERS: ");
		for (int i = 0; i < trail.length; i++) {

		}
		for (int j : trail) {
			sb.append(j + ", ");
		}
		System.out.println(sb);
	}

	/**
	 *
	 * @param bestTrail
	 */
	private void printisValid(int[] bestTrail) {
		if (false == groups.isValid(bestTrail)) {
			System.out.println("ALL VALID GROUPS: FALSE");
		} else {
			System.out.println("ALL VALID GROUPS: TRUE");

		}
	}

	/**
	 *
	 * @param bestTrail
	 */
	private void printEachGroupMaxDistance(int[] bestTrail) {
		StringBuilder md = new StringBuilder("EACH GROUP MAX ED: ");
		double[] g = groups.getEachGroupMaxDistance(bestTrail);
		double sumMaxEd = 0;
		for (double j : g) {
			sumMaxEd += j;
			md.append(j + ", ");
		}
		this.sumEd = sumMaxEd;
		System.out.println(md);
	}

	/**
	 *
	 * @param bestTrail
	 */
	private void printEachGroupGh(int[] bestTrail) {
		StringBuilder gh = new StringBuilder("EACH GROUP GH: ");
		double[] g = groups.getEachGroupGH(bestTrail);
		for (double j : g) {
			gh.append(j + ", ");
		}
		System.out.println(gh);
	}

	/**
	 *
	 * @param pheromones
	 */
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
	
	private void printValidPretty(int[] bestTrail ){
		StringBuilder output = new StringBuilder();
		double[] gh = groups.getEachGroupGH(bestTrail);
		double[] ed = groups.getEachGroupMaxDistance(bestTrail);
		System.out.println("SUM GH: " + getSumGh(bestTrail) + "\n ----------\n");
		for ( int i = 0; i < bestTrail.length / 4; i++ ){
			output.append("Group " + (i+1) + 
				"\nMembers: " + bestTrail[i*4] + ", " + bestTrail[(i*4)+1] + ", " + bestTrail[(i*4)+2] + ", " + bestTrail[(i*4)+3] +
				"\nGH: " + gh[i] + 
				"\nED: " + ed[i] + 
				"\n ----------\n" );
		}
		System.out.println(output);
	}
}
