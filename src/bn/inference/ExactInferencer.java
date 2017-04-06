package bn.inference;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import bn.core.Assignment;
import bn.core.BayesianNetwork;
import bn.core.Distribution;
import bn.core.RandomVariable;

public class ExactInferencer {
	public static Distribution enumarationAsk(BayesianNetwork bn, RandomVariable x, Assignment e){
		Distribution q = new Distribution(x);
		for(Object d : x.getDomain()){
			Assignment ne = new Assignment(); //Making a deep copy of e
			
			Map<RandomVariable, Object> a = e;
			Iterator<Entry<RandomVariable, Object>> itr = a.entrySet().iterator();
			while (itr.hasNext()) {
			    Entry<RandomVariable, Object> entry = itr.next();
			    ne.set(entry.getKey(), entry.getValue());
			}
			ne.set(x, d);
			
			q.put(d, enumerateAll(bn, bn.getVariableListTopologicallySorted(), ne));
		}
		
		q.normalize();
		return q;
	}
	
	private static double enumerateAll(BayesianNetwork bn, List<RandomVariable> vars, Assignment e){
		if(vars.isEmpty())
			return 1.0;
		RandomVariable y = vars.get(0);
		if(e.containsKey(y))
			return bn.getProb(y, e) * enumerateAll(bn, vars.subList(1, vars.size()), e); //May cause an issue using bn for prob and not vars
		else{
			double sumY = 0.0;
			
			for(Object d : y.getDomain()){ //Sum over Y
				Assignment ne = new Assignment(); //Making a deep copy of e
				
				Map<RandomVariable, Object> a = e;
				Iterator<Entry<RandomVariable, Object>> itr = a.entrySet().iterator();
				while (itr.hasNext()) {
				    Entry<RandomVariable, Object> entry = itr.next();
				    ne.put(entry.getKey(), entry.getValue());
				}
				ne.set(y, d);
				
				sumY += (bn.getProb(y, ne) * enumerateAll(bn, vars.subList(1, vars.size()), ne));
			}
			
			return sumY;
		}
	}

	public static void main(String args[]){
		if(args.length < 2){
			System.err.println("ERROR! Not enough arguments");
			return;
		}
		
		BayesianNetwork bn = BayesianNetwork.constructFromFile(args[0]);

		Assignment e = new Assignment();
		for(int i = 2; i < args.length; i += 2)
			e.set(bn.getVariableByName(args[i]), args[i+1]);
		
		System.out.println(ExactInferencer.enumarationAsk(bn, bn.getVariableByName(args[1]), e));
	}
}
