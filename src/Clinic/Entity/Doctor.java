/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinic.Entity;

import java.time.*;

/**
 *
 * @author jon
 */
public class Doctor extends Staff { //Staff already implements Comparable<> and CSVHandler<>
    private String regNum;
    private String specialization;
    private String availability; // e.g. "Mon-Fri 10AM-5PM"
    private boolean isAvailable;
    private String licenseNum;
    private LocalDate licenseExpDate; //set as date datatype
    
    public static boolean searchByRegMode = false; //Flag for special search
    
    // Only for FileHandler use
    public Doctor(){}
    
    // Modified constructor to implement properties of Staff class
    public Doctor(String name, String phoneNum, String email, String icNumber, 
            String employmentType,LocalDate hireDate, String regNum, 
            String specialization, String availability, String licenseNum,
            LocalDate licenseExpDate) {
        super(name, phoneNum, email, icNumber, employmentType, hireDate, "D-");
        this.regNum = regNum;
        this.specialization = specialization;
        this.availability = availability;
        this.isAvailable = true;
        this.licenseNum = licenseNum;
        this.licenseExpDate = licenseExpDate;
    }
    
    // For CSVHandler use
    protected Doctor(String staffId, String name, String phoneNum, String email,
            String icNumber, String employmentType, String status, 
            LocalDate hireDate, String regNum, String specialization, 
            String availability, boolean isAvailable, String licenseNum, 
            LocalDate licenseExpDate){
        super(staffId, name, phoneNum, email, icNumber, employmentType, status, hireDate);
        this.regNum = regNum;
        this.specialization = specialization;
        this.availability = availability;
        this.isAvailable = isAvailable;
        this.licenseNum = licenseNum;
        this.licenseExpDate = licenseExpDate;
        
    }

    public String getRegNum() {return regNum;}
    public String getSpecialization() {return specialization;}
    public String getAvailability() {return availability;}
    public boolean isIsAvailable() {return isAvailable;}
    public String getLicenseNum() {return licenseNum;}
    public LocalDate getLicenseExpDate() {return licenseExpDate;}

    public void setRegNum(String regNum) {
        this.regNum = regNum;
    }
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    public void setAvailability(String availability) {
        this.availability = availability;
    }
    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    public void setLicenseNum(String licenseNum) {
        this.licenseNum = licenseNum;
    }
    public void setLicenseExpDate(LocalDate licenseExpDate) {
        this.licenseExpDate = licenseExpDate;
    }

    @Override
    public String toCSV() {
        return (super.toCSV() + ";" + regNum + ";" + specialization + ";" + 
                availability + ";" + isAvailable + ";" + licenseNum + ";" +
                String.valueOf(licenseExpDate));
    }
    
    @Override
    public Doctor fromCSV(String[] field){
        //for Staff class
        String staffId = field[0].trim();
        String name = field[1].trim();
        String phoneNum = field[2].trim();
        String email = field[3].trim();
        String icNumber = field[4].trim();
        String employmentType = field[5].trim();
        String status = field[6].trim();
        LocalDate hireDate = LocalDate.parse(field[7].trim());
        
        //for Doctor class
        String regNum = field[8].trim();
        String specialization = field[9].trim();
        String availability = field[10].trim();
        boolean isAvailable = Boolean.parseBoolean(field[11].trim());
        String licenseNum = field[12].trim();
        LocalDate licenseExpDate = LocalDate.parse(field[13].trim());        
                
        // Object Creation
        // Return a brand new instance using the parsed data
        return new Doctor(staffId,name,phoneNum,email,icNumber,employmentType,
                status,hireDate,regNum,specialization,availability,isAvailable,
                licenseNum,licenseExpDate);
    }
    
    // Override and display super from Staff
    @Override
    public String toString() {
        return super.toString() + 
                "regNum: " + regNum + 
                ", specialization: " + specialization + 
                ", availability: " + availability + 
                ", isAvailable: " + (isAvailable ? "Available" : "Not Available") + 
                ", licenseNum: " + licenseNum + 
                ", licenseExpDate: " + licenseExpDate + '}';
    }
    
    @Override
    public int compareTo(Staff other) {
        if (searchByRegMode && other instanceof Doctor) {
            return this.regNum.compareToIgnoreCase(((Doctor) other).regNum);
        }
        return super.compareTo(other); // Fallback to Staff ID comparison
    }

    @Override
    public boolean equals(Object obj) {
        if (searchByRegMode && obj instanceof Doctor) {
            return this.regNum.equalsIgnoreCase(((Doctor) obj).regNum);
        }
        return super.equals(obj);
    }
    
}