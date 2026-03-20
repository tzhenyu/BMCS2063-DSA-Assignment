/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinic.Boundary;
import Clinic.Control.MedicineController;
import Clinic.Entity.Medicine;
import java.util.Scanner;
/**
 *
 * @author zhenyu
 */
public class MedicineBoundary {
    private MedicineController controller;
    private Scanner scanner;
    
    public MedicineBoundary() {
        controller = new MedicineController();
        scanner = new Scanner(System.in);
    }
    
    public void start() {
        while (true) {
            System.out.println("\n--- Medicine Management ---");
            System.out.println("1. Add Medicine");
            System.out.println("2. Delete Medicine");
            System.out.println("3. List All Medicines");
            System.out.println("4. Search Medicine");
            System.out.println("5. Sort Medicines");
            System.out.println("6. Exit");
            System.out.print("Select option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addMedicine();
                case 2 -> deleteMedicine();
                case 3 -> {
                    //Print the UI
                    System.out.println("\n=== Medicine List===");
                    
                    //Fetch the clipboard of text and immediately print it out
                    System.out.println(controller.listAllMedicine());
                }
                case 4 -> searchMedicine();
                case 5 -> sortMedicine();
                case 6 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option");
            }
        }
    }

    private void addMedicine() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Description: ");
        String desc = scanner.nextLine();
        System.out.print("Batch Number: ");
        String batch = scanner.nextLine();

        // expiry date and lastUpdated can be simplified for demo
        controller.insertMedicine(new Medicine(
            name, desc, batch, java.time.LocalDateTime.now(), java.time.LocalDateTime.now()
        ));

        System.out.println("Medicine added.");
    }

private void deleteMedicine() {
        System.out.print("Enter the name of the medicine to delete: ");
        String searchName = scanner.nextLine();

        // Create a temporary Medicine object just to hold the name we want to delete
        // We can pass null for the other fields because our equals() method only checks the name!
        Medicine dummyMedicine = new Medicine(searchName, null, null, null, null);

        // Ask the controller to delete it, and check the true/false result
        boolean isDeleted = controller.deleteMedicine(dummyMedicine);

        if (isDeleted) {
            System.out.println("Success! " + searchName + " has been removed.");
        } else {
            System.out.println("Error: " + searchName + " was not found in the system.");
        }
    }

    private void searchMedicine() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void sortMedicine() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
