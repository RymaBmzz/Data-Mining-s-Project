package application;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import weka.core.Instances;
import weka.gui.beans.DataSetEvent;

public class Try_KNN {
	public static String newline = System.getProperty("line.separator");
	private static boolean ASC = true;
    private static boolean DESC = false;
	String nomFichier = null; // path du fichier 
	Instances dataset = null; // all the dataset
	static String[][] data =null;
	static String[][] test_data = null; // tuple of test 40% , take each tuple and test it with others of train one by one 
	static String[][] train_data = null;
	static String[] classes = null;
	double per_test = 0.1; // pourcentage de data test
	double per_train = 1 - per_test;
	//static int k = 3; // on prend généralement sqtr(data)/2
	HashMap<Integer, Object> distances = null;
	List<String> toPrint1=null;
	List<String> toPrint2=null;
	List<String> toPrint3=null;
	
	PrétraitementDataset dataPreprocess = new PrétraitementDataset();

	
	public void knn_algorithme(String nomFich,int k) throws IOException {
		nomFichier = nomFich;
		
		dataset = LireDataset(nomFichier);
		data = dataPreprocess.CleanDataset(dataset, nomFichier);
				
		classes = getClasses(); // distinct classes of the dataset
		
//		System.out.println("data.length " + data.length);
		
		int start_train = 1, end_train = (int) (data.length * per_train);
		int start_test= (end_train), end_test = (data.length);
		
		
		System.err.println("\n\ntrain from line 1 to line " + (end_train-1));
		String S1= "\n\ntrain " + end_train + " tuples => " + "90 % du dataset";

		System.err.println("test from line " + (start_test) + " to line " + (end_test-1) + "\n");
		String S2="\ntest "+ (end_test-start_test) +" tuples => " + "10 % du dataset\n";
		toPrint1=new ArrayList<>();		
		toPrint1.add(S1);
		toPrint1.add(S2);
		
		train_data = create_train_data(start_train, end_train);
		test_data = create_test_data(start_test, end_test);
		
		/*
		for(int i=0; i<train_data.length; i++)
		{
			for (int j=0; j<train_data[i].length; j++)
				System.out.print("train " + (start_train) + "/ " + train_data[i][j] + ",");
			start_train++;
			System.out.println();
		}
		
		
		for(int i=0; i<test_data.length; i++)
		{
			for (int j=0; j<test_data[i].length; j++)
				System.out.print("test " + (start_test) + "/ " + test_data[i][j] + ",");
			start_test++;
			System.out.println();
		}
		*/
		
		// appliquer le KNN sur 
		distances  = appliquer_knn(k);	
		
		générer_information_knn(distances,k);
		
	}

	
	
	private void générer_information_knn(HashMap<Integer, Object> distances,int k) {
		int class_col = data[0].length -1;
		int pos_classé = 0, neg_classé = 0, non_classé = 0;
		
		System.out.println("==     Precision détaillée par tuple     ==\n");
		toPrint2=new ArrayList<>();	
		String S3="==     Precision détaillée par tuple     ==\n";
		
		toPrint2.add(S3);
		
		for (Map.Entry<Integer, Object> entry : distances.entrySet()) {
		    Integer key = entry.getKey(); // indexe du tuple de test_data qu'on est entrain de tester
		    String[] k_voisin = (String[]) entry.getValue();
		    
		    String[] val_classes = new String[k];
		    
		    for(int i=0; i<k_voisin.length; i++)
		    {
		    	String index = k_voisin[i].split(",")[0];
		    	int indexe = Integer.parseInt(index);
		    	val_classes[i] = train_data[indexe][class_col];
		    }
		    
		    ArrayList<String> valeurs = classe_positive(val_classes);
		    
		    if(valeurs.size()==1)
		    {
		    	// chercher si positif ou négatif
		    	if(valeurs.get(0).equals(test_data[key][class_col]))
		    	{
		    		pos_classé++;
		    		System.out.println("tuple " + (key+1) + " [" + test_data[key][class_col] + "]"+
		    						" bien classée à " + valeurs.get(0));
		    		String S4="\ntuple " + (key+1) + " [" + test_data[key][class_col] + "]"+" bien classée à " + valeurs.get(0)+"\n";
		    		toPrint2.add(S4);
		    	}
		    	else
		    	{
		    		neg_classé++;
		    		System.out.println("\ntuple " + (key+1) + " [" + test_data[key][class_col] + "]"+
    						" mal classée à " + valeurs.get(0));
		    		String S5="\ntuple " + (key+1) + " [" + test_data[key][class_col] + "]"+" mal classée à " + valeurs.get(0)+"\n";
		    		toPrint2.add(S5);
		    	}
		    }
		    else
		    	// ça veut dire que différente classe peuvent être attribué donc faut changer la valeur de k
		    {
		    	non_classé++;  
		    	System.out.println("\ntuple " + (key+1) + " [" + test_data[key][class_col] + "]"+
						" non classé car " + valeurs.size() + " valeurs possibles");
		    	String S6="\ntuple " + (key+1) + " [" + test_data[key][class_col] + "]"+
						" non classé car " + valeurs.size() + " valeurs possibles\n";
		    	toPrint2.add(S6);
		    }
		}
		
		toPrint3=new ArrayList<>();	
		double bien  = (double)(pos_classé) / (double)(test_data.length) * 100;
		System.err.println("\nTaux de bien classé  = " + pos_classé + "/" + test_data.length + 
				" = " + String.format("%.2f", bien) + " %");
		String S7="\nTaux de bien classé  = " + pos_classé + "/" + test_data.length + 
				" = " + String.format("%.2f", bien) + " %";
		toPrint3.add(S7);
		double mal = (double)(neg_classé) / (double)(test_data.length) * 100;
		System.err.println("Taux de mal classé  = " + neg_classé + "/" + test_data.length + 
				" = " + String.format("%.2f", mal) + " %");
		
		String S8="Taux de mal classé  = " + neg_classé + "/" + test_data.length + 
				" = " + String.format("%.2f", mal) + " %";
		toPrint3.add(S8);
		
		double non  = (double)(non_classé) / (double)(test_data.length) * 100;
		System.err.println("Taux de non classé  = " + non_classé + "/" + test_data.length + 
				" = " + String.format("%.2f", non) + " %");
		String S9="Taux de non classé  = " + non_classé + "/" + test_data.length + 
				" = " + String.format("%.2f", non) + " %";
		toPrint3.add(S9);
	}
	

	private ArrayList<String> classe_positive(String[] tenNumbersArray) {
		HashMap<String, Integer> numberAndItsOcuurenceMap = new HashMap<String, Integer>();
		ArrayList<String> diff_valeurs = new ArrayList<String>();
		
		// calculer la fréquence de chaque classe
		for (String num : tenNumbersArray) {
		    Integer frequency = numberAndItsOcuurenceMap.get(num);
		    numberAndItsOcuurenceMap.put(num, frequency != null ? frequency + 1 : 1);
		}
		
		for (Entry<String, Integer> entry : numberAndItsOcuurenceMap.entrySet()) {
		    String key = entry.getKey();
		    Integer value = entry.getValue();
		    //System.err.println("element : " + key + " => " + value);
		}
		
		// trouver la classe ayant la distance minimum
		int minValueInMap=(Collections.min(numberAndItsOcuurenceMap.values()));  
        for (Entry<String, Integer> entry : numberAndItsOcuurenceMap.entrySet()) {  // Itrate through hashmap
            if (entry.getValue()==minValueInMap) {
 //               System.out.println(entry.getKey());     // Print the key with max value
                diff_valeurs.add(entry.getKey()); // elle contient les différentes valeurs avec la plus petite valeur
            }
        }
		
        return diff_valeurs;
	}



	private String[] getClasses() {

		String[] classes = new String[data.length -1];
		int class_col = data[0].length -1;
		for(int i=1; i<data.length; i++)
			classes[i-1] = data[i][class_col];
		
		
		ArrayList<String> store = new ArrayList<String>(); // so the size can vary

	    for (int n = 0; n < classes.length; n++){
	        if (!store.contains(classes[n])){ // if numbers[n] is not in store, then add it
	            store.add(classes[n]);
	        }
	    }
	    
	    classes = new String[store.size()];
	    for (int n = 0; n < store.size(); n++){
	        classes[n] = store.get(n);
	    }
				
		return classes;
	}


	// fonction pour lire le fichier et récupérer le dataset
	private Instances LireDataset(String nomFichier) throws IOException {
		Instances dataset = null;
		String DATASETPATH = "./data/" + nomFichier;
		LoadData mg = new LoadData();
		dataset = mg.loadDataset(DATASETPATH);
		return dataset;	
	}
	
	
	private String[][] create_train_data(int start_train, int end_train) {
		int nb_lines = end_train-1;
		int nb_cols = data[0].length;
		String[][] train_data = new String[nb_lines][nb_cols];
		int k =0;
		for(int i = start_train; i<end_train; i++)
		{
			for(int j=0; j<data[i].length; j++)
				train_data[k][j] = data[i][j];
			k++;
		}
			

		return train_data;
	}
	
	
	private String[][] create_test_data(int start_test, int end_test) {
		int nb_lines = end_test - start_test;
		int nb_cols = data[0].length;
		String[][] test_data = new String[nb_lines][nb_cols];
		int k = 0;
		
		for(int i = start_test; i<end_test; i++)
		{	
			for(int j=0; j<data[i].length; j++)
				test_data[k][j] = data[i][j];
			k++;
		}
		
		return test_data;
	}
	


	private HashMap<Integer, Object> appliquer_knn(int k) {
		HashMap<Integer, Object>D = new HashMap <Integer,Object> (); 
		String[] k_voisins = null; // pour chaque tuple de test_data on récoupère les k voisins les plus proche
		
		for (int i=0; i<test_data.length; i++)
		{
			// pour chaque ligne on teste avec train_data et on retourne 
			k_voisins = calculer_distance(test_data[i], k);
			D.put(i, k_voisins);
		}
		return D;
	}


	private String[] calculer_distance(String[] tuple, int k) {
		String[] k_voisins = new String[k];
		HashMap<Integer, Double>voisins = new HashMap <Integer, Double> (); // <indexDeTrain, distance>
		
		for(int i=0; i<train_data.length; i++)
		{	
			double distance = 0, distanceNum = 0, distanceNom = 0;
			for(int j=0; j<train_data[i].length-1; j++)
			{
				//System.out.print("\nvaleur i " + i + " " + data[0][j] +
				//System.out.println("type is " + data[0][j] + " train_data[" + i + "][" + j + "] " + train_data[i][j]);
				
				if(data[0][j].equals("Numeric"))
					distanceNum += distance_numérique(tuple[j], train_data[i][j]);
				
				if(data[0][j].equals("Nominal") || data[0][j].equals("String"))
					distanceNom += distance_nominale(tuple[j], train_data[i][j]);
				
				if(data[0][j].equals("Date"))
					distanceNom += distance_date(tuple[j], train_data[i][j]);
				/**	
				if(data[0][j].equals("Relation"))
					// traitement particulier à voir
				*/
//				distance ++;
			}
			//distanceNum = Math.sqrt(distanceNum);
			distance = distanceNum + distanceNom;
			distance = Math.sqrt(distance);
			// là on a la distance entre le tuple et une ligne de train test
			voisins.put(i, distance);
		}
		
		
		/** chercher min et max */
		double min = voisins.get(0), max = voisins.get(0);
		
		for (Map.Entry<Integer, Double> entry : voisins.entrySet()) {
		    Integer key = entry.getKey();
		    Double value = entry.getValue();
		    
		    if(min < value)
		    	min = value;
		    
		    if(max > value)
		    	max = value;
		}
		
		
		/** normaliser les distance de voisins 
		for (Map.Entry<Integer, Double> entry : voisins.entrySet()) {
		    Integer key = entry.getKey();
		    Double value = entry.getValue();
		    
		    double val_norm = (value - min) / (max - min);
		    
		    voisins.put(key, val_norm);
		}*/
		
		
		// là on ordonne voisins du plus petit au plus grand selon la valeur distances
		// on prend les k voisins avec la plus petite distance et les rend dans k_voisisn
		HashMap<Integer, Double>voisins_sorted = (HashMap<Integer, Double>) sortByConfidence(voisins, ASC);
		int i =0;
		for (Map.Entry<Integer, Double> entry : voisins_sorted.entrySet()) {
		    Integer key = entry.getKey();
		    Double value = entry.getValue();
		    //System.err.println("index in trainData : " + key + ", distance : " + value + "|");
		  
		    if (i<k)
		    	k_voisins[i] = key + "," + value;
		    else
		    	break;
		    i++;
		}
		//
		return k_voisins;
	}

	
	private double distance_date(String string, String string2) {
		String[] l = string.split("-"); 
		String[] l2 = string2.split("-"); 
		
		LocalDate date = LocalDate.of( Integer.parseInt(l[0]) , Integer.parseInt(l[1]) , Integer.parseInt(l[2]) );
		LocalDate date2 = LocalDate.of( Integer.parseInt(l2[0]) , Integer.parseInt(l2[1]) , Integer.parseInt(l2[2]) );
		
		if(date.compareTo(date2) == 0)
			return 0;
		else
			return 1;
	}


	private double distance_nominale(String string, String string2) {
		// voir distance de manhattan
		if(string.equals(string2))
			return 0; // ici la différence totale
		else 
			return 1; // ici c'est la même
	}


	private double distance_numérique(String string, String string2) {
		// distance de hamming
//		System.err.println("string : " + string + " and string2 : " + string2);
		double val1 = Double.parseDouble(string);
		double val2 = Double.parseDouble(string2);
		double val = Math.abs(val1-val2);
		return Math.pow(val, 2);
	}
	
	
	private static Map<Integer, Double> sortByConfidence(Map<Integer, Double> unsortMap, final boolean order)
    {
        List<Entry<Integer, Double>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> b, LinkedHashMap::new));

    }

	public List<String> get_toPrint1(){
		return toPrint1;
	}
	
	public List<String> get_toPrint2(){
		return toPrint2;
	}
	
	public List<String> get_toPrint3(){
		return toPrint3;
	}
}
