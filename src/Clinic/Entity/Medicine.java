/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Clinic.Entity;

import java.time.LocalDate;
/**
 *
 * @author zhenyu
 */
public class Medicine {

    private static final String ROW_FORMAT = "| %-25s | %-35s | %-8s | %-13s | %-11s | %-12s |%n";

    private String name;
    private String description;
    private int quantity;
    private String registrationNum;
    private LocalDate expiryDate;
    private LocalDate lastUpdated;

    public Medicine(String name, String description,int quantity, String registrationNum, LocalDate expiryDate, LocalDate lastUpdated) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.registrationNum = registrationNum;
        this.expiryDate = expiryDate;
        this.lastUpdated = lastUpdated;
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

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
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
            lastUpdated
        );
    }
    
    public int compareTo(Medicine other) {
        return this.name.compareTo(other.name);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Medicine otherMedicine = (Medicine) obj;
        // Compare them based on their name (ignoring uppercase/lowercase)
        return this.name.equalsIgnoreCase(otherMedicine.name);
    }

    public String toCSV() {
        return (name + ";" + description + ";" + String.valueOf(quantity) + ";" + registrationNum + ";" + expiryDate + ";" + lastUpdated);
    }
}
