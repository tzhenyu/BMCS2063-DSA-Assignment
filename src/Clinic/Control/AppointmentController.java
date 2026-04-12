/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinic.Control;

/**
 *
 * @author Asus
 */

import Clinic.Entity.Appointment;
import ListADT.ListADT;
import StorageHandler.FileHandler;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.Stack;

public class AppointmentController implements FileHandler {
    private PatientController patientController;
    private static final String FILENAME = "src/appointment.csv";
    private ListADT<Appointment> appointmentList;
    private static final int MAX_PER_SLOT = 7;
    private static final String[] SLOTS = {"1", "2", "3", "4", "5", "6", "7"};
    private final String dateFormat = "dd-MM-yyyy";
    private Stack<Appointment> appointmentListStack;
    
    public AppointmentController(PatientController patientController) {
        this.patientController = patientController;
        appointmentList = new ListADT<>();
    }
    
    public boolean isPatientExists(String patientId) {
        return patientController.isPatientExists(patientId);
    }
    
    public String[] getAllTimeSlots() {
        return new String[] {
            "10-11",
            "11-12",
            "12-13",
            "13-14",
            "14-15",
            "15-16",
            "16-17"
        };
    }
    
    private String mapSlotNumberToTime(String slotNumber) {
        switch(slotNumber) {
            case "1": return "10-11";
            case "2": return "11-12";
            case "3": return "12-13";
            case "4": return "13-14";
            case "5": return "14-15";
            case "6": return "15-16";
            case "7": return "16-17";
            default: return "";
        }
    }
    
    // booking the damn appointment
    public boolean bookAppointment(String apptId, String patientId, LocalDate date, String slotNumber, String purpose, String remarks) {

        // validate patient ID exists
        if (!patientController.isPatientExists(patientId)) {
            System.out.println("Patient ID not found. Please register patient first.");
            return false;
        }
        // Validate slot number first
        if (!isValidSlot(slotNumber)) {
            return false;
        }

        // Convert slot number → time
        String timeSlot = mapSlotNumberToTime(slotNumber);
        if (timeSlot.isEmpty()) {
            System.out.println("Invalid slot mapping.");
            return false;
        }

        // Check slot capacity
        if (isSlotFull(date, timeSlot)) {
            System.out.println("Slot full! (" + getSlotCount(date, timeSlot) + "/" + MAX_PER_SLOT + ")");
            return false;
        }
        
        for (Appointment a : appointmentList) {
            if (a.getPatientId().equals(patientId) &&
                a.getDate().equals(date) &&
                a.getTime().equals(timeSlot)) {

                System.out.println("Patient already has an appointment at this time!");
                return false;
            }
        }

        Appointment appt = new Appointment(apptId, patientId, date, timeSlot, purpose, remarks);
        appointmentList.insert(appt);
        saveFile();

        System.out.println("Booked " + timeSlot + " (" + getSlotCount(date, timeSlot) + "/" + MAX_PER_SLOT + ")");
        return true;
    }
    
    public String generateNextAppointmentId() {
        int max = 0;

        for (Appointment appointment : appointmentList) {
            String appointmentId = appointment.getAppointmentId();

            if (appointmentId != null && appointmentId.matches("A\\d{4}")) {
                int number = Integer.parseInt(appointmentId.substring(1));
                if (number > max) {
                    max = number;
                }
            }
        }

        return String.format("A%04d", max + 1);
    }
        
    // search and show the appointment
    public Appointment searchAppointment(String apptId) {
        return appointmentList.search(new Appointment(apptId, "", LocalDate.now() , "", "", ""));
    }
    
    public String displayAppointments() {
        return appointmentList.display();
    }
    
    public void sortAppointmentsLatest() {
    // Sort by date
        appointmentList.sort((a, b) -> a.getDate().compareTo(b.getDate()));

        appointmentListStack = new Stack<>();
            for (int i = appointmentList.size() - 1; i >= 0; i--) {
                appointmentListStack.push(appointmentList.getEntry(i));
            }
    }
    
    public void popLatestAppointments() {
        
        if (appointmentListStack == null || appointmentListStack.isEmpty()) {
            System.out.println("No appointments in stack.");
            return;
        }

        for (int i = appointmentListStack.size(); i > 0; i--) {
            Appointment appt = appointmentListStack.pop();
            System.out.println(appt);
        }
    }
    
    public String displayBySlot(LocalDate date, String slot) {
        String formattedDate = date.format(DateTimeFormatter.ofPattern(dateFormat));
        String result = "\n=== " + formattedDate + " " + slot + " ===\n";

        int count = 0;

        for (Appointment a : appointmentList) {
            if (a.getDate().equals(date) && a.getTime().equals(slot)) {
                result += a + "\n";
                count++;
            }
        }

        result += "Total: " + count + "/" + MAX_PER_SLOT + "\n";
        return result;
    }
    
    // sorting the appointments
    public void sortAppointments() {
        appointmentList.sort();  // By appointmentId
    }
    
    public int getTotalAppointments() {
        return appointmentList.size();
    }
    
    //saving and loading files
    @Override
    public void loadFile() {
        System.out.println("Hold up! file loading");
        appointmentList.clear();
        
        
        File file = new File(FILENAME);
        if (!file.exists()) {
            System.out.println("No existing appointments file");
            return;
        }
        
        
        Appointment appt = new Appointment();
        try (Scanner scanner = new Scanner(file)) {
            if (scanner.hasNextLine()){ 
                scanner.nextLine();  // Header
            
            }
            
            while (scanner.hasNextLine()) {
                String[] fields = scanner.nextLine().split(";", -1);
                Appointment loaded = appt.fromCSV(fields);
                if (loaded != null){ 
                    appointmentList.insert(loaded);
                }
            }
            System.out.println("Loaded " + appointmentList.size() + " appointments");
        } catch (Exception e) {
            System.out.println("Load failed: " + e.getMessage());
        }
    }
    
    @Override
    public void saveFile() {
        try (PrintWriter writer = new PrintWriter(new File(FILENAME))) {
            writer.println("APPOINTMENT ID;PATIENT ID;DATE;TIME;PURPOSE;REMARKS");
            for (Appointment appt : appointmentList) {
                writer.println(appt.toCSV());
            }
            System.out.println("Saved " + appointmentList.size() + " appointments");
        } catch (IOException e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }
    
    // custom slot helpers
    private boolean isValidSlot(String slot) {
        for (String s : SLOTS) {
            if (s.equals(slot)){ 
                return true;
            }
        }
        System.out.println("Valid slots: " + String.join(", ", SLOTS));
        return false;
    }
    
    public boolean isSlotFull(LocalDate date, String slot) {
        return getSlotCount(date, slot) >= MAX_PER_SLOT;
    }
    
    public int getSlotCount(LocalDate date, String slot) {
        int count = 0;  
        for (Appointment a : appointmentList) {
            if (a.getDate().equals(date) && a.getTime().equals(slot)){
                count++;
            }
        }
        return count;
    }
    
    //for Medical Report uses
    public String getAppointmentsByPatientId(String patientId) {
        StringBuilder result = new StringBuilder();

        for (Appointment a : appointmentList) {
            if (a.getPatientId().equalsIgnoreCase(patientId)) {
                result.append(a.toString());
            }
        }

        if (result.length() == 0) {
            return "No appointments found.\n";
        }

        return result.toString();
    }
}