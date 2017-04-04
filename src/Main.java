import java.io.FileInputStream;
import java.io.IOException;
import bn.core.*;
import bn.parser.*;

public class Main {
	
	public static void main(String args[]){
		String inputFilePath = "src/bn/examples/alarm.bif";
		
		/*
		XMLBIFParser parser = new XMLBIFParser();
		BayesianNetwork bn;
		
		bn = parser.readNetworkFromFile("/examples/aima-alarm.xml");
		*/
	
		BIFParser parser;
		BayesianNetwork bn = null;
		
		try {
			parser = new BIFParser(new FileInputStream(inputFilePath));
			bn = parser.parseNetwork();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		bn.print();
		Assignment a = new Assignment();
		a.set(bn.getVariableByName("INTUBATION"), 5);
		System.out.println(a);
		System.out.println(bn.getProb(bn.getVariableByName("INTUBATION"), a));
	}
}
