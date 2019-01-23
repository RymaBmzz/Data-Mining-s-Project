package application;

import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import weka.core.Instances;

public class Pr�traitementDataset extends JFrame{
	String[] NomInstMissVal = {"breast-cancer.arff", "hypothyroid.arff", "labor.arff", "soybean.arff", "supermarket.arff", "vote.arff"};
	public String [][] data =null;
	Object [] colonnes = null;
	String [] att_numerique = null;
	String [] col_names = {"Attributs", "Min", "Max"};
	String mes_att_num�rique [][] = new String [40][3];
	String mes_att_num�riqueF[][] = null;
	public int nb_ligne, nb_att, nb_att_num�rique = 0;
	@SuppressWarnings("null")
	
	public String[][] CleanDataset(Instances dataset, String nomFichier) {
		
		this.setLocationRelativeTo(null);
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setTitle("Valeurs Statistiques");
		this.setSize(500,520);
		
		boolean Instval_manq = false;
		for(int i=0; i<NomInstMissVal.length; i++)
			if(nomFichier.equals(NomInstMissVal[i]))
			{
				Instval_manq = true;
				break;
			}
		
	//	if(nomFichier.equals("labor.arff") || nomFichier.equals("hypothyroid.arff")) //Missing values
		if(Instval_manq) // l'instance contient des valeurs manquantes
		{
			//Rempalcer la valeurs manquantes avant
			System.out.println("--ICI Traitement3");
			
			nb_att= dataset.numAttributes();
			nb_ligne= dataset.numInstances();
		    colonnes = new String[nb_att]; 
			data = new String[nb_ligne+1][nb_att];
		    String att_name=null;
			String[] all_att=null;
			
			// Remplacement des valeurs manquante
			
			for(int l=0; l < nb_ligne; l++)
				for(int j=0; j < nb_att; j++)
				{
					if(dataset.instance(l).isMissing(j)) {
						//Verifier type de l'attribut
						if(dataset.attribute(j).isNumeric())
						{
							String mean = dataset.meanOrMode(dataset.attribute(j)) + "";
						//	System.out.println(mean);
							dataset.instance(l).setValue(dataset.attribute(j), Double.parseDouble(mean));
						}
						if(dataset.attribute(j).isNominal())
						{
							double val = dataset.meanOrMode(dataset.attribute(j));
							String s = dataset.attribute(j).value((int) val);
							dataset.instance(l).setValue(dataset.attribute(j), s);
						}
					}
					
				}
				
			//Apr�s remplacement on normalise les valeurs
			for(int j=0; j < nb_att; j++)
			{
				att_name= dataset.attribute(j).toString();
				all_att= att_name.split(" ");
				System.out.println(all_att);
				colonnes[j]=all_att[1]; // Il s'agit du nom de l'attribut
				//System.out.println("all_att[1] " +all_att[1]);
				
				//data[0][j] = all_att[2]; // Ici il s'agit du type : numeric, date ou autre	
				if(dataset.attribute(j).isDate())
					data[0][j] = dataset.attribute(j).toString();
					//data[0][j] = "Date";	
				if(dataset.attribute(j).isNominal())
					data[0][j] = "Nominal";
				if(dataset.attribute(j).isNumeric())
					data[0][j] = "Numeric";
				if(dataset.attribute(j).isString())
					data[0][j] = "String";
				if(dataset.attribute(j).isRelationValued())
					data[0][j] = "Relation";
				if((all_att[2]).equals("numeric"))
				{
					System.out.println(all_att[1]);
					mes_att_num�rique[nb_att_num�rique][0] = all_att[1]; // prendre le nom de l'attribut en question
					nb_att_num�rique++;
				}
			}
			mes_att_num�riqueF = new String[nb_att_num�rique][3];
			
			for(int i=1; i < data.length; i++)
			{
				String s = dataset.get(i-1).toString(); // recupere tt l'instance c.�.d. une ligne du benchmark
				String[] tab = s.split(","); //r�cuperer chaque attribut alone
				
				for(int k=0; k < tab.length; k++) //tab.length = nb_attributs
					data[i][k]= tab[k]; //remplir notre matrice des donn�es
			}
			
			
			for(int i = 0; i < nb_att_num�rique; i++)
			{
				//calculer la moyennes des att_num�rique
				String att = mes_att_num�rique[i][0]; 
				//trouver l'index de att dans colonnes
				int index = TrouverIndex(att, (String[]) colonnes);
				String tabValeur[] = new String[data.length-1];
				
				//prendre les valeurs de la colonne index de la matrices data => data[1-fin][index]
				for (int k = 1; k < data.length; k++) //on prend pas la ligne contenant le type
				{
					tabValeur[k-1] = data[k][index];
				}
				
				//{"Attributs", "Min", "Max"};
				
				float min = getMinValue(tabValeur);
				mes_att_num�rique[i][1] = min+"";
				
				float max = getMaxValue(tabValeur);
				mes_att_num�rique[i][2] = max+"";
				

			}
			
			for(int i = 0; i < mes_att_num�riqueF.length; i++)
				for (int j = 0; j < mes_att_num�riqueF[i].length; j++)
					mes_att_num�riqueF[i][j] = mes_att_num�rique[i][j];
			
			for(int j = 0; j<nb_att; j++)
			{
				att_name= dataset.attribute(j).toString();
				all_att= att_name.split(" ");
			//	colonnes[j] => Il s'agit du nom de l'attribut
				
				//data[0][j] = all_att[2]; // Ici il s'agit du type : numeric, date ou autre	
				if((all_att[2]).equals("numeric"))
				{
					float min = recuperMin(colonnes[j], mes_att_num�riqueF);
					float max = recuperMax(colonnes[j], mes_att_num�riqueF);
				//  	
					for(int i=1;i<data.length; i++)
					{
						float val = Float.parseFloat(data[i][j]);
						if(max-min != 0)
							data[i][j] = Float.toString((val - min)/(max-min));
							//datasett.instance(i-1).setValue(datasett.attribute(j), Double.parseDouble((val - min)/(max-min)));
					}
				}
			}
			
			// data contient le dataset  normalis�
		
		}
////////////////////////////////////////////////////////////////////////////////////////////
		else
		{
			nb_att= dataset.numAttributes();
			nb_ligne= dataset.numInstances();
		    colonnes = new String[nb_att]; 
			data = new String[nb_ligne+1][nb_att];
		    String att_name=null;
			String[] all_att=null;
			
			for(int j=0; j < nb_att; j++)
			{
				att_name= dataset.attribute(j).toString();
				all_att= att_name.split(" ");
				System.out.println(all_att);
				colonnes[j]=all_att[1]; // Il s'agit du nom de l'attribut
				//System.out.println("all_att[1] " +all_att[1]);
				
				//data[0][j] = all_att[2]; // Ici il s'agit du type : numeric, date ou autre	
				if(dataset.attribute(j).isDate())
					data[0][j] = "Date";	
				if(dataset.attribute(j).isNominal())
					data[0][j] = "Nominal";
				if(dataset.attribute(j).isNumeric())
					data[0][j] = "Numeric";
				if(dataset.attribute(j).isString())
					data[0][j] = "String";
				if(dataset.attribute(j).isRelationValued())
					data[0][j] = "Relation";
				if((all_att[2]).equals("numeric"))
				{
					System.out.println(all_att[1]);
					mes_att_num�rique[nb_att_num�rique][0] = all_att[1]; // prendre le nom de l'attribut en question
					nb_att_num�rique++;
				}
				
				mes_att_num�riqueF = new String[nb_att_num�rique][3];
			}
			
			
			for(int i=1; i < data.length; i++)
			{
				String s = dataset.get(i-1).toString(); // recupere tt l'instance c.�.d. une ligne du benchmark
				String[] tab = s.split(","); //r�cuperer chaque attribut alone
				
				for(int k=0; k < tab.length; k++) //tab.length = nb_attributs
					data[i][k]= tab[k]; //remplir notre matrice des donn�es
			}
			
			
			for(int i = 0; i < nb_att_num�rique; i++)
			{
				//calculer la moyennes des att_num�rique
				String att = mes_att_num�rique[i][0]; 
				//trouver l'index de att dans colonnes
				int index = TrouverIndex(att, (String[]) colonnes);
				String tabValeur[] = new String[data.length-1];
				
				//prendre les valeurs de la colonne index de la matrices data => data[1-fin][index]
				for (int k = 1; k < data.length; k++) //on prend pas la ligne contenant le type
				{
					tabValeur[k-1] = data[k][index];
				}
				
				//{"Attributs", "Min", "Max"};
				
				float min = getMinValue(tabValeur);
				mes_att_num�rique[i][1] = min+"";
				
				float max = getMaxValue(tabValeur);
				mes_att_num�rique[i][2] = max+"";
				

			}
			
			for(int i = 0; i < mes_att_num�riqueF.length; i++)
				for (int j = 0; j < mes_att_num�riqueF[i].length; j++)
					mes_att_num�riqueF[i][j] = mes_att_num�rique[i][j];
			
			for(int j = 0; j<nb_att; j++)
			{
				att_name= dataset.attribute(j).toString();
				all_att= att_name.split(" ");
			//	colonnes[j] => Il s'agit du nom de l'attribut
				
				//data[0][j] = all_att[2]; // Ici il s'agit du type : numeric, date ou autre	
				if((all_att[2]).equals("numeric"))
				{
					float min = recuperMin(colonnes[j], mes_att_num�riqueF);
					float max = recuperMax(colonnes[j], mes_att_num�riqueF);
				//  	
					for(int i=1;i<data.length; i++)
					{
						float val = Float.parseFloat(data[i][j]);
						if(max-min != 0)
							data[i][j] = Float.toString((val - min)/(max-min)); /** Formule Normalisation */
							//datasett.instance(i-1).setValue(datasett.attribute(j), Double.parseDouble((val - min)/(max-min)));
					}
				}
			}
			
			// data contient le dataset normalis�
		}
		return data;
	}
		
	private float recuperMax(Object object, String[][] mes_att_num�riqueF2) {
		// TODO Auto-generated method stub
		String nom = object.toString();
		for(int i=0; i<mes_att_num�riqueF2.length; i++)
		{
			if(mes_att_num�riqueF2[i][0].equals(nom))
				return Float.parseFloat(mes_att_num�riqueF2[i][2]);
		}
		return 0;
	}

	private float recuperMin(Object object, String[][] mes_att_num�riqueF2) {
		// TODO Auto-generated method stub
		String nom = object.toString();
		for(int i=0; i<mes_att_num�riqueF2.length; i++)
		{
			if(mes_att_num�riqueF2[i][0].equals(nom))
				return Float.parseFloat(mes_att_num�riqueF2[i][1]);
		}
		return 0;
	}

	public float getMinValue(String tab[]) {
		float min = Float.parseFloat(tab[0]);
		for (int i = 1; i < tab.length; i++)
			if(Float.parseFloat(tab[i]) < min)
				min  = Float.parseFloat(tab[i]);
		return min;
	}
	
	public float getMaxValue(String tab[]) {
		float max = Float.parseFloat(tab[0]);
		for (int i = 1; i < tab.length; i++)
			if(Float.parseFloat(tab[i]) > max)
				max  = Float.parseFloat(tab[i]);
		return max;		
	}
	
	public int TrouverIndex(String valeur, String tab[]) {
		for (int i = 0; i < tab.length; i++)
			if(valeur.equals(tab[i]))
				return i;
	
		return -1;
	}

}
