package IHM;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import weka.core.Instances;

public class Gui_LoadedData  extends JFrame {
	
	public String [][] data =null;
	Object [] colonnes = null;
	public int nb_ligne, nb_att;
	@SuppressWarnings("null")
	
	public void Gui_OneDataset(Instances dataset){
		this.setLocationRelativeTo(null);
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setTitle("DATASET GUI");
		this.setSize(500,520);
		
		//utiliser Instances.getStructure()
		nb_att= dataset.numAttributes();
		nb_ligne= dataset.numInstances();
	    colonnes = new String[nb_att]; 
		data = new String[nb_ligne][nb_att];
	    String att_name=null;
		String[] all_att=null;
		System.out.println(dataset.attribute(0));
		
		for(int j=0;j<nb_att;j++)
		{
			att_name= dataset.attribute(j).toString();
			all_att= att_name.split(" ");
			colonnes[j]=all_att[1];
			//	System.out.println("attribut" +colonnes[j]);
			
		}
		
		
		for(int i=0;i< dataset.numInstances();i++)
		{
			String s = dataset.get(i).toString(); // recupere tt l'instance ,ligne du benchmark
			//String s = dataset.instance(i).toString();
			String[] tab = s.split(","); //récuperer chaque attribut alone
			
			for(int k=0;k<tab.length;k++) //tab.length = nb_attributs
				data[i][k]= tab[k]; //remplir notre matrice des données
		
		}
		
		//création du tableau sur JTable
		JTable tableau = new JTable(data, (String[])colonnes);
		//scroll
		this.getContentPane().add(new JScrollPane(tableau));
	}
	
	
	
	
/*	public void Data_into_list(Reader reader){
		ArffReader arff=new ArffReader(reader,1000);
		Instances dataset= arff.getStructure();
		dataset.setClassIndex(0);
		Instance inst;
		while(inst = arff.readInstance(dataset) != null)
		{
			while()
			
		}	
	}*/
}
