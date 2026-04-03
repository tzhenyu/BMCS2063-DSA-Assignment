/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinic.Boundary;

/**
 *
 * @author ASUS
 */

import java.util.Scanner;
import Clinic.Control.DoctorScheduleControl;

public class PatientBoundary {

    private DoctorScheduleControl controller;
    private Scanner sc = new Scanner(System.in);

    public PatientBoundary(DoctorScheduleControl controller) {
        this.controller = controller;
    }

    public void menu(String patientId) {

        int choice;

        do {
            System.out.println("\n=== Patient Menu ===");
            System.out.println("1. View Available Slots");
            System.out.println("2. Book Appointment");
            System.out.println("3. View My Appointments");
            System.out.println("4. Cancel Appointment");
            System.out.println("5. Change Appointment");
            System.out.println("0. Exit");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    controller.viewAvailableSlots("");
                    break;

                case 2:
                    System.out.print("Doctor ID: ");
                    String doc = sc.nextLine();

                    System.out.print("Date: ");
                    String date = sc.nextLine();

                    System.out.print("Time (e.g. 10-11): ");
                    String time = sc.nextLine();

                    if (controller.bookAppointment(patientId, doc, date, time, "")) {
                        System.out.println("Booking successful!");
                    } else {
                        System.out.println("Booking failed!");
                    }
                    break;

                case 3:
                    controller.viewMyAppointments(patientId);
                    break;

                case 4:
                    System.out.print("Date: ");
                    String d = sc.nextLine();

                    System.out.print("Time: ");
                    String t = sc.nextLine();

                    if (controller.cancelAppointment(patientId, d, t)) {
                        System.out.println("Cancelled!");
                    }
                    break;

                case 5:
                    System.out.print("Old Date: ");
                    String od = sc.nextLine();

                    System.out.print("Old Time: ");
                    String ot = sc.nextLine();

                    System.out.print("New Doctor: ");
                    String nd = sc.nextLine();

                    System.out.print("New Date: ");
                    String ndt = sc.nextLine();

                    System.out.print("New Time: ");
                    String nt = sc.nextLine();

                    if (controller.changeAppointment(patientId, od, ot, nd, ndt, nt)) {
                        System.out.println("Changed!");
                    }
                    break;
            }

        } while (choice != 0);
    }
}
