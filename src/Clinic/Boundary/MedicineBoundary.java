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
    private static final String ROW_FORMAT = "| %-25s | %-35s | %-8s | %-13s | %-11s | %-16s |%n";
        
    private MedicineController controller;
    private Scanner scanner;
    private int pageIndex = 0; //pagination feature
    
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
            System.out.println("\n--- Medicine Management ---");
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
                case 4 -> exit();
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
        
        controller.addMedicine(name, desc, quantity, registrationNum, expiryDate);
        
        System.out.println("Medicine added.");
        saveFile();
        clearConsole();
    }

private void deleteMedicine() {
        clearConsole();
        
        System.out.println(controller.viewMedicine());
        
        System.out.print("Enter the name of the medicine to delete: ");
        String searchName = scanner.nextLine();

        // Create a temporary Medicine object just to hold the name we want to delete
        // We can pass null for the other fields because our equals() method only checks the name!
        

        // Ask the controller to delete it, and check the true/false result
        boolean isDeleted = controller.deleteMedicineByName(searchName);

        if (isDeleted) {
            System.out.println("Success! " + searchName + " has been removed.");
            saveFile();
        } else {
            System.out.println("Error: " + searchName + " was not found in the system.");
        }
    }


    private void searchMedicine() {
        clearConsole();
        System.out.print("Search: ");
        // trim is used to remove the whitespace before and after
        String query = scanner.nextLine().trim();  
        controller.searchAndDisplay(query);
        viewMedicine();
    }

    
    private void viewMedicinePage(int page) {
        clearConsole();
        System.out.printf(ROW_FORMAT, "NAME", "DESCRIPTION", "QUANTITY", "REG. NUM", "EXPIRY DATE", "LAST UPDATED"); // header
        controller.calculatePaginationRangeAndDisplay(page);
        controller.checkStock();
    }
    
    private void viewMedicine() {
        clearConsole();
        System.out.printf(ROW_FORMAT, "NAME", "DESCRIPTION", "QUANTITY", "REG. NUM", "EXPIRY DATE", "LAST UPDATED"); // header
        viewMedicinePage(0);
        viewMedicineMenu();
        
    }
    private void viewMedicineMenu() {
        // Combines sort, search, pagination feature
        

        while(true) {
            System.out.println("-- OPTIONS --------------------------------------------------------------------------------------------------------------------");
            System.out.print("SORT BY ");
            System.out.print(" [1] Name");
            System.out.print(" [2] Description");
            System.out.print(" [3] Quantity");
            System.out.print(" [4] Registration Number");
            System.out.print(" [5] Expiry Date");
            System.out.print(" [6] Last Updated");
            System.out.print(" \n        ");
            System.out.print(" [7] Search");
            System.out.print(" [8] Previous Page");
            System.out.print(" [9] Next Page");
            System.out.print(" [0] Back to Main Menu");

            System.out.print("\nEnter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // reading choice
                
            //referenced from https://www.geeksforgeeks.org/java/java-comparator-interface/
            switch (choice) {
                case 1 -> sortMedicine(Comparator.comparing(Medicine::getName));
                case 2 -> sortMedicine(Comparator.comparing(Medicine::getDescription));
                case 3 -> sortMedicine(Comparator.comparing(Medicine::getQuantity));
                case 4 -> sortMedicine(Comparator.comparing(Medicine::getRegistrationNumber));
                case 5 -> sortMedicine(Comparator.comparing(Medicine::getExpiryDate));
                case 6 -> sortMedicine(Comparator.comparing(Medicine::getLastUpdated));
                
                case 7 -> searchMedicine();
                case 8 -> previousPage();
                case 9 -> nextPage();
                case 0 -> {pageIndex = 0; menu(); return;}

                default -> System.out.println("Invalid option.");
            }

            
        }
    }
    
    private void nextPage(){
        pageIndex++; 
        viewMedicinePage(pageIndex);
    }

    private void previousPage() {
        if (pageIndex > 0) {
            pageIndex--; 
            viewMedicinePage(pageIndex);
        } else {
            System.out.println("Already on the first page.");
            viewMedicinePage(pageIndex);  // redisplay current page
        }
    }

    private void sortMedicine(Comparator<Medicine> comparator) {
        controller.sortMedicine(comparator);
        viewMedicine();
    }
    private void exit() {
        System.out.println("Exiting...");
        System.exit(0);
    }
    
    public static void clearConsole() {
        for (int i = 0; i < 50; i++) { // Print 50 blank lines
            System.out.println();
        }
    }
}
