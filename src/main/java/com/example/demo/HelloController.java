package com.example.demo;

import classes.Location;
import classes.Provider;
import classes.Timeslot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.w3c.dom.events.MouseEvent;

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
    private ToggleGroup Appointment; //links office and imaging radio buttons to each other

    @FXML
    private Button bt_cancelAppt;

    @FXML
    private Button bt_clearSelection;

    @FXML
    private Button bt_loadProviders;

    @FXML
    private Button bt_scheduleAppt;

    @FXML
    private ComboBox<?> cb_providerSC;

    @FXML
    private ComboBox<?> cb_timeslotR;

    @FXML
    private ComboBox<?> cb_timslotSC;

    @FXML
    private DatePicker dp_apptDateSC;

    @FXML
    private DatePicker dp_dobR;

    @FXML
    private DatePicker dp_dobSC;

    @FXML
    private Menu m_appt;

    @FXML
    private Menu m_listOfAppts;

    @FXML
    private Menu m_statement;

    @FXML
    private RadioButton rb_imaging;

    @FXML
    private RadioButton rb_officeVisit;

    @FXML
    private TextArea ta_outputDisplay;

    @FXML
    private TableColumn<?, ?> tc_county;

    @FXML
    private TableColumn<?, ?> tc_city;

    @FXML
    private TableColumn<?, ?> tc_zip;

    @FXML
    private TextField tf_fnameR;

    @FXML
    private TextField tf_fnameSC;

    @FXML
    private TextField tf_lnameR;

    @FXML
    private TextField tf_lnameSC;

    @FXML
    public void initialize(){
        ObservableList<Location> locations = FXCollections.observableArrayList(Location.values());

    }

    @FXML
    public void clearSelection(ActionEvent event){
        tf_fnameSC.setText(null);
        tf_lnameSC.setText(null);
        dp_dobSC.getEditor().setText(null);
    }

    @FXML
    public void scheduleAppointment(ActionEvent event){
        tf_fnameSC.getText();
        tf_lnameSC.getText();
        dp_dobSC.getEditor().getText();
    }

    @FXML
    public void cancelAppointment(ActionEvent event){
        tf_fnameSC.getText();
        tf_lnameSC.getText();
        dp_dobSC.getEditor().getText();
    }

}