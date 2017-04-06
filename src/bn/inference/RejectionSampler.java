package bn.inference;

import java.util.Iterator;
import java.util.Map.Entry;

import bn.core.Assignment;
import bn.core.BayesianNetwork;
import bn.core.Distribution;
import bn.core.RandomVariable;

public class RejectionSampler {
	
	//Only ~0.1% of samples are consistent
	public static Distribution rejectionSampling(BayesianNetwork bn, RandomVariable x, Assignment e, int sampleSize){
		Distribution distrib = new Distribution(x);
		for(Object domain : x.getDomain())
			distrib.put(domain, 0.0);
		
		for(int i = 0; i < sampleSize; i++){
			Assignment sample = priorSample(bn);
			if(isConsistent(sample, e)){
				distrib.replace(sample.get(x), distrib.get(sample.get(x)) + 1);
			}
		}
		distrib.normalize();
		return distrib;
	}
	
	private static Assignment priorSample(BayesianNetwork bn){
		Assignment x = new Assignment();
		for(RandomVariable var : bn.getVariableListTopologicallySorted()){
			x.set(var, "true");
			double probability = bn.getProb(var, x);
			x.remove(var);
			if(Math.random() < probability)
				x.set(var, "true");
			else
				x.set(var, "false");
		}
		
		return x;
	}
	
	private static boolean isConsistent(Assignment a1, Assignment a2){ //If a1 is consistent with a2's constraints
		Iterator<Entry<RandomVariable, Object>> itr = a2.entrySet().iterator();
		while (itr.hasNext()) {
		    Entry<RandomVariable, Object> entry = itr.next();
		    if(!(a1.containsKey(entry.getKey()) && a1.get(entry.getKey()).equals(entry.getValue()))){
		    	return false;
		    }
		}
		return true;
	}


	public static void main(String args[]){
		if(args.length < 3){
			System.err.println("ERROR! Not enough arguments");
			return;
		}
		
		BayesianNetwork bn = BayesianNetwork.constructFromFile(args[1]);

		Assignment e = new Assignment();
		for(int i = 3; i < args.length; i += 2)
			e.set(bn.getVariableByName(args[i]), args[i+1]);
		
		System.out.println(rejectionSampling(bn, bn.getVariableByName(args[2]), e, Integer.parseInt(args[0])));
	}
}