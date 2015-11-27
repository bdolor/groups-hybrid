package main.java.GroupsHybrid.AntColony.Interfaces;

import java.util.List;

import main.java.GroupsHybrid.AntColony.Node;

public interface AntColony {

	public abstract List<Integer> solve(List<Node> nodes, int iterations,
			int antCount, double alpha, double beta, double rho, double Q);

}