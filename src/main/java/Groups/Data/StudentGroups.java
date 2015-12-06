/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.Groups.Data;

import java.util.ArrayList;
/**
 *
 * @author bpayne
 */
public class StudentGroups extends StudentScores{
	
	public final static int MAXIMUM_GH_PER_GROUP = 14;
	public final static int MAXIMUM_STUDENTS = 512;

	/**
	 * Required for the output, intended for use once the best/fittest class has
	 * been selected.
	 * 
	 * @return Array
	 */
	public String[] getMembers(int[] bestTrail) {
		int dimension1 = this.MAXIMUM_STUDENTS / 4;
		String[] members = new String[dimension1];

		// loop through bestGroup to get individual members
		for (int i = 0; i < this.MAXIMUM_STUDENTS / 4; i++) {

			int i1 = bestTrail[(i * 4)];
			int i2 = bestTrail[(i * 4) + 1];
			int i3 = bestTrail[(i * 4) + 2];
			int i4 = bestTrail[(i * 4) + 3];

			String s1 = Integer.toString(i1);
			String s2 = Integer.toString(i2);
			String s3 = Integer.toString(i3);
			String s4 = Integer.toString(i4);

			members[i] = s1 + "," + s2 + "," + s3 + "," + s4;

		}

		return members;

	}

	/**
	 * Output the highest Euclidean distance of each group.
	 * 
	 * @return double eachGroupMaxDistance
	 */
	public double[] getEachGroupMaxDistance(int[] bestTrail) {
		double[] eachGroupMaxDistance = new double[(this.MAXIMUM_STUDENTS / 4)];
		// loop through bestGroup to get individual members
		for (int i = 0; i < this.MAXIMUM_STUDENTS / 4; i++) {

			int s1 = bestTrail[(i * 4)];
			int s2 = bestTrail[(i * 4) + 1];
			int s3 = bestTrail[(i * 4) + 2];
			int s4 = bestTrail[(i * 4) + 3];

			// set the variable with values
			eachGroupMaxDistance[i] = this.getMaxDistance(s1, s2, s3, s4);
		}

		return eachGroupMaxDistance;

	}

	/**
	 * 
	 * @param bestTrail
	 * @return 
	 */
	public boolean isValid(int[] bestTrail) {
		Boolean allValidGroups = true;

		for (int i = 0; i < StudentScores.MAXIMUM_STUDENTS / 4; i++) {

			int s1 = bestTrail[(i * 4)];
			int s2 = bestTrail[(i * 4) + 1];
			int s3 = bestTrail[(i * 4) + 2];
			int s4 = bestTrail[(i * 4) + 3];

			if (this.getMaxDistance(s1, s2, s3, s4) <= 2) {
				// only valid groups can contribute to the overall heterogenity
				// of all groups
				// continue on with loop to measure all valid groups
				allValidGroups = false;
			}

			double gh = this.getGhValue(s1, s2, s3, s4);
			if (gh < 0.5) {
				// only valid groups can contribute to the overall heterogenity
				// of all groups
				// continue on with loop to measure all valid groups
				allValidGroups = false;
			}
		}

		return allValidGroups;
	}

	
}
