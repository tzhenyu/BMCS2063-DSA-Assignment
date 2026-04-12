/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinic.Entity;

import StorageHandler.CSVHandler;
import java.time.*;

/**
 *
 * @author jonathan
 */

public class Staff implements Comparable<Staff>, CSVHandler<Staff>{
    private String staffId;
    private static int idCounter = 0;
    private String name;
    private String phoneNum;
    private String email;
    private String icNumber; 
    private String employmentType; // Part-Time, Full-Time, Contract, On-Call, etc..
    private String status;  //Active, On Leave, Terminated
    private LocalDate hireDate; //Receive and store 

    // Only for FileHandler use
    public Staff(){};
    
    // Dummy constructor specifically for ADT searching
    public Staff(String staffId) {
        this.staffId = staffId;
    }
    
    //No staffId constructor
    public Staff(String name, String phoneNum, String email, String icNumber, 
            String employmentType, LocalDate hireDate) {
        idCounter++;
        this.staffId = "S-" + String.format("%03d", idCounter); //pass to method to get updated number
        this.name = name;
        this.phoneNum = phoneNum;
        this.email = email;
        this.icNumber = icNumber;
        this.employmentType = employmentType;
        this.status = "Active"; // Set as active upon creation
        this.hireDate = hireDate;
    }

    // Used by child class only, custom prefix string as parameter
    protected Staff(String name, String phoneNum, String email, String icNumber, 
            String employmentType, LocalDate hireDate, String prefix) {
        idCounter++;
        this.staffId = prefix + String.format("%03d", idCounter); //pass to method to get updated number
        this.name = name;
        this.phoneNum = phoneNum;
        this.email = email;
        this.icNumber = icNumber;
        this.employmentType = employmentType;
        this.status = "Active"; // Set as active upon creation
        this.hireDate = hireDate; //Set as creation date
    }
    
    //Used for CSV when read and stored
    //Parameter staffId is added
    protected Staff(String staffId, String name,String phoneNum,String email,
            String icNumber,String employmentType,String status,
            LocalDate hireDate) {
        this.staffId = staffId; // idCounter will recount based on the number of records in CSV and updated
        this.name = name;
        this.phoneNum = phoneNum;
        this.email = email;
        this.icNumber = icNumber;
        this.employmentType = employmentType;
        this.status = status;
        this.hireDate = hireDate;
        
        updateCounter(staffId);
    }
    
    private void updateCounter(String idToParse){
        try {
            //Full convert any format input only numbers eg. "015"
            String parts = idToParse.replaceAll("\\D+", "");
            if (!parts.isEmpty()) {
                // Convert the numeric string ("015") into an integer (15)
                int parsedId = Integer.parseInt(parts);
                
                // If this ID is higher than our current counter, update the counter!
                if (parsedId > idCounter) {
                    idCounter = parsedId;
                }
            }
        } catch (Exception e) { //catch all failures for further investigation
            System.out.println("Unable to parse from "+ idToParse);
        }
    }
    
    public void setHireDate(LocalDate hireDate){
        this.hireDate = hireDate;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setphoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }
    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getstaffId() {return staffId;}
    public String getName() {return name;}
    public String getphoneNum() {return phoneNum;}
    public String getEmail() {return email;}
    public String getIcNumber() {return icNumber;}
    public String getEmploymentType() {return employmentType;}
    public String getStatus() {return status;}
    public LocalDate getHireDate() {return hireDate;}
    
    @Override
    //Check logic for sort
    public int compareTo(Staff other) {
        return this.staffId.compareTo(other.staffId);
    }
    
    @Override
    //Check logic for comparison
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Staff)) return false;

        Staff other = (Staff) obj;
        return this.staffId.equals(other.staffId);
    }
    
    @Override
    public String toCSV() {
        return (staffId + ";" + name + ";" + phoneNum + ";" + email + ";" + 
                icNumber + ";" + employmentType + ";" + status + ";" 
                + String.valueOf(hireDate));
    }
    
    @Override
    public Staff fromCSV(String[] field) {
        // 1. Data Cleaning (The same logic from your loop)
        String staffId = field[0].trim();
        String name = field[1].trim();
        String phoneNum = field[2].trim();
        String email = field[3].trim();
        String icNumber = field[4].trim();
        String employmentType = field[5].trim();
        String status = field[6].trim();
        LocalDate hireDate = LocalDate.parse(field[7].trim());

        // 2. Object Creation
        // Return a brand new instance using the parsed data
        return new Staff(staffId, name, phoneNum, email, icNumber, employmentType, status, hireDate);
    }
    
    @Override
    public String toString() {
        return "Staff{" + "staffId=" + staffId 
                + ", name: " + name 
                + ", phoneNum: " + phoneNum 
                + ", email: " + email 
                + ", icNumber: " + icNumber 
                + ", employmentType: " + employmentType 
                + ", status: " + status 
                + ", hireDate: " + hireDate + '}';
    }
}