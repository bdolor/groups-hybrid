package main.java.GroupsHybrid.GeneticAlgorithm.GroupEncoding;


import java.util.ArrayList;
import java.util.Stack;

import main.java.GroupsHybrid.Data.StudentScores;
import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.IChromosome;
import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.IFactory;
import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.IStudentChromosome;

public class GroupEncodingChromosome implements IChromosome<int[]>, IFactory<GroupEncodingChromosome>, IStudentChromosome {

	public final static int MAXIMUM_STUDENTS = 512;

	private int[] Encoding;
	private Double fitness = null;
	private StudentScores scores = new StudentScores();

	public GroupEncodingChromosome() {
		this.setEncoding(this.getRandomEncoding());
		this.applyCorrection();
	}
	/**
	 * Returns a value other than zero on the condition that it is a valid group
	 * @return double sumGh
	 */
	@Override
	public double getFitness() {		
		double sumGh;
		
		if (fitness == null) {
			sumGh = 0d;
				ArrayList<Stack<Integer>> groups = this.getGroupStacks();
				for (Stack<Integer> group : groups) {
					sumGh += this.scores.getGhValue(group.get(0), group.get(1), group.get(2), group.get(3));
				}			
			this.fitness = sumGh;
		}		
		
		return this.fitness;
	}

	@Override
	public int[] getEncoding() {
		return this.Encoding;
	}

	public void setEncoding(int[] encoding) {
		this.fitness = null;
		this.Encoding = encoding;
	}

	public boolean isValid() {
		boolean ret = true;
		ArrayList<Stack<Integer>> groups = this.getGroupStacks();		
		for (Stack<Integer> group : groups) {
			if ((this.scores.getMaxDistance(group.get(0), group.get(1), group.get(2), group.get(3)) <= 2)
					|| this.scores.getGhValue(group.get(0), group.get(1), group.get(2), group.get(3)) <= 0.5) {
				ret = false;
			}
		}

		return ret;
	}

	public void applyCorrection() {
		this.setEncoding(this.getCorrectedEncoding());
	}

	protected int[] getRandomEncoding() {
		int[] encoding = new int[GroupEncodingChromosome.MAXIMUM_STUDENTS];
		for (int i = 0; i < GroupEncodingChromosome.MAXIMUM_STUDENTS; i++) {
			encoding[i] = (int) (1 + Math.random() * (GroupEncodingChromosome.MAXIMUM_STUDENTS / 4));
		}
		return encoding;
	}

	protected int[] getCorrectedEncoding() {

		ArrayList<Stack<Integer>> groups = this.getGroupStacks();

		boolean isValid = false;
		while (!isValid) {
			isValid = true;
			for (Stack<Integer> group : groups) {
				while (group.size() > 4) {
					isValid = false;
					for (int i = 0; i < groups.size(); i++) {
						if (groups.get(i).size() < 4) {
							int studentToMove = group.pop();
							groups.get(i).push(studentToMove);
							break;
						}
					}
				}
			}
		}

		int[] correctedEncoding = new int[GroupEncodingChromosome.MAXIMUM_STUDENTS];
		for (int i = 0; i < groups.size(); i++) {
			Stack<Integer> group = groups.get(i);
			correctedEncoding[group.get(0) - 1] = i + 1;
			correctedEncoding[group.get(1) - 1] = i + 1;
			correctedEncoding[group.get(2) - 1] = i + 1;
			correctedEncoding[group.get(3) - 1] = i + 1;
		}

		return correctedEncoding;
	}

	/**
	 * Take an array of 4 * 128 groupIDs, put them into 
	 * a multidimensional array of 128, uses the index/key value (0 - 511) as 
	 * student ID. Returns an array with incomplete/invalid groups (both greater than,
	 * equal to, and less than 4 students per group) 
	 * 
	 * @return ArrayList groups
	 */
	public ArrayList<Stack<Integer>> getGroupStacks() {
		ArrayList<Stack<Integer>> groups = new ArrayList<Stack<Integer>>();

		// create an empty array of 128 groups
		for (int i = 0; i < GroupEncodingChromosome.MAXIMUM_STUDENTS / 4; i++) {
			groups.add(new Stack<Integer>());
		}
		
		for (int i = 0; i < GroupEncodingChromosome.MAXIMUM_STUDENTS; i++) {
			// get the value of groupID from the flat array of 4 * 128 groupIDs
			int studentGroup = this.getEncoding()[i];
			// use the value of grouptID - 1 as key
			// push the value of i (translates to studentID) into the group
			groups.get(studentGroup - 1).push(i + 1);
		}

		return groups;
	}

	@Override
	public GroupEncodingChromosome CreateChromosome() {
		return new GroupEncodingChromosome();
	}
	@Override
	public ArrayList<int[]> getStudentGroups() {
		ArrayList<int[]> ret = new ArrayList<int[]>();
		
		ArrayList<Stack<Integer>> groups = this.getGroupStacks();
		for(Stack<Integer> stack : groups) {
			int[] students = new int[4];
			students[0] = stack.get(0);
			students[1] = stack.get(1);
			students[2] = stack.get(2);
			students[3] = stack.get(3);		
			
			ret.add(students);
		}
		
		return ret;
	}
}
