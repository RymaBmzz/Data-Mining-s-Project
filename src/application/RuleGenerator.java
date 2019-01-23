package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;


public class RuleGenerator {
	
	private static boolean ASC = true;
    private static boolean DESC = false;
    
	HashMap<String, Double>rules = new HashMap <String,Double> ();
	HashMap<String, Double>rules_all = new HashMap <String,Double> (); // rules.put(key, value)
	HashMap<String, Double>rules_sorted=new HashMap <String,Double> ();
	
    public HashMap<String,Double> get_all_rules(){
		return this.rules_all;	
    }
    
    public HashMap<String,Double> get_confidence_rules(){
		return this.rules_sorted;	
    }
	public void PrintSubSets(List<Set<String>> itemsetList, List<Set<String>> list, double minimumConfidence){
		int nbFrequentItemsets = list.size();
		System.out.println("\n--- Association rules ---\n");
		for (int i=0; i<nbFrequentItemsets; i++) // loop sur les itemsets les plus fréquents
		{
			System.out.println("Frequent itemset :" + (i+1));
			Set<String> v = list.get(i);
	        String[] tab = v.toArray(new String[v.size()]); // convertir le type Set en Array
	        
	        ArrayList<ArrayList<String>> listSubSets = new ArrayList<ArrayList<String>>(); 
			listSubSets = getSubsets(tab); // récupérer les subitems
			
			rules = createRules(minimumConfidence, itemsetList, listSubSets);
			System.out.println("\n");
		}

	}
	
	
	private HashMap<String, Double> createRules(double minConf, List<Set<String>> itemsetList, ArrayList<ArrayList<String>> listSubSets) 
	{
		int nb = 0;
		int size = listSubSets.size();
		ArrayList<String> first, rest = new ArrayList<String>();
		HashMap<String, Double>rules = new HashMap <String,Double> (); // rules.put(key, value)
		HashMap<String, Double>rules_all = new HashMap <String,Double> ();
		for (int i=0; i<size-1; i++)
		{
			first = listSubSets.get(i);
			for(int j = i+1; j<size; j++)
			{
				rest = listSubSets.get(j);
				if(intersectVide(first, rest)) // i.e. intersection vide
				{
					nb++;
					
					// créer la règle
					double conf1 = findConfidence(itemsetList, first, rest);
					if(conf1 >= minConf)
					{
						System.out.printf("rule " + nb + ": " + first + " -> " + rest + ", confidence: " + conf1 + " *\n");
					   
					}
						else
						System.out.printf("rule " + nb + ": " + first + " -> " + rest + ", confidence: " + conf1 + "\n");
					
					nb++;
				
					double conf2 = findConfidence(itemsetList, rest, first);
					if(conf2 >= minConf)
						System.out.printf("rule " + nb + ": " + rest + " -> " + first + ", confidence: " + conf2 + " *\n");
					else
						System.out.printf("rule " + nb + ": " + rest + " -> " + first + ", confidence: " + conf2 + "\n");
					String règle1 = first + " -> " + rest;
					String règle2 = rest + " -> " + first;
					
					this.rules_all.put(règle1, conf1);
					this.rules_all.put(règle2, conf2);
					
					if(conf1 >= minConf)
						rules.put(règle1, conf1);
					if(conf2 >= minConf)
						rules.put(règle2, conf2);
				}
			}
		}

	
		rules_sorted = (HashMap<String, Double>) sortByConfidence(rules, DESC);
		System.out.print("\n");
		for (Map.Entry<String, Double> entry : rules_sorted.entrySet()) {
		    String key = entry.getKey();
		    Object value = entry.getValue();
		    System.err.println("Resulted/rule " + ", " + key+ ", confidence: " + value);
		}
		//rules_sorted.forEach((key, value) -> System.err.println("\nResulted/rule " + ", " + key+ ", confidence: " + value));
		
		return rules_sorted;
	}

	
	private static Map<String, Double> sortByConfidence(Map<String, Double> unsortMap, final boolean order)
    {
        List<Entry<String, Double>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> b, LinkedHashMap::new));

    }
	

	private double findConfidence(List<Set<String>> itemsetList, ArrayList<String> A, ArrayList<String> B) {
		ArrayList<String> A_union_B = new ArrayList<String>();
		A_union_B = union(A, B);
		int sup_A_union_B = 0;
		int supp_A = 0;
		for(int i=0; i<itemsetList.size(); i++)
		{
			//System.out.println("itemsetList.get(i) " + itemsetList.get(i));
			if(itemsetList.get(i).containsAll(A))
				supp_A++;
			if(itemsetList.get(i).containsAll(A_union_B))
				sup_A_union_B++;
		}
		if (supp_A == 0)
			return 0;
		else
		{
			double val = (double)sup_A_union_B / (double)supp_A;
			return val;
		}
			
	}


    private ArrayList<String> union(ArrayList<String> list1, ArrayList<String> list2) {
        Set<String> set = new HashSet<String>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<String>(set);
    }
    
    
	private boolean intersectVide(List<String> A, List<String> B) {
	    List<String> rtnList = new ArrayList<String>();
	    for(String dto : A) {
	        if(B.contains(dto)) {
	            return false; // i.e. l'intersection n'est pas vide
	        }
	    }
	    return true;
	}
	
	
	private ArrayList<ArrayList<String>> getSubsets(String[] tab){
		int n = tab.length;		
		ArrayList<ArrayList<String>> listOLists = new ArrayList<ArrayList<String>>();

		// Run a loop from 0 to 2^n
		for (int i = 0; i < (1<<n); i++)
		{			
			String[] a = new String[n];
			ArrayList<String> singleList = new ArrayList<String>();
			
			int m = 1; // m is used to check set bit in binary representation.
			// Print current subset
			for (int j = 0; j < n; j++)
			{
				if ((i & m) > 0)
				{		
					if(tab[j] != null)
						singleList.add(tab[j]);
				}
				m = m << 1;
			}
			if(singleList.size() != 0  && singleList.size() != n)
				listOLists.add(singleList);
		}
		
		return listOLists;
	}

}
