package main.java.Groups.AntColony;
import main.java.Groups.AntColony.Interfaces.AntColony;

import main.java.Groups.AntColony.Structure.AntColonyArray;
import main.java.Groups.Data.StudentScores;


public class Program {

	private final static int MAX_ITERATIONS = 200;
	private final static int ANTS = (int) Math.round(0.02 * StudentScores.MAXIMUM_STUDENTS);// 10 ants
	private final static double ALPHA = 0.1;// was 3.0; used in probability calculation
	private final static double BETA = 0.1;// was 2.0 used in probability calculation
	private final static double RHO = 0.1;// was 0.01; used in pheromone calcuation
	private final static double Q = 0.9;// was 2.0; used in pheromone calculation
	
	public static void main(String[] args) {
		StudentScores scores = new StudentScores();
		int [] studentNodes = scores.getAllSummedScores();
		AntColony ac = new AntColonyArray();
		long start = System.currentTimeMillis();
		
		ac.solve(studentNodes, MAX_ITERATIONS, ANTS, ALPHA, BETA, RHO, Q);
		System.out.println("time: " + (System.currentTimeMillis() - start));
	}

}
