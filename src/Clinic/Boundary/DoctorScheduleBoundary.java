/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinic.Boundary;

/**
 *
 * @author ASUS
 */

/*
 * DoctorScheduleBoundary.java - Fully Fixed Version
 */

import Clinic.Control.DoctorScheduleControl;
import Clinic.Entity.Doctor;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class DoctorScheduleBoundary {
    private DoctorScheduleControl control;
    private Scanner scanner;

    public DoctorScheduleBoundary(DoctorScheduleControl control) {
        this.control = control;
        scanner = new Scanner(System.in);
    }

    public void start() {
        int choice = 0;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        
        do {
            System.out.println("\n=== Doctor Schedule Menu ===");
            System.out.println("Week: " + control.getWeekStart().format(df) + " - " + control.getWeekEnd().format(df));
            System.out.println("1. View Schedule");
            System.out.println("2. Mark Availability");
            System.out.println("3. Remove Availability");
            System.out.println("0. Exit");
            System.out.print("Choice: ");

            // Handle invalid input
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input!");
                scanner.nextLine();
                continue;
            }

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: displaySchedule(); break;
                case 2: markAvailability(); break;
                case 3: removeAvailability(); break;
            }
        } while (choice != 0);
    }

    private void displaySchedule() {
        System.out.println(control.getScheduleDisplay());
    }

    private void markAvailability() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        
        System.out.println("\n=== Mark Availability ===");
        System.out.println("This Week: " + control.getWeekStart().format(df) + " - " + control.getWeekEnd().format(df));
        System.out.println("Doctors: ");
        for (Doctor d : control.getDoctorList()) {
            System.out.println("  " + d.getDoctorId() + ": " + d.getName());
        }

        System.out.print("Date (dd-MM-yyyy e.g. 03-04-2026): ");
        String date = scanner.nextLine();

        if (!isValidDate(date)) {
            System.out.println("Invalid date format!");
            return;
        }

        System.out.print("Time Slot (10-11): ");
        String slot = scanner.nextLine();

        if (!isValidTimeSlot(slot)) {
            System.out.println("Invalid time slot!");
            return;
        }

        System.out.print("Doctor ID: ");
        String doctorId = scanner.nextLine();

        if (!isValidDoctor(doctorId)) {
            System.out.println("Invalid doctor ID!");
            return;
        }

        if (control.addAvailability(doctorId, date, slot)) {
            System.out.println("Added " + doctorId + " to " + date + " " + slot);
        } else {
            System.out.println("Failed - slot full or already booked");
        }
    }

    private void removeAvailability() {
        System.out.println("\n=== Remove Availability ===");

        System.out.print("Date (dd-MM-yyyy): ");
        String date = scanner.nextLine();

        if (!isValidDate(date)) {
            System.out.println("Invalid date format!");
            return;
        }

        System.out.print("Time Slot: ");
        String slot = scanner.nextLine();

        if (!isValidTimeSlot(slot)) {
            System.out.println("Invalid time slot!");
            return;
        }

        System.out.print("Doctor ID: ");
        String doctorId = scanner.nextLine();

        if (!isValidDoctor(doctorId)) {
            System.out.println("Invalid doctor ID!");
            return;
        }

        if (control.removeAvailability(doctorId, date, slot)) {
            System.out.println("Removed");
        } else {
            System.out.println("Not found");
        }
    }
    
    private boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private boolean isValidTimeSlot(String slot) {
        String[] validSlots = {"10-11", "11-12", "12-13", "13-14", "14-15", "15-16", "16-17"};
        for (String s : validSlots) {
            if (s.equals(slot)) return true;
        }
        return false;
    }
    
    private boolean isValidDoctor(String doctorId) {
        for (Doctor d : control.getDoctorList()) {
            if (d.getDoctorId().equals(doctorId)) return true;
        }
        return false;
    }
}
