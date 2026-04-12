/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinic.Boundary;

/**
 *
 * @author Asus
 */

import Clinic.Control.AppointmentController;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import Clinic.Entity.Appointment;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class AppointmentBoundary {
    private AppointmentController controller;
    private Scanner scanner;
    private final String dateFormat = "dd-MM-yyyy";
    
    public AppointmentBoundary(AppointmentController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }
    
    public void start() {
        int choice;
        controller.loadFile();  // Load on start
        
        do {
            System.out.println("\n=== APPOINTMENT SYSTEM ===");
            System.out.println("1. Book Appointment");
            System.out.println("2. Search Appointment");
            System.out.println("3. Display All Appointments");
            System.out.println("4. Sort Appointments");
            System.out.println("5. Check Slot Availability");
            System.out.println("6. Sort Appointment in Latest format");
            System.out.println("0. Save & Exit");
            System.out.print("Choice: ");
       
            String input = scanner.nextLine();

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                choice = -1;
            }
            
            System.out.println("======================");
            
            
            switch (choice) {
                case 1: 
                    bookAppointment(); 
                    break;
                case 2: 
                    searchAppointment(); 
                    break;
                case 3: 
                    System.out.println(controller.displayAppointments()); 
                    break;
                case 4: 
                    System.out.println("\n=== UNSORTED APPOINTMENTS ===");
                    System.out.println(controller.displayAppointments());
                    controller.sortAppointments();
                    System.out.println("\n=== SORTED APPOINTMENTS ===");
                    System.out.println(controller.displayAppointments());
                    System.out.println("Appointments sorted by ID");
                    break;
                case 5: 
                    checkSlot(); 
                    break;
                    
                case 6:
                    controller.sortAppointmentsLatest();                        

                    System.out.println("=======================");
                    System.out.println("Latest Appointment:");
                    controller.popLatestAppointments();
                    System.out.println("=======================");
                    break;
                    
                case 0: 
                    controller.saveFile();
                    System.out.println("Goodbye!");
                    break;
                default: System.out.println("Invalid choice!");
            }
        } while (choice != 0);
    }
    
    private void bookAppointment() {
        System.out.println("\n=== BOOK APPOINTMENT ===");
        System.out.println("Slots: \n"
                + "1) 10-11\n"
                + "2) 11-12\n"
                + "3) 12-13\n"
                + "4) 13-14\n"
                + "5) 14-15\n"
                + "6) 15-16\n"
                + "7) 16-17\n");
        System.out.print("Choose timeslot [1-7]: ");
        String slot = scanner.nextLine().trim();
        while (!slot.matches("[1-7]")) {
            System.out.println("Invalid slot! Choose between 1 and 7.");
            slot = scanner.nextLine().trim();
        }
        
        String id = controller.generateNextAppointmentId();
        System.out.println("Generated Appointment ID: " + id);
        
        System.out.print("Patient ID : "); // should be connected with patient id stuff la
        String patientId = scanner.nextLine().trim().toUpperCase();
        
        if(!controller.isPatientExists(patientId)){
            System.out.println("Patient ID not found.Please register patient first.");
            return;
        }
        
        System.out.print("Date: ");
        String input = scanner.nextLine().trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        
        
        LocalDate dateStr;
        
        while (true) {
            try {
                dateStr = LocalDate.parse(input, formatter);
                break; 
            } catch (Exception e) {
                System.out.println("Invalid date format! Use dd-MM-yyyy.");
                System.out.print("Re-enter date: ");
                input = scanner.nextLine().trim();
            }
        }
        
        System.out.print("Purpose: ");
        String purpose = scanner.nextLine().trim();
        
        System.out.print("Remarks (from doctor): ");
        String remarks = scanner.nextLine().trim();
        
        if (controller.bookAppointment(id, patientId, dateStr, slot, purpose, remarks)) {
        System.out.println("Booking confirmed!");
        controller.saveFile();
        } else {
            System.out.println("Booking failed!");
        }
        
        
    }
    
    private void searchAppointment() {
        System.out.println("\n=== SEARCH APPOINTMENT ===");
        System.out.print("Appointment ID: ");
        
        String id = scanner.nextLine().trim();

        while (!id.matches("A\\d+")) {
            System.out.println("Invalid Appointment ID! Format: A followed by numbers (e.g. A001)");
            id = scanner.nextLine().trim();
        }
        
        Appointment found = controller.searchAppointment(id);
        if (found != null) {
            System.out.println("Found ! "+ found.toString());
        } else {
            System.out.println("Not found: " + id);
        }
    }
    
    private void checkSlot() {
        System.out.println("\n=== CHECK APPOINTMENTS BY DATE ===");

        System.out.print("Enter date (dd-MM-yyyy): ");
        String input = scanner.nextLine().trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);

        try {
            LocalDate date = LocalDate.parse(input, formatter);

            boolean hasAppointments = false;

            // Loop through all slots
            for (String slot : controller.getAllTimeSlots()) {
                String slotDisplay = controller.displayBySlot(date, slot);

                // displayBySlot returns a string even if no appointments, so we check the count
                if (!slotDisplay.contains("Total: 0")) {
                    System.out.println(slotDisplay);
                    hasAppointments = true;
                }
            }

            if (!hasAppointments) {
                System.out.println("No appointments found on " + date.format(formatter));
            }

        } catch (Exception e) {
            System.out.println("Invalid date! Please enter in dd-MM-yyyy format.");
        }
    }
}