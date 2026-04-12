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
            System.out.println("====================================================");
            System.out.println("                 Medical Management");
            System.out.println("====================================================");
            System.out.println();
            System.out.println("1. Add Medicine");
            System.out.println("2. Delete Medicine");
            System.out.println("3. View Medicines");
            System.out.println("4. Exit");

            

        while (true) {
            int choice = input("Select option: ");

            switch (choice) {
                case 1 -> addMedicine();
                case 2 -> deleteMedicine();
                case 3 -> viewMedicine();
                case 4 -> {saveFile(); return;}
                default -> {System.out.println("Invalid option.");}
            }
        }
    }


    private void addMedicine() {
        clearConsole();
        System.out.print("Name: ");
        String name = scanner.nextLine();
        
        // Validate that medicine doesn't already exist
        while (controller.searchMedicineByQuery(name)) {
            System.out.println("A medicine with this name already exists. Please try a different name.");
            System.out.print("Name: ");
            name = scanner.nextLine();
        }
        
        System.out.print("Description: ");
        String desc = scanner.nextLine();
        
        System.out.print("Registration Number (e.g., MAL13115223XZ): ");
        String registrationNum = scanner.nextLine();
        // Validate that medicine with this registration number doesn't already exist
        while (controller.searchMedicineByQuery(registrationNum)) {
            System.out.println("A medicine with this Registration Number already exists. Please try a different one.");
            System.out.print("Registration Number (e.g., MAL13115223XZ): ");
            registrationNum = scanner.nextLine();
        }
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
                } else if (parseDate.isEqual(today)) {
                    System.out.println("Invalid: Expiry date cannot be today.");
                } else {
                    expiryDate = parseDate;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format! Please use YYYYMMDD (e.g., 20260324).");
            }
        }

        int quantity = input("Quantity: ");
        while (quantity <= 0) {
            System.out.println("Invalid quantity. Please try again.");
            quantity = input("Quantity: ");
        }
        
        controller.insertMedicine(new Medicine(
            name, desc, quantity, registrationNum, expiryDate, LocalDateTime.now()
        ));
        
        System.out.print("Medicine added.");
        scanner.nextLine();
        saveFile();
        menu();
    }

private void deleteMedicine() {
    pageIndex = 0;  // Reset to first page

    OUTER:
            while (true) {
                clearConsole();
                System.out.printf(ROW_FORMAT, "NAME", "DESCRIPTION", "QUANTITY", "REG. NUM", "EXPIRY DATE", "LAST UPDATED");
                controller.calculatePaginationRangeAndDisplay(pageIndex);
                controller.checkStock();
                
                System.out.println("\n[1] Previous Page  [2] Next Page  [0] Delete Medicine");
                int choice = input("Select option: ");
                switch (choice) {
                    case 1:
                        if (pageIndex > 0) {
                            pageIndex--;
                        } 
                        break;
                    case 2:
                        pageIndex++;
                        break;
                    case 0:
                        break OUTER;
                    default:
                        System.out.print("Invalid option.");
                        scanner.nextLine();
                        break;
                }
            }
        
        while (true) {
            System.out.print("Enter the name of the medicine to delete: ");
            String searchName = scanner.nextLine();

            // Create a temporary Medicine object just to hold the name we want to delete
            // We can pass null for the other fields because our equals() method only checks the name!
            Medicine dummyMedicine = new Medicine(searchName, null, 0, null, null, null);

            // Ask the controller to delete it, and check the true/false result
            boolean isDeleted = controller.deleteMedicine(dummyMedicine);

            if (isDeleted) {
                System.out.print("Success! " + searchName + " has been removed.");
                scanner.nextLine();
                saveFile();
                menu();
            } else {
                System.out.println("Error: " + searchName + " was not found in the system.");
                System.out.print("Try again? (y/n): ");
                String retry = scanner.nextLine().trim().toLowerCase();
                if (!retry.equals("y")) {
                    menu();
                }
            }

        }
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
            viewMedicinePage(pageIndex);
            
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

            int choice = input("\nEnter choice: ");
                
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
                case 0 -> {pageIndex = 0; menu();}

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
    }
    
    private void searchMedicine() {
        clearConsole();
        System.out.print("Search by NAME, DESCRIPTION, or REGISTRATION NUMBER: ");
        // trim is used to remove the whitespace before and after
        String query = scanner.nextLine().trim();  
        controller.searchAndDisplay(query);
    }
    
    public static void clearConsole() {
        for (int i = 0; i < 50; i++) { // Print 50 blank lines
            System.out.println();
        }
    }

    private int input(String prompt) {
    while (true) {
        System.out.print(prompt);
        if (scanner.hasNextInt()) {
            int value = scanner.nextInt();
            scanner.nextLine();
            return value;
        } else {
            scanner.nextLine();
            System.out.println("Invalid input. Please enter a number.");
        }
    }
}
}
