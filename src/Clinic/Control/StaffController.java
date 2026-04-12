/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinic.Control;

import Clinic.Entity.Staff;
import Clinic.Entity.Doctor;
import ListADT.ListADT;
import StorageHandler.FileHandler;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Scanner;

/**
 *
 * @author jon
 */
public class StaffController implements FileHandler{
    
    private final ListADT<Staff> staffList;
    private final ListADT<Staff> doctorList;
    private static final String FILENAME = "src/staff.csv";
    
    public StaffController() {
        staffList = new ListADT<>();
        doctorList = new ListADT<>();
    }
    
    public void registerStaff(Staff newEmployee) {
        // Check if the object passed in is actually a Doctor
        if (newEmployee instanceof Doctor) {
            doctorList.insert(newEmployee);
            System.out.println("Success: Doctor " + newEmployee.getName() + " registered with ID " + newEmployee.getstaffId());
        } else {
            staffList.insert(newEmployee);
            System.out.println("Success: Staff " + newEmployee.getName() + " registered with ID " + newEmployee.getstaffId());
        }
    }
    
    public String viewSortedStaff(int sortOption) {
        Comparator<Staff> sortComparator;

        if (sortOption == 2) {
            // Create a Comparator to sort alphabetically by Name
            sortComparator = new Comparator<Staff>() {
                @Override
                public int compare(Staff s1, Staff s2) {
                    return s1.getName().compareToIgnoreCase(s2.getName());
                }
            };
        } else {
            // Create a Comparator to sort by Staff ID (Default)
            sortComparator = new Comparator<Staff>() {
                @Override
                public int compare(Staff s1, Staff s2) {
                    return s1.getstaffId().compareToIgnoreCase(s2.getstaffId());
                }
            };
        }

        staffList.sort(sortComparator);
        doctorList.sort(sortComparator);

        return viewStaff(); 
    }
    
    public String viewStaff() { // ADT display() method?
        if (staffList.isEmpty()&&doctorList.isEmpty()) {
            return "No staff members currently registered in the system.";
        }

        StringBuilder table = new StringBuilder();
        //Table Header
        String header = String.format("%-8s | %-25s | %-15s | %-25s | %-12s | %-10s | %-10s", 
                "ID", "Name", "Phone", "Email", "Emp. Type", "Status", "Hire Date");
        
        table.append(header).append("\n");
        table.append("-".repeat(125)).append("\n");

        // Loop through the list and format each row
        for (Staff st : staffList) {
            String row = String.format("%-8s | %-25s | %-15s | %-25s | %-12s | %-10s | %-10s",
                    st.getstaffId(), 
                    st.getName(), 
                    st.getphoneNum(), 
                    st.getEmail(), 
                    st.getEmploymentType(), 
                    st.getStatus(), 
                    st.getHireDate().toString());
            
            table.append(row).append("\n");
        }
        
        for (Staff st : doctorList) {
            String row = String.format("%-8s | %-25s | %-15s | %-25s | %-12s | %-10s | %-10s",
                    st.getstaffId(), 
                    st.getName(), 
                    st.getphoneNum(), 
                    st.getEmail(), 
                    st.getEmploymentType(), 
                    st.getStatus(), 
                    st.getHireDate().toString());
            
            table.append(row).append("\n");
        }

        return table.toString();
    }
    
    public boolean updateStaff(String staffId, String choice, String newValue) {
        Staff staffToUpdate = searchStaff(staffId);
        
        // If they don't exist, return false to tell the UI it failed
        if (staffToUpdate == null) {
            return false; 
        }

        try {
            switch (choice) {
                case "1":
                    staffToUpdate.setName(newValue);
                    break;
                case "2":
                    staffToUpdate.setphoneNum(newValue);
                    break;
                case "3":
                    staffToUpdate.setEmail(newValue);
                    break;
                case "4":
                    staffToUpdate.setEmploymentType(newValue);
                    break;
                case "5":
                    staffToUpdate.setStatus(newValue);
                    break;
                case "6":
                    java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDate newDate = LocalDate.parse(newValue, formatter);
                    staffToUpdate.setHireDate(newDate);
                    break;
                case "7":
                    // Safely check and downcast for Doctor-specific fields
                    if (staffToUpdate instanceof Doctor) {
                        ((Doctor) staffToUpdate).setAvailability(newValue);
                    } else {
                        return false; // Failed: Tried to update availability for a non-doctor
                    }
                    break;
                default:
                    return false; // Invalid choice
            }
            return true; // Update was 100% successful!
            
        } catch (Exception e) {
            // Catches parsing errors (like typing "hello" for a date)
            System.out.println("System Error during update: Invalid data format.");
            return false; 
        }
    }
    
    public Staff searchStaff(String targetId) {
        // Convert to uppercase just to be safe
        targetId = targetId.toUpperCase();
        
        Staff searchTarget = new Staff(targetId);

        if (targetId.startsWith("D-")) {
            // Uses ListADT.search()
            return doctorList.search(searchTarget); 
        } else {
            // Uses ListADT.search()
            return staffList.search(searchTarget);
        }
    }
    
    public boolean removeStaff(String targetId) {
        // Convert to uppercase to prevent case-sensitivity issues
        targetId = targetId.toUpperCase();
        
        Staff targetToRemove = new Staff(targetId);

        if (targetId.startsWith("D-")) {
            // Returns true if deleted successfully, false if not found
            return doctorList.remove(targetToRemove); 
        } else {
            return staffList.remove(targetToRemove);
        }
    }
    
    public void sortStaff(Staff NewObject){
        staffList.sort();
    }

    public Doctor searchDoctorByRegNum(String regNum) {
    // Enable Registration Number mode
    Doctor.searchByRegMode = true;

    try {
        // Sort the doctor list by the new criteria (regNum), uses the object's compareTo
        doctorList.sort();

        Doctor dummy = new Doctor();
        dummy.setRegNum(regNum); // Ensure you have this setter

        Staff result = doctorList.earlyExitSearch(dummy);

        return (Doctor) result;
    } finally {
        // reset the mode to default
        Doctor.searchByRegMode = false;
    }
}
    
    @Override
    public void loadFile() {
            System.out.println("Loading from file: " + FILENAME);
            File csvFile = new File(FILENAME);

            Staff st = new Staff();                    
            Doctor dr = new Doctor();
            
            try (Scanner scanner = new Scanner(csvFile)) {
                 //skips header
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] fields = line.split(";", -1);

                    try {
                    // Checks for correct column count where Doctor > Staff
                    if (fields.length == 8) {
                        Staff loadedStaff = st.fromCSV(fields);
                        staffList.insert(loadedStaff);
                    } else if (fields.length == 14) {
                        Doctor loadedDoctor = dr.fromCSV(fields);
                        doctorList.insert(loadedDoctor); // Fixed! Now they go to the right list.
                    }
                } catch (Exception e) {
                    System.out.println("Warning: Corrupted line skipped -> " + line);
                }
                }
                System.out.println("File Loaded.\n");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

    @Override
    public void saveFile() {
            System.out.println("Saving file to: " + FILENAME);
            File csvFile = new File(FILENAME);
            try (PrintWriter writer = new PrintWriter(csvFile)) {

                writer.println("STAFF ID;FULL NAME;PHONE NO;EMAIL;IC NO;EMPLOYMENT TYPE;EMPLOYMENT STATUS;HIRE DATE;MEDICAL REGISTRATION NO"
                        + ";SPECIALIZATION;SCHEDULE AVAILABILITY;AVAILABILITY STATUS;LICENSE NUMBER;LICENSE EXPIRY DATE"); //< CHANGE TO YOUR HEADER!
                
                for (Staff st: staffList) {            //< CHANGE TO YOUR ENTITY!
                    writer.println(st.toCSV());
                }
                
                // Polymorphism upcasting
                for (Staff doc : doctorList) {
                    writer.println(doc.toCSV());
                }
                
                writer.close();
                System.out.println("Filed saved.");
            } catch (IOException e) {
                System.out.println(e);
            }
        }
}
