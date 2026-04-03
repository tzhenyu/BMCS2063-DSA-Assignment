/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinic.Boundary;
import Clinic.Control.MedicineController;
import Clinic.Entity.Medicine;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.Scanner;
/**
 *
 * @author zhenyu
 */
public class MedicineBoundary {
     
    String regex = "^MAL\\d{8}[AXTNH][CERSYZ]?$";
    private static final String ROW_FORMAT = "| %-25s | %-35s | %-8s | %-13s | %-11s | %-12s |%n";
        
    private MedicineController controller;
    private Scanner scanner;
    
    public MedicineBoundary() {
        controller = new MedicineController();
        scanner = new Scanner(System.in);
    }
    
    public void loadFile() {
        controller.loadFile();
    }
    
    public void saveFile() {
        controller.saveFile();
    }
    
    public void start() {
        loadFile();
        menu();
    }
        
    public void menu() {
        clearConsole();
        while (true) {
            System.out.println("====================================================");
            System.out.println("                 Medical Management");
            System.out.println("====================================================");
            System.out.println();
            System.out.println("1. Add Medicine");
            System.out.println("2. Delete Medicine");
            System.out.println("3. View Medicines");
            System.out.println("4. Exit");
            System.out.print("Select option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addMedicine();
                case 2 -> deleteMedicine();
                case 3 -> viewMedicine();
                case 4 -> {saveFile(); return;}
                default -> System.out.println("Invalid option");
            }
        }
    }


    private void addMedicine() {
        clearConsole();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Description: ");
        String desc = scanner.nextLine();
        
        System.out.print("Registration Number (e.g., MAL13115223XZ): ");
        String registrationNum = scanner.nextLine();
        while (!registrationNum.matches(regex)) {
            System.out.println("Invalid format. Please try again.");
            System.out.print("Registration Number (e.g., MAL13115223XZ): ");
            registrationNum = scanner.nextLine();
        }
        
        
        // referenced from https://stackoverflow.com/questions/42763103/convert-string-yyyy-mm-dd-to-localdatetime
        LocalDate expiryDate = null;
        LocalDate today = LocalDate.now();

        DateTimeFormatter expiryDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        

        while (expiryDate == null) {
            System.out.print("Expiry Date (YYYYMMDD): ");
            // trim is used to remove the whitespace before and after
            String date = scanner.nextLine().trim();     
            try {
                LocalDate parseDate = LocalDate.parse(date, expiryDateFormatter);
                
                if (parseDate.isBefore(today)) {
                    System.out.println("Invalid: Expiry date cannot be in the past. Today is " + today);    
                } else {
                    expiryDate = parseDate;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format! Please use YYYYMMDD (e.g., 20260324).");
            }
        }

        System.out.print("Quantity: ");
        int quantity = scanner.nextInt();
        while (quantity <= 0) {
            System.out.println("Invalid quantity. Please try again.");
            System.out.print("Quantity: ");
            quantity = scanner.nextInt();
        }
        
        controller.insertMedicine(new Medicine(
            name, desc, quantity, registrationNum, expiryDate, LocalDateTime.now()
        ));

        System.out.println("Medicine added.");
        clearConsole();
    }

private void deleteMedicine() {
        clearConsole();
        System.out.print("Enter the name of the medicine to delete: ");
        String searchName = scanner.nextLine();

        // Create a temporary Medicine object just to hold the name we want to delete
        // We can pass null for the other fields because our equals() method only checks the name!
        Medicine dummyMedicine = new Medicine(searchName, null, 0, null, null, null);

        // Ask the controller to delete it, and check the true/false result
        boolean isDeleted = controller.deleteMedicine(dummyMedicine);

        if (isDeleted) {
            System.out.println("Success! " + searchName + " has been removed.");
        } else {
            System.out.println("Error: " + searchName + " was not found in the system.");
        }
    }

    private void viewMedicine() {
        clearConsole();
        //Print the UI
        System.out.printf(ROW_FORMAT, "NAME", "DESCRIPTION", "QUANTITY", "REG. NUM", "EXPIRY DATE", "LAST UPDATED");
                    
        //Fetch the clipboard of text and immediately print it out
        System.out.println(controller.viewMedicine());
        controller.checkStock();
        sortMedicine();
        
    }
    
    private void searchMedicine() {
        clearConsole();
        System.out.print("Search: ");
        // trim is used to remove the whitespace before and after
        String query = scanner.nextLine().trim();  
        controller.searchAndDisplay(query);
    }

    private void sortMedicine() {
        while(true) {
            System.out.println("-- SORT OPTIONS ---------------------------------------------------------------------------------------------------------------");
            System.out.print(" [1] Name");
            System.out.print(" [2] Description");
            System.out.print(" [3] Quantity");
            System.out.print(" [4] Registration Number");
            System.out.print(" [5] Expiry Date");
            System.out.print(" [6] Last Updated");
            System.out.print(" \n [7] Search");
            System.out.print(" [8] Back to Main Menu");

            System.out.print("\nEnter choice: ");

            int choice = scanner.nextInt();
                scanner.nextLine(); // reading choice
                
            //referenced from https://www.geeksforgeeks.org/java/java-comparator-interface/
            switch (choice) {
                case 1 -> controller.sortMedicine(Comparator.comparing(Medicine::getName)); 
                case 2 -> controller.sortMedicine(Comparator.comparing(Medicine::getDescription));
                case 3 -> controller.sortMedicine(Comparator.comparing(Medicine::getQuantity));
                case 4 -> controller.sortMedicine(Comparator.comparing(Medicine::getRegistrationNumber));
                case 5 -> controller.sortMedicine(Comparator.comparing(Medicine::getExpiryDate));
                case 6 -> controller.sortMedicine(Comparator.comparing(Medicine::getLastUpdated));
                case 7 -> searchMedicine();
                case 8 -> {menu(); return;}
                default -> System.out.println("Invalid option.");
            }

            //list the medicine list after the sort
            viewMedicine();
        }
    }
    
    public static void clearConsole() {
        for (int i = 0; i < 50; i++) { // Print 50 blank lines
            System.out.println();
        }
    }
}
