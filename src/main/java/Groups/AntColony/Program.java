package main.java.Groups.AntColony;

import main.java.Groups.AntColony.Interfaces.AntColony;

import main.java.Groups.AntColony.Structure.AntColonyArray;
import main.java.Groups.Data.StudentScores;

public class Program {

	private final static int MAX_ITERATIONS = 1500;
	private final static int ANTS = (int) Math.round(0.02 * StudentScores.MAXIMUM_STUDENTS);// 10 ants
	private final static double ALPHA = 2.3;// used in probability calculation, increasing this number decreases result (0.108 ^ 0.1 = 0.8) vs (0.108 ^ 3.0 = 0.0012)
	private final static double BETA = 0.1;// used in probability calculation, increasing this number decreases result (0.49 ^ 0.1 = 0.93) vs (0.49 ^ 2.0 = 0.24)
	// the combined measurement of both the above values becomes a denominator, the bigger the denominator, the smaller the probability (0.744 / 293 = 0.0025) vs (0.744 / 193 = 0.0038)
	private final static double RHO = 0.06;// used in pheromone calcuation, mostly affects evaporation rate

	public static void main(String[] args) {
		System.out.println("COMP-658 Computational Intelligence - Assignment 4");
		System.out.println("Athabasca University");
		System.out.println();

		System.out.println("Tool for configuring, executing and monitoring ACO to optimize student group heterogenity.");
		System.out.println();

		long start = System.currentTimeMillis();

		//Program.run();
		Program.runRandomParameterSearch();
		//Program.runSystematicBetaParameterSearch();
		//Program.runSystematicAlphaParameterSearch();
		//Program.runSystematicRhoParameterSearch();

		System.out.println("time: " + (System.currentTimeMillis() - start));
	}

	private static void run() {
		AntColony ac = new AntColonyArray();
		StudentScores scores = new StudentScores();
		int[] studentNodes = scores.getAllSummedScores();

		boolean validSolution = false;

		do {
			validSolution = ac.solve(studentNodes, MAX_ITERATIONS, ANTS, ALPHA, BETA, RHO);

		} while (validSolution == false);
	}

	private static void runRandomParameterSearch() {
		AntColony ac = new AntColonyArray();
		StudentScores scores = new StudentScores();
		int[] studentNodes = scores.getAllSummedScores();

		double[] variables = new double[]{0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2.0, 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8, 2.9, 3.0};
		double[] evaporate = new double[]{0.001, 0.002, 0.003, 0.004, 0.005, 0.006, 0.007, 0.008, 0.009, 0.01, 0.02, 0.03, 0.04, 0.05, 0.06, 0.07, 0.08, 0.09, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
		int max = variables.length;
		int start = 0;

		do {
			int rand1 = (int) (Math.random() * variables.length);
			int rand2 = (int) (Math.random() * variables.length);
			int rand3 = (int) (Math.random() * evaporate.length);

			ac.solve(studentNodes, MAX_ITERATIONS, ANTS, variables[rand1], variables[rand2], evaporate[rand3]);
			start++;

		} while (start != max);
	}

	private static void runSystematicBetaParameterSearch() {
		AntColony ac = new AntColonyArray();
		StudentScores scores = new StudentScores();
		int[] studentNodes = scores.getAllSummedScores();
		double[] variables = new double[]{0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2.0, 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8, 2.9, 3.0};
		int max = variables.length;
		int start = 0;
		do {

			ac.solve(studentNodes, MAX_ITERATIONS, ANTS, 0.1, variables[start], RHO);
			start++;

		} while (start != max);
	}

	private static void runSystematicAlphaParameterSearch() {
		AntColony ac = new AntColonyArray();
		StudentScores scores = new StudentScores();
		int[] studentNodes = scores.getAllSummedScores();
		double[] variables = new double[]{0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9, 2.0, 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8, 2.9, 3.0};
		int max = variables.length;
		int start = 0;

		do {

			ac.solve(studentNodes, MAX_ITERATIONS, ANTS, variables[start], 0.1, RHO);
			start++;

		} while (start != max);
	}

	private static void runSystematicRhoParameterSearch() {
		AntColony ac = new AntColonyArray();
		StudentScores scores = new StudentScores();
		int[] studentNodes = scores.getAllSummedScores();
		double[] evaporate = new double[]{0.001, 0.002, 0.003, 0.004, 0.005, 0.006, 0.007, 0.008, 0.009, 0.01, 0.02, 0.03, 0.04, 0.05, 0.06, 0.07, 0.08, 0.09, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
		int max = evaporate.length;
		int start = 0;
		do {

			ac.solve(studentNodes, MAX_ITERATIONS, ANTS, 0.1, 0.1, evaporate[start]);
			start++;

		} while (start != max);
	}
}
