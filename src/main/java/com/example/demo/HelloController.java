package com.example.demo;

import classes.Provider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private Button bt_loadProviders;


    @FXML
    private ComboBox<Provider> cb_providerSC;

    @FXML
    private DatePicker db_apptDateSC;

    @FXML
    void appointmentDatePicker(ActionEvent event){
        DatePicker datePicker = new DatePicker();
        LocalDate today = LocalDate.now();
        LocalDate sixMonthsLater = today.plusMonths(6);

    }

    //@Override
    public void initialize(){
        //ObservableList<Provider> providerObservableList = FXCollections.observableArrayList(Provider.get);
       //cb_providerSC.setItems(providerObservableList);

    }

    /**will load up list of providers and print them in provider combo box*/
    //@FXML
    /*void addProvidersToComboBox(ActionEvent event){
        cb_providerSC.getItems();

    }*/




}