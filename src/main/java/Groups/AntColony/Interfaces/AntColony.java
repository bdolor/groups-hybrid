package main.java.Groups.AntColony.Interfaces;

import java.util.List;

public interface AntColony {

	public abstract List<Integer> solve(int[] studentNodes, int iterations,
			int antCount, double alpha, double beta, double rho, double Q);

}