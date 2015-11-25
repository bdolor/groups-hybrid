package main.java.GroupsHybrid.GeneticAlgorithm.UserInterface;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import main.java.GroupsHybrid.GeneticAlgorithm.GeneticAlgorithm;
import main.java.GroupsHybrid.GeneticAlgorithm.GeneticAlgorithmConfig;
import main.java.GroupsHybrid.GeneticAlgorithm.Data.StudentScores;
import main.java.GroupsHybrid.GeneticAlgorithm.GroupEncoding.GroupEncodingChromosome;
import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.IChromosome;
import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.IReport;
import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.IStudentChromosome;

import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class ReportFrame<T extends IChromosome<?>> extends ApplicationFrame implements IReport<T> {

	private static final long serialVersionUID = 1L;

	private int Evolution = 0;
	private double bestFitness = 0;
	private int bestFitnessEvolution = 0;

	private XYSeries averageFitness;
	private XYSeries maxFitness;

	private JLabel StatusLabel = new JLabel("Status:   Initializing");
	private JLabel EvolutionLabel = new JLabel("Evolution:");
	private JLabel ElapsedLabel = new JLabel("Elapsed:");
	private JLabel PopulationBestFitnessLabel = new JLabel("Best Fitness:");
	private JLabel PopulationAverageFitnessLabel = new JLabel("Average Fitness:");

	private JLabel BestFitnessLabel = new JLabel("Best Valid Fitness: 0");
	private JLabel BestFitnessEvolutionLabel = new JLabel("Number of Evolutions: 0");

	private JTable BestChromosome = new JTable();

	/**
	 * Create the frame.
	 */
	public ReportFrame() {

		super("COMP-658 Assignment 2");
		setBounds(400, 400, 450, 300);

		this.averageFitness = new XYSeries("Average Fitness");
		this.maxFitness = new XYSeries("Best Fitness");

	}

	@Override
	public void updateReport(double averageFitness, double maxFitness, IChromosome fittest, boolean isConverged, long startTime) {
		this.Evolution++;

		if (fittest.isValid() && maxFitness > this.bestFitness) {
			this.bestFitness = maxFitness;
			this.bestFitnessEvolution = this.Evolution;

			this.BestFitnessLabel.setText(String.format("Best Valid Fitness:   %.3f", this.bestFitness));
			this.BestFitnessEvolutionLabel.setText(String.format("Number of Evolutions:   %d", this.bestFitnessEvolution));

			this.updateBestChromosome((T) fittest);

		}

		if (isConverged) {
			this.StatusLabel.setText("Status:   Converged");
		} else {
			this.StatusLabel.setText("Status:   Evolving");
		}

		this.PopulationBestFitnessLabel.setText(String.format("Best Local Fitness:   %.3f", maxFitness));
		this.PopulationAverageFitnessLabel.setText(String.format("Average Fitness:   %.3f", averageFitness));
		this.EvolutionLabel.setText(String.format("Evolution:   %d", this.Evolution));
		this.ElapsedLabel.setText(
				String.format("Elapsed:   %d sec", (int) ((System.nanoTime() - startTime) / 1000000000d)));

		this.averageFitness.add(this.Evolution, averageFitness);
		this.maxFitness.add(this.Evolution, maxFitness);
	}

	protected void updateBestChromosome(T chromosome) {

		IStudentChromosome studentChromosome = (IStudentChromosome) chromosome;
		StudentScores scores = new StudentScores();

		ArrayList<int[]> groups = studentChromosome.getStudentGroups();

		for (int i = 0; i < groups.size(); i++) {
			int s1 = groups.get(i)[0];
			int s2 = groups.get(i)[1];
			int s3 = groups.get(i)[2];
			int s4 = groups.get(i)[3];

			this.BestChromosome.setValueAt(i + 1, i, 0);
			this.BestChromosome.setValueAt(String.format("%d, %d, %d, %d", s1, s2, s3, s4), i, 1);
			this.BestChromosome.setValueAt(String.format("%.3f", scores.getMaxDistance(s1, s2, s3, s4)), i, 2);
			this.BestChromosome.setValueAt(String.format("%.3f", scores.getGhValue(s1, s2, s3, s4)), i, 3);
		}
		;
	}

	protected ChartPanel getFitnessChart() {

		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(this.averageFitness);
		dataset.addSeries(this.maxFitness);

		JFreeChart chart = ChartFactory.createXYLineChart("", // chart
																// title
				"Evolution", // x axis label
				"Fitness", // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
		);

		final XYPlot plot = chart.getXYPlot();
		ValueAxis axis = plot.getDomainAxis();
		axis.setAutoRange(true);
		axis = plot.getRangeAxis();

		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(800, 400));

		chartPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

		return chartPanel;
	}

	protected JPanel getConfigurationPanel(GeneticAlgorithm<?> algorithm) {
		JPanel panel = new JPanel();
		BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(layout);

		GeneticAlgorithmConfig config = algorithm.getConfig();

		panel.add(new JLabel(String.format("Population Size:   %d", config.getPopulationSize())));
		panel.add(new JLabel(String.format("Maximum Evolutions:   %d", config.getMaximumEvolutions())));
		panel.add(new JLabel(String.format("Crossover Rate:   %.3f", config.getCrossoverProbability())));
		panel.add(new JLabel(String.format("Mutation Rate:   %.3f", config.getMutationProbability())));
		panel.add(new JLabel(String.format("Elitists:   %d", config.getEliteChromosomeCount())));
		panel.add(new JLabel(String.format("Crossover Method:   %s", algorithm.getCrossOver())));
		panel.add(new JLabel(String.format("Mutation Method:   %s", algorithm.getMutation())));
		panel.add(new JLabel(String.format("Select Method:   %s", algorithm.getSelect())));
		panel.add(new JLabel(String.format("Adaptive Mode:   %s", config.isAdapativeEnabled() ? "Enabled" : "Disabled")));

		panel.setBorder(
				new CompoundBorder(BorderFactory.createTitledBorder("Configuration"), new EmptyBorder(5, 30, 10, 10)));
		panel.setAlignmentY(TOP_ALIGNMENT);

		return panel;

	}

	protected JPanel getStatusPanel() {

		JPanel panel = new JPanel();
		BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(boxLayout);

		panel.add(this.StatusLabel);
		panel.add(this.EvolutionLabel);
		panel.add(this.ElapsedLabel);
		panel.add(this.PopulationBestFitnessLabel);
		panel.add(this.PopulationAverageFitnessLabel);
		panel.add(this.BestFitnessLabel);
		panel.add(this.BestFitnessEvolutionLabel);		

		panel.setBorder(
				new CompoundBorder(BorderFactory.createTitledBorder("Runtime Status"), new EmptyBorder(5, 30, 10, 10)));
		panel.setAlignmentY(TOP_ALIGNMENT);

		return panel;

	}

	protected JPanel getBestSolutionPanel() {
		JPanel panel = new JPanel();
		BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(boxLayout);

		panel.add(this.BestFitnessLabel);
		panel.add(this.BestFitnessEvolutionLabel);


		this.BestChromosome = new JTable(GroupEncodingChromosome.MAXIMUM_STUDENTS / 4, 4);
		this.BestChromosome.getColumnModel().getColumn(0).setHeaderValue("Group Id");
		this.BestChromosome.getColumnModel().getColumn(1).setHeaderValue("Students");
		this.BestChromosome.getColumnModel().getColumn(2).setHeaderValue("Max Distance");
		this.BestChromosome.getColumnModel().getColumn(3).setHeaderValue("Group gH");

		panel.add(new JScrollPane(this.BestChromosome));

		panel.setBorder(
				new CompoundBorder(BorderFactory.createTitledBorder("Best Solution"), new EmptyBorder(5, 30, 10, 10)));

		return panel;
	}

	@Override
	public void initializeReport(GeneticAlgorithm<?> algorithm) {

		JPanel infoPanel = new JPanel();
		JPanel displayPanel = new JPanel();

		BoxLayout frameLayout = new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS);
		BoxLayout infoLayout = new BoxLayout(infoPanel, BoxLayout.X_AXIS);
		BoxLayout displayLayout = new BoxLayout(displayPanel, BoxLayout.X_AXIS);

		this.setLayout(frameLayout);
		infoPanel.setLayout(infoLayout);
		displayPanel.setLayout(displayLayout);

		infoPanel.add(this.getConfigurationPanel(algorithm));
		infoPanel.add(this.getStatusPanel());

		infoPanel.setBorder(BorderFactory.createEtchedBorder());

		displayPanel.add(this.getFitnessChart());
		displayPanel.add(this.getBestSolutionPanel());

		this.add(infoPanel);
		this.add(displayPanel);

		this.pack();
	}

}
