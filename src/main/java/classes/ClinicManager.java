package classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Scanner;
import util.Date;
import util.List;
import util.Sort;

public class ClinicManager implements Commands {
    private final Scanner scanner;
    private final List<Appointment> appointmentList = new List();
    private final List<Imaging> imagingList = new List();
    private final List<Provider> providers = new List();
    private final List<Technician> technicians = new List();
    private int technicianIndex = 0;

    public ClinicManager() {
        this.scanner = new Scanner(System.in);
        this.loadProviders();
        this.technicianRotation();
        System.out.println("Clinic Manager is running.");
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
            System.out.println("Error: File not found!");
        }

    }

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
        } else if (tokens[0].equals("T")) {
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
            System.out.println("Invalid provider type.");
        }

    }

    private void scheduleAppointment(String[] tokens) {
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

    }

    public void imagingAppointment(String[] tokens) {
        try {
            if (tokens.length < 6) {
                System.out.println("Error: Insufficient data to schedule the appointment");
                return;
            }

            String date = tokens[1];
            String timeSlotId = tokens[2];
            String firstName = tokens[3];
            String lastName = tokens[4];
            String dob = tokens[5];
            String imagingServices = tokens[6];
            if (!this.isValidTimeslot(timeSlotId)) {
                System.out.println(timeSlotId + " is not a valid time slot.");
                return;
            }

            Date appointmentDate = new Date(date);
            Date bday = new Date(dob);
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

            if (!bday.isValid()) {
                System.out.println("Error: Patient date of birth: " + String.valueOf(bday) + " is not a valid calendar date.");
                return;
            }

            if (bday.isTodayOrAfter()) {
                System.out.println("Error: Patient date of birth: " + String.valueOf(bday) + " is today or a future date.");
                return;
            }

            if (!this.isValidImagingService(imagingServices)) {
                System.out.println("Error: " + imagingServices + " - imaging service not provided.");
                return;
            }

            Timeslot selectedTimeslot = this.getTimeslotFromString(timeSlotId);
            if (selectedTimeslot == null) {
                System.out.println("Error: Invalid Timeslot");
                return;
            }

            Radiology imagingSrv = Radiology.valueOf(imagingServices.toUpperCase());
            Technician availableTech = this.findAvailableTechnician(selectedTimeslot, imagingSrv);
            if (availableTech == null) {
                System.out.println("Cannot find technician at all locations for" + imagingServices);
                return;
            }

            Profile patientProfile = new Profile(firstName, lastName, bday);
            Iterator var14 = this.imagingList.iterator();

            Imaging appointment;
            while(var14.hasNext()) {
                appointment = (Imaging)var14.next();
                if (appointment.getDate().equals(appointmentDate) && appointment.getTimeslot().equals(selectedTimeslot) && appointment.getPatient().getProfile().equals(patientProfile)) {
                    System.out.println("Error: an appointment with the same patient exist.");
                    return;
                }
            }

            Person patient = new Person(patientProfile);
            appointment = new Imaging(appointmentDate, selectedTimeslot, patient, availableTech, imagingSrv);
            this.imagingList.add(appointment);
            System.out.println(appointment.toString() + " booked.");
        } catch (Exception var16) {
            System.out.println("Missing tokens");
        }

    }

    public void cancelAppointment(String[] tokens) {
        try {
            if (tokens.length < 5) {
                System.out.println("Error: Missing data tokens");
                return;
            }

            String dateStr = tokens[1];
            String timeslotID = tokens[2];
            String firstName = tokens[3];
            String lastName = tokens[4];
            String dob = tokens[5];
            Date appointmentDate = new Date(dateStr);
            Date birthDay = new Date(dob);
            Profile patientProfile = new Profile(firstName, lastName, birthDay);
            Person patient = new Person(patientProfile);
            Timeslot selectedTimeslot = this.getTimeslotFromString(timeslotID);
            if (selectedTimeslot == null) {
                System.out.println(timeslotID + " is not a valid time slot.");
                return;
            }

            boolean officeAppointmentCanceled = this.removeOfficeAppointment(appointmentDate, selectedTimeslot, patient);
            boolean imagingAppointmentCanceled = this.removeImagingAppointment(appointmentDate, selectedTimeslot, patient);
            PrintStream var10000;
            String var10001;
            if (!officeAppointmentCanceled && !imagingAppointmentCanceled) {
                var10000 = System.out;
                var10001 = appointmentDate.toString();
                var10000.println(var10001 + " " + selectedTimeslot.toString() + " - " + patientProfile.toString() + " does not exist.");
            } else {
                var10000 = System.out;
                var10001 = appointmentDate.toString();
                var10000.println(var10001 + " " + selectedTimeslot.toString() + " - " + patientProfile.toString() + " appointment has been canceled.");
            }
        } catch (Exception var14) {
            System.out.println("Error:");
        }

    }

    public void rescheduleAppointment(String[] tokens) {
        try {
            if (tokens.length < 6) {
                System.out.println("Error: Missing data tokens");
                return;
            }

            String dateStr = tokens[1];
            String timeSlotId = tokens[2];
            String firstName = tokens[3];
            String lastName = tokens[4];
            String dob = tokens[5];
            String newTimeslotId = tokens[6];
            Date appointmentDate = new Date(dateStr);
            Date birthday = new Date(dob);
            Profile patientProfile = new Profile(firstName, lastName, birthday);
            Person patient = new Person(patientProfile);
            Timeslot previousTimeslot = this.getTimeslotFromString(timeSlotId);
            Timeslot newTimeslot = this.getTimeslotFromString(newTimeslotId);
            if (previousTimeslot == null) {
                System.out.println(timeSlotId + " is not a valid time slot.");
                return;
            }

            if (newTimeslot == null) {
                System.out.println(newTimeslotId + " is not a valid time slot.");
                return;
            }

            Appointment originalAppointment = this.findOfficeAppointment(appointmentDate, previousTimeslot, patient);
            if (originalAppointment == null) {
                System.out.println("No matching appointment to reschedule");
                return;
            }

            if (this.findAppointmentByPatientAndTimeslot(appointmentDate, newTimeslot, patient)) {
                PrintStream var10000 = System.out;
                String var10001 = String.valueOf(patientProfile);
                var10000.println(var10001 + " has an existing appointment at " + String.valueOf(appointmentDate) + " " + String.valueOf(newTimeslot));
                return;
            }

            if (this.isProviderAvailableAtTimeslot(originalAppointment.getProvider(), appointmentDate, newTimeslot)) {
                originalAppointment.setTimeslot(newTimeslot);
                System.out.println("Rescheduled to " + originalAppointment.toString());
            } else {
                System.out.println("The provider is not available");
            }
        } catch (Exception var15) {
            System.out.println("Error: ");
        }

    }

    private boolean isValidImagingService(String service) {
        try {
            Radiology.valueOf(service.toUpperCase());
            return true;
        } catch (IllegalArgumentException var3) {
            return false;
        }
    }

    private Technician findAvailableTechnician(Timeslot timeslot, Radiology imagingService) {
        int startIndex = this.technicianIndex;
        boolean technicianFound = false;

        do {
            Technician technician = (Technician)this.technicians.get(this.technicianIndex);
            this.technicianIndex = (this.technicianIndex + 1) % this.technicians.size();
            if (this.isTechnicianAvailable(technician, timeslot, imagingService)) {
                technicianFound = true;
                return technician;
            }
        } while(this.technicianIndex != startIndex);

        return null;
    }

    private Appointment findOfficeAppointment(Date appointmentDate, Timeslot timeslot, Person patient) {
        Iterator var4 = this.appointmentList.iterator();

        Appointment appointment;
        do {
            if (!var4.hasNext()) {
                return null;
            }

            appointment = (Appointment)var4.next();
        } while(!appointment.getDate().equals(appointmentDate) || !appointment.getTimeslot().equals(timeslot) || !appointment.getPatient().equals(patient));

        return appointment;
    }

    private boolean isProviderAvailableAtTimeslot(Provider provider, Date appointmentDate, Timeslot timeslot) {
        Iterator var4 = this.appointmentList.iterator();

        Appointment appointment;
        do {
            if (!var4.hasNext()) {
                return true;
            }

            appointment = (Appointment)var4.next();
        } while(!appointment.getProvider().equals(provider) || !appointment.getDate().equals(appointmentDate) || !appointment.getTimeslot().equals(timeslot));

        return false;
    }

    private boolean findAppointmentByPatientAndTimeslot(Date appointmentDate, Timeslot timeslot, Person patientProfile) {
        Iterator var4 = this.appointmentList.iterator();

        Appointment appointment;
        do {
            if (!var4.hasNext()) {
                return false;
            }

            appointment = (Appointment)var4.next();
        } while(!appointment.getDate().equals(appointmentDate) || !appointment.getTimeslot().equals(timeslot) || !appointment.getPatient().equals(patientProfile));

        return true;
    }

    private boolean isTechnicianAvailable(Technician technician, Timeslot timeslot, Radiology imagingService) {
        Iterator var4 = this.imagingList.iterator();

        Imaging imagingAppointment;
        do {
            if (!var4.hasNext()) {
                return true;
            }

            imagingAppointment = (Imaging)var4.next();
        } while(!imagingAppointment.getProvider().getProfile().equals(technician.getProfile()) || !imagingAppointment.getTimeslot().equals(timeslot) || !imagingAppointment.getRoom().equals(imagingService));

        return false;
    }

    private boolean removeImagingAppointment(Date appointmentDate, Timeslot timeslot, Person patientProfile) {
        for(int i = 0; i < this.imagingList.size(); ++i) {
            Imaging imagingAppointment = (Imaging)this.imagingList.get(i);
            if (imagingAppointment.getDate().equals(appointmentDate) && imagingAppointment.getTimeslot().equals(timeslot) && imagingAppointment.getPatient().equals(patientProfile)) {
                this.imagingList.remove(imagingAppointment);
                return true;
            }
        }

        return false;
    }

    private boolean removeOfficeAppointment(Date appointmentDate, Timeslot timeslot, Person patientProfile) {
        for(int i = 0; i < this.appointmentList.size(); ++i) {
            Appointment appointment = (Appointment)this.appointmentList.get(i);
            if (appointment.getDate().equals(appointmentDate) && appointment.getTimeslot().equals(timeslot) && appointment.getPatient().equals(patientProfile)) {
                this.appointmentList.remove(appointment);
                return true;
            }
        }

        return false;
    }

    private boolean technicianHasAppointmentAtTimeslot(Technician technician, Timeslot timeslot) {
        Iterator var3 = this.imagingList.iterator();

        Imaging appointment;
        do {
            if (!var3.hasNext()) {
                return false;
            }

            appointment = (Imaging)var3.next();
        } while(!appointment.getProvider().equals(technician) || !appointment.getTimeslot().equals(timeslot));

        return true;
    }

    private void displayProviders() {
        System.out.println("Providers loaded to the list.");
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

    private void technicianRotation() {
        System.out.println("Rotation list for the technicians.");
        StringBuilder rotationList = new StringBuilder();

        for(int i = 0; i < this.technicians.size(); ++i) {
            Technician tech = (Technician)this.technicians.get(i);
            rotationList.append(tech.getProfile().getFirstName()).append(" ").append(tech.getProfile().getLastName()).append(" ").append(" (").append(tech.getLocation().name()).append(")");
            if (i < this.technicians.size() - 1) {
                rotationList.append(" --> ");
            }
        }

        System.out.println(rotationList);
    }

    public void displayAppointmentsSortedByDate() {
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

    public void displayAppointmentsSortedByPatient() {
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

    public void displayAppointmentsSortedByCounty() {
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

    public void displayBillingStatements() {
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

    public void displayOfficeAppointments() {
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

    public void displayImagingAppointments() {
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

    public void displayExpectedCredit() {
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
            System.out.println("\n** Credit amount ordered by provider. **");

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

    public void processCommand(String commandLine) {
        if (!commandLine.equals("Q")) {
            String[] tokens = commandLine.split(",");
            switch (tokens[0].trim()) {
                case "D" -> this.scheduleAppointment(tokens);
                case "T" -> this.imagingAppointment(tokens);
                case "C" -> this.cancelAppointment(tokens);
                case "R" -> this.rescheduleAppointment(tokens);
                case "PA" -> this.displayAppointmentsSortedByDate();
                case "PP" -> this.displayAppointmentsSortedByPatient();
                case "PL" -> this.displayAppointmentsSortedByCounty();
                case "PS" -> this.displayBillingStatements();
                case "PO" -> this.displayOfficeAppointments();
                case "PI" -> this.displayImagingAppointments();
                case "PC" -> this.displayExpectedCredit();
                case "Q" -> this.stop();
                default -> System.out.println("Invalid command!");
            }

        }
    }

    private boolean isValidTimeslot(String timeslotID) {
        boolean var10000;
        switch (timeslotID) {
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
            case "10":
            case "11":
            case "12":
                var10000 = true;
                break;
            default:
                var10000 = false;
        }

        return var10000;
    }

    private boolean isProviderAvailable(Provider provider, Timeslot timeslot) {
        Iterator var3 = this.appointmentList.iterator();

        Appointment existingAppointment;
        do {
            if (!var3.hasNext()) {
                return true;
            }

            existingAppointment = (Appointment)var3.next();
        } while(!existingAppointment.getProvider().equals(provider) || !existingAppointment.getTimeslot().equals(timeslot));

        PrintStream var10000 = System.out;
        String var10001 = provider.toString();
        var10000.println(var10001 + " is not available at " + String.valueOf(timeslot));
        return false;
    }

    private Doctor findProviderByNpl(String npl) {
        Iterator var2 = this.providers.iterator();

        while(var2.hasNext()) {
            Provider provider = (Provider)var2.next();
            if (provider instanceof Doctor doctor) {
                if (doctor.getNpl().equals(npl)) {
                    return doctor;
                }
            }
        }

        return null;
    }

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

    public void run() {
        while(true) {
            String commandLine = this.scanner.nextLine().trim();
            if (!commandLine.isEmpty()) {
                this.processCommand(commandLine);
                if (commandLine.equals("Q")) {
                    this.stop();
                    return;
                }
            }
        }
    }

    public void stop() {
        System.out.println("Clinic Manager terminated.");
        this.scanner.close();
    }
}