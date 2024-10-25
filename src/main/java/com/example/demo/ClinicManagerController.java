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

/**this class acts as the Controller in the MVC style of this software, controlling what the user sees, processing data, and processing appointments*/
public class ClinicManagerController {

    /**list of appointments*/
    private final List<Appointment> appointmentList = new List<>();
    /**list of imaging appointments*/
    private final List<Imaging> imagingList = new List<>();
    /**list of providers*/
    private final List<Provider> providers = new List<>();
    /**list of technicians*/
    private final List<Technician> technicians = new List<>();
    /**list of doctors*/
    private final List<Doctor> doctors = new List<>();
    /**string rep of doctors to be displayed in the combo box*/
    private ObservableList<String> listOfDoctors = FXCollections.observableArrayList();
    /***/
    private int technicianIndex = 0;

    /**group of radio buttons in the schedule/cancel tab*/
    @FXML
    private ToggleGroup Appointment;
    /**button used to cancel appointments*/
    @FXML
    private Button bt_cancelAppt;
    /**button used to clear all fields from a given tab*/
    @FXML
    private Button bt_clearSelection;
    /**button used to load providers from the providers text file onto a combo box or in the text area*/
    @FXML
    private Button bt_loadProviders;
    /**button used to schedule appointments*/
    @FXML
    private Button bt_scheduleAppt;
    /**combo box that holds all providers that work */
    @FXML
    private ComboBox<String> cb_providerSC;
    /**combo box that holds all timeslots for rescheduling*/
    @FXML
    private ComboBox<?> cb_timeslotR;
    /**combo box that holds all timeslots for scheduling and cancelling*/
    @FXML
    private ComboBox<?> cb_timslotSC;
    /**date picker for scheduling or cancelling appointment date*/
    @FXML
    private DatePicker dp_apptDateSC;
    /**date picker for inputting date of birth for rescheduling appointment*/
    @FXML
    private DatePicker dp_dobR;
    /**date picker for inputting date of birth for scheduling/cancelling appointments*/
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

    /**default constructor of clinic manager controller*/
    public ClinicManagerController() {
        this.loadProviders();
    }

    /**When load providers button is clicked, display doctors in combo box
     * and display technicians in text area*/
    @FXML
    public void displayProvidersInGUI(ActionEvent event){
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

    /**Uses providers.txt to load the GUI with all providers that work for the clinic*/
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
    /**given user input from gui, construct an appointment to schedule
     * @param actionEvent is event that triggers this handler*/
    @FXML
    private void scheduleAppointment(ActionEvent actionEvent) {
        try {

            //check if first name, last name, appointment date, or dob text fields/date picker is null when schedule button was pressed
            //also check for non-null radio buttons and non-null provider and timeslot combo boxes
            if(tf_fnameSC.getText().isEmpty() || tf_lnameSC.getText().isEmpty() || dp_apptDateSC.getValue() == null || dp_dobSC.getValue() == null
            || Appointment.getSelectedToggle() == null || cb_timeslotR.getValue() == null || (rb_officeVisit.isSelected() && cb_providerSC.getValue() == null)){

                if (tf_fnameSC.getText().isEmpty()) {
                    ta_outputDisplay.appendText("Missing first name. Please enter first name.\n"); //print to GUI that field is missing
                }
                if (tf_lnameSC.getText().isEmpty()) {
                    ta_outputDisplay.appendText("Missing last name. Please enter last name.\n"); //print to GUI that field is missing
                }
                if (dp_apptDateSC.getValue() == null) {
                    ta_outputDisplay.appendText("Missing appointment date. Please choose an appointment date.\n"); //print to GUI that field is missing
                }
                if (dp_dobSC.getValue() == null) {
                    ta_outputDisplay.appendText("Missing date of birth. Please input your date of birth.\n"); //print to GUI that field is missing
                }
                if (Appointment.getSelectedToggle() == null) {
                    ta_outputDisplay.appendText("Missing appointment type. Please choose a type of appointment.\n");
                }
                if (cb_timeslotR.getValue() == null) {
                    ta_outputDisplay.appendText("Missing a requested timeslot for appointment. Please choose a timeslot.\n");
                }
                if (rb_officeVisit.isSelected() && cb_providerSC.getValue() == null) {
                    ta_outputDisplay.appendText("Missing provider preference. Please choose a provider.\n");
                }
                return;
            }
            //set these values to their respective text fields or date picker
            String date = dp_apptDateSC.getEditor().getText();
            String timeSlotID = cb_timslotSC.getValue().toString();
            String firstName = tf_fnameSC.getText();
            String lastName = tf_lnameSC.getText();
            String dob = dp_dobSC.getEditor().getText();

            Date appointmentDate = new Date(date);
            Date birthDate = new Date(dob);
            if (!appointmentDate.isValid()) {
                ta_outputDisplay.appendText("Error: Appointment date: " + String.valueOf(appointmentDate) + " is not valid in the calendar.\n");
                return;
            }
            if (!appointmentDate.isNotTodayOrBefore()) {
                ta_outputDisplay.appendText("Error: Appointment date: " + String.valueOf(appointmentDate) + " is today or before today.\n");
                return;
            }
            if (appointmentDate.isWeekend()) {
                ta_outputDisplay.appendText("Error: Appointment date: " + String.valueOf(appointmentDate) + " is on a weekend.\n");
                return;
            }
            if (!appointmentDate.isWithin6Months()) {
                ta_outputDisplay.appendText("Error: Appointment date " + String.valueOf(appointmentDate) + " is not within six months from today.\n");
                return;
            }
            if (!birthDate.isValid()) {
                ta_outputDisplay.appendText("Error: Patient date of birth: " + String.valueOf(birthDate) + " is not a valid calendar date.\n");
                return;
            }
            if (birthDate.isTodayOrAfter()) {
                ta_outputDisplay.appendText("Error: Patient date of birth: " + String.valueOf(birthDate) + " is today or a future date.\n");
                return;
            }

            Profile patientProfile = new Profile(firstName, lastName, birthDate);
            Timeslot selectedTimeslot = this.getTimeslotFromString(timeSlotID); //get selected timeslot from combo box

            //iterate through doctors list to check which doctor.toString matches with
            Iterator<Doctor> var1 = this.doctors.iterator();
            Provider selectedProvider = null;
            while (var1.hasNext()) { //add doctor to observable list
                Doctor doctor = var1.next();
                if (doctor.toString().equalsIgnoreCase(cb_providerSC.getValue())) {
                    selectedProvider = doctor;
                    break;
                }
            }

            Person patient = new Person(patientProfile);
            Appointment newAppointment = new Appointment(appointmentDate, selectedTimeslot, patient, selectedProvider);

            Iterator<classes.Appointment> var17 = this.appointmentList.iterator();
            //iterate through list of appointments to see if appointment already exists in scheduler
            while (var17.hasNext()) {
                Appointment existingAppointment = (Appointment) var17.next();
                if (existingAppointment.equals(newAppointment)) {
                    ta_outputDisplay.appendText(patientProfile.toString() + " has an existing appointment at the same time slot.");
                    return;
                }
            }
            if (!this.isProviderAvailable(selectedProvider, selectedTimeslot)) {
                return;
            }
            this.appointmentList.add(newAppointment);
            String var10001 = appointmentDate.toString();
            assert selectedProvider != null;
            ta_outputDisplay.appendText(var10001 + " " + selectedTimeslot.toString() + " " + patientProfile.toString() + " [" + ((Provider) selectedProvider).toString() + "] booked.\n");
        } catch (Exception var20) {
            Exception e = var20;
            ta_outputDisplay.appendText("Error scheduling appointment: " + e.getMessage());
        }

    }

    /**checks if provider is available at requested timeslot
     * @param provider is the selected provider in GUI combo box
     * @param timeslot is the selected timeslot by user in GUI
     * @return true if provider is available, false otherwise and prints to GUI*/
    private boolean isProviderAvailable(Provider provider, Timeslot timeslot) {
        Iterator<classes.Appointment> var3 = this.appointmentList.iterator();
        Appointment existingAppointment;
        do {
            if (!var3.hasNext()) {
                return true;
            }
            existingAppointment = (Appointment)var3.next();
        } while(!existingAppointment.getProvider().equals(provider) || !existingAppointment.getTimeslot().equals(timeslot));

        String var10001 = provider.toString();
        ta_outputDisplay.appendText(var10001 + " is not available at \n" + String.valueOf(timeslot));
        return false;
    }

    /**applies string representation of timeslot to a timeslot
     * @param timeSlot is the timeslot gained from user input
     * @return timeslot representation*/
    private Timeslot getTimeslotFromString(String timeSlot) {
        Timeslot var10000;
        switch (timeSlot) {
            case "1" -> var10000 = Timeslot.slot1();
            case "2" -> var10000 = Timeslot.slot2();
            case "3" -> var10000 = Timeslot.slot3();
            case "4" -> var10000 = Timeslot.slot4();
            case "5" -> var10000 = Timeslot.slot5();
            case "6" -> var10000 = Timeslot.slot6();
            case "7" -> var10000 = Timeslot.slot7();
            case "8" -> var10000 = Timeslot.slot8();
            case "9" -> var10000 = Timeslot.slot9();
            case "10" -> var10000 = Timeslot.slot10();
            case "11" -> var10000 = Timeslot.slot11();
            case "12" -> var10000 = Timeslot.slot12();
            default -> var10000 = null;
        }

        return var10000;
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
            ta_outputDisplay.appendText("Invalid provider type.\n"); //Erase this
        }

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
        ta_outputDisplay.appendText(String.valueOf(rotationList) + "\n");
    }


    /**clears all values from tab*/
    @FXML
    public void clearSelection(ActionEvent event) {
        tf_fnameSC.clear();
        tf_lnameSC.clear();
        dp_apptDateSC.getEditor().clear();
        dp_dobSC.getEditor().clear();
        Appointment.selectToggle(null);
    }



}
