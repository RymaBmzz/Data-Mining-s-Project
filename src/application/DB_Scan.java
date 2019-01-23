package application;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import weka.core.Instances;

public class DB_Scan {
	
	String nomFichier = null; // path du fichier 
	static Instances dataset = null; // all the dataset
	static String[][] data = null, all_data = null;
	static String[] type_att = null;
	static boolean[] is_visited = null, is_in_cluster = null; // chaque valeur correspond à une instance donc ligne de data
	static ArrayList<Integer> indice_instances = new ArrayList<Integer>();  // les indices des instances de data
	static ArrayList<Integer> voisinage_p = new ArrayList<Integer>();
	static ArrayList<Integer> voisinage_p1 = new ArrayList<Integer>();
	static double minPts, epsilon;
	HashMap<Integer, Object> distances = null;
	PrétraitementDataset dataPreprocess = new PrétraitementDataset();
	 double Inertie_Intraclasse = 0, Inertie_Interclasse = 0;
	 HashMap<Integer, ArrayList<Integer>>all_clusters = new HashMap <Integer, ArrayList<Integer>> ();
	 ArrayList<Integer> noise = new ArrayList<Integer>();
	
	 public double get_intrac(){ return Inertie_Intraclasse;}
	 public double get_interc(){ return Inertie_Interclasse;}
	 
	 public HashMap<Integer, ArrayList<Integer>> get_clusters(){
		 return all_clusters;
	 }
	 
	 public ArrayList<Integer> get_noise(){ return noise;}
	 
	public void appliquer_DBScan(String nomFich, double minPtsVoisinage, double e) throws IOException {
		System.err.println("\nClass DBScan");
		
		minPts = minPtsVoisinage;
		epsilon = e;
		nomFichier = nomFich;
		
		dataset = LireDataset(nomFichier);
		all_data = dataPreprocess.CleanDataset(dataset, nomFichier);
		
		int nb_att = all_data[0].length - 1; // on ne prend pas l'attribut classe
		int nb_instance = all_data.length - 1; // pour éliminer la première ligne des types des instances
		
		type_att = new String[nb_att];
		data = new String[nb_instance][nb_att];
		
		
		// récupérer le type des attributs
		for(int j = 0; j<nb_att; j++)
			type_att[j] = all_data[0][j];
		
		
		// récupérer data sans la classe
		int k=0;
		for(int i=1; i<all_data.length; i++)
		{
			for(int j=0; j<nb_att; j++)
				data[k][j] = all_data[i][j];

			k++;
		}
			
		// initialiser visited à false et in_cluster à false et indice_instance à i
		is_visited = new boolean[nb_instance];
		is_in_cluster = new boolean[nb_instance];
		
		for(int i = 0; i<nb_instance; i++)
		{
			is_visited[i] = false;
			is_in_cluster[i] = false;
			indice_instances.add(i);
		}
		
		
		// select random value from indice_instance
		Random randomGenerator;
		randomGenerator = new Random();
		int index_point_p = 0, index = 0, k2=0, nb_cluster = 0;		
		
			
		int nb_unvisited  = is_visited.length;
		do
		{
			// choisir point p aléatoire qui est une instance
			index = randomGenerator.nextInt(indice_instances.size());
			index_point_p = indice_instances.get(index);
//			System.err.println((k2+1) + "/ point choisi est : " + index_point_p);
			k2++;
			
			// marquer point p comme visité
			is_visited[index_point_p] = true;
			
			// get voisinage de p
			voisinage_p = getVoisinage_p(index_point_p);
			
//			System.out.println("size voisinage for point " + index_point_p + " is : " + voisinage_p.size());
			
			if(voisinage_p.size() >= minPts)
			{
				ArrayList<Integer> cluster = new ArrayList<Integer>();  // créer new cluster
				cluster.add(index_point_p);
				is_in_cluster[index_point_p] = true;
				
				// parcourir chaque point du voisinage
				for(int l=0; l<voisinage_p.size(); l++)
				{
					int point_p1 = voisinage_p.get(l);
					if(is_visited[point_p1] == false)
					{
						// marquer le point visité
						is_visited[point_p1] = true;
						
						voisinage_p1 = getVoisinage_p(point_p1);
						
						if(voisinage_p1.size() >= minPts)
						{
							// ajouter ces point à voisinage_p
							voisinage_p = union(voisinage_p, voisinage_p1);
							
							if(is_in_cluster[point_p1] == false)
							{
								cluster.add(point_p1);
								is_in_cluster[point_p1] = true;
							}
						}
					}
					/*
					if(is_in_cluster[point_p1] == false)
					{
						cluster.add(point_p1);
						is_in_cluster[point_p1] = true;
					}*/
					
				}
				if(cluster.size() >= minPts)
				{
					all_clusters.put(nb_cluster, cluster);
					nb_cluster++;
				}
				else
				{
					noise.add(index_point_p);			
					}
				
			}
			else
			{
				noise.add(index_point_p);
			}
			
			nb_unvisited--; // un point non visité de moins
			indice_instances.remove(index);
		}
		while(nb_unvisited > 0);
		
/*****************************************************************************************************************/			    
	    
	    HashMap<Integer, String[]>all_centroid = new HashMap <Integer, String[]> ();
	        
	    HashMap<Integer, String[]>all_nominal = new HashMap <Integer, String[]> ();
	    
	    for (Map.Entry<Integer, ArrayList<Integer>> entry : all_clusters.entrySet()) {
		    Integer key = entry.getKey();
		    ArrayList<Integer> value_cluster = entry.getValue();
		    
		    
		    String[] nominal = new String[value_cluster.size()];
		    
		    // cluster number key
		    double moy = 0;
		    String[] centroid = new String[nb_att];
		    
		    for(int j=0; j<type_att.length; j++)
		    	if(type_att[j].equals("Numeric"))
		    		centroid[j] = 0 + ""; // on calcule la moyenne
		    
		    	else
		    	{
		    		//int index_mode = (int) dataset.meanOrMode(dataset.attribute(j)); // mode nominal
		    		centroid[j] = "coucou";		    		
		    	}
		    
		    for(int r = 0; r<value_cluster.size(); r++)
		    {
		    	int index_val = value_cluster.get(r);

		    	for(int j=0; j<data[index_val].length; j++)
		    	{
		    		if(type_att[j].equals("Numeric"))
		    		{
		    			double val = Double.parseDouble(data[index_val][j]) + Double.parseDouble(centroid[j]);
		    			centroid[j] = val + "";
		    		}
		    	}		    		
		    }
		    
		    
		    for(int j=0; j<data[0].length; j++)
	    	{
		    	nominal = new String[value_cluster.size()];
		    	for(int r = 0; r<value_cluster.size(); r++)
		    	{
		    		int index_val = value_cluster.get(r);
		    	
		    		if(!type_att[j].equals("Numeric"))
		    		{
		    			nominal[r] =  data[index_val][j];
		    			System.out.println("nominal= "+nominal[r]);
		    		}
		    	}
		    	if(!type_att[j].equals("Numeric"))
		    		all_nominal.put(j, nominal);
		    	
		    }
		    
		    for(int j=0; j<type_att.length; j++)
		    	if(type_att[j].equals("Numeric"))
		    	{
		    		moy = Double.parseDouble(centroid[j]) / value_cluster.size();
		    		centroid[j] = moy + ""; // on calcule la moyenne
		    	}
		    
		    if(all_nominal.size() != 0)
		    {
		    	for (Entry<Integer, String[]> entry2 : all_nominal.entrySet())
			    {
			    	Integer key2 = entry2.getKey();
				    String[] value_nominal = entry2.getValue();
				    System.out.println("**************" + key2);
				    String mode_j = mode(value_nominal);
				    centroid[key2] = mode_j;
			    }
		    }
		    
		    
		    all_centroid.put(key, centroid);   
		}
	    
/*****************************************************************************************************************/	    
	    
	    
	    for (Map.Entry<Integer, String[]> entry : all_centroid.entrySet()) {
		    Integer key = entry.getKey();
		    String[] value_centroid = entry.getValue();
		    
		    System.out.println("\n********** centroid cluster" + (key+1) + " **********");
		    
		    for(int r = 0; r<value_centroid.length; r++)
		    	System.out.print(value_centroid[r] + ", ");
		    System.out.println();
	    }
    
	
/*****************************************************************************************************************/	    
	    
	   
	    
	    for (Map.Entry<Integer, ArrayList<Integer>> entry : all_clusters.entrySet()) {
		    Integer key = entry.getKey();
		    ArrayList<Integer> value_cluster = entry.getValue();
		    
		    String[] centroid_key = all_centroid.get(key);
		    double Intraclasse_cluster = 0, diff =0;
		    for(int r = 0; r<value_cluster.size(); r++)
		    {
		    	int index_val = value_cluster.get(r);
		    	
		    	for(int j=0; j<data[0].length; j++)
		    	{
		    		if(type_att[j].equals("Numeric"))
		    		{
		    			diff = Double.parseDouble(data[index_val][j]) - Double.parseDouble(centroid_key[j]); 
		    			Intraclasse_cluster += Math.pow(diff, 2);
		    		}
		    		else
		    		{
		    			if(type_att[j].equals("Nominal"))
		    			{
		    				if(!centroid_key[j].equals(data[index_val][j]))
			    				Intraclasse_cluster += 1;
		    			}
		    			
		    			
		    		}
		    	}
		    }
//		    System.out.println("Inertie_Intraclasse Cluster : " + (key+1) + " is : " + Intraclasse_cluster);
		    Inertie_Intraclasse += Intraclasse_cluster;
	    }
	    
	    
/*************************aaaaaaaaaaaaaa****************************************************************************************/	    
	    
	    String[] centroid_total = new String[nb_att];
	    HashMap<Integer, String[]>all_nominal2 = new HashMap <Integer, String[]> ();
	    
	    for (Map.Entry<Integer, String[]> entry : all_centroid.entrySet()) {
	    	
		    Integer key = entry.getKey();
		    String[] value_centroid = entry.getValue();
		      		    		    
		    for(int j=0; j<type_att.length; j++)
		    	if(type_att[j].equals("Numeric"))
		    		centroid_total[j] = 0 + ""; // on calcule la moyenne
		    	else
		    	{
		    		//int index_mode = (int) dataset.meanOrMode(dataset.attribute(j)); // mode nominal
		    		centroid_total[j] = "coucou";
		    	}
		    
		    
		    for(int r = 0; r<value_centroid.length; r++)
		    {
		    	if(type_att[r].equals("Numeric"))
	    		{
	    			double val = Double.parseDouble(value_centroid[r]) + Double.parseDouble(centroid_total[r]);
	    			centroid_total[r] = val + "";
	    		}
		    } 
	    }
	    
	    
	    //String[] nominal = new String[all_centroid.size()];
	    
	    for(int j=0; j<data[0].length; j++)
    	{
	    	//nominal = new String[all_clusters.size()];
	    	String[] nominal = new String[all_centroid.size()];
	    	for(int r = 0; r<all_centroid.size(); r++)
	    	{
	    		String[] val_centroid = all_centroid.get(r);
	    	
	    		if(!type_att[j].equals("Numeric"))
	    		{
	    			nominal[r] =  val_centroid[j];
	    		}
	    	}
	    	if(!type_att[j].equals("Numeric"))
	    		all_nominal2.put(j, nominal);
	    }
	    
	    
	    for (Entry<Integer, String[]> entry2 : all_nominal.entrySet())
	    {
	    	Integer key2 = entry2.getKey();
		    String[] value_nominal = entry2.getValue();
		    
		    String mode_j = mode(value_nominal);
		    centroid_total[key2] = mode_j;
	    }
	    
	    
	    for(int j=0; j<type_att.length; j++)
	    	if(type_att[j].equals("Numeric"))
	    	{
	    		if(all_clusters.size() != 0)
	    		{
	    			double moy = Double.parseDouble(centroid_total[j]) / all_clusters.size();
		    		centroid_total[j] = moy + ""; // on calcule la moyenne
	    		}
	    		
	    		else
	    			centroid_total[j] = "0";
	    		
	    	}
	
/*****************************************************************************************************************/	   
	    /*
	    System.out.println("centroid_total");
	    for(int j=0; j<type_att.length; j++)
	    	System.out.print(centroid_total[j] + "| ");	 
	    System.out.println();
 		*/
		    
/*****************************************************************************************************************/
	    
	    for (Map.Entry<Integer, String[]> entry : all_centroid.entrySet()) {
		    Integer key = entry.getKey();
		    String[] value_centroid = entry.getValue();
		    
		    
		    for(int r = 0; r<value_centroid.length; r++)
		    {
//		    	System.out.print(value_centroid[r] + ", ");
		    	
		    	if(type_att[r].equals("Numeric"))
	    		{
	    			double diff = Double.parseDouble(value_centroid[r]) - Double.parseDouble(centroid_total[r]); 
	    			Inertie_Interclasse += Math.pow(diff, 2);
	    		}
	    		else
	    		{
	    			if(!value_centroid[r].equals(centroid_total[r]))
	    				Inertie_Interclasse += 1;
	    		}
		    }
//		    System.out.println();
	    }
	    
/*****************************************************************************************************************/
	    
	    System.out.println("\n********** Inertie_Interclasse : " + Inertie_Interclasse + " **********");
	    System.out.println("\n********** Inertie_Intraclasse : " + Inertie_Intraclasse + " **********");
	/*    
	    
	    
	    for (Map.Entry<Integer, ArrayList<Integer>> entry : all_clusters.entrySet()) {
		    Integer key = entry.getKey();
		    ArrayList<Integer> value_cluster = entry.getValue();
		    
		    System.out.println("\n********** cluster" + (key+1) + " **********");
		    
		    for(int r = 0; r<value_cluster.size(); r++)
		    {
		    	int index_val = value_cluster.get(r);
		    	System.out.print("instance " + (index_val+1) + " : ");
		    	for(int j=0; j<data[index_val].length; j++)
		    		System.out.print(data[index_val][j] + ", ");
		    	System.out.println();
		    }    
		}
		
		
		System.out.println("\n\n********** Noisey instances" + " **********");
	    
	    for(int r = 0; r<noise.size(); r++)
	    {
	    	int index_val = noise.get(r);
	    	System.out.print("instance " + (index_val+1) + " : ");
	    	for(int j=0; j<data[index_val].length; j++)
	    		System.out.print(data[index_val][j] + ", ");
	    	System.out.println();
	    } 
	    
*/	    
	}// Fin classe
	
	
	private ArrayList<Integer> union(ArrayList<Integer> list1, ArrayList<Integer> list2) {
        Set<Integer> set = new HashSet<Integer>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<Integer>(set);
    }


	private ArrayList<Integer> getVoisinage_p(int index_pnt_p) {
		ArrayList<Integer> voisinage = new ArrayList<Integer>();
		
		// calculer distance de point p de tous les autres points
		for(int i=0; i<data.length; i++)
			if(i != index_pnt_p)
			{
				double distance = caluculer_distance(i, index_pnt_p);
				if(distance <= epsilon)
					voisinage.add(i);
//				System.err.println("distance entre index_p " + (index_pnt_p+1) + " and index_i " + (i+1) + " is " + distance);
			}
	
		return voisinage;
	}


	private double caluculer_distance(int i, int index_p) {
		String[] point_i = null, point_p = null; 
		int nb_att = data[0].length;
		point_i = new String[nb_att];
		point_p = new String[nb_att];
		
		for(int j=0; j<nb_att; j++)
		{
			point_i[j] = data[i][j];
			point_p[j] = data[index_p][j];
		}
		
		double d = 0;
		for(int j=0; j<type_att.length; j++)
		{
			if(type_att[j].equals("Numeric"))
				d += distance_numérique(point_i[j], point_p[j]);
			
			if(type_att[j].equals("Nominal") || data[0][j].equals("String"))
				d += distance_nominale(point_i[j], point_p[j]);
			
			if(type_att[j].equals("Date"))
				d += distance_date(point_i[j], point_p[j]);
			
//			System.err.println("type is " + type_att[j] +  ", beetween " + point_i[j] + " and " + point_p[j] + " it's " + d);
		}
		
		return Math.sqrt(d);
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
			return 0; // ici ils sont égaux
		else 
			return 1; // ici ils sont différent 
	}


	private double distance_numérique(String string, String string2) {
		// distance de hamming
//		System.err.println("string : " + string + " and string2 : " + string2);
		double val1 = Double.parseDouble(string);
		double val2 = Double.parseDouble(string2);
		double val = Math.abs(val1-val2);
		return Math.pow(val, 2);
	}

	
	// fonction pour lire le fichier et récupérer le dataset
	private Instances LireDataset(String nomFichier) throws IOException {
		Instances dataset = null;
		String DATASETPATH = "./data/" + nomFichier;
		LoadData mg = new LoadData();
		dataset = mg.loadDataset(DATASETPATH);
		return dataset;	
	}

	
	public static String mode(String[] array) {
        String mode = array[0];
        int maxCount = 0;
        for (int i = 0; i < array.length; i++) {
            String value = array[i];
            int count = 0;
            for (int j = 0; j < array.length; j++) {
                if (array[j].equals(value)) 
                	count++;
                if (count > maxCount) {
                    mode = value;
                    maxCount = count;
                    }
                }
        }
        if (maxCount > 1) {
            return mode;
        }
        return null;
    }
	
}
