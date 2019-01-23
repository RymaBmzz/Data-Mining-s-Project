package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MenuController {

	@FXML
	private void load_scene(ActionEvent event) throws IOException {
	    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MenuFX.fxml"));
	    Parent root1 = (Parent) fxmlLoader.load();
	    Stage stage = new Stage();
	    //set what you want on your stage
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.setTitle("Page du Menu");
	    stage.setScene(new Scene(root1));
	    stage.setResizable(false);
	    stage.show();
	}
	
	@FXML
	private void load_opt1(ActionEvent event) throws IOException {
	    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Opt1_DataSet.fxml"));
	    Parent root1 = (Parent) fxmlLoader.load();
	    Stage stage = new Stage();
	    //set what you want on your stage
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.setTitle("Affichage d'un Data Set");
	    stage.setScene(new Scene(root1));
	    stage.setResizable(false);
	    stage.show();
	}
	
	@FXML
	private void load_opt2(ActionEvent event) throws IOException {
	    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Opt2.fxml"));
	    Parent root2 = (Parent) fxmlLoader.load();
	    Stage stage = new Stage();
	    //set what you want on your stage
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.setTitle("Choisir et Afficher un Data Set");
	    stage.setScene(new Scene(root2));
	    stage.setResizable(false);
	    stage.show();
	}
	
	@FXML
	private void load_opt3(ActionEvent event) throws IOException {
	    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Opt3.fxml"));
	    Parent root2 = (Parent) fxmlLoader.load();
	    Stage stage = new Stage();
	    //set what you want on your stage
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.setTitle("Statistiques");
	    stage.setScene(new Scene(root2));
	    stage.setResizable(false);
	    stage.show();
	}
}
