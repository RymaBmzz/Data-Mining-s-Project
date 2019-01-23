package application;

import IHM.LoadData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import weka.core.Instance;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;




public class Opt1_Controller implements Initializable{
	public int Opt2_On=-1;
	File selectedDataset;
	String datasetname=null;
	public String [][] data =null;
	String[] colonnes = null, type_colonne =null;
	public int nb_ligne,nb_att;
	public Instances dataset = null;
	String DATASETPATH=null;
	double minPts, epsilon;
	@FXML
	public TableView<Map> tableView_opt1;
	
	@FXML
	public TableView<Map> tableView_opt2;
    
	@FXML
	public  TextArea textdetails;
	
	@FXML
	public Button chooseDS;
	
	 @FXML
	    private Button buttnormalisation;

	 @FXML
	 private Label labeldescription;
	 
	 @FXML
	 private Button buttBoxplot;
	 
	 @FXML
	 private Button buttBoxplotNrm;
	 
    @FXML
    private BarChart<?, ?> histogramme;

    @FXML
    private ComboBox<String> comboAttributes;

    @FXML
    private Label labelHistog;
    
	ObservableList<String> listAttribute; 
	
	    @FXML
	    private Button chooseDS1;

	    @FXML
	    private Button valStat;

	  
	    @FXML
	    private Button buttInstances;


	    @FXML
	    private TextArea textareafreqitem_all;

	    @FXML
	    private TextArea textarearegleassoc_all;

	    @FXML
	    private TextArea timefrequentitem;

	    @FXML
	    private TextArea timeregleassoc;

	   
	    @FXML
	    private TextArea textareafreqitem_support;

	    @FXML
	    private TextArea textarearegleassoc_confiance;
	    
	    @FXML
	    private TextField support_min;

	    @FXML
	    private TextField confidence_min;

	    @FXML
	    private Button button_suppmin;

	    @FXML
	    private Button button_confmin;
	    
	    @FXML
	    private TextField k_value;

	    @FXML
	    private Button button_knn;
	    /*
	    @FXML
	    private TextField textarea_print2;
		*/
	    @FXML
	    private TextArea textarea_print1;

	    @FXML
	    private TextArea textarea_print3;
	    
	    @FXML
	    private TextArea textarea_print22;
	    


	    @FXML
	    private TextArea affich_cluster;

	    @FXML
	    private TextArea affich_noise;

	    @FXML
	    private TextField param_eps;

	    @FXML
	    private TextField param_minpts;

	    @FXML
	    private TextField param_intrac;

	    @FXML
	    private TextField param_interc;

	    @FXML
	    private Button button_dbscan;
					//*******************************************************************//
	
public void Load_Dataset(){

	 String DATASETPATHstat = "./data/iris.arff";
	LoadData mg = new LoadData();
	try {
		this.dataset = mg.loadDataset(DATASETPATHstat);
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	nb_att= dataset.numAttributes();
	nb_ligne= dataset.numInstances();
    colonnes = new String[nb_att]; 
    type_colonne = new String[nb_att]; 
	data = new String[nb_ligne][nb_att];
    String att_name=null;
	String[] all_att=null;
	//System.out.println(dataset.attribute(0));
	for(int j=0;j<nb_att;j++)
	{
		att_name= dataset.attribute(j).toString();
		all_att= att_name.split(" ");
		colonnes[j]=(String)all_att[1];
		type_colonne[j]=(String)all_att[2];
	//	System.out.println("attribut" +colonnes[j]);
		
	}
	
	
	
	for(int i=0;i< dataset.numInstances();i++)
	{
			String s= dataset.get(i).toString(); // recupere tt l'instance ,ligne du benchmark
			String[] tab = s.split(","); //récuperer chaque attribut alone
			
			for(int k=0;k<tab.length;k++) //tab.length = nb_attributs
				data[i][k]= tab[k]; //remplir notre matrice des données
	
	}

	
}

			//*******************************************************************//

public void Load_from_Dataset(String DATASETPATH){
	
	LoadData mg = new LoadData();
	try {
		this.dataset = mg.loadDataset(DATASETPATH);
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	nb_att= this.dataset.numAttributes();
	nb_ligne= this.dataset.numInstances();
    colonnes = new String[nb_att]; 
    type_colonne = new String[nb_att]; 
	data = new String[nb_ligne][nb_att];
    String att_name=null;
	String[] all_att=null;
	//System.out.println(dataset.attribute(0));
	for(int j=0;j<nb_att;j++)
	{
		att_name= dataset.attribute(j).toString();
		all_att= att_name.split(" ");
		colonnes[j]= dataset.attribute(j).name();
		type_colonne[j]=(String)all_att[2];
	//	System.out.println("attribut" +colonnes[j]);
		
	}
	
	
	
	for(int i=0;i< dataset.numInstances();i++)
	{
			String s= dataset.get(i).toString(); // recupere tt l'instance ,ligne du benchmark
			String[] tab = s.split(","); //récuperer chaque attribut alone
			
			for(int k=0;k<tab.length;k++) //tab.length = nb_attributs
				data[i][k]= tab[k]; //remplir notre matrice des données
	
	}

	
}

			//*******************************************************************//

public void construct_tableView() {

	Load_Dataset();
	
	String cl_name,type;
	TableColumn<Map, String> Column;
	tableView_opt1.getColumns().clear();
	tableView_opt1.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	
	//construction des colonnes du  table view
	tableView_opt1.setItems(load_datasetToMap(tableView_opt1));
    String listcol=null;
		for(int size=0;size<nb_att;size++)
		{
			cl_name=colonnes[size];
			type= type_colonne[size];
			String column = dataset.attribute(size).name();
			Column = new TableColumn<>(column); 
			//sous colonne(type de la colonne)
		//	type_Column = new TableColumn<>(type); 
		//	Column.getColumns().set(size,type_Column);
			
			//rajouterles colonnes à la table view
			Column.setCellValueFactory(new MapValueFactory(column));
	        tableView_opt1.getColumns().add(Column);
		}
	
}

			//*******************************************************************//

private ObservableList<Map> load_datasetToMap(TableView<Map> table){
	//appel pour avoir les informations et le contenu du dataset
	
	String value;
	ObservableList<Map> allinstances= FXCollections.observableArrayList();
	for(int i=1;i<nb_ligne;i++){
		Map<String,String> oneline= new HashMap<>();
		Instance instance= dataset.instance(i);
		for(int j=0;j<instance.numAttributes();j++)
		{
			 if(instance.attribute(j).isNumeric())
			  value= ""+instance.value(j);
			 else 
				value= instance.stringValue(j);
			 oneline.put(instance.attribute(j).name(),value);
		}
		allinstances.add(oneline);
	}
	table.setItems(allinstances);
	return allinstances;
}

               //*******************************************************************//
public void construct_tableViewOpt2() {

	String cl_name,type;
	TableColumn<Map, String> Column;
	tableView_opt2.getColumns().clear();
	tableView_opt2.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	
	//construction des colonnes du  table view
	tableView_opt2.setItems(load_datasetToMap(tableView_opt2));
    String listcol=null;
		for(int size=0;size<nb_att;size++)
		{
			type= type_colonne[size];	
			String column = dataset.attribute(size).name();
			textdetails.appendText("\n Attribut : "+column+"\t Type : "+type);
			Column = new TableColumn<>(column); 
			//sous colonne(type de la colonne)		
			//rajouterles colonnes à la table view
			Column.setCellValueFactory(new MapValueFactory(column));
	        tableView_opt2.getColumns().add(Column);
		}
	
}




			  //*******************************************************************//

public void Choose_Dataset(ActionEvent event) throws IOException, InterruptedException{
	
	FileChooser fc= new FileChooser();
	fc.setInitialDirectory(new File("C:\\Program Files\\Weka-3-8\\data"));
	selectedDataset= fc.showOpenDialog(null);
	Opt2_On=1;
	
	if(selectedDataset != null){
		textdetails.clear();
		textdetails.appendText("Data Set : "+selectedDataset.getName());
		DATASETPATH= "./data/"+selectedDataset.getName();
		this.datasetname=selectedDataset.getName();
		Load_from_Dataset(DATASETPATH);
		textdetails.appendText("\nNombre d'attributs : "+nb_att+"\nNombre d'instance : "+nb_ligne);
		construct_tableViewOpt2();
		listAttribute = FXCollections.observableArrayList();
		String[] col_att = null; // tableau contenant les attributs qu'on mettra dans la comboBox
		col_att = new String[nb_att+1];
		col_att[0] = "---";
		listAttribute.add(col_att[0]);
		
		for(int j=0; j < nb_att; j++)
		{
			String att_name= dataset.attribute(j).name();
			col_att[j+1] = att_name;
			listAttribute.add(col_att[j+1]);
			//System.out.println(col_att[j+1]);
		}
		comboAttributes.setItems(listAttribute);
		
	}
	else{ textdetails.appendText("Data Set Choisi non valide !");
		System.out.println("Data Set choisi non valide");}
}


/************************ OPT3 ***********************************/
@SuppressWarnings("null")
public Instances datasetopt2 = null;
@FXML
public TableView<Map> tableView_opt3;
@FXML
public TextArea textdsinfo;

String[] NomInstMissVal = {"breast-cancer.arff", "hypothyroid.arff", "labor.arff", "soybean.arff", "supermarket.arff", "vote.arff"};
String [] col_names = {"Attributs", "Min", "Max", "Mediane", "Q1", "Q3", "Mode", "Moyenne", "Moy-Mediane", "Skew"};
String [] att_numerique = null;
public int nb_att_numérique = 0;

String mes_att_numérique [][] = new String [40][10];
String mes_att_numériqueF[][] = null;

public void Show_Stat_Values(String nomFichier) {
	
    nb_ligne=this.dataset.numInstances();
    nb_att=this.dataset.numAttributes();
    colonnes = new String[nb_att]; 
	data = new String[nb_ligne][nb_att];
    String att_name=null;
	
	boolean fichier_avec_val_manq = false;
	
	for (int i = 0; i<NomInstMissVal.length; i++)
		if(nomFichier.equals(NomInstMissVal[i]))
			{
				fichier_avec_val_manq = true;
				break;
			}
	
	if(fichier_avec_val_manq)	
	{
		// Remplacement des valeurs manquante
		JOptionPane job;
		job = new JOptionPane();
		textdetails.appendText(" \nMESSAGE AVERTISSEMENT \nAttention : L'affichage se fait après remplacement des valeurs manquantes ");
		
		for(int l=0; l < nb_ligne; l++)
			for(int j=0; j < nb_att; j++)
			{
				if(dataset.instance(l).isMissing(j)) {
					if(dataset.attribute(j).isNumeric()) // Remplacer les valeurs des attributs numériques
					{
						String mean = dataset.meanOrMode(dataset.attribute(j)) + "";
						dataset.instance(l).setValue(dataset.attribute(j), Double.parseDouble(mean));
					}
				}
			}
		
		//Après remplacement on appelle la fonction pour les valeurs statistiques
		for(int j=0; j < nb_att; j++)
		{
			att_name= dataset.attribute(j).name();
			if(dataset.attribute(j).isNumeric())
			{
				
				mes_att_numérique[nb_att_numérique][0] = att_name; // prendre le nom de l'attribut en question
				//System.out.println("att numerique: "+mes_att_numérique[nb_att_numérique][0]);
				nb_att_numérique++;
			}
		}
		
		mes_att_numériqueF = new String[nb_att_numérique][10];
		
		colonnes = GetTabColonne(dataset);
		data = new String[nb_ligne][nb_att];
		data = GetMatriceData(dataset);
		
		for(int i = 0; i < nb_att_numérique; i++)
		{
			//System.out.println("att numerique: "+mes_att_numérique[i][0]);
			//calculer la moyennes des att_numérique
			String att = mes_att_numérique[i][0]; 
			//trouver l'index de att dans colonnes
			int index = TrouverIndex(att, (String[]) colonnes);
			String tabValeur[] = new String[nb_ligne];
			
			//prendre les valeurs de la colonne index de la matrices data => data[1-fin][index]
			for (int k = 0; k < nb_ligne; k++) //on prend pas la ligne contenant le type
			{
				tabValeur[k] = data[k][index];
				//System.out.println(Float.parseFloat(tabValeur[k]) + " -----> " + k);
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
			
			float diff = moyenne - mediane;
			mes_att_numérique[i][8] = diff+"";
			//System.out.println("here : moyenne= "+moyenne+" mediane= "+mediane+" mode = "+mode);
			if((moyenne-mediane)>=0 && (moyenne-mediane)< 0.5 && (mediane-mode)<0.5 && (mediane-mode)>=0 )
				{mes_att_numérique[i][9] = "Symetrical";
			  // System.out.println(mes_att_numérique[i][9]);
			   }
			else 
			{
				if(moyenne<=mediane && mediane<=mode)
					mes_att_numérique[i][9] = "Asymetrie Gauche";
				else
					if(moyenne>=mediane && mediane>=mode)
						mes_att_numérique[i][9] = "Asymetrie Droite";
					else mes_att_numérique[i][9] = "Asymetrie";
			}
		}
		
		for(int i = 0; i < mes_att_numériqueF.length; i++)
			for (int j = 0; j < mes_att_numériqueF[i].length; j++)
				mes_att_numériqueF[i][j] = mes_att_numérique[i][j];
	}

	// C.à.d que ce n'est pas un dateset avec des valeurs manquantes
	else
	{	
		for(int j=0; j < nb_att; j++)
		{
			att_name= dataset.attribute(j).name();
			if(dataset.attribute(j).isNumeric() && !dataset.attribute(j).isDate())
			{
				mes_att_numérique[nb_att_numérique][0] = att_name; // prendre le nom de l'attribut en question
				nb_att_numérique++;
			}	
		}
		
		mes_att_numériqueF = new String[nb_att_numérique][10];
		data = new String[nb_ligne][nb_att];
		data = GetMatriceData(dataset);
		colonnes = GetTabColonne(dataset);
		
		System.out.println("data[0][0] = "+data[0][0]);
		for(int i = 0; i < nb_att_numérique; i++)
		{	//System.out.println("attribut[" + i + "] = " + mes_att_numérique[i][0]);
			//calculer la moyennes des att_numérique
			String att = mes_att_numérique[i][0]; 
			//trouver l'index de att dans colonnes
			int index = TrouverIndex(att, (String[]) colonnes);
			nb_ligne=dataset.numInstances();
			String tabValeur[] = new String[nb_ligne];
			
			//prendre les valeurs de la colonne index de la matrices data => data[1-fin][index]
			for (int k = 0; k < nb_ligne; k++) //on prend pas la ligne contenant le type
			{
				System.out.println("nb ligne = "+nb_ligne+" k= "+k);
				System.out.println("data[k][ind] ="+data[k][index]);
				tabValeur[k] = data[k][index];
			//	System.out.println(Float.parseFloat(tabValeur[k]) + " -----> " + k);
			}
		//	System.out.println("out boucle");
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
		
			float diff = moyenne - mediane;
			mes_att_numérique[i][8] = diff+"";
			
			if(diff == 3)
				mes_att_numérique[i][9] = "Symetrical";
			else 
			{
				
					mes_att_numérique[i][9] = "Asymetrical";
			}
			//System.out.println(" diff "+diff);
		}
		
		for(int i = 0; i < mes_att_numériqueF.length; i++)
			for (int j = 0; j < mes_att_numériqueF[i].length; j++)
				mes_att_numériqueF[i][j] = mes_att_numérique[i][j];
	}
	
	
	if(nb_att_numérique == 0)					
	{
		JOptionPane job2;
		job2 = new JOptionPane();
		textdetails.appendText("\nMESSAGE AVERTISSEMENT \nAttention : Il n'y a pas d'attributs numériques!");
	}
	else construct_tableViewOpt3();
} 


// Fonctions
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
public float getquartile(String tab[],float percent) {
	float q;
	float [] tabf = new float[tab.length];
	for (int i = 0; i < tab.length; ++i) {
        tabf[i]=Float.parseFloat(tab[i]) ;
            	
        }
	q=quartile(tabf, percent);
	return q;		
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


// fonction pour récupérer la matrice data[nb_ligne][nb_att] à partir du dataset
public String[][] GetMatriceData(Instances dataset){
	int nb_att=this.dataset.numAttributes();
	int nb_ligne=this.dataset.numInstances();
	String att_name = null;
	String [][] data =null; // matrice de retour
	
	nb_att= dataset.numAttributes();
	nb_ligne= dataset.numInstances();
	data = new String[nb_ligne][nb_att];
	//System.out.println("GetMatriceData= "+nb_ligne+" att="+nb_att);
	for(int i=0; i < nb_ligne; i++)
	{
		String s = dataset.get(i).toString(); // recupere tt l'instance c.à.d. une ligne du benchmark
		String[] tab = s.split(","); //récuperer chaque attribut alone
		
		for(int k=0; k < nb_att; k++) //tab.length = nb_attributs
			{data[i][k]= tab[k]; //remplir notre matrice des données
			//System.out.println(data[i][k]);
			}
	}	
	return data;	
}


// fonction pour récupérer la tableau colonnes[nb_att] à partir du dataset
public String[] GetTabColonne(Instances dataset){
	int nb_att, nb_ligne;
	String att_name = null;
	String [] colonnes = null;
	
	nb_att= dataset.numAttributes();
	nb_ligne= dataset.numInstances();
    colonnes = new String[nb_att]; 
	
	for(int j=0; j < nb_att; j++)
	{
		att_name= dataset.attribute(j).name();
		colonnes[j]= att_name;
	}
	return colonnes;
}
//*******************************************************************//

private ObservableList<Map> load_datasetToMapOpt3(TableView<Map> table){
//appel pour avoir les informations et le contenu du dataset

String value;
ObservableList<Map> allinstances= FXCollections.observableArrayList();
for(int i=0;i<nb_att_numérique;i++){
	Map<String,String> oneline= new HashMap<>();
	
	for(int j=0;j<col_names.length;j++)
	{
		 
		 oneline.put(col_names[j],mes_att_numériqueF[i][j]);
	}
	allinstances.add(oneline);
}
//table.setItems(allinstances);
return allinstances;
}

          //*******************************************************************//
public void construct_tableViewOpt3() {

	String cl_name,type;
	TableColumn<Map, String> Column = null;
	tableView_opt2.getColumns().clear();
	tableView_opt2.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	
	//construction des colonnes du  table view
	tableView_opt2.setItems(load_datasetToMapOpt3(tableView_opt2));
   String listcol=null;
		for(int size=0;size<col_names.length;size++)
		{
			String column= col_names[size];	
			Column = new TableColumn<>(column); 
			Column.setCellValueFactory(new MapValueFactory(column));
	        tableView_opt2.getColumns().add(Column);
		}
	
}


public void Calcul_Stats(String datasetname,Instances dataset){
	this.dataset=MissValues_replace(this.dataset);
	 Show_Stat_Values( datasetname);
	
	}



public void Change_Scene(ActionEvent event) throws IOException{
	labeldescription.setText("Affichage des 5 chiffres pour chaque attribut : Min, Max, Median, \nQ1,Q3, ainsi que leurs Modes");
	Calcul_Stats(this.datasetname,this.dataset);
}

public void ReLoad_data(ActionEvent event){
	labeldescription.setText("Affichage de quelques informations telles que le nombre d’instances, \nnombre d’attributs et leurs types");
	construct_tableViewOpt2();
}

@Override
public void initialize(URL location, ResourceBundle resources) {
	// TODO Auto-generated method stub
	comboAttributes.setItems(listAttribute);
}
/*************************Missing Values************************/
//Fonction pour remplacer les valeurs manquantes d'une instance
	public Instances MissValues_replace(Instances datasett) {
		int nb_ligne, nb_att;
		
		nb_att= datasett.numAttributes();
		nb_ligne= datasett.numInstances();
		
		// Remplacement des valeurs manquante
		for(int l=0; l < nb_ligne; l++)
			for(int j=0; j < nb_att; j++)
			{
				if(datasett.instance(l).isMissing(j)) 
				{
					//Verifier type de l'attribut
					if(datasett.attribute(j).isNumeric())
					{
						double mean = datasett.meanOrMode(datasett.attribute(j));
						datasett.instance(l).setValue(datasett.attribute(j), mean);
					}
					
					if(datasett.attribute(j).isNominal())
					{
						double val = datasett.meanOrMode(datasett.attribute(j));
						String s = datasett.attribute(j).value((int) val);
						datasett.instance(l).setValue(datasett.attribute(j), s);
					}
				}	
			}
		return datasett;
	}
	
/********************* BOX¨PLOTS **************/
public void GetBoxPlots(ActionEvent event){
	BoxAndWhiskerDemo demo = null;
	try {
		demo = new BoxAndWhiskerDemo("Box-and-Whisker Chart",datasetname);
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	demo.pack();
	RefineryUtilities.centerFrameOnScreen(demo);
	demo.setVisible(true);
	demo.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
}

public void GetBoxPlotsNomarlized(ActionEvent event){
	BoxAndWhiskerDemo2 demo = null;
	try {
		demo = new BoxAndWhiskerDemo2("Box-and-Whisker Chart",datasetname);
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	demo.pack();
	RefineryUtilities.centerFrameOnScreen(demo);
	demo.setVisible(true);
	demo.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	this.dataset=MissValues_replace(this.dataset);
	
}

/*********************NORMALISATION ************************/
public void NormaliserDataset(ActionEvent event) {
	String[] tabValeur;
	String[][] data;
	this.dataset=MissValues_replace(this.dataset);
	data = new String[nb_ligne][nb_att];
	data = GetMatriceData(dataset);
	
	// Normalisation
	for(int j=0; j < nb_att; j++)
	{	
		//Verifier type de l'attribut
		if(dataset.attribute(j).isNumeric() && !dataset.attribute(j).isDate())
		{
			tabValeur = new String[nb_ligne];
			for(int i=0; i<nb_ligne; i++)
				tabValeur[i] = data[i][j]; 
			
			float min = getMinValue(tabValeur);
			float max = getMaxValue(tabValeur);
			
			for(int l=0; l < nb_ligne; l++)
			{
			//	float val = Float.parseFloat(datasett.attribute(j));
				float val = Float.parseFloat(tabValeur[l]);
				if(max-min != 0)
				{
					float v = (val - min)/(max-min);
					dataset.instance(l).setValue(dataset.attribute(j), v);
				}
			}
		}
	}

//System.out.println("NORMALISATION Effectuée sur le dataset ");
textdetails.appendText("\nNormalisation Effectuée sur le dataset \n"+datasetname);

}
/************************** Histogramme *****************************/

public float getMoyenne(String tab[]) {
	float sum = 0;
	for(int i=0;i<tab.length;i++)
		sum += Float.parseFloat(tab[i]);
	
	return sum/tab.length;
}

public int nb_occ_numerique_histo(float borneInf, float borneSup, String[] tab) {
	int occ =0;
	for(int i=0; i<tab.length; i++) {
		//System.out.println("valeur string "+tab[i]+" float= "+Float.parseFloat(tab[i]));
		if((Float.parseFloat(tab[i])>=borneInf && Float.parseFloat(tab[i])<borneSup) )
			occ++;
	}
	return occ;
}
public void Construct_Histogramme(ActionEvent event){

	 XYChart.Series attribute_data= new XYChart.Series<>();
	 attribute_data.setName("Distribution des valeurs de l'attribut");
		
		
	String Attribut_Histo= comboAttributes.getValue();
	
	

	String[] tabValeur;
	String[] colonnes = new String[nb_att];
	colonnes = GetTabColonne(dataset);
	String[][] data, dataR = new String[nb_ligne][nb_att];
	data = GetMatriceData(dataset);
	if(Attribut_Histo.equals("---"))
	{
		JOptionPane job;
		//Boite de message d'erreur
		job = new JOptionPane();
		job.showMessageDialog(null, "Attention : Aucun attribut n'est selctionné",
				"Message d'avertissement", JOptionPane.WARNING_MESSAGE);
	}
	else
	{
		labelHistog.setText("Distribution de l'attribut\n "+Attribut_Histo);
		/* 
		 * Nom de fichiers contanant des valeurs manquantes
		 {"breast-cancer.arff", "hypothyroid.arff", "labor.arff", "soybean.arff", "supermarket.arff", "vote.arff"};
		 */
		boolean Instval_manq = false;
		for(int i=0; i<NomInstMissVal.length; i++)
			if(datasetname.equals(NomInstMissVal[i]))
			{
				Instval_manq = true;
				break;
			}
			if(Instval_manq) // l'instance contient des valeurs manquantes
			{
				textdetails.appendText("Fichier " + datasetname + " has MissValues call fonction");
				this.dataset = MissValues_replace(dataset); //replace missing values
			}
			
			textdetails.appendText("Attribut selectionné : " + Attribut_Histo);
			
			int index = TrouverIndex(Attribut_Histo, colonnes);
			dataR = GetMatriceData(this.dataset);
			tabValeur = new String[nb_ligne];
			for(int i=0; i<nb_ligne; i++)
				tabValeur[i] = dataR[i][index];//récuperer la colonne de l'attribut sélectionné
			
			if(dataset.attribute(index).isNominal())
			{
				histogramme.getData().clear();
				textdetails.appendText("Attirbut NOMINAL");
				
				// calculer occurence pour chaque valeur dans le tableau tabValeur
				LinkedHashMap<String,Integer> h = new LinkedHashMap<String,Integer>(); // key=>Valeur attribut, value=>frequence
			    String exp = null;
			    
		       for(int i=0; i<tabValeur.length; i++){
		            if(h.containsKey(tabValeur[i])){
		                h.put(tabValeur[i], h.get(tabValeur[i]) + 1);
		            } else {
		                h.put(tabValeur[i], 1);
		            }
		        }
			    
			
		        for (String key:h.keySet()){
	             	
	 				attribute_data.getData().add(new XYChart.Data(key, h.get(key)));
	 				//System.out.println("key= "+key+"value ="+h.get(key));
				}
		        histogramme.getData().add(attribute_data);
			}//fin nominal
			
			if(dataset.attribute(index).isNumeric()  && !dataset.attribute(index).isDate() ) 
			{
				
				// trouver min, max et moy
				float min = getMinValue(tabValeur);
				float max = getMaxValue(tabValeur);
				float maxdernier=(float) (max+0.1);
				float moy = getMoyenne(tabValeur);
				float att_q1= getquartile(tabValeur, 25);
				float att_q3=getquartile(tabValeur,75);
				int min_q1 = nb_occ_numerique_histo(min, att_q1, tabValeur);
				int q1_moy = nb_occ_numerique_histo(att_q1, moy, tabValeur);
				int moy_q3 = nb_occ_numerique_histo(moy, att_q3, tabValeur);
				int q3_max = nb_occ_numerique_histo(att_q3, maxdernier, tabValeur);
				//System.out.println("Min= "+min+" Q1= "+att_q1+" moy= "+moy+" Q3= "+att_q3+" Max= "+max);
				LinkedHashMap<String, Integer> hm = new LinkedHashMap<String, Integer>(); //calculer occurence de valeurs
			    hm.put("(1 Min-Q1)", min_q1);
			    hm.put("(2 Q1-Moy)", q1_moy);
			    hm.put("(3 Moy-Q3)", moy_q3);
			    hm.put("(4 Q3-Max)", q3_max);
			    
				histogramme.getData().clear();
				textdetails.appendText("Attirbut NUMERIQUE");
			
		        
		        /*for (Map.Entry<String, Integer> hm1 : hm.entrySet())
		        {
		        	attribute_data.getData().add(new XYChart.Data(hm1.getKey(), hm1.getValue()));
		        	
		        }*/
				for (String key:hm.keySet()){
	             	
	 				attribute_data.getData().add(new XYChart.Data(key, hm.get(key)));
	 				//System.out.println("key= "+key+"value ="+hm.get(key));
				}
		        histogramme.getData().add(attribute_data);
				
			} // fin numerique
			
			if(dataset.attribute(index).isDate() || dataset.attribute(index).isString() || dataset.attribute(index).isRelationValued())
			{
				histogramme.getData().clear();
				attribute_data.getData().add(new XYChart.Data("0",0));
				histogramme.getData().add(attribute_data);
			}
			
	
}
} //fin construct histogramme	


// a priori 
//fonction qui génère les items set fréquent et calcule les règles d'associations 
void apriori_frequentitemset( String path,float min_support ) throws IOException {
      AprioriFrequentItemsetGenerator<String> generator =
              new AprioriFrequentItemsetGenerator<>();

      List<Set<String>> itemsetList = new ArrayList<>();
      String[] l = null;
     // String path = "./data/car.csv";
      int nb_ligne  = 0;
     /* Scanner scanner = new Scanner(new File(path));
      while(scanner.hasNextLine()){
      	l = scanner.nextLine().split(",");
      	itemsetList.add(new HashSet<>(Arrays.asList(l)));
      	nb_ligne++;
      }
      scanner.close();
      
		*/
      FileInputStream fstream = new FileInputStream(path);
      BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

      String strLine;

      //Read File Line By Line
      while ((strLine = br.readLine()) != null)   {
      	// Print the content on the console
      	//System.out.println (strLine);
      	if(!strLine.startsWith("%") && !strLine.startsWith("@"))
        	{
      		l = strLine.split(",");
      		itemsetList.add(new HashSet<>(Arrays.asList(l)));
        	}	
      }
      
    /*  long startTime = System.nanoTime();
      FrequentItemsetData<String> data = generator.generate(itemsetList, 0.2); // min_support
      //contient freqItemList // nb trans // support min // supportCountMat // 
      long endTime = System.nanoTime();

      int i = 1;*/
      br.close();
      
      long startTime = System.nanoTime();
      FrequentItemsetData<String> data = generator.generate(itemsetList, min_support); // minimum support of 0.2 = 20% => 0.2 x itemsetList.size() = 2
      // min_support//contient freqItemList // nb trans // support min // supportCountMat // 
      long endTime = System.nanoTime();
      int i = 1;

  //    System.out.println("--- Frequent itemsets ---");

     
      Set<String> itemsetlast = null;
      for (Set<String> itemset : data.getFrequentItemsetList()) {
         /* System.out.printf("itemset %2d: %9s, support: %1.1f\n",
                            i++, 
                            itemset,
                            data.getSupport(itemset)*itemsetList.size()); // *itemsetList.size() pour avoir le nombre d'occurence
          */
          double number=  data.getSupport(itemset)*itemsetList.size();//data.getSupport(itemset);
      	String res=(i++)+"	"+itemset+"	"+number+"\n"; // itemset= [small] , number=0.33 .... / itemseet[1,2,3] number=support
          textareafreqitem_all.appendText(res);
	        
      }
      
      
      List<Set<String>> liste;
      liste= generator.getListR();
      for(int indice =0;indice <liste.size();indice++)
      {
      	Set<String> element=liste.get(indice);
      	String affiche= element+"\n";
      	textareafreqitem_support.appendText(affiche);
      	
      }
     /* System.out.printf("Mined frequent itemset in %d milliseconds.\n", 
                        (endTime - startTime) / 1_000_000);*/
      float time= (endTime - startTime) / 1_000_000;
      timefrequentitem.appendText(time+"ms\n");
      
  }

void apriori_reglesassociations( String path,float min_confiance ) throws IOException {
    AprioriFrequentItemsetGenerator<String> generator =
            new AprioriFrequentItemsetGenerator<>();

    List<Set<String>> itemsetList = new ArrayList<>();
    String[] l = null;
   // String path = "./data/car.csv";
    int nb_ligne  = 0;
   
    FileInputStream fstream = new FileInputStream(path);
    BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

    String strLine;

    //Read File Line By Line
    while ((strLine = br.readLine()) != null)   {
    	
    	if(!strLine.startsWith("%") && !strLine.startsWith("@"))
      	{
    		l = strLine.split(",");
    		for (int i=0;i<l.length;i++)
    			l[i]= "Att"+ (i+1) +" | "+ l[i];
    		itemsetList.add(new HashSet<>(Arrays.asList(l)));
      	}	
    }
    
  
    br.close();
    
   
    FrequentItemsetData<String> data = generator.generate(itemsetList, 0.2); // minimum support of 0.2 = 20% => 0.2 x itemsetList.size() = 2
   
    List<Set<String>> liste1;
    liste1 = generator.getListR(); // récupérer la liste des itemsets les plus fréquent
    
    RuleGenerator ruleGenerator = new RuleGenerator();
    long startTime1 = System.nanoTime();
    ruleGenerator.PrintSubSets(itemsetList, liste1, min_confiance);
    long endTime1 = System.nanoTime();
    int i = 1;

   
    
    
    String res;
    HashMap<String,Double> associationRuleList_all=null;
    associationRuleList_all= ruleGenerator.get_all_rules();
    for (Map.Entry<String, Double> entry : associationRuleList_all.entrySet()) {
		    String key = entry.getKey();
		    Object value = entry.getValue();
		    res="Resulted/rule " + ", " + key+ ", confidence: " + value+"\n\n";
		    textarearegleassoc_all.appendText(res);
    }
    
    String res1;
    HashMap<String,Double> associationRuleList_all1=null;
    
    associationRuleList_all1=ruleGenerator.get_confidence_rules();
    for (Map.Entry<String, Double> entry : associationRuleList_all1.entrySet()) {
		    String key = entry.getKey();
		    Object value = entry.getValue();
		    res1="Rule " + ":  " + key+ ", confidence:	" + value+"\n\n";
        //rajouter zone texte
		    textarearegleassoc_confiance.appendText(res1);
    }

 //   System.out.printf("Mined association rules in %d milliseconds.\n",
       //               (endTime - startTime) / 1_000_000);
    
    
	    float time2= (endTime1 - startTime1) / 1_000_000;
    timeregleassoc.appendText(time2+"ms\n");
    
}

public void look_freqitemset(ActionEvent event) throws IOException, InterruptedException{
	
	
	if(datasetname != null){
		textareafreqitem_all.clear();
		textareafreqitem_support.clear();
		
		timefrequentitem.clear();
		
		textareafreqitem_all.appendText("Data Set : "+selectedDataset.getName()+"\n\n");
		textareafreqitem_all.appendText("-------------------------------\n\n");
		
	
		String DATASETPATH= "./data/"+datasetname;
		apriori_frequentitemset(DATASETPATH,Float.parseFloat(support_min.getText()));
	}
}


public void look_reglesassoc(ActionEvent event) throws IOException, InterruptedException{
	
	if(datasetname != null){
		
		textarearegleassoc_confiance.clear();
		textarearegleassoc_all.clear();
		timeregleassoc.clear();
		textarearegleassoc_all.appendText("Data Set : "+selectedDataset.getName()+"\n\n");
		textarearegleassoc_all.appendText("-------------------------------\n\n");
		
		String DATASETPATH= "./data/"+datasetname;
		apriori_reglesassociations(DATASETPATH,Float.parseFloat(confidence_min.getText()));
	}
}

public void clustering_knn(){

	Try_KNN knn = new Try_KNN();
	textarea_print1.clear();
	//textarea_print2.clear();
	textarea_print3.clear();
	textarea_print22.clear();
	try {
		knn.knn_algorithme(datasetname,Integer.parseInt(k_value.getText()));
		List<String> list1= knn.get_toPrint1();
		List<String> list2= knn.get_toPrint2();
		List<String> list3= knn.get_toPrint3();
		int i=0;
		while (i < list1.size()){
			
			textarea_print1.appendText(list1.get(i)+"\n");
			i++;
		}
		/*
		i=0;
		while (i < list2.size()){
			String lineBreak = System.getProperty("line.separator");
			textarea_print2.appendText(list2.get(i) + "         " + lineBreak);
			i++;
		}
		*/
		i=0;
		while( i < list3.size()){
			textarea_print3.appendText(list3.get(i)+"\n");
			i++;
		}
		
		
		
		i=0;
		while (i < list2.size()){
			textarea_print22.appendText(list2.get(i));
			i++;
		}
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

public void dbscan_algorithm(ActionEvent event) throws IOException
{
	affich_cluster.clear();
	affich_noise.clear();
	param_intrac.clear();
	param_interc.clear();
	
	minPts=Float.parseFloat(param_minpts.getText());
	epsilon=Float.parseFloat(param_eps.getText());
	DB_Scan dbScan = new DB_Scan();
	dbScan.appliquer_DBScan(datasetname, minPts, epsilon); 
	double  intrac=dbScan.get_intrac();
	double interc=dbScan.get_interc();
	param_intrac.setText(""+interc);//setText(""+intrac);
	param_interc.setText(""+intrac);
	
	 HashMap<Integer, ArrayList<Integer>>all_clusters = new HashMap <Integer, ArrayList<Integer>> ();
	 ArrayList<Integer> noise = new ArrayList<Integer>();
	 all_clusters= dbScan.get_clusters();
	 noise= dbScan.get_noise();
	 
	  for (Map.Entry<Integer, ArrayList<Integer>> entry : all_clusters.entrySet()) {
		    Integer key = entry.getKey();
		    ArrayList<Integer> value_cluster = entry.getValue();
		    
		    affich_cluster.appendText("\n\n********** cluster" + (key+1) + " **********\n");
		    
		    for(int r = 0; r<value_cluster.size(); r++)
		    {
		    	int index_val = value_cluster.get(r);
		    	affich_cluster.appendText("instance " + (index_val+1) + " : ");
		    	for(int j=0; j<data[index_val].length; j++)
		    		affich_cluster.appendText(data[index_val][j] + ", ");
		    	affich_cluster.appendText("\n");
		    }    
		}
		
		
	  affich_noise.appendText("\n\n********** Noisey instances" + " **********\n");
	    
	    for(int r = 0; r<noise.size(); r++)
	    {
	    	int index_val = noise.get(r);
	    	affich_noise.appendText("instance " + (index_val+1) + " : ");
	    	for(int j=0; j<data[index_val].length; j++)
	    		affich_noise.appendText(data[index_val][j] + ", ");
	    	affich_noise.appendText("\n");
	    } 
	    
}
}
