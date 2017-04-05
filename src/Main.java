import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import bn.core.*;
import bn.inference.ExactInferencer;
import bn.parser.*;

public class Main {
	
	public static void main(String args[]){
		String inputFilePath = "src/bn/examples/aima-alarm.xml";
		
		XMLBIFParser parser = new XMLBIFParser();
		BayesianNetwork bn = null;

		/*
		BIFParser parser;
		BayesianNetwork bn = null;
		*/
		
		try {
			/*
			parser = new BIFParser(new FileInputStream(inputFilePath));
			bn = parser.parseNetwork();
			*/
			bn = parser.readNetworkFromFile(inputFilePath);
		} catch (IOException | ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}
		
		Assignment a = new Assignment();
		a.set(bn.getVariableByName("M"), "true");
		
		System.out.println(ExactInferencer.enumarationAsk(bn, bn.getVariableByName("J"), a));
	}
}
