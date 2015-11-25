package main.java.GroupsHybrid.GeneticAlgorithm.Common;

import main.java.GroupsHybrid.GeneticAlgorithm.Interfaces.IChromosome;

public abstract class BinaryChromosome implements IChromosome<String> {
	
	private String BinaryEncoding = null;
	private int Length;

	protected String getBinaryEncoding() {
		return this.BinaryEncoding;
	}
	
	protected void setBinaryEncoding(String encoding) {
		this.BinaryEncoding = encoding;
		this.Length = encoding.length();		
	}
	
	protected int getLength() {
		return this.Length;
	}
	
	public BinaryChromosome(int length) {
		this.setBinaryEncoding(this.getRandomBinary(length));
	}

	public BinaryChromosome(String encoding) {
		this.setBinaryEncoding(encoding);
	}
	
	protected String getRandomBinary(int length) {
		String encoding = "";
		for (int i=0; i<length; i++) {
			encoding += Math.round(Math.random());
		}
		return encoding;
	}
	
	@SuppressWarnings("unchecked")
	public static BinaryChromosome NewInstance(IChromosome<String> o) throws GeneticAlgorithmException {
		IChromosome<String> newInstance;
		try {
			
			newInstance = o.getClass().newInstance();
			
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new GeneticAlgorithmException(e.getMessage());			
		}
		
		return (BinaryChromosome) newInstance;				
	}

}
