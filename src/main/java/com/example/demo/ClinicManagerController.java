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
import java.io.PrintStream;
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
        } catch (FileNotFoundException var4) {
            System.out.println("Error: File not found!"); //replace this with printing to text area in GUI
        }

    }

    /*private void scheduleAppointment(String[] tokens) {
        try {
            if (tokens.length < 7) {
                System.out.println("Missing tokens");
                return;
            }

            String date = tokens[1];
            String timeSlotID = tokens[2];
            String firstName = tokens[3];
            String lastName = tokens[4];
            String dob = tokens[5];
            String npiStr = tokens[6];
            if (!this.isValidTimeslot(timeSlotID)) {
                System.out.println(timeSlotID + " is not a valid time slot.");
                return;
            }

            int npi;
            try {
                npi = Integer.parseInt(npiStr);
            } catch (NumberFormatException var19) {
                throw new RuntimeException("Error: NPI not a valid int");
            }

            Date appointmentDate = new Date(date);
            Date birthDate = new Date(dob);
            if (!appointmentDate.isValid()) {
                System.out.println("Error: Appointment date: " + String.valueOf(appointmentDate) + " is not valid in the calendar");
                return;
            }

            if (!appointmentDate.isNotTodayOrBefore()) {
                System.out.println("Error: Appointment date: " + String.valueOf(appointmentDate) + " is today or before today");
                return;
            }

            if (appointmentDate.isWeekend()) {
                System.out.println("Error: Appointment date: " + String.valueOf(appointmentDate) + " is on a weekend");
                return;
            }

            if (!appointmentDate.isWithin6Months()) {
                System.out.println("Error: Appointment date " + String.valueOf(appointmentDate) + " is not within six months from today");
                return;
            }

            if (!birthDate.isValid()) {
                System.out.println("Error: Patient date of birth: " + String.valueOf(birthDate) + " is not a valid calendar date.");
                return;
            }

            if (birthDate.isTodayOrAfter()) {
                System.out.println("Error: Patient date of birth: " + String.valueOf(birthDate) + " is today or a future date.");
                return;
            }

            Profile patientProfile = new Profile(firstName, lastName, birthDate);
            Timeslot selectedTimeslot = this.getTimeslotFromString(timeSlotID);
            Provider selectedProvider = null;
            Iterator var14 = this.providers.iterator();

            while(var14.hasNext()) {
                Provider provider = (Provider)var14.next();
                if (provider instanceof Doctor doctor) {
                    if (doctor.getNpl().equals(String.valueOf(npiStr))) {
                        selectedProvider = doctor;
                        break;
                    }
                }
            }

            if (selectedProvider == null) {
                System.out.println("Error: Provider with NPI " + npi + " does not exist.");
                return;
            }

            Doctor doctor = this.findProviderByNpl(npiStr);
            if (doctor == null) {
                System.out.println("Error: Doctor with Npl " + npiStr + " not found.");
                return;
            }

            Person patient = new Person(patientProfile);
            Appointment newAppointment = new Appointment(appointmentDate, selectedTimeslot, patient, selectedProvider);
            Iterator var17 = this.appointmentList.iterator();

            while(var17.hasNext()) {
                Appointment existingAppointment = (Appointment)var17.next();
                if (existingAppointment.equals(newAppointment)) {
                    System.out.println(patientProfile.toString() + " has an existing appointment at the same time slot.");
                    return;
                }
            }

            if (!this.isProviderAvailable(doctor, selectedTimeslot)) {
                return;
            }

            this.appointmentList.add(newAppointment);
            PrintStream var10000 = System.out;
            String var10001 = appointmentDate.toString();
            var10000.println(var10001 + " " + selectedTimeslot.toString() + " " + patientProfile.toString() + " [" + ((Provider)selectedProvider).toString() + "] booked.");
        } catch (Exception var20) {
            Exception e = var20;
            System.out.println("Error scheduling appointment: " + e.getMessage());
        }

    }*/

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
    }

    /**prints all technicians to the text area*/
    @FXML
    public void technicianRotation() {
        ta_outputDisplay.appendText("Rotation list for the technicians.\n");
        StringBuilder rotationList = new StringBuilder();

        for(int i = 0; i < this.technicians.size(); ++i) {
            Technician tech = (Technician)this.technicians.get(i);
            rotationList.append(tech.getProfile().getFirstName()).append(" ").append(tech.getProfile().getLastName()).append(" ").append(" (").append(tech.getLocation().name()).append(")");
            if (i < this.technicians.size() - 1) {
                rotationList.append(" --> ");
            }
        }
        ta_outputDisplay.appendText(String.valueOf(rotationList));
    }


    @FXML
    public void clearSelection(ActionEvent event) {
        tf_fnameSC.clear();
        tf_lnameSC.clear();
        dp_apptDateSC.getEditor().clear();
        dp_dobSC.getEditor().clear();
        Appointment.selectToggle(null);
    }



}
