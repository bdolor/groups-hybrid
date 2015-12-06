package main.java.Groups.AntColony;
import main.java.Groups.AntColony.Interfaces.AntColony;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.java.Groups.AntColony.Structure.AntColonyArray;
import main.java.Groups.Data.StudentScores;


public class Program {

	private final static int MAX_ITERATIONS = 10000;
	private final static int ANTS = (int) Math.round(0.1 * StudentScores.MAXIMUM_STUDENTS);
	private final static double ALPHA = 0.1;//3.0;
	private final static double BETA = 2;//2.0;
	private final static double RHO = 0.1;//0.01;
	private final static double Q = 0.1;//2.0;
	
	public static void main(String[] args) {
		StudentScores scores = new StudentScores();
		int [] studentNodes = scores.getAllSummedScores();
		AntColony ac = new AntColonyArray();
		long start = System.currentTimeMillis();
		ac.solve(studentNodes, MAX_ITERATIONS, ANTS, ALPHA, BETA, RHO, Q);
		System.out.println("time: " + (System.currentTimeMillis() - start));
	}

}
