package it.polito.tdp.gestionale;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.gestionale.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

//controller del turno A --> switchare al master_turnoB per turno B

public class FXMLController {
	
	private Model model;
	private boolean flag = false;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCorsiFrequentati(ActionEvent event) {
    	
    	this.model.creaGrafo();
    	
    	this.txtResult.setText("Grafo creato!\n#Vertici: "+this.model.getNumeroVertici()+"\n#Archi: "+this.model.getNumeroArchi()+"\n");
    	
    	this.model.calcolaFrequenza();
    	
    	for(Integer i: this.model.getFrequenza().keySet()) {
    		this.txtResult.appendText("Studenti iscritti a "+i+" corsi: "+this.model.getFrequenza().get(i)+"\n");
    	}
    	
    	this.flag = true;


    }

    @FXML
    void doVisualizzaCorsi(ActionEvent event) {
    	
    	
    	if(flag == false) {
    		this.txtResult.setText("Creare prima il grafo");
    		return;
    	}
    	
    	this.model.trovaInsieme();
    	
    	if(this.model.getBest()==null) {
    		this.txtResult.setText("Non esiste un insieme minimo");
    		return;
    	}
    	
    	this.txtResult.setText(this.model.getBest().toString());

    }

    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'DidatticaGestionale.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
	}
}
