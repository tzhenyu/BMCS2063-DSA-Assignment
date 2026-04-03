/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author zhenyu
 */
//import ListADT.Node;
//import ListADT.ListADT;
//import Clinic.Boundary.MedicineBoundary;
//public class Main {
//
//
//
//    public static void main(String[] args) {
////        Initialize Boundary
//        MedicineBoundary medicineBoundary = new MedicineBoundary();
//        
//        medicineBoundary.start();
////        //Playing with Nodes
////        Node<String> n1 = new Node<>("apple");
////        Node<String> n2 = new Node<>("banana");        
////        Node<String> n3 = new Node<>("cidar");        
////        
////        // Circular linked list
////        n1.setNextNode(n2);
////        n2.setNextNode(n3);
////        n3.setNextNode(n1);
////        
//////        //Print out data, will cause infinite loop
//////        Node<String> current = n1;
//////        
//////        while (current != null) { 
//////            System.out.println(current.getData());
//////            current = current.getNextNode();
//////        }
////        
////        // Prints out data, but will stop when loop back to start
////        Node<String> start = n1;
////        Node<String> current = start;
////        
////        do {
////            System.out.println(current.getData());
////            current = current.getNextNode();
////        } while (current != start);
////        
////        
////        //Playing with insert()
////        //Define object
////        ListADT<String> patient = new ListADT<>();
////        
////        patient.insert("your mama");
////        patient.insert("your baba");
////        
////        patient.display(); //someone implement this!
//    }
//}
package Clinic.Boundary;


import Clinic.Control.DoctorScheduleControl;
import Clinic.Entity.Doctor;
import Clinic.Entity.Patient;
import ListADT.ListADT;
import ListADT.ListADTInterface;
import Clinic.Boundary.PatientBoundary;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // =========================
        // STEP 1: Create Doctor List
        // =========================
        ListADTInterface<Doctor> doctorList = new ListADT<>();

        Doctor d1 = new Doctor("D001", "Alice", "Cardiology", "012", "a@mail.com", "", true);
        Doctor d2 = new Doctor("D002", "Bob", "Dental", "013", "b@mail.com", "", true);
        Doctor d3 = new Doctor("D003", "Charlie", "ENT", "014", "c@mail.com", "", true);

        doctorList.insert(d1);
        doctorList.insert(d2);
        doctorList.insert(d3);

        // =========================
        // STEP 2: Create Schedule
        // =========================
        ListADTInterface<Doctor>[] schedule = new ListADT[7];

        for (int i = 0; i < schedule.length; i++) {
            schedule[i] = new ListADT<>();
        }

        // =========================
        // STEP 3: Create Control
        // =========================
        DoctorScheduleControl control = new DoctorScheduleControl(doctorList, schedule);

        // =========================
        // STEP 4: Add Sample Availability
        // =========================
        control.addAvailability("D001", "10-11");
        control.addAvailability("D002", "10-11");
        control.addAvailability("D003", "11-12");

        // =========================
        // STEP 5: Interactive Patient Menu
        // =========================
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter your patient ID: ");
        String patientId = sc.nextLine();

        int choice = -1;
        while (choice != 0) {
            System.out.println("\n--- Patient Menu ---");
            System.out.println("1. View Doctor Schedule");
            System.out.println("2. View Available Slots");
            System.out.println("3. Book Appointment");
            System.out.println("4. Cancel Appointment");
            System.out.println("5. Change Appointment");
            System.out.println("6. View My Appointments");
            System.out.println("0. Exit");
            System.out.print("Choose: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    System.out.println(control.getScheduleDisplay());
                    break;
                case 2:
                    control.viewAvailableSlots(""); // show all slots
                    break;
                case 3:
                    System.out.print("Doctor ID: "); String docId = sc.nextLine();
                    System.out.print("Date (YYYY-MM-DD): "); String date = sc.nextLine();
                    System.out.print("Time (e.g., 10-11): "); String time = sc.nextLine();
                    boolean booked = control.bookAppointment(patientId, docId, date, time, "");
                    System.out.println("Booking " + (booked ? "successful" : "failed"));
                    break;
                case 4:
                    System.out.print("Date (YYYY-MM-DD): "); String cDate = sc.nextLine();
                    System.out.print("Time (e.g., 10-11): "); String cTime = sc.nextLine();
                    boolean canceled = control.cancelAppointment(patientId, cDate, cTime);
                    System.out.println("Cancel " + (canceled ? "successful" : "failed"));
                    break;
                case 5:
                    System.out.print("Old Date: "); String oldDate = sc.nextLine();
                    System.out.print("Old Time: "); String oldTime = sc.nextLine();
                    System.out.print("New Doctor ID: "); String newDoc = sc.nextLine();
                    System.out.print("New Date: "); String newDate = sc.nextLine();
                    System.out.print("New Time: "); String newTime = sc.nextLine();
                    boolean changed = control.changeAppointment(patientId, oldDate, oldTime, newDoc, newDate, newTime);
                    System.out.println("Change " + (changed ? "successful" : "failed"));
                    break;
                case 6:
                    control.viewMyAppointments(patientId);
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }

        sc.close();
    }
}