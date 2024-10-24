package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class ClinicManagerController {

    @FXML
    private ToggleGroup Appointment;

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
    private TableColumn<?, ?> tc_city;

    @FXML
    private TableColumn<?, ?> tc_county;

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
    public void clearSelection(ActionEvent event) {
        tf_fnameSC.clear();
        tf_lnameSC.clear();
        dp_apptDateSC.getEditor().clear();
    }

}
