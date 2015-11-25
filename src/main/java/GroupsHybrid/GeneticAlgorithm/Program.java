package main.java.GroupsHybrid.GeneticAlgorithm;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import main.java.GroupsHybrid.GeneticAlgorithm.Common.BitInversionMutation;
import main.java.GroupsHybrid.GeneticAlgorithm.Common.GeneticAlgorithmException;
import main.java.GroupsHybrid.GeneticAlgorithm.Common.PermutationMutation;
import main.java.GroupsHybrid.GeneticAlgorithm.Common.RouletteSelect;
import main.java.GroupsHybrid.GeneticAlgorithm.Common.BinaryTournamentSelect;
import main.java.GroupsHybrid.GeneticAlgorithm.Common.SinglePointCrossover;
import main.java.GroupsHybrid.GeneticAlgorithm.GroupEncoding.GroupEncodingChromosome;
import main.java.GroupsHybrid.GeneticAlgorithm.GroupEncoding.GroupEncodingCrossover;
import main.java.GroupsHybrid.GeneticAlgorithm.GroupEncoding.GroupEncodingMutation;
import main.java.GroupsHybrid.GeneticAlgorithm.GroupEncoding.MultiPointCrossover;
import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.IMutation;
import main.java.GroupsHybrid.GeneticAlgorithm.StudentGrouping.StudentGroupCrossover;
import main.java.GroupsHybrid.GeneticAlgorithm.StudentGrouping.StudentGroups;
import main.java.GroupsHybrid.GeneticAlgorithm.StudentGrouping.StudentGroupsFactory;
import main.java.GroupsHybrid.GeneticAlgorithm.UserInterface.ReportFrame;

public class Program {

	public static void main(String[] args) {

		System.out.println("COMP-658 Computational Intelligence - Assignment 2");
		System.out.println("Athabasca University");
		System.out.println();
		System.out.println();
		
		System.out.println("Tool for configuring, executing and monitoring Genetic Algorithm to optimize student group heterogenity.");

		
		CommandLineAlgorithmParser parser = new CommandLineAlgorithmParser();
		
		parser.printHelp();
		
		//String[] a = new String[] {"-s", "500", "-na", "-g"};
		
		GeneticAlgorithm algorithm = null;
		try {
			algorithm = parser.getAlgorithm(args);
		} catch (Exception e) {
		System.out.println();
			System.out.println();
			System.out.println("Error parsing arguments:");
			System.out.println();
			System.out.println("   " + e.getMessage());
			System.out.println();
			System.out.println("Please try again.");
			System.out.println();
			System.exit(1);;		
		}

		//Program.RunPermutationEncoding();
		//Program.RunGroupEncoding();
		//Program.RunAdaptiveGroupEncoding();
		//Program.RunAdaptivePermutationEncoding();

		
	   ReportFrame report = new ReportFrame();
	   report.initializeReport(algorithm);
	   report.setVisible(true);
	    
		algorithm.setReport(report);;
		
	    try {
			algorithm.Evolve();
		} catch (GeneticAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	}
	}



	private static void RunPermutationEncoding() {

		ReportFrame report = new ReportFrame();

		GeneticAlgorithmConfig config = new GeneticAlgorithmConfig();

		GeneticAlgorithm<StudentGroups> ga = new GeneticAlgorithm<>();

		ga.Config = config;
		ga.CrossOver = new StudentGroupCrossover<>();
		ga.Mutation = new PermutationMutation<>();
		//ga.Select = new RouletteSelect<>();
		ga.Select = new BinaryTournamentSelect<>();
		ga.Factory = new StudentGroupsFactory<>();
		ga.setReport(report);

		report.initializeReport(ga);
		report.setVisible(true);

		try {
			ga.Evolve();
		} catch (GeneticAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void RunGroupEncoding() {

		ReportFrame report = new ReportFrame();

		GeneticAlgorithmConfig config = new GeneticAlgorithmConfig();

		GeneticAlgorithm<GroupEncodingChromosome> ga = new GeneticAlgorithm<>();

		ga.Config = config;
		ga.CrossOver = new GroupEncodingCrossover<>();
		ga.Mutation = new GroupEncodingMutation<>();
		ga.Select = new RouletteSelect<>();
		//ga.Select = new BinaryTournamentSelect<>();
		ga.Factory = new GroupEncodingChromosome();
		ga.setReport(report);

		report.initializeReport(ga);
		report.setVisible(true);

		try {
			ga.Evolve();
		} catch (GeneticAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private static void RunAdaptivePermutationEncoding() {

		ReportFrame report = new ReportFrame();

		GeneticAlgorithmConfig config = new GeneticAlgorithmConfig();

		GeneticAlgorithm<StudentGroups> ga;
		ga = new AdaptiveGeneticAlgorithm<>();

		ga.Config = config;
		ga.CrossOver = new StudentGroupCrossover<>();
		ga.Mutation = new PermutationMutation<>();
		//ga.Select = new RouletteSelect<>();
		ga.Select = new BinaryTournamentSelect<>();
		ga.Factory = new StudentGroupsFactory<>();
		ga.setReport(report);

		report.initializeReport(ga);
		report.setVisible(true);

		try {
			ga.Evolve();
		} catch (GeneticAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private static void RunAdaptiveGroupEncoding() {

		ReportFrame report = new ReportFrame();

		GeneticAlgorithmConfig config = new GeneticAlgorithmConfig();

		GeneticAlgorithm<GroupEncodingChromosome> ga;
		ga = new AdaptiveGeneticAlgorithm<>();

		ga.Config = config;
		ga.CrossOver = new GroupEncodingCrossover<>();
		ga.Mutation = new GroupEncodingMutation<>();
		//ga.Select = new RouletteSelect<>();
		ga.Select = new BinaryTournamentSelect<>();
		ga.Factory = new GroupEncodingChromosome();
		ga.setReport(report);

		report.initializeReport(ga);
		report.setVisible(true);

		try {
			ga.Evolve();
		} catch (GeneticAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
