package main.java.Groups.AntColony.Interfaces;

public interface AntColony {

	public abstract boolean solve(int[] studentNodes, int iterations,
			int antCount, double alpha, double beta, double rho);

}