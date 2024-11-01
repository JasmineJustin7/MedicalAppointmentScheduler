/**
 *This class controls GUI Clinic Manager in scene builder
 * @author Jasmine Justin
 * @author Jimena Reyes
 */
package com.example.demo;

import classes.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import util.Date;
import util.List;
import util.Sort;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableView;
import javafx.scene.control.MenuItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Scanner;

/**this class acts as the Controller in the MVC style of this software, controlling what the user sees, processing data, and processing appointments*/
public class ClinicManagerController {

    /**
     * list of appointments
     */
    private final List<Appointment> appointmentList = new List<>();
    /**
     * list of imaging appointments
     */
    private final List<Imaging> imagingList = new List<>();
    /**
     * list of providers
     */
    private final List<Provider> providers = new List<>();
    /**
     * list of technicians
     */
    private final List<Technician> technicians = new List<>();
    /**
     * list of doctors
     */
    private final List<Doctor> doctors = new List<>();
    /**
     * string rep of doctors to be displayed in the combo box
     */
    private ObservableList<String> listOfDoctors = FXCollections.observableArrayList();
    /**
     * keeps track of technicians within circular list
     */
    private int technicianIndex = 0;
    /**
     * group of radio buttons in the schedule/cancel tab
     */
    @FXML
    private ToggleGroup Appointment;
    /**
     * button used to cancel appointments
     */
    @FXML
    private Button bt_cancelAppt;
    /**
     * button used to clear all fields from a given tab
     */
    @FXML
    private Button bt_clearAppt;
    /**
     * button associated with clearing text fields, date picker and combo boxes
     */
    @FXML
    private Button bt_clearSelection;
    /**
     * button used to load providers from the providers text file onto a combo box or in the text area
     */
    @FXML
    private Button bt_loadProviders;
    /**
     * button used to schedule appointments
     */
    @FXML
    private Button bt_scheduleAppt;
    /**
     * button to reschedule appointment
     */
    @FXML
    private Button bt_rescheduleAppt;
    /**
     * combo box that holds all providers that work
     */
    @FXML
    private ComboBox<String> cb_providerSC;
    /**
     * combo box that holds all timeslots for rescheduling
     */
    @FXML
    private ComboBox<String> cb_timeslotR;
    /**
     * combo box for new timeslot when rescheduling
     */
    @FXML
    private ComboBox<String> cb_tSlotR;
    /**
     * combo box that holds all timeslots for scheduling and cancelling
     */
    @FXML
    private ComboBox<Radiology> cb_services;
    /**combo box to display timeslots in schedule/cancel tab*/
    @FXML
    private ComboBox<String> cb_timeslotSC;
    /**
     * date picker for new appointment
     */
    @FXML
    private DatePicker dp_newDR;
    /**
     * date picker for current appt date for rescheduling appointment
     */
    @FXML
    private DatePicker dp_apptDR;
    /**
     * date picker for scheduling or cancelling appointment date
     */
    @FXML
    private DatePicker dp_apptDateSC;
    /**
     * date picker for inputting date of birth for rescheduling appointment
     */
    @FXML
    private DatePicker dp_dobR;
    /**
     * date picker for inputting date of birth for scheduling/cancelling appointments
     */
    @FXML
    private DatePicker dp_dobSC;
    /**
     * radio button to choose an imaging service
     */
    @FXML
    private RadioButton rb_imaging;
    /**
     * radio button for choosing an office visit
     */
    @FXML
    private RadioButton rb_officeVisit;
    /**
     * text area to store errors and messages to users
     */
    @FXML
    private TextArea ta_outputDisplay;
    /**
     * table view that stores locations of providers
     */
    @FXML
    private TableView<Location> tableViewClinicLocations;
    /**
     * table column to store cities
     */
    @FXML
    private TableColumn<Location, String> tc_city;
    /**
     * table column to store counties
     */
    @FXML
    private TableColumn<Location, String> tc_county;
    /**
     * table column to store zips
     */
    @FXML
    private TableColumn<Location, String> tc_zip;
    /**
     * text field for first name in reschedule tab
     */
    @FXML
    private TextField tf_fnameR;
    /**
     * text field for first name in schedule/cancel tab
     */
    @FXML
    private TextField tf_fnameSC;
    /**
     * text field for last name in reschedule tab
     */
    @FXML
    private TextField tf_lnameR;
    /**
     * text field for last name in schedule/cancel tab
     */
    @FXML
    private TextField tf_lnameSC;

    /**
     * button for clearing data in the Service Summary tab
     */
    @FXML
    private Button bt_clearSS;

    /**
     * button for printing the data depending on the appointment radio button selected
     */

    @FXML
    private Button bt_printSS;
    /**
     * menu item for imaging services only
     */
    @FXML
    private MenuItem mi_imagingSS;

    /**
     * menu item for patient statement
     */
    @FXML
    private MenuItem mi_patientSS;

    /**
     * menu item for provider credit amount
     */
    @FXML
    private MenuItem mi_providerSS;

    /**
     * menu item for sorting by date
     */
    @FXML
    private MenuItem mi_sortByDateSS;

    /**
     * menu item for sorting my location
     */
    @FXML
    private MenuItem mi_sortByLocationSS;

    /**
     * menu item for sorting by office visits only
     */
    @FXML
    private MenuItem mi_visitsSS;


    /**
     * default constructor of clinic manager controller
     */
    public ClinicManagerController() {
        this.loadProviders();
    }

    /**
     * When load providers button is clicked, display doctors in combo box
     * and display technicians in text area
     *
     * @param event triggers this event handler
     */
    @FXML
    public void displayProvidersInGUI(ActionEvent event) {
        Iterator<Doctor> var1 = this.doctors.iterator();
        while (var1.hasNext()) { //add doctor to observable list
            Doctor doctor = var1.next();
            listOfDoctors.add(doctor.toString());
        }
        //Display doctors in combo box
        cb_providerSC.setItems(listOfDoctors);

        //Display technicians in text area
        technicianRotation();
        bt_loadProviders.setDisable(true);

    }

    /**
     * Uses providers.txt to load the GUI with all providers that work for the clinic
     */
    private void loadProviders() {
        try {
            File file = new File("providers.txt");
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String providerInfo = fileScanner.nextLine();
                this.parseLine(providerInfo);
            }
            Sort.provider(this.providers);
        } catch (FileNotFoundException var4) {
            ta_outputDisplay.appendText("Error: File not found!"); //replace this with printing to text area in GUI
        }

    }

    /**
     * load services once imaging service radio button is clicked
     * @param actionEvent is the event
     */
    @FXML
    public void loadServices(ActionEvent actionEvent) {
        ObservableList<Radiology> radiologies = FXCollections.observableArrayList(Radiology.values());
        cb_services.setItems(radiologies);

    }

    /**
     * given user input from gui, construct an appointment to schedule
     * @param actionEvent is event that triggers this handler
     */
    @FXML
    private void scheduleAppointment(ActionEvent actionEvent) {
        try {
            if (rb_imaging.isSelected()) {
                imagingAppointment(actionEvent);
                return;
            }
            if (isInputErrorForOfficeAppointment()) return;
            String date = dp_apptDateSC.getEditor().getText();
            String timeSlotID = cb_timeslotSC.getValue();
            String firstName = tf_fnameSC.getText();
            String lastName = tf_lnameSC.getText();
            String dob = dp_dobSC.getEditor().getText();
            Date appointmentDate = new Date(date);
            Date birthDate = new Date(dob);
            if (isDateError(appointmentDate, birthDate)) return;
            Profile patientProfile = new Profile(firstName, lastName, birthDate);
            Timeslot selectedTimeslot = getTimeslot(timeSlotID);
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
            if (isExistingOfficeAppointment(var17, newAppointment, patientProfile))
                return;
            if (!this.isProviderAvailable(selectedProvider, selectedTimeslot)) {
                return;
            }
            this.appointmentList.add(newAppointment);
            String var10001 = appointmentDate.toString();
            assert selectedProvider != null;
            ta_outputDisplay.appendText(var10001 + " " + selectedTimeslot.toString() + " " + patientProfile.toString() + " [" + ((Provider) selectedProvider).toString() + "] booked.\n");
        } catch (Exception var20) {
            ta_outputDisplay.appendText("Error scheduling appointment: " + var20.getMessage() + "\n");
        }
    }

    /**
     * private method checking if the requested office appointment already exists in the scheduler
     * @param patientProfile is the patient profile of the user
     * @param newAppointment is the appointment requested by the user
     * @param var17          is the appointment to be compared to other appointments in the scheduler
     * @return true if the appointment already exists in the scheduler, false otherwise
     */
    private boolean isExistingOfficeAppointment(Iterator<classes.Appointment> var17, classes.Appointment newAppointment, Profile patientProfile) {
        while (var17.hasNext()) {
            Appointment existingAppointment = (Appointment) var17.next();
            if (existingAppointment.equals(newAppointment)) {
                ta_outputDisplay.appendText(patientProfile.toString() + " has an existing appointment at the same time slot.\n");
                return true;
            }
        }
        return false;
    }

    /**
     * private method that checks if user has input all required fields
     * @return true if there was an error in one of the user's input fields, false otherwise
     */
    private boolean isInputErrorForOfficeAppointment() {
        if (tf_fnameSC.getText().isEmpty() || tf_lnameSC.getText().isEmpty() || dp_apptDateSC.getValue() == null || dp_dobSC.getValue() == null
                || Appointment.getSelectedToggle() == null || (rb_officeVisit.isSelected() && cb_providerSC.getSelectionModel().isEmpty()) || cb_timeslotSC.getSelectionModel().isEmpty()) {

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
            if (cb_timeslotSC.getSelectionModel().isEmpty()) {
                ta_outputDisplay.appendText("Missing a requested timeslot for appointment. Please choose a timeslot.\n");
            }
            if (rb_officeVisit.isSelected() && cb_providerSC.getSelectionModel().isEmpty()) {
                ta_outputDisplay.appendText("Missing provider preference. Please choose a provider.\n");
            }
            return true;
        }
        return false;
    }

    /**
     * event handler used to make imaging appointments
     * will be used if radio button imaging service is selected
     * @param actionEvent triggers this event handler
     */
    public void imagingAppointment(ActionEvent actionEvent) {
        try {
            if (isInputErrorForImagingAppointment()) return;
            String date = dp_apptDateSC.getEditor().getText();
            String timeSlotID = cb_timeslotSC.getValue();
            String firstName = tf_fnameSC.getText();
            String lastName = tf_lnameSC.getText();
            String dob = dp_dobSC.getEditor().getText();
            String imagingServices = cb_services.getValue().toString();
            Date appointmentDate = new Date(date);
            Date bday = new Date(dob);
            if (isDateError(appointmentDate, bday)) return;
            if (!this.isValidImagingService(imagingServices)) {
                ta_outputDisplay.appendText("Error: " + imagingServices + " - imaging service not provided.\n");
                return;
            }
            Timeslot selectedTimeslot = getTimeslot(timeSlotID);
            if (selectedTimeslot == null) {
                ta_outputDisplay.appendText("Error: Invalid Timeslot.\n");
                return;
            }
            Radiology imagingSrv = Radiology.valueOf(imagingServices.toUpperCase());
            Technician availableTech = this.findAvailableTechnician(selectedTimeslot, imagingSrv);
            if (availableTech == null) {
                ta_outputDisplay.appendText("Cannot find technician at all locations for \n" + imagingServices);
                return;
            }
            Profile patientProfile = new Profile(firstName, lastName, bday);
            Iterator var14 = this.imagingList.iterator();
            Imaging appointment;
            if (isExistingImagingAppointment(var14, appointmentDate, selectedTimeslot, patientProfile))
                return;
            Person patient = new Person(patientProfile);
            appointment = new Imaging(appointmentDate, selectedTimeslot, patient, availableTech, imagingSrv);
            this.imagingList.add(appointment);
            ta_outputDisplay.appendText(appointment.getDate().toString() + " " + appointment.getTimeslot().toString() + " " + appointment.getPatient() + " [" +
                    appointment.getProvider().getProfile() + ", " + appointment.getProvider().getLocation() + " [" + appointment.getRoom().toString() + "]]" + " booked.\n");
        } catch (Exception var16) {
            ta_outputDisplay.appendText("Missing tokens.\n");
        }
    }

    /**
     * checks if appointment already exists in scheduler
     * @param appointmentDate  is the date requested by user
     * @param patientProfile   is the profile of user
     * @param selectedTimeslot is the requested timeslot by user
     * @param var14            is the appointment to be checked
     * @return true if it already exists, false otherwise
     */
    private boolean isExistingImagingAppointment(Iterator var14, Date appointmentDate, Timeslot selectedTimeslot, Profile patientProfile) {
        Imaging appointment;
        while (var14.hasNext()) {
            appointment = (Imaging) var14.next();
            if (appointment.getDate().equals(appointmentDate) && appointment.getTimeslot().equals(selectedTimeslot) && appointment.getPatient().getProfile().equals(patientProfile)) {
                ta_outputDisplay.appendText("Error: an appointment with the same patient exist.\n");
                return true;
            }
        }
        return false;
    }

    /**
     * private method to check if all fields to schedule an imaging appointment have been filled and prints messages otherwise
     * @return true if error has been found, false otherwise
     */
    private boolean isInputErrorForImagingAppointment() {
        if (tf_fnameSC.getText().isEmpty() || tf_lnameSC.getText().isEmpty() || dp_apptDateSC.getValue() == null || dp_dobSC.getValue() == null
                || Appointment.getSelectedToggle() == null || cb_timeslotSC.getSelectionModel().isEmpty()
                || cb_services.getSelectionModel().isEmpty()) {
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
            if (cb_timeslotSC.getSelectionModel().isEmpty()) {
                ta_outputDisplay.appendText("Missing a requested timeslot for appointment. Please choose a timeslot.\n");
            }
            if (cb_services.getSelectionModel().isEmpty()) {
                ta_outputDisplay.appendText("Missing imaging service. Please choose an imaging service.\n");
            }
            return true;
        }
        return false;
    }

    /**
     * private method that checks if there is an issue with the date provided by user
     * @param appointmentDate is the date of appointment requested by user
     * @param bday            is the date of birth of the user
     * @return true if an error with the date is found, false otherwise
     */
    private boolean isDateError(Date appointmentDate, Date bday) {
        if (!appointmentDate.isValid()) {
            ta_outputDisplay.appendText("Error: Appointment date: " + String.valueOf(appointmentDate) + " is not valid in the calendar.\n");
            return true;
        }
        if (!appointmentDate.isNotTodayOrBefore()) {
            ta_outputDisplay.appendText("Error: Appointment date: " + String.valueOf(appointmentDate) + " is today or before today.\n");
            return true;
        }
        if (appointmentDate.isWeekend()) {
            ta_outputDisplay.appendText("Error: Appointment date: " + String.valueOf(appointmentDate) + " is on a weekend.\n");
            return true;
        }
        if (!appointmentDate.isWithin6Months()) {
            ta_outputDisplay.appendText("Error: Appointment date " + String.valueOf(appointmentDate) + " is not within six months from today.\n");
            return true;
        }
        if (!bday.isValid()) {
            ta_outputDisplay.appendText("Error: Patient date of birth: " + String.valueOf(bday) + " is not a valid calendar date.\n");
            return true;
        }
        if (bday.isTodayOrAfter()) {
            ta_outputDisplay.appendText("Error: Patient date of birth: " + String.valueOf(bday) + " is today or a future date.\n");
            return true;
        }
        return false;
    }

    /**
     * returns a technician that is available for the requested imaging service at requested timeslot
     * Note: there is one room per imaging service at a given office
     * @param imagingService is the requested service by the user
     * @param timeslot       is the requested timeslot
     * @return a technician that is available to be added to the patient's appointment
     */
    private Technician findAvailableTechnician(Timeslot timeslot, Radiology imagingService) {
        int startIndex = this.technicianIndex;
        boolean technicianFound = false;

        do {
            Technician technician = (Technician) this.technicians.get(this.technicianIndex);
            this.technicianIndex = (this.technicianIndex + 1) % this.technicians.size();
            if (this.isTechnicianAvailable(technician, timeslot, imagingService)) {
                technicianFound = true;
                return technician;
            }
        } while (this.technicianIndex != startIndex);

        return null;
    }

    /**
     * checks if technician is available at given timeslot and given imaging service
     * @param timeslot       is the timeslot requested by the user
     * @param technician     is the provider being checked
     * @param imagingService is the service requested for by the user
     * @return true if technician is available, false otherwise
     */
    private boolean isTechnicianAvailable(Technician technician, Timeslot timeslot, Radiology imagingService) {
        Iterator var4 = this.imagingList.iterator();
        Imaging imagingAppointment;
        do {
            if (!var4.hasNext()) {
                return true;
            }
            imagingAppointment = (Imaging) var4.next();
        } while (!imagingAppointment.getProvider().getProfile().equals(technician.getProfile()) || !imagingAppointment.getTimeslot().equals(timeslot) || !imagingAppointment.getRoom().equals(imagingService));

        return false;
    }

    /**
     * checks if the service provided is a valid imaging service
     * @param service is the given service to be checked
     * @return true if the service is valid, false otherwise
     */
    private boolean isValidImagingService(String service) {
        try {
            Radiology.valueOf(service.toUpperCase());
            return true;
        } catch (IllegalArgumentException var3) {
            return false;
        }
    }

    /**
     * cancel appointment using GUI
     * @param actionEvent is the event that fires this event handler
     */
    @FXML
    public void cancelAppointment(ActionEvent actionEvent) {
        try {
            //check if first name, last name, appointment date, or dob text fields/date picker is null when schedule button was pressed
            //also check for non-null radio buttons and non-null provider and timeslot combo boxes
            if (tf_fnameSC.getText().isEmpty() || tf_lnameSC.getText().isEmpty() || dp_apptDateSC.getValue() == null || dp_dobSC.getValue() == null
                    || cb_timeslotSC.getSelectionModel().isEmpty()) {

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
                if (cb_timeslotSC.getSelectionModel().isEmpty()) {
                    ta_outputDisplay.appendText("Missing a requested timeslot for appointment. Please choose a timeslot.\n");
                }
                return;
            }
            //set these values to their respective text fields or date picker
            String date = dp_apptDateSC.getEditor().getText();
            String timeSlotID = cb_timeslotSC.getValue();
            String firstName = tf_fnameSC.getText();
            String lastName = tf_lnameSC.getText();
            String dob = dp_dobSC.getEditor().getText();

            Date appointmentDate = new Date(date);
            Date birthDay = new Date(dob);
            Profile patientProfile = new Profile(firstName, lastName, birthDay);
            Person patient = new Person(patientProfile);

            Timeslot selectedTimeslot = getTimeslot(timeSlotID);

            boolean officeAppointmentCanceled = this.removeOfficeAppointment(appointmentDate, selectedTimeslot, patient);
            boolean imagingAppointmentCanceled = this.removeImagingAppointment(appointmentDate, selectedTimeslot, patient);
            String var10001;
            if (!officeAppointmentCanceled && !imagingAppointmentCanceled) {
                var10001 = appointmentDate.toString();
                ta_outputDisplay.appendText(var10001 + " " + selectedTimeslot.toString() + " - " + patientProfile.toString() + " does not exist.\n");
            } else {
                var10001 = appointmentDate.toString();
                ta_outputDisplay.appendText(var10001 + " " + selectedTimeslot.toString() + " - " + patientProfile.toString() + " appointment has been canceled.\n");
            }
        } catch (Exception var14) {
            ta_outputDisplay.appendText("Error: ");
        }
    }

    /**
     * private method used to get Timeslot object for provided timeslot time given by user
     * @param timeSlotID is the timeslot provided by user
     * @return is the Timeslot associated with the timeslot given by user
     */
    private static Timeslot getTimeslot(String timeSlotID) {
        Timeslot selectedTimeslot = null;
        if (timeSlotID.equals(Timeslot.slot1().toString())) {
            selectedTimeslot = Timeslot.slot1();
        } else if (timeSlotID.equals(Timeslot.slot2().toString())) {
            selectedTimeslot = Timeslot.slot2();
        } else if (timeSlotID.equals(Timeslot.slot3().toString())) {
            selectedTimeslot = Timeslot.slot3();
        } else if (timeSlotID.equals(Timeslot.slot4().toString())) {
            selectedTimeslot = Timeslot.slot4();
        } else if (timeSlotID.equals(Timeslot.slot5().toString())) {
            selectedTimeslot = Timeslot.slot5();
        } else if (timeSlotID.equals(Timeslot.slot6().toString())) {
            selectedTimeslot = Timeslot.slot6();
        } else if (timeSlotID.equals(Timeslot.slot7().toString())) {
            selectedTimeslot = Timeslot.slot7();
        } else if (timeSlotID.equals(Timeslot.slot8().toString())) {
            selectedTimeslot = Timeslot.slot8();
        } else if (timeSlotID.equals(Timeslot.slot9().toString())) {
            selectedTimeslot = Timeslot.slot9();
        } else if (timeSlotID.equals(Timeslot.slot10().toString())) {
            selectedTimeslot = Timeslot.slot10();
        } else if (timeSlotID.equals(Timeslot.slot11().toString())) {
            selectedTimeslot = Timeslot.slot11();
        } else if (timeSlotID.equals(Timeslot.slot12().toString())) {
            selectedTimeslot = Timeslot.slot12();
        }
        return selectedTimeslot;
    }

    /**
     * removes office appointment from list of appointments
     * @param appointmentDate is the requested appointment date by user
     * @param patientProfile  is the profile of the user
     * @param timeslot        is the timeslot requested by user
     * @return true is office appointment was successfully removed, false otherwise
     */
    private boolean removeOfficeAppointment(Date appointmentDate, Timeslot timeslot, Person patientProfile) {
        for (int i = 0; i < this.appointmentList.size(); ++i) {
            Appointment appointment = (Appointment) this.appointmentList.get(i);
            if (appointment.getDate().equals(appointmentDate) && appointment.getTimeslot().equals(timeslot) && appointment.getPatient().equals(patientProfile)) {
                this.appointmentList.remove(appointment);
                return true;
            }
        }
        return false;
    }

    /**
     * remove the imaging appointment from the scheduler
     * @param timeslot        is the timeslot provided by user
     * @param patientProfile  is the patient profile of the user
     * @param appointmentDate is the appointment date of the user
     * @return true if imaging appointment was removed, false otherwise
     */
    private boolean removeImagingAppointment(Date appointmentDate, Timeslot timeslot, Person patientProfile) {
        for (int i = 0; i < this.imagingList.size(); ++i) {
            Imaging imagingAppointment = (Imaging) this.imagingList.get(i);
            if (imagingAppointment.getDate().equals(appointmentDate) && imagingAppointment.getTimeslot().equals(timeslot) && imagingAppointment.getPatient().equals(patientProfile)) {
                this.imagingList.remove(imagingAppointment);
                return true;
            }
        }

        return false;
    }

    /**
     * checks if provider is available at requested timeslot
     * @param provider is the selected provider in GUI combo box
     * @param timeslot is the selected timeslot by user in GUI
     * @return true if provider is available, false otherwise and prints to GUI
     */
    private boolean isProviderAvailable(Provider provider, Timeslot timeslot) {
        Iterator<classes.Appointment> var3 = this.appointmentList.iterator();
        Appointment existingAppointment;
        do {
            if (!var3.hasNext()) {
                return true;
            }
            existingAppointment = (Appointment) var3.next();
        } while (!existingAppointment.getProvider().equals(provider) || !existingAppointment.getTimeslot().equals(timeslot));
        return false;
    }

    /**
     * applies string representation of timeslot to a timeslot
     * @param timeSlot is the timeslot gained from user input
     * @return timeslot representation
     */
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

    /**
     * Take input from providers file to input each provider in their respective lists
     * @param line is a line separated by a new line in the providers text file
     */
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

    /**
     * prints all technicians to the text area
     */
    @FXML
    public void technicianRotation() {
        ta_outputDisplay.appendText("Rotation list for the technicians.\n");
        StringBuilder rotationList = new StringBuilder();

        for (int i = 0; i < this.technicians.size(); ++i) {
            Technician tech = (Technician) this.technicians.get(i);
            rotationList.append(tech.getProfile().getFirstName()).append(" ").append(tech.getProfile().getLastName()).append(" ").append(" (").append(tech.getLocation().name()).append(")");
            if (i < this.technicians.size() - 1) {
                rotationList.append(" --> ");
            }
        }
        ta_outputDisplay.appendText(String.valueOf(rotationList) + "\n");
    }

    /**
     * Loads timeslots, and clinic locations
     */
    @FXML
    public void initialize() {
        loadTimeSlots();
        loadClinicLocations();
    }

    /**
     * load timeslots provided by the clinic to the schedule/cancel and reschedule tabs
     */
    @FXML
    private void loadTimeSlots() {
        ObservableList<String> timeSlots = FXCollections.observableArrayList();
        timeSlots.add(Timeslot.slot1().toString());
        timeSlots.add(Timeslot.slot2().toString());
        timeSlots.add(Timeslot.slot3().toString());
        timeSlots.add(Timeslot.slot4().toString());
        timeSlots.add(Timeslot.slot5().toString());
        timeSlots.add(Timeslot.slot6().toString());
        timeSlots.add(Timeslot.slot7().toString());
        timeSlots.add(Timeslot.slot8().toString());
        timeSlots.add(Timeslot.slot9().toString());
        timeSlots.add(Timeslot.slot10().toString());
        timeSlots.add(Timeslot.slot11().toString());
        timeSlots.add(Timeslot.slot12().toString());

        cb_timeslotSC.setItems(timeSlots);
        cb_timeslotR.setItems(timeSlots);
        cb_tSlotR.setItems(timeSlots);
    }

    /**
     * loads locations of the clinic to the clinic locations tab
     */
    @FXML
    private void loadClinicLocations() {
        ObservableList<Location> locations = FXCollections.observableArrayList(Location.values());
        tc_city.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().name())); // Use name() for the city
        tc_county.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCounty())); // Use getCounty() for county
        tc_zip.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getZip())); // Use getZip() for zip
        tableViewClinicLocations.setItems(locations);
    }

    /**
     * reschedule user appointment
     * @param event is the event that triggers this event handler
     */
    @FXML
    void rescheduleAppointment(ActionEvent event) {
        try {
            if (isInputErrorForReschedule()) return;

            String firstName = tf_fnameR.getText();
            String lastName = tf_lnameR.getText();
            String dob = dp_dobR.getEditor().getText();
            String oldDate = dp_apptDR.getEditor().getText(); // Old appointment date
            String oldTimeSlotID = cb_tSlotR.getValue(); // Old timeslot
            String newDate = dp_newDR.getEditor().getText(); // New appointment date
            String newTimeSlotID = cb_timeslotR.getValue(); // New timeslot
            Date appointmentDate = new Date(oldDate); // Old date
            Date newAppointmentDate = new Date(newDate); // New date
            Date birthDate = new Date(dob);

            if (isDateError(newAppointmentDate, birthDate)) {
                return;
            }
            Timeslot newTimeslot = getTimeslot(newTimeSlotID);
            Profile patientProfile = new Profile(firstName, lastName, birthDate);

            // Find existing appointment
            Appointment oldAppointment = findAppointment(appointmentDate, oldTimeSlotID, patientProfile);
            if (oldAppointment == null) {
                ta_outputDisplay.appendText("Error: No existing appointment found to reschedule.\n");
                return;
            }

            //method to see if provider is still unavailable
            for (Appointment appointment : appointmentList) {
                if (appointment.getDate().equals(newAppointmentDate) &&
                        appointment.getProvider().equals(oldAppointment.getProvider()) &&
                        appointment.getTimeslot().equals(newTimeslot)) {
                    ta_outputDisplay.appendText("Error: A conflicting appointment exists on " + newAppointmentDate +
                            " at " + newTimeslot + " with provider " + oldAppointment.getProvider() + ".\n");
                    return;
                }
            }

            if (!isProviderAvailableForReschedule(oldAppointment.getProvider(), newTimeslot, newAppointmentDate)) {
                return;
            }

            // Create and replace old appointment with new one
            Appointment newAppointment = new Appointment(newAppointmentDate, newTimeslot, oldAppointment.getPatient(), oldAppointment.getProvider());
            appointmentList.remove(oldAppointment);
            appointmentList.add(newAppointment);
            ta_outputDisplay.appendText("Appointment for " + patientProfile + " rescheduled from " + appointmentDate + " to " + newAppointmentDate + " at " + newTimeslot + ".\n");
        } catch (Exception e) {
            ta_outputDisplay.appendText("Error rescheduling appointment: " + e.getMessage() + "\n");
        }
    }

    /**
     * Checks if the provider is available for rescheduling at the requested timeslot on a specific date.
     * @param provider is the selected provider in the GUI combo box.
     * @param timeslot is the selected timeslot by the user in the GUI.
     * @param date     is the date for the requested appointment.
     * @return true if the provider is available, false otherwise, and prints to the GUI.
     */
    private boolean isProviderAvailableForReschedule(Provider provider, Timeslot timeslot, Date date) {
        Iterator<classes.Appointment> var3 = this.appointmentList.iterator();
        Appointment existingAppointment;

        do {
            if (!var3.hasNext()) {
                return true; // Provider is available if no matching appointment found
            }
            existingAppointment = (Appointment) var3.next();
        } while (!existingAppointment.getProvider().equals(provider) ||
                !existingAppointment.getTimeslot().equals(timeslot) ||
                !existingAppointment.getDate().equals(date));
        ta_outputDisplay.appendText(provider.toString() + " is not available at " + timeslot + " on " + date + "\n");
        return false;
    }

    /**
     * checks if any of the fields that the user needs to fill out is blank on the rescheduling tab
     * @return true if an error was found, false otherwise
     */
    private boolean isInputErrorForReschedule() {
        if (tf_fnameR.getText().isEmpty() || tf_lnameR.getText().isEmpty() || dp_apptDR.getValue() == null || dp_dobR.getValue() == null
                || cb_timeslotR.getSelectionModel().isEmpty() || cb_tSlotR.getSelectionModel().isEmpty() || dp_newDR.getValue() == null) {

            if (tf_fnameR.getText().isEmpty()) {
                ta_outputDisplay.appendText("Error: Please enter First Name.\n");
            }
            if (tf_lnameR.getText().isEmpty()) {
                ta_outputDisplay.appendText("Error: Please enter Last Name.\n");
            }
            if (dp_apptDR.getValue() == null) {
                ta_outputDisplay.appendText("Error: Please choose old Appointment Date.\n");
            }
            if (dp_dobR.getValue() == null) {
                ta_outputDisplay.appendText("Error: Please input Date of Birth.\n");
            }
            if (cb_tSlotR.getSelectionModel().isEmpty()) {
                ta_outputDisplay.appendText("Error: Please choose old Timeslot.\n");
            }
            if (cb_timeslotR.getSelectionModel().isEmpty()) {
                ta_outputDisplay.appendText("Error: Please choose new Timeslot.\n");
            }
            if (dp_newDR.getValue() == null) {
                ta_outputDisplay.appendText("Error: Please choose new Appointment Date.\n");
            }
            return true;
        }
        return false;
    }

    /**
     * Helper method to find an existing appointment
     * @param patientProfile is the patient profile of the user
     * @param date           is the old date of previous appointment provided by the user
     * @param timeSlotID     is the old timeslot provided by the user
     * @return appointment that is being checked
     */
    private Appointment findAppointment(Date date, String timeSlotID, Profile patientProfile) {
        Timeslot requestedTimeslot = getTimeslot(timeSlotID);
        for (Appointment appointment : appointmentList) {
            boolean dateMatch = appointment.getDate().equals(date);
            boolean timeslotMatch = appointment.getTimeslot().equals(requestedTimeslot);
            boolean patientMatch = appointment.getPatient().equals(new Person(patientProfile)); // Adjusted

            if (dateMatch && timeslotMatch && patientMatch) {
                return appointment;
            }
        }
        return null;
    }

    /**
     * clears all values from tab
     * @param event is the event that triggers this event handler
     */
    @FXML
    public void clearSelection(ActionEvent event) {
        tf_fnameSC.clear();
        tf_lnameSC.clear();
        dp_apptDateSC.getEditor().clear();
        dp_dobSC.getEditor().clear();
        Appointment.selectToggle(null);

        //Clears time selection and Provider
        cb_timeslotSC.getSelectionModel().clearSelection();
        cb_providerSC.getSelectionModel().clearSelection();

        //Reschedule tab
        tf_fnameR.clear();
        tf_lnameR.clear();
        cb_timeslotR.getSelectionModel().clearSelection();
        cb_tSlotR.getSelectionModel().clearSelection();
        dp_apptDR.getEditor().clear();
        dp_dobR.getEditor().clear();
        dp_newDR.getEditor().clear();
    }

    /**displays appointments sorted by date
     * @param actionEvent is the event that triggers this handler*/
    @FXML
    public void displayAppointmentsSortedByDate(ActionEvent actionEvent) {
        if (this.appointmentList.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
        } else {
            System.out.println("\n** List of appointments, ordered by date/time/provider.\n");
            Sort.sortedByDate(this.appointmentList);
            Iterator var1 = this.appointmentList.iterator();

            while(var1.hasNext()) {
                Appointment appointment = (Appointment)var1.next();
                System.out.println(appointment.toString());
            }

            System.out.println("**end of list**\n");
        }
    }

    /**displays appointments sorted by patient
     * @param actionEvent is the event that triggers this handler*/
    @FXML
    public void displayAppointmentsSortedByPatient(ActionEvent actionEvent) {
        if (this.appointmentList.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
        } else {
            Sort.sortByPatient(this.appointmentList);
            System.out.println("** List of appointments, ordered by first name/last name/dob.");
            Iterator var1 = this.appointmentList.iterator();

            while(var1.hasNext()) {
                Appointment appointment = (Appointment)var1.next();
                System.out.println(appointment.toString());
            }

            System.out.println("**end of list**\n");
        }
    }

    /**displays appointments sorted by county
     * @param actionEvent is the event that triggers this handler*/
    @FXML
    public void displayAppointmentsSortedByCounty(ActionEvent actionEvent) {
        if (this.appointmentList.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
        } else {
            Sort.sortByCounty(this.appointmentList);
            System.out.println("\n** List of appointments, ordered by county/date/time.\n");
            Iterator var1 = this.appointmentList.iterator();

            while(var1.hasNext()) {
                Appointment appointment = (Appointment)var1.next();
                System.out.println(appointment.toString());
            }

        }
    }

    /**displays billing statements
     * @param actionEvent is the event that triggers this handler*/
    @FXML
    public void displayBillingStatements(ActionEvent actionEvent) {
        if (this.appointmentList.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
        } else {
            List<Person> uniquePatients = new List();
            double[] patientBills = new double[this.appointmentList.size()];

            int i;
            for(i = 0; i < this.appointmentList.size(); ++i) {
                Appointment appointment = (Appointment)this.appointmentList.get(i);
                Person patient = appointment.getPatient();
                Provider provider = appointment.getProvider();
                double charge = 0.0;
                if (provider instanceof Doctor) {
                    Doctor doctor = (Doctor)provider;
                    double var10000;
                    switch (doctor.getSpecialty()) {
                        case FAMILY -> var10000 = 250.0;
                        case PEDIATRICIAN -> var10000 = 300.0;
                        case ALLERGIST -> var10000 = 350.0;
                        default -> var10000 = 0.0;
                    }

                    charge = var10000;
                } else if (provider instanceof Technician) {
                    charge = (double)provider.rate();
                }

                boolean patientExists = false;

                for(int j = 0; j < uniquePatients.size(); ++j) {
                    if (((Person)uniquePatients.get(j)).equals(patient)) {
                        patientBills[j] += charge;
                        patientExists = true;
                        break;
                    }
                }

                if (!patientExists) {
                    uniquePatients.add(patient);
                    patientBills[uniquePatients.size() - 1] = charge;
                }
            }

            Sort.patient(uniquePatients);
            System.out.println("\n** Billing statement ordered by patient. **");

            for(i = 0; i < uniquePatients.size(); ++i) {
                Person patient = (Person)uniquePatients.get(i);
                double totalDue = patientBills[i];
                System.out.printf("(%d) %s [due: $%.2f]\n", i + 1, patient.getProfile().toString(), totalDue);
            }

            System.out.println("** end of list **\n");

            while(!this.appointmentList.isEmpty()) {
                this.appointmentList.remove((Appointment)this.appointmentList.get(0));
            }

            while(!this.imagingList.isEmpty()) {
                this.imagingList.remove((Imaging)this.imagingList.get(0));
            }

        }
    }


    /**displays office appointments
     * @param actionEvent is the event that triggers this handler*/
    @FXML
    public void displayOfficeAppointments(ActionEvent actionEvent) {
        if (this.appointmentList.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
        } else {
            Sort.officeAppointment(this.appointmentList);
            System.out.println("** List of office appointments ordered by county/date/time.");
            Iterator var1 = this.appointmentList.iterator();

            while(var1.hasNext()) {
                Appointment officeAppointment = (Appointment)var1.next();
                PrintStream var10000 = System.out;
                String var10001 = officeAppointment.getDate().toString();
                var10000.println(var10001 + " " + officeAppointment.getTimeslot().toString() + " " + officeAppointment.getPatient().getProfile().toString() + " [" + officeAppointment.getProvider().toString() + "]");
            }

            System.out.println("** end of list **");
        }
    }
    /**displays imaging appointments
     * @param actionEvent is the event that triggers this handler*/
    @FXML
    public void displayImagingAppointments(ActionEvent actionEvent) {
        if (this.imagingList.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
        } else {
            Sort.imagingAppointment(this.imagingList);
            System.out.println("\n** List of radiology appointments ordered by county/date/time");
            Iterator var1 = this.imagingList.iterator();

            while(var1.hasNext()) {
                Imaging imagingAppointments = (Imaging)var1.next();
                System.out.println(imagingAppointments.toString());
            }

            System.out.println("**end of list**\n");
        }
    }

    /**displays credit
     * @param actionEvent is the event that triggers this handler*/
    @FXML
    public void displayExpectedCredit(ActionEvent actionEvent) {
        if (this.appointmentList.isEmpty()) {
            System.out.println("Schedule calendar is empty.");
        } else {
            List<Provider> uniqueProviders = new List();
            Iterator var2 = this.appointmentList.iterator();

            Iterator var6;
            while(var2.hasNext()) {
                Appointment appointment = (Appointment)var2.next();
                Provider provider = appointment.getProvider();
                boolean providerExists = false;
                var6 = uniqueProviders.iterator();

                while(var6.hasNext()) {
                    Provider existingProvider = (Provider)var6.next();
                    if (existingProvider.equals(provider)) {
                        providerExists = true;
                        break;
                    }
                }

                if (!providerExists) {
                    uniqueProviders.add(provider);
                }
            }

            Sort.provider(uniqueProviders);
            System.out.println("\n** Credit amount ordered by provider. **"); //use ta_output

            for(int i = 0; i < uniqueProviders.size(); ++i) {
                Provider provider = (Provider)uniqueProviders.get(i);
                double totalCredit = 0.0;
                var6 = this.appointmentList.iterator();

                while(var6.hasNext()) {
                    Appointment appointment = (Appointment)var6.next();
                    if (appointment.getProvider().equals(provider)) {
                        totalCredit += (double)provider.rate();
                    }
                }

                System.out.printf("(%d) %s [credit amount: $%.2f]\n", i + 1, provider.getProfile().toString(), totalCredit);
            }

            System.out.println("** end of list **\n");
        }
    }


}