/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinic.Control;
import ListADT.ListADT;
import Clinic.Entity.Medicine;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;
/**
 *
 * @author zhenyu
 */
public class MedicineController {
    
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
    
    public String listAllMedicine() {
      return medicineList.display();
    }
    
    public void searchMedicineByName(Medicine medicineName) {
        medicineList.search(medicineName);
    }
    
    public void sortMedicine(String by) {
        
    }
    
    public void loadFile(String file) {
        System.out.println("Loading from file: " + file);
        File csvFile = new File(file);
        try (Scanner scanner = new Scanner(csvFile)) {
            // skips header
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] field = line.split(";", -1);
                
                String name = field[0].trim();
                String description = field[1].trim();
                int quantity = Integer.parseInt(field[2].trim());
                String regNum = field[3].trim();
                LocalDate expiryDate = LocalDate.parse(field[4].trim());
                LocalDate lastUpdated = LocalDate.parse(field[5].trim());
                
                // create object and insert into your ADT
                Medicine med = new Medicine(name, description, quantity, regNum, expiryDate, lastUpdated);
                medicineList.insert(med);
            }
            System.out.println("File Loaded.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void saveFile(String file) {
        // referenced from https://www.youtube.com/watch?v=dHZaqMmQNO4
        System.out.println("Saving file to:" + file);
        File csvFile = new File(file);
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
    
    public void PaginateList() {
       //TODO: COMPLETE PAGINATE LIST FUNCTION
    }
}
