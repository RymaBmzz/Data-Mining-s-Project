package application;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import IHM.LoadData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.FileChooser;
import weka.core.Instance;
import weka.core.Instances;

/***************************** BoxPlot *************************/
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.BoxAndWhiskerToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.Log;
import org.jfree.util.LogContext;

import com.sun.glass.events.WindowEvent;

import weka.core.Instances;
/**************************************************************/

 public class Opt3_Controller implements Initializable {
	 String datasetname=null;
	public String [][] data =null;
	Object [] colonnes = null;
	String [] att_numerique = null;
	public String [] col_names = {"Attribut", "Min", "Max", "Mediane", "Q1", "Q3", "Mode", "Moyenne"};
	String mes_att_numérique [][] = new String [20][8];
	public String mes_att_numériqueF[][] = null;
	public int nb_ligne, nb_att, nb_att_numérique = 0;
	@SuppressWarnings("null")
	public Instances datasetopt2 = null;
	File selectedDataset;
	@FXML
	public Label datasetchoosen;
	
	@FXML
	public TableView<Map> tableView_opt3;
	
	@FXML 
	public Button buttdataset;
	
	@FXML
	public TextArea textdsinfo;
	
	
/*	public void Load_from_Dataset(String DATASETPATH){
		
		LoadData mg = new LoadData();
		try {
			this.datasetopt2 = mg.loadDataset(DATASETPATH);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		nb_att= this.datasetopt2.numAttributes();
		nb_ligne= this.datasetopt2.numInstances();
	    colonnes = new String[nb_att]; 
		data = new String[nb_ligne][nb_att];
	    String att_name=null;
		String[] all_att=null;
		//System.out.println(dataset.attribute(0));
		for(int j=0;j<nb_att;j++)
		{
			att_name= this.datasetopt2.attribute(j).toString();
			all_att= att_name.split(" ");
			colonnes[j]= this.datasetopt2.attribute(j).name();
		
		}
		

		for(int i=0;i< this.datasetopt2.numInstances();i++)
		{
				String s= this.datasetopt2.get(i).toString(); // recupere tt l'instance ,ligne du benchmark
				String[] tab = s.split(","); //récuperer chaque attribut alone
				
				for(int k=0;k<tab.length;k++) //tab.length = nb_attributs
					data[i][k]= tab[k]; //remplir notre matrice des données
		
		}

		
	}
	*/
	public void Show_Stat_Values(Instances dataset) {
	
		//utiliser Instances.getStructure()
		nb_att= dataset.numAttributes();
		nb_ligne= dataset.numInstances();
	    colonnes = new String[nb_att]; 
		data = new String[nb_ligne][nb_att];
	    String att_name=null;
		String[] all_att=null;
		
		for(int j=0; j < nb_att; j++)
		{
			att_name= dataset.attribute(j).toString();
			all_att= att_name.split(" ");
			System.out.println(all_att);
			colonnes[j]=all_att[1]; // Il s'agit du nom de l'attribut
			//System.out.println("all_att[1] " +all_att[1]);
			
			data[0][j] = all_att[2]; // Ici il s'agit du type : numeric, date ou autre	
			if((all_att[2]).equals("numeric"))
			{
				System.out.println(all_att[1]);
				mes_att_numérique[nb_att_numérique][0] = all_att[1]; // prendre le nom de l'attribut en question
				nb_att_numérique++;
			}
			
			mes_att_numériqueF = new String[nb_att_numérique][8];
		}
		
		
		for(int i=1; i < nb_ligne; i++)
		{
			String s = dataset.get(i).toString(); // recupere tt l'instance c.à.d. une ligne du benchmark
			//String s = dataset.instance(i).toString();
			String[] tab = s.split(","); //récuperer chaque attribut alone
			
			for(int k=0; k < tab.length; k++) //tab.length = nb_attributs
				data[i][k]= tab[k]; //remplir notre matrice des données
		}
		
		
		for(int i = 0; i < nb_att_numérique; i++)
		{
			//calculer la moyennes des att_numérique
			String att = mes_att_numérique[i][0]; 
			//trouver l'index de att dans colonnes
			int index = TrouverIndex(att, (String[]) colonnes);
		//	String tabValeur[] = new String[data.length-1];
			String tabValeur[] = new String[data.length-1];
			
			//prendre les valeurs de la colonne index de la matrices data => data[1-fin][index]
			for (int k = 1; k < data.length; k++) //on prend pas la ligne contenant le type
			{
				tabValeur[k-1] = data[k][index];
				//System.out.println(Float.parseFloat(tabValeur[k-1]) + " -----> " + k);
			}
			
			//{"Attributs", "Min", "Max", "Mediane", "Q1", "Q3", "Mode", "Moyenne"};
			float moy = MoyenneAtt(tabValeur);
			
			float min = getMinValue(tabValeur);
			mes_att_numérique[i][1] = min+"";
			
			float max = getMaxValue(tabValeur);
			mes_att_numérique[i][2] = max+"";
			
			float tab[] = new float[tabValeur.length];
			for(int l = 0; l < tab.length; l++)
				tab[l] = Float.parseFloat(tabValeur[l]);
			
			Arrays.sort(tab);
			
			float mediane = getMedian(tab); // c'est aussi le Q2
			mes_att_numérique[i][3] = mediane+"";
			
			float q1 = quartile(tab, 25);
			mes_att_numérique[i][4] = q1+"";
			
			float q3 = quartile(tab, 75);
			mes_att_numérique[i][5] = q3+"";

			float mode = getMode(tabValeur);
			mes_att_numérique[i][6] = mode+""; 
			
			float moyenne = MoyenneAtt(tabValeur);
			mes_att_numérique[i][7] = moyenne+"";
			
			/*
			float midRange = getMaxValue(tabValeur);
			mes_att_numérique[i][7] = midRange+"";
			*/
		}
		
		for(int i = 0; i < mes_att_numériqueF.length; i++)
			for (int j = 0; j < mes_att_numériqueF[i].length; j++)
				mes_att_numériqueF[i][j] = mes_att_numérique[i][j];
		
		/*
		att_numerique= new String[nb_ligne];
		for(int p=0;p<nb_ligne;p++)
			att_numerique[p]=data[p][0];
		float moy=MoyenneAtt(att_numerique);
		System.out.printf("moyenne= ",moy);
		*/
		if(nb_att_numérique == 0)					
		{
			JOptionPane job;
			//Boite de message d'erreur
			job = new JOptionPane();
			job.showMessageDialog(null, "Attention : Il n'y a pas d'attributs numériques",
					"Message d'erreur", JOptionPane.WARNING_MESSAGE);
		}
		
		//création du tableau sur JTable
		//JTable tableau = new JTable(mes_att_numériqueF, (String[])col_names);
		
	}
	
	public float MoyenneAtt(String tab[]) {
		float sum = 0;
		int size=tab.length;

		for(int i=0;i<size;i++)
			sum += Float.parseFloat(tab[i]);
		
		return ((float)sum/size);
		
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
	
	public float getMode(String tab[]) {
	    float maxValue = 0;
	    int maxCount = 0;

	    for (int i = 0; i < tab.length; ++i) {
	        int count = 0;
	        for (int j = 0; j < tab.length; ++j) {
	            if (Float.parseFloat(tab[j]) == Float.parseFloat(tab[i])) 
	            	++count;
	        }
	        if (count > maxCount) {
	            maxCount = count;
	            maxValue = Float.parseFloat(tab[i]);
	        }
	    }

	    return maxValue;
	}
	
	// the array double[] m MUST BE SORTED
	public float getMedian(float[] m) {
	    int middle = m.length/2;
	    if (m.length%2 == 1) {
	        return m[middle];
	    } else {
	        return (float) ((m[middle-1] + m[middle]) / 2.0);
	    }
	}
	
    /**
     * Retrive the quartile value from an array
     * .
     * @param values THe array of data
     * @param lowerPercent The percent cut off. For the lower quartile use 25,
     *      for the upper-quartile use 75
     * @return
     */
    public float quartile(float[] values, float lowerPercent) {

        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("The data array either is null or does not contain any data.");
        }

        // Rank order the values
        float[] v = new float[values.length];
        System.arraycopy(values, 0, v, 0, values.length);
        Arrays.sort(v);

        int n = (int) Math.round(v.length * lowerPercent / 100);
        
        return v[n];

    } 
    
	  //*******************************************************************//

public void Choose_Dataset(ActionEvent event) throws IOException, InterruptedException{

FileChooser fc= new FileChooser();
fc.setInitialDirectory(new File("C:\\Program Files\\Weka-3-8\\data"));
selectedDataset= fc.showOpenDialog(null);

if(selectedDataset != null){
	textdsinfo.clear();
	textdsinfo.appendText("Data Set : "+selectedDataset.getName());
	String DATASETPATH = "C:/Program Files/Weka-3-8/data/"+selectedDataset.getName();
	this.datasetname=selectedDataset.getName();
	LoadData mg = new LoadData();
	try {
		this.datasetopt2 = mg.loadDataset(DATASETPATH);
		nb_att= this.datasetopt2.numAttributes();
		nb_ligne= this.datasetopt2.numInstances();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	textdsinfo.appendText("\nNombre d'attributs : "+nb_att+"\nNombre d'instance : "+nb_ligne);
	
	Calcul_Stats(this.datasetname,this.datasetopt2);

}
else{ textdsinfo.appendText("Data Set Choisi non valide !");
	System.out.println("Data Set choisi non valide");}
}
    
    //*******************************************************************//

private ObservableList<Map> load_datasetToMap(TableView<Map> table){
	//appel pour avoir les informations et le contenu du dataset
	
	String value;
	ObservableList<Map> allinstances= FXCollections.observableArrayList();
	for(int i=1;i<nb_att_numérique;i++){
		Map<String,String> oneline= new HashMap<>();
		
		for(int j=0;j<col_names.length;j++)
		{
			 
			 oneline.put(col_names[j],mes_att_numériqueF[i][j]);
		}
		allinstances.add(oneline);
	}
	table.setItems(allinstances);
	return allinstances;
}

               //*******************************************************************//
    public void construct_tableViewOpt3(TableView tableView_opt2) {

    	String cl_name,type;
    	TableColumn<Map, String> Column = null;
    	tableView_opt2.getColumns().clear();
    	tableView_opt2.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    	
    	//construction des colonnes du  table view
    	tableView_opt2.setItems(load_datasetToMap(tableView_opt2));
        String listcol=null;
    		for(int size=0;size<nb_att_numérique;size++)
    		{
    			String column= col_names[size];	
    			Column = new TableColumn<>(column); 
    			Column.setCellValueFactory(new MapValueFactory(column));
    	        tableView_opt2.getColumns().add(Column);
    		}
    	
    }
    
    
    public void Calcul_Stats(String datasetname,Instances dataset){
    	
    
    	Show_Stat_Values(dataset);
    	construct_tableViewOpt3(tableView_opt3);
    	
    	
    	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
    	
	}
	public void write_string(String x)
	{
		datasetchoosen.setText("Data Set : "+x);}
    

}
