/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author zhenyu
 */
import Clinic.Boundary.MedicineBoundary;
import Clinic.Boundary.PatientBoundary;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        MedicineBoundary medicineBoundary = new MedicineBoundary();
        PatientBoundary patientBoundary = new PatientBoundary();
        
        clearConsole();
        displayHeader();
        
        while(true) {
            System.out.println("[1] Medicine Management");
            System.out.println("[2] Patient Management");
            System.out.println("[3] Appointments/Availability");
            System.out.println("[4] Exit");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // reading choice
                
            switch (choice) {
                case 1 -> medicineBoundary.start();
                case 2 -> patientBoundary.menu();
                case 3 -> {System.out.println("gurt");}
                case 4 -> {return;}
                default -> System.out.println("Invalid option.");
            }

        }
    }
    
    public static void displayHeader() {
        System.out.println("====================================================");
        System.out.println("         Welcome to TAR UMT Clinic Management");
        System.out.println("====================================================");
        System.out.println();
    }
    
    public static void clearConsole() {
        for (int i = 0; i < 50; i++) { // Print 50 blank lines
            System.out.println();
        }
    }
}

