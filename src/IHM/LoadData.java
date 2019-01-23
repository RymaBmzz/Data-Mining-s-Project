package IHM;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import weka.core.*;
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.converters.ConverterUtils.DataSource;

public class LoadData {

    public Instances loadDataset(String path) throws IOException {
        Instances dataset = null;
        Reader reader = null;
		try {
			/*************using Instances class **************************/
		//	reader = new FileReader(path);
		//	dataset = new Instances(reader);// ou bien  dataset = DataSource.read(path); ou path est un string
			
			/************** using converters.Datasource class *********************/
			DataSource obj= new DataSource(path);
		    dataset = obj.getDataSet();
	
		} catch (Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        return dataset;
    }
}
