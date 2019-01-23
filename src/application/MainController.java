package application;


import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {

	@FXML
	private AnchorPane rootPane;
	
	@FXML
    private Button buttonMenu;

    @FXML
    private Button buttonpart2;
    
	public void display_menu(ActionEvent event) throws IOException{
		System.out.println("Menu !");
		AnchorPane pane= FXMLLoader.load(getClass().getResource("MenuFX.fxml"));
		rootPane.getChildren().setAll(pane);
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
	    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("frequentitemset_apriori.fxml"));
	    Parent root = (Parent) fxmlLoader.load();
	    Stage stage = new Stage();
	    //set what you want on your stage
	    stage.initModality(Modality.APPLICATION_MODAL);
	    stage.setTitle("Frequent Item sets");
	    stage.setScene(new Scene(root));
	    stage.setResizable(false);
	    stage.show();
	}
}


