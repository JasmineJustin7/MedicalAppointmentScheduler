package com.example.demo;

import classes.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import util.Date;
import util.List;
import util.Sort;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

public class ClinicManagerController {

    private final Scanner scanner;
    private final List<Appointment> appointmentList = new List();
    private final List<Imaging> imagingList = new List();
    private final List<Provider> providers = new List();
    private final List<Technician> technicians = new List();
    private final List<Doctor> doctors = new List();
    private int technicianIndex = 0;

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
    private ComboBox<String> cb_providerSC;
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


    /**When load providers button is clicked, display doctors in combo box
     * and display technicians in text area*/
    @FXML
    public void displayProvidersInGUI(ActionEvent event){
        ObservableList<String> listOfDoctors = FXCollections.observableArrayList();
        Iterator<Doctor> var1 = this.doctors.iterator();
        while(var1.hasNext()){ //add doctor to observable list
            Doctor doctor = var1.next();
            listOfDoctors.add(doctor.toString());
        }
        //Display doctors in combo box
        cb_providerSC.setItems(listOfDoctors);

        //Display technicians in text area
        technicianRotation();
        bt_loadProviders.setDisable(true);

    }



    private void loadProviders() {
        try {
            File file = new File("providers.txt");
            Scanner fileScanner = new Scanner(file);

            while(fileScanner.hasNextLine()) {
                String providerInfo = fileScanner.nextLine();
                this.parseLine(providerInfo);
            }

            Sort.provider(this.providers);
            this.displayProviders();
        } catch (FileNotFoundException var4) {
            System.out.println("Error: File not found!"); //replace this with printing to text area in GUI
        }

    }

    /**This is past displayProviders function from old clinic manager - need to delete after GUI is set up
     * */
    public void displayProviders() {
        System.out.println("Providers loaded to the list."); //Erase this or put it into TextArea
        Iterator var1 = this.providers.iterator();

        while(var1.hasNext()) {
            Provider provider = (Provider)var1.next();
            if (provider instanceof Doctor doctor) {
                System.out.printf("[%s %s %s, %s, %s %s] [%s, #%s]\n", doctor.getProfile().getFirstName(), doctor.getProfile().getLastName(), doctor.getProfile().getDob(), doctor.getLocation().name(), doctor.getLocation().getCounty(), doctor.getLocation().getZip(), doctor.getSpecialty().toString(), doctor.getNpl());
            } else if (provider instanceof Technician technician) {
                System.out.printf("[%s %s %s, %s, %s %s][rate: $%.2f]\n", technician.getProfile().getFirstName(), technician.getProfile().getLastName(), technician.getProfile().getDob(), technician.getLocation().name(), technician.getLocation().getCounty(), technician.getLocation().getZip(), (double)technician.getRatePerVisit());
            }
        }

    }

    /**Take input from providers file to input each provider in their respective lists
     * @param line is a line separated by a new line in the providers text file*/
    private void parseLine(String line) {
        String[] tokens = line.split("\\s+");
        String firstName;
        String lastName;
        String dob;
        Location location;
        if (tokens[0].equals("D")) {
            firstName = tokens[1];
            lastName = tokens[2];
            dob = tokens[3];
            location = Location.valueOf(tokens[4].toUpperCase());
            Specialty specialty = Specialty.valueOf(tokens[5].toUpperCase());
            String npi = tokens[6];
            Profile profile = new Profile(firstName, lastName, new Date(dob));
            Doctor doctor = new Doctor(profile, location, specialty, npi);
            this.providers.add(doctor);
            this.doctors.add(doctor);
        } else if (tokens[0].equals("T")) { //change this to account for radio button
            firstName = tokens[1];
            lastName = tokens[2];
            dob = tokens[3];
            location = Location.valueOf(tokens[4].toUpperCase());
            int ratePerVisit = Integer.parseInt(tokens[5]);
            Profile profile = new Profile(firstName, lastName, new Date(dob));
            Technician technician = new Technician(profile, location, ratePerVisit);
            this.providers.add(technician);
            this.technicians.add(technician);
        } else {
            System.out.println("Invalid provider type."); //Erase this
        }

    }

    public ClinicManagerController() {
        this.scanner = new Scanner(System.in); //Originally meant to read from terminal; erase this, I think
        this.loadProviders();
        //this.technicianRotation();
    }

    /**prints all technicians to the text area*/
    @FXML
    public void technicianRotation() {
        System.out.println("List of Technicians."); //print to text area
        ta_outputDisplay.appendText("Rotation list for the technicians.\n");
        StringBuilder rotationList = new StringBuilder();

        for(int i = 0; i < this.technicians.size(); ++i) {
            Technician tech = (Technician)this.technicians.get(i);
            rotationList.append(tech.getProfile().getFirstName()).append(" ").append(tech.getProfile().getLastName()).append(" ").append(" (").append(tech.getLocation().name()).append(")");
            if (i < this.technicians.size() - 1) {
                rotationList.append(" --> ");
            }
        }

        System.out.println(rotationList);
        //ObservableList<String> listOfTechnicians = FXCollections.observableArrayList();
        //listOfTechnicians.add(String.valueOf(rotationList));
        ta_outputDisplay.appendText(String.valueOf(rotationList));

    }


    @FXML
    public void clearSelection(ActionEvent event) {
        tf_fnameSC.clear();
        tf_lnameSC.clear();
        dp_apptDateSC.getEditor().clear();
        dp_dobSC.getEditor().clear();
    }



}
