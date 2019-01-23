package application;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

public class Apriori_controller {
	File selectedDataset;
	@FXML
    private Separator textareafreqitems;

	  @FXML
	    private TextArea textareafreqitem_all;

	    @FXML
	    private TextArea textarearegleassoc_all;


	    @FXML
	    private TextArea textareafreqitem_support;

	    @FXML
	    private TextArea textarearegleassoc_confiance;
    
    @FXML
     TextArea timefrequentitem;

    @FXML
     TextArea timeregleassoc;
    
    @FXML
    private Button dataset;
    
	//fonction qui génère les items set fréquent et calcule les règles d'associations 
	  void apriori_frequentitemset( String path ) throws IOException {
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
	        FrequentItemsetData<String> data = generator.generate(itemsetList, 0.3); // minimum support of 0.2 = 20% => 0.2 x itemsetList.size() = 2
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
	        

	        List<Set<String>> liste1;
	        liste1 = generator.getListR(); // récupérer la liste des itemsets les plus fréquent
	        
	        RuleGenerator ruleGenerator = new RuleGenerator();
	        long startTime1 = System.nanoTime();
	        ruleGenerator.PrintSubSets(itemsetList, liste1, 0.7);
	        long endTime1 = System.nanoTime();
	        i = 1;

	       
	        
	        
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
	  
	   
	   
public void Choose_DatasetAssociation(ActionEvent event) throws IOException, InterruptedException{
			
			FileChooser fc= new FileChooser();
			String directory="D:\\Etudes\\Master SII 2017_2019\\M2 SII\\M2 2018_2019\\Data Mining\\TP\\apriori_sans gui\\datasets";
			fc.setInitialDirectory(new File(directory)); //
			selectedDataset= fc.showOpenDialog(null);
			
			if(selectedDataset != null){
				textareafreqitem_all.clear();
				textareafreqitem_support.clear();
				textarearegleassoc_confiance.clear();
				textarearegleassoc_all.clear();
				timefrequentitem.clear();
				timeregleassoc.clear();
				textareafreqitem_all.appendText("Data Set : "+selectedDataset.getName()+"\n");
				textarearegleassoc_all.appendText("Data Set : "+selectedDataset.getName()+"\n");
				String DATASETPATH= "D:/Etudes/Master SII 2017_2019/M2 SII/M2 2018_2019/Data Mining/TP/apriori_sans gui/datasets/"+selectedDataset.getName();
				apriori_frequentitemset(DATASETPATH);
}
}

}//fin de classe