/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Clinic.Entity;

import java.time.LocalDateTime;
/**
 *
 * @author zhenyu
 */
public class Medicine {

    private String name;
    private String description;
    private String batchNumber;
    private LocalDateTime expiryDate;
    private LocalDateTime lastUpdated;

    public Medicine(String name, String description, String batchNumber, LocalDateTime expiryDate, LocalDateTime lastUpdated) {
        this.name = name;
        this.description = description;
        this.batchNumber = batchNumber;
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

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "Medicine{" + "name=" + name + ", description=" + description + ", batchNumber=" + batchNumber + ", expiryDate=" + expiryDate + ", lastUpdated=" + lastUpdated + '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Medicine otherMedicine = (Medicine) obj;
        // Compare them based on their name (ignoring uppercase/lowercase)
        return this.name.equalsIgnoreCase(otherMedicine.name);
    }
}
