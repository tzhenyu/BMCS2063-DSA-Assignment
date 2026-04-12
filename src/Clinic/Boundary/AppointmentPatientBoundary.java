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
 * PatientBoundary.java - Fully Fixed Version
 */

import java.util.Scanner;
import Clinic.Control.DoctorScheduleControl;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class AppointmentPatientBoundary {
    private DoctorScheduleControl controller;
    private Scanner sc = new Scanner(System.in);

    public AppointmentPatientBoundary(DoctorScheduleControl controller) {
        this.controller = controller;
    }

    public void menu(String patientId) {
        int choice;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        
        do {
            System.out.println("\n=== Patient Menu (" + patientId + ") ===");
            System.out.println("Week: " + controller.getWeekStart().format(df) + " - " + controller.getWeekEnd().format(df));
            System.out.println("1. View Available Slots");
            System.out.println("2. Book Appointment");
            System.out.println("3. View My Appointments");
            System.out.println("4. Cancel Appointment");
            System.out.println("5. Change Appointment");
            System.out.println("0. Exit");

            // VALIDATION ADDED - menu choice
            while (true) {
                System.out.print("Enter choice: ");
                if (sc.hasNextInt()) {
                    choice = sc.nextInt();
                    sc.nextLine();
                    if (choice >= 0 && choice <= 5) break;
                } else {
                    sc.nextLine(); // clear invalid input
                }
                System.out.println("Invalid choice. Enter 0–5 only.");
            }

            switch (choice) {
                case 1:
                    controller.viewAvailableSlots();
                    System.out.println("\nCopy exact date/time from above to book!");
                    break;

                case 2:
                    System.out.println("\n=== Book Appointment ===");
                    controller.viewAvailableSlots();
                    // VALIDATION ADDED - date
                    String date;
                    while (true) {
                        System.out.print("Date (dd-MM-yyyy e.g. 03-04-2026): ");
                        date = sc.nextLine();
                        if (isValidDate(date)) break;
                        System.out.println("Invalid date format!");
                    }

                    // VALIDATION ADDED - time slot (STRICT)
                    String time;
                    while (true) {
                        System.out.print("Time Slot (10-11 ... 16-17): ");
                        time = sc.nextLine();
                        if (isValidTimeSlot(time)) break;
                        System.out.println("Invalid slot! Use available clinic slots only.");
                    }

                    if (controller.bookAppointment(patientId, date, time)) {
                        System.out.println("Booked successfully!");
                    } else {
                        System.out.println("Failed - slot empty/full or wrong format");
                    }
                    break;

                case 3:
                    controller.viewMyAppointments(patientId);
                    break;

                case 4:
                    System.out.println("\n=== Cancel (view your appts first) ===");
                    controller.viewMyAppointments(patientId);

                    // VALIDATION ADDED - date
                    String d;
                    while (true) {
                        System.out.print("Date (dd-MM-yyyy): ");
                        d = sc.nextLine();
                        if (isValidDate(d)) break;
                        System.out.println("Invalid date format!");
                    }

                    // VALIDATION ADDED - time
                    String t;
                    while (true) {
                        System.out.print("Time Slot (10-11 ... 16-17): ");
                        t = sc.nextLine();
                        if (isValidTimeSlot(t)) break;
                        System.out.println("Invalid slot!");
                    }

                    if (controller.cancelAppointment(patientId, d, t)) {
                        System.out.println("Cancelled!");
                    } else {
                        System.out.println("Not found");
                    }
                    break;

                case 5:
                    System.out.println("\n=== Change Appointment ===");
                    controller.viewMyAppointments(patientId);

                    // VALIDATION ADDED - old date
                    String oldDate;
                    while (true) {
                        System.out.print("Old Date (dd-MM-yyyy): ");
                        oldDate = sc.nextLine();
                        if (isValidDate(oldDate)) break;
                        System.out.println("Invalid date format!");
                    }

                    // VALIDATION ADDED - old time
                    String oldTime;
                    while (true) {
                        System.out.print("Old Time (10-11 ... 16-17): ");
                        oldTime = sc.nextLine();
                        if (isValidTimeSlot(oldTime)) break;
                        System.out.println("Invalid slot!");
                    }

                    controller.viewAvailableSlots();

                    // VALIDATION ADDED - new date
                    String newDate;
                    while (true) {
                        System.out.print("New Date (dd-MM-yyyy): ");
                        newDate = sc.nextLine();
                        if (isValidDate(newDate)) break;
                        System.out.println("Invalid date format!");
                    }

                    // VALIDATION ADDED - new time
                    String newTime;
                    while (true) {
                        System.out.print("New Time (10-11 ... 16-17): ");
                        newTime = sc.nextLine();
                        if (isValidTimeSlot(newTime)) break;
                        System.out.println("Invalid slot!");
                    }

                    if (controller.changeAppointment(patientId, oldDate, oldTime, newDate, newTime)) {
                        System.out.println("Changed successfully!");
                    } else {
                        System.out.println("Failed - old not found or new unavailable");
                    }
                    break;
            }
        } while (choice != 0);
    }

    // VALIDATION ADDED - date format check
    private boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // VALIDATION ADDED - strict clinic time slots
    private boolean isValidTimeSlot(String slot) {
        String[] validSlots = {"10-11", "11-12", "12-13", "13-14", "14-15", "15-16", "16-17"};
        for (String s : validSlots) {
            if (s.equals(slot)) return true;
        }
        return false;
    }
}