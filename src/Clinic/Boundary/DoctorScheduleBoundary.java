/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinic.Boundary;

/**
 *
 * @author ASUS
 */

import Clinic.Control.DoctorScheduleControl;
import java.util.Scanner;
import Clinic.Entity.Doctor;

public class DoctorScheduleBoundary {

    private DoctorScheduleControl control;
    private Scanner scanner;

    public DoctorScheduleBoundary(DoctorScheduleControl control) {
        this.control = control;
        scanner = new Scanner(System.in);
    }

    public void start() {
        int choice;

        do {
            System.out.println("\n=== Doctor Schedule Menu ===");
            System.out.println("1. View Schedule");
            System.out.println("2. Mark Availability");
            System.out.println("3. Remove Availability");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // clear buffer

            switch (choice) {
                case 1:
                    displaySchedule();
                    break;
                case 2:
                    markAvailability();
                    break;
                case 3:
                    removeAvailability();
                    break;
            }

        } while (choice != 0);
    }

    // DISPLAY ONLY
    private void displaySchedule() {
        String result = control.getScheduleDisplay();
        System.out.println(result);
    }

    // GET INPUT → PASS TO CONTROL
    private void markAvailability() {
        System.out.print("Enter Doctor ID: ");
        String doctorId = scanner.nextLine();

        System.out.print("Enter Time Slot (e.g. 10-11): ");
        String slot = scanner.nextLine();

        boolean success = control.addAvailability(doctorId, slot);

        if (success) {
            System.out.println("Availability added.");
        } else {
            System.out.println("Failed (slot full / doctor not found / duplicate).");
        }
    }

    // GET INPUT → PASS TO CONTROL
    private void removeAvailability() {
        System.out.print("Enter Doctor ID: ");
        String doctorId = scanner.nextLine();

        System.out.print("Enter Time Slot: ");
        String slot = scanner.nextLine();

        boolean success = control.removeAvailability(doctorId, slot);

        if (success) {
            System.out.println("Availability removed.");
        } else {
            System.out.println("Failed (not found).");
        }
    }
}
