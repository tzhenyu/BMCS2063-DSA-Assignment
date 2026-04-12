/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinic.Control;
import StorageHandler.FileHandler;
import ListADT.ListADT;
import Clinic.Entity.Medicine;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Scanner;
/**
 *
 * @author zhenyu
 */
public class MedicineController implements FileHandler {
    
    //referenced from https://www.geeksforgeeks.org/java/how-to-print-colored-text-in-java-console/
    private static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    
    private static final String FILENAME = "src/medicine.csv";
    private static final String ROW_FORMAT = "| %-25s | %-35s | %-8s | %-13s | %-11s | %-16s |%n";
    
    private static final int DISPLAY_AMOUNT = 8;
    
    private ListADT<Medicine> medicineList;
    
    public MedicineController() {
        medicineList = new ListADT<>();
    }
    
    public void insertMedicine(Medicine medicine) {
        medicineList.insert(medicine);
    }
    
    public boolean deleteMedicine(Medicine medicine) {
       return medicineList.remove(medicine);
    }
    
    public boolean deleteMedicineByName(String name) {
        //Just delete with matched name
       Medicine dummyMedicine = new Medicine(name, null, 0, null, null, null);
       return medicineList.remove(dummyMedicine);
    }
    
    public String viewMedicine() {
      return medicineList.display();
    }
    
    public void checkStock() {
        for (Medicine m: medicineList) {
            // referenced from https://www.baeldung.com/java-date-difference
            long daysToExpiry = ChronoUnit.DAYS.between(LocalDate.now(), m.getExpiryDate());

            if (m.getQuantity() < 100) {
                System.out.println(ANSI_RED + "LOW STOCK!!: " + m.getName() + " - Stock: " + m.getQuantity() + ANSI_RESET);
            } else if (daysToExpiry<= 30) { 
                System.out.println(ANSI_RED + "EXPIRES SOON!!: " + m.getName() + " - Expiry Date: " + m.getExpiryDate() + ANSI_RESET);
            }
        }
    }
    
    public Medicine searchMedicineByName(Medicine medicineName) {
        Medicine found = medicineList.search(medicineName);
        
        return found;
    }
    
    public void sortMedicine(Comparator<Medicine> by) {
        medicineList.sort(by);
    }
    
    // Helper method to get all medicines matching the query
    private ListADT<Medicine> getMedicinesByQuery(String query) {
        ListADT<Medicine> results = new ListADT<>();
        for (Medicine m : medicineList) {
            if (m.getName().toLowerCase().contains(query.toLowerCase()) ||
                m.getDescription().toLowerCase().contains(query.toLowerCase()) || 
                m.getRegistrationNumber().toLowerCase().contains(query.toLowerCase())) {
                results.insert(m);
            }
        }
        return results;
    }
    
    public boolean searchMedicineByQuery(String query) {
        return getMedicinesByQuery(query).size() > 0;
    }
    
    @Override
    public void loadFile() {
        System.out.println("Loading from file: " + FILENAME);
        File csvFile = new File(FILENAME);
        
        Medicine m = new Medicine();
        
        try (Scanner scanner = new Scanner(csvFile)) {
            // skips header
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] fields = line.split(";", -1);
                
                Medicine med = m.fromCSV(fields);
                if (med != null) {
                    medicineList.insert(med);
                }
            }
            System.out.println("File Loaded.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    @Override
    public void saveFile() {
        // referenced from https://www.youtube.com/watch?v=dHZaqMmQNO4
        System.out.println("Saving file to: " + FILENAME);
        File csvFile = new File(FILENAME);
        try (PrintWriter writer = new PrintWriter(csvFile)) {
            
            writer.println("NAME;DESCRIPTION;QUANTITY;REG. NUM;EXPIRY DATE;LAST UPDATED DATE");
            
            for (Medicine m: medicineList) {
                writer.println(m.toCSV());
            }
            // referenced from https://stackoverflow.com/questions/2885173/how-do-i-create-a-file-and-write-to-it
            writer.close();
            System.out.println("Filed saved.");
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
    public void calculatePaginationRangeAndDisplay(int pageIndex) {
        DateTimeFormatter lastUpdateDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        int totalItems = medicineList.size();
        int startIndex =  pageIndex * DISPLAY_AMOUNT;
        int endIndex = Math.min(startIndex + DISPLAY_AMOUNT, medicineList.size());       
        
        //Empty content
        if (totalItems == 0) {
            System.out.println("The medicine list is currently empty.");
            return;
        }
        
        //Page less than 1 page
        if (startIndex < 0) {
            System.out.println("This is already first page");
            return;
        }
        
        //End of page
        if (startIndex >= medicineList.size()) {
            System.out.println("No more entries found.");
            return;
        }
        
       
       for (int i = startIndex; i < endIndex; i++) {
           Medicine entry = medicineList.getEntry(i);
           String formattedDate = entry.getLastUpdated().format(lastUpdateDateFormatter);
           System.out.printf(ROW_FORMAT, entry.getName(), entry.getDescription(), entry.getQuantity(), entry.getRegistrationNumber(), entry.getExpiryDate(), formattedDate);
       }
       
        System.out.println("\nDisplaying items " + (startIndex + 1) + " to " + endIndex);

    }
    
    // CAN BE USED ON APPOINTMENT CLASS
    public void giveMedicine(Medicine m, int amount) {
        // This is to be used for other entity to deduct the amount of medicine
        Medicine found = searchMedicineByName(m);
        // Condition 1: no such medicine
        if (found == null) {
            System.out.println("No such medicine.");
            return;
        }
        
        // Condition 2: not enough stock
        if (found.getQuantity() >= amount) {
            found.deductStock(amount);
            System.out.println("Dispensed " + amount + " of " + found.getName() + ".");
            saveFile();
        } else {
            System.out.println("Not enough stock for " + found.getName() + "!");
        }
    }
    
    public void searchAndDisplay(String query) {
        // enter to continue, referenced from https://stackoverflow.com/questions/26184409/java-console-prompt-for-enter-input-before-moving-on
        DateTimeFormatter lastUpdateDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        Scanner scanner = new Scanner(System.in);
        
        System.out.println("--- Search Results for: " + query + " ---");
        System.out.printf(ROW_FORMAT, "NAME", "DESCRIPTION", "QUANTITY", "REG. NUM", "EXPIRY DATE", "LAST UPDATED");

        // Get matching medicines using the helper method
        ListADT<Medicine> results = getMedicinesByQuery(query);
        
        if (results.size() == 0) {
            System.out.println(ANSI_RED + "No matches found." + ANSI_RESET);
        } else {
            for (Medicine m : results) {
                String formattedDate = m.getLastUpdated().format(lastUpdateDateFormatter);
                System.out.printf(ROW_FORMAT, m.getName(), m.getDescription(), m.getQuantity(), m.getRegistrationNumber(), m.getExpiryDate(), formattedDate);
            }
        }
        
        System.out.print("Enter to continue...");
        scanner.nextLine();
    }

}
