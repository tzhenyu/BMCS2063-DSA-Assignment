/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Clinic.Entity;

import java.time.LocalDate;
import StorageHandler.CSVHandler;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 *
 * @author zhenyu
 */

// make medicine comparable for sort purpose
public class Medicine implements Comparable<Medicine>, CSVHandler<Medicine> {

    private static final String ROW_FORMAT = "| %-25s | %-35s | %-8s | %-13s | %-11s | %-16s |%n";

    private String name;
    private String description;
    private int quantity;
    private String registrationNum;
    private LocalDate expiryDate;
    private LocalDateTime lastUpdated;

    public Medicine(String name, String description,int quantity, String registrationNum, LocalDate expiryDate, LocalDateTime lastUpdated) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.registrationNum = registrationNum;
        this.expiryDate = expiryDate;
        this.lastUpdated = lastUpdated;
    }
    
    public Medicine() {
        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRegistrationNumber() {
        return registrationNum;
    }

    public void setRegistrationNumber(String registrationNum) {
        this.registrationNum = registrationNum;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        
        DateTimeFormatter lastUpdateDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Define the format: 5 columns, each 20 characters wide, left-aligned
        // Separated by the "|" pipe character for a table look

        // We only return the DATA row here. 
        // Usually, the "Header" is printed once in your Main class before the loop.
        return String.format(ROW_FORMAT, 
            name, 
            description, 
            String.valueOf(quantity), 
            registrationNum, 
            expiryDate, 
            lastUpdated.format(lastUpdateDateFormatter)
        );
    }
    
    @Override
    public int compareTo(Medicine other) {
        //default sort by name
        return this.name.compareTo(other.name);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Medicine otherMedicine = (Medicine) obj;
        // Compare them based on their name (ignoring uppercase/lowercase)
        return this.registrationNum.equalsIgnoreCase(otherMedicine.registrationNum);
    }

    @Override
    public String toCSV() {
        return (name + ";" + description + ";" + String.valueOf(quantity) + ";" + registrationNum + ";" + expiryDate + ";" + lastUpdated);
    }
    
    @Override
    public Medicine fromCSV(String[] field) {
        // 1. Data Cleaning (The same logic from your loop)
        String name = field[0].trim();
        String description = field[1].trim();
        int quantity = Integer.parseInt(field[2].trim());
        String regNum = field[3].trim();
        LocalDate expiryDate = LocalDate.parse(field[4].trim());
        LocalDateTime lastUpdated = LocalDateTime.parse(field[5].trim());

        // 2. Object Creation
        // Return a brand new instance using the parsed data
        return new Medicine(name, description, quantity, regNum, expiryDate, lastUpdated);
    }
    
    public void deductStock(int amount) {
        if (amount > 0) {
            this.quantity -= amount;
            
            //update the last updated date
            this.lastUpdated = java.time.LocalDateTime.now();

        }
    }

    

}
