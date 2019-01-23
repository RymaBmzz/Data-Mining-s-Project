package application;

import java.util.Arrays;
import IHM.LoadData;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import javafx.collections.transformation.SortedList;
import weka.core.Instances;

/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2004, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc. 
 * in the United States and other countries.]
 *
 * ----------------------
 * BoxAndWhiskerDemo.java
 * ----------------------
 * (C) Copyright 2003, 2004, by David Browning and Contributors.
 *
 * Original Author:  David Browning (for the Australian Institute of Marine Science);
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * $Id: BoxAndWhiskerDemo.java,v 1.12 2004/06/02 14:35:42 mungady Exp $
 *
 * Changes
 * -------
 * 21-Aug-2003 : Version 1, contributed by David Browning (for the Australian Institute of 
 *               Marine Science);
 * 27-Aug-2003 : Renamed BoxAndWhiskerCategoryDemo --> BoxAndWhiskerDemo, moved dataset creation
 *               into the demo (DG);
 *
 */


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
import org.omg.PortableServer.ServantRetentionPolicyValue;

import com.sun.glass.events.WindowEvent;

import weka.core.Instances;

/**
 * Demonstration of a box-and-whisker chart using a {@link CategoryPlot}.
 *
 * @author David Browning
 */
public class BoxAndWhiskerDemo extends ApplicationFrame {
	String[] NomInstMissVal = {"hypothyroid.arff", "labor.arff", "supermarket.arff"};
	String[] NomInstNominal = {"ReutersCorn-test.arff", "ReutersCorn-train.arff", "ReutersGrain-test.arff", "ReutersGrain-train.arff", 
			"breast-cancer.arff", "contact-lenses.arff", "soybean.arff", "vote.arff", "weather.nominal.arff"};
	public String [][] data =null;
	Object [] colonnes = null;
	String [] att_numerique = null;
	String mes_att_numérique [][] = new String [40][7];
	String mes_att_numériqueF[][] = null;
	public int nb_ligne, nb_att, nb_att_numérique = 0;	

    /** Access to logging facilities. */
    private static final LogContext LOGGER = Log.createContext(BoxAndWhiskerDemo.class);

    public BoxAndWhiskerDemo(final String title, String nomFichier) throws IOException {

        super(title);
        
	    colonnes = new String[nb_att]; 
		data = new String[nb_ligne][nb_att];
	    String att_name=null;
		String[] all_att=null;
		Instances datasett = null;
        
		datasett = LireDataset(nomFichier);
		nb_att= datasett.numAttributes();
		nb_ligne= datasett.numInstances();
		data = GetMatriceData(datasett);
		colonnes = GetTabColonne(datasett);
		
		boolean inst_nominal = false;
		for(int i = 0; i<NomInstNominal.length; i++)
			if(nomFichier.equals(NomInstNominal[i]))
			{
				inst_nominal = true;
				break;
			}
		
		boolean inst_valMaq = false;
		for(int i = 0; i<NomInstMissVal.length; i++)
			if(nomFichier.equals(NomInstMissVal[i]))
			{
				inst_valMaq = true;
				break;
			}
		
		if(inst_nominal)
		{
			JOptionPane job;
			job = new JOptionPane();
			job.showMessageDialog(null, "Attention :  Il n'y a pas d'attributs numériques",
					"Message d'avertissement", JOptionPane.WARNING_MESSAGE);
		}
		else
		{
			if(inst_valMaq)
			{				
				// Remplacement des valeurs manquante
				for(int l=0; l < nb_ligne; l++)
					for(int j=0; j < nb_att; j++)
					{
						if(datasett.instance(l).isMissing(j)) {
							if(datasett.attribute(j).isNumeric()) // Remplacer seulement les attributs numériques
							{
								String mean = datasett.meanOrMode(datasett.attribute(j)) + "";
								datasett.instance(l).setValue(datasett.attribute(j), Double.parseDouble(mean));
							}
						}
						
					}
				
				//Après remplacement on appelle le a fonction du BoxPlot
				JOptionPane job;
				job = new JOptionPane();
				job.showMessageDialog(null, "Attention : L'affichage se fait après remplacement des valeurs manquantes",
						"Message d'avertissement", JOptionPane.WARNING_MESSAGE);
				
		        final BoxAndWhiskerCategoryDataset ddataset = createSampleDataset(datasett);

		        final CategoryAxis xAxis = new CategoryAxis("Attribute");
		        final NumberAxis yAxis = new NumberAxis("Value");
		        yAxis.setAutoRangeIncludesZero(false);
		        final BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
		        renderer.setFillBox(false);
		        renderer.setToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
		        final CategoryPlot plot = new CategoryPlot(ddataset, xAxis, yAxis, renderer);

		        final JFreeChart chart = new JFreeChart(
		            "Box-and-Whisker Demo",
		            new Font("SansSerif", Font.BOLD, 14),
		            plot,
		            true
		        );
		        final ChartPanel chartPanel = new ChartPanel(chart);
		        chartPanel.setPreferredSize(new java.awt.Dimension(450, 270));
		        setContentPane(chartPanel);
		        
		        //addWindowListener(exitListener);
		        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}
			// C.à.d l'instance du fichier ne contient pas de valeurs manquantes
			else
			{		        
		        final BoxAndWhiskerCategoryDataset ddataset = createSampleDataset(datasett);

		        final CategoryAxis xAxis = new CategoryAxis("Attribute");
		        final NumberAxis yAxis = new NumberAxis("Value");
		        yAxis.setAutoRangeIncludesZero(false);
		        final BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
		        renderer.setFillBox(false);
		        renderer.setToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
		        final CategoryPlot plot = new CategoryPlot(ddataset, xAxis, yAxis, renderer);

		        final JFreeChart chart = new JFreeChart(
		            "Box-and-Whisker Demo",
		            new Font("SansSerif", Font.BOLD, 14),
		            plot,
		            true
		        );
		        final ChartPanel chartPanel = new ChartPanel(chart);
		        chartPanel.setPreferredSize(new java.awt.Dimension(450, 270));
		        setContentPane(chartPanel);
		        
		        //addWindowListener(exitListener);
		        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}
		}		        
    }


	/**
     * Creates a sample dataset.
     * 
     * @return A sample dataset.
     */
    private BoxAndWhiskerCategoryDataset createSampleDataset(Instances dataset) {

        final DefaultBoxAndWhiskerCategoryDataset ddataset = new DefaultBoxAndWhiskerCategoryDataset();
    	
		nb_att= dataset.numAttributes();
		nb_ligne= dataset.numInstances();
	    colonnes = GetTabColonne(dataset);
		data = GetMatriceData(dataset);
	    String att_name=null;
		String[] all_att=null;
		
		for(int j=0; j < nb_att; j++)
		{
			att_name= dataset.attribute(j).name();
			if(dataset.attribute(j).isNumeric() && !dataset.attribute(j).isDate())
			{
				mes_att_numérique[nb_att_numérique][0] = att_name; // prendre le nom de l'attribut en question
				nb_att_numérique++;
			}
		}
			
		mes_att_numériqueF = new String[nb_att_numérique][7];
		
		for(int i=0; i < nb_ligne; i++)
		{
			String s = dataset.get(i).toString(); // recupere tt l'instance c.à.d. une ligne du benchmark
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
			String tabValeur[] = new String[nb_ligne];
			List list1 = new ArrayList();
			
			//prendre les valeurs de la colonne index de la matrices data => data[1-fin][index]
			for (int k = 0; k < nb_ligne; k++) //on prend pas la ligne contenant le type
			{
				tabValeur[k] = data[k][index];
				list1.add(data[k][index]);
			}
			
			List list2 = new ArrayList();
			for(int l = 0; l < tabValeur.length; l++)
				list2.add(Float.parseFloat(tabValeur[l]));
			
			list2.sort(null);
			
	        LOGGER.debug("Adding attributes " + i);
	        LOGGER.debug(list2.toString());
	        ddataset.add(list2, "Attribute " + att, "" + (i+1));
		}
    			    	
    	/*
        List list1 = new ArrayList();
        
        for(int i=1; i<45;i++)
        	list1.add(i + 0.12);
        	
        LOGGER.debug("Adding series " + 1);
        LOGGER.debug(list1.toString());
        ddataset.add(list1, "Series " + 0, " Type " + 0);
        */
		return ddataset;
    }

    // ****************************************************************************
    // * JFREECHART DEVELOPER GUIDE                                               *
    // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
    // * to purchase from Object Refinery Limited:                                *
    // *                                                                          *
    // * http://www.object-refinery.com/jfreechart/guide.html                     *
    // *                                                                          *
    // * Sales are used to provide funding for the JFreeChart project - please    * 
    // * support us so that we can continue developing free software.             *
    // ****************************************************************************
    
	public int TrouverIndex(String valeur, String tab[]) {
		for (int i = 0; i < tab.length; i++)
			if(valeur.equals(tab[i]))
				return i;
	
		return -1;
	}   
	
	// fonction pour lire le fichier et récupérer le dataset
	public Instances LireDataset(String nomFichier) throws IOException {
		Instances dataset = null;
		String DATASETPATH = "./data/" + nomFichier;
		LoadData mg = new LoadData();
		dataset = mg.loadDataset(DATASETPATH);
		return dataset;	
	}
	
	// fonction pour récupérer la matrice data[nb_ligne][nb_att] à partir du dataset
	public String[][] GetMatriceData(Instances dataset){
		int nb_att, nb_ligne;
		String att_name = null;
		String [][] data =null; // matrice de retour
		
		nb_att= dataset.numAttributes();
		nb_ligne= dataset.numInstances();
		data = new String[nb_ligne][nb_att];
		
		for(int i=0; i < nb_ligne; i++)
		{
			String s = dataset.get(i).toString(); // recupere tt l'instance c.à.d. une ligne du benchmark
			String[] tab = s.split(","); //récuperer chaque attribut alone
			
			for(int k=0; k < nb_att; k++) //tab.length = nb_attributs
				data[i][k]= tab[k]; //remplir notre matrice des données
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

    
}

