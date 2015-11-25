package main.java.GroupsHybrid.GeneticAlgorithm;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import main.java.GroupsHybrid.GeneticAlgorithm.Common.BinaryTournamentSelect;
import main.java.GroupsHybrid.GeneticAlgorithm.Common.GeneticAlgorithmException;
import main.java.GroupsHybrid.GeneticAlgorithm.Common.PermutationMutation;
import main.java.GroupsHybrid.GeneticAlgorithm.Common.RouletteSelect;
import main.java.GroupsHybrid.GeneticAlgorithm.GroupEncoding.GroupEncodingChromosome;
import main.java.GroupsHybrid.GeneticAlgorithm.GroupEncoding.GroupEncodingMutation;
import main.java.GroupsHybrid.GeneticAlgorithm.GroupEncoding.MultiPointCrossover;
import main.java.GroupsHybrid.GeneticAlgorithm.StudentGrouping.StudentGroupCrossover;
import main.java.GroupsHybrid.GeneticAlgorithm.StudentGrouping.StudentGroupsFactory;

public class CommandLineAlgorithmParser {

	protected Options getCommandLineOptions() {
		Options options = new Options();

		GeneticAlgorithmConfig config = new GeneticAlgorithmConfig();

		options.addOption("s", "populationsize", true,
				String.format("Set population size.  Default is %d", config.getPopulationSize()));

		options.addOption("x", "maxevolution", true,
				String.format("Set maximum evolutions.  Default is %d", config.getPopulationSize()));

		options.addOption("c", "crossover", true,
				String.format("Set crossover rate.  Default is %.3f", config.getCrossoverProbability()));

		options.addOption("m", "mutation", true,
				String.format("Set mutation rate.  Default is %.3f", config.getMutationProbability()));

		options.addOption("e", "elitists", true,
				String.format("Set elite chromosome count.  Default is %d", config.getEliteChromosomeCount()));

		options.addOption("r", "roulette", false, "Use default roulette selection.");

		options.addOption("t", "tournament", false, "Use tournament selection.");

		options.addOption("p", "permutation", false, "Use default permutation encoding.");

		options.addOption("g", "group", false, "Use group encoding.");

		options.addOption("pc", "parentcount", false,
				String.format("Number of parents in crossover. Default is %d. Valid only for group encoding.",
						config.getRequiredParentCount()));

		options.addOption("a", "adaptive", false, "Use default adaptive setting.");

		options.addOption("na", "nonadaptive", false, "Disable adatpive setting.");

		return options;
	}

	public void printHelp() {
		HelpFormatter formatter = new HelpFormatter();
		System.out.println();
		System.out.println();
		System.out.println("example usage: genetic-algorithm --populationsize 200 --roulette --elitists 5");
		System.out.println();
		formatter.printHelp("genetic-algorithm", this.getCommandLineOptions());
	}

	public GeneticAlgorithm getAlgorithm(String[] args) throws ParseException, GeneticAlgorithmException {
		CommandLineParser parser = new DefaultParser();
		CommandLine line = parser.parse(this.getCommandLineOptions(), args);

		GeneticAlgorithmConfig config = new GeneticAlgorithmConfig();
		config.setPopulationSize(this.getIntOptionValue(line, "populationsize", config.getPopulationSize()));
		config.setMaximumEvolutions(this.getIntOptionValue(line, "maxevolution", config.getMaximumEvolutions()));
		config.setCrossoverProbability(this.getDoubleOptionValue(line, "crossover", config.getCrossoverProbability()));
		config.setMutationProbability(this.getDoubleOptionValue(line, "mutation", config.getMutationProbability()));
		config.setEliteChromosomeCount(this.getIntOptionValue(line, "elitists", config.getEliteChromosomeCount()));
		config.setRequiredParentCount(this.getIntOptionValue(line, "parentcount", config.getRequiredParentCount()));

		GeneticAlgorithm algorithm = null;

		if (line.hasOption("nonadaptive")) {
			algorithm = new GeneticAlgorithm();
			config.setAdapativeEnabled(false);
		} else {
			algorithm = new AdaptiveGeneticAlgorithm();
			config.setAdapativeEnabled(true);
		}

		algorithm.setConfig(config);

		if (line.hasOption("group")) {
			algorithm.setCrossOver(new MultiPointCrossover(config.getRequiredParentCount()));
			algorithm.setMutation(new GroupEncodingMutation());
			algorithm.setFactory(new GroupEncodingChromosome());
		} else {
			algorithm.setCrossOver(new StudentGroupCrossover<>());
			algorithm.setMutation(new PermutationMutation<>());
			algorithm.setFactory(new StudentGroupsFactory<>());
		}

		algorithm.setSelect(line.hasOption("roulette") ? new RouletteSelect() : new BinaryTournamentSelect() );

		return algorithm;
	}

	private int getIntOptionValue(CommandLine line, String description, int defaultValue)
			throws GeneticAlgorithmException {

		int value = -1;

		if (line.hasOption(description)) {
			try {
				value = Integer.parseInt(line.getOptionValue(description).trim());
			} catch (NumberFormatException e) {
				throw new GeneticAlgorithmException(String.format("Argument %s is not valid for %s.",
						line.getOptionValue(description), description));
			}

		} else {
			value = defaultValue;
		}
		return value;
	}

	private double getDoubleOptionValue(CommandLine line, String description, double defaultValue) throws GeneticAlgorithmException {

		double value = -1;

		if (line.hasOption(description)) {
			try {
				value = Double.parseDouble(line.getOptionValue(description).trim());
			} catch (NumberFormatException e) {
				throw new GeneticAlgorithmException(String.format("Argument %s is not valid for %s.",
						line.getOptionValue(description), description));
			}
		} else {
			value = defaultValue;
		}
		return value;
	}

}
