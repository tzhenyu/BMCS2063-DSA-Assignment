/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinic.Boundary;

import java.util.Scanner;
import Clinic.Entity.Staff;
import Clinic.Entity.Doctor;
import java.time.LocalDate;
import Clinic.Control.StaffController;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 *
 * @author jon
 */
public class StaffBoundary {
    private Scanner scanner = new Scanner(System.in);
    private StaffController controller = new StaffController();
    
    public void start() {
        loadFile();
        mainMenu();
    }
    
    public void mainMenu() {
        String choice;
        do {
            System.out.println("====Clinic Staff Management System====");
            System.out.println("1. Register Staff");
            System.out.println("2. View all Staff after sort");
            System.out.println("3. Update Staff");
            System.out.println("4. Delete Staff");
            System.out.println("5. Search Doctors by Registration Number");
            System.out.println("0. Exit Staff Management System");
            System.out.println("");
            System.out.print("Choose an option(0-7): ");
            
            // Read as a String directly. .trim() removes any accidental spaces!
            choice = scanner.nextLine().trim(); 
            
            switch (choice) {
                case "1": 
                    addStaffUI();
                    break;
                case "2": 
                    viewAllStaffUI(); 
                    break;
                case "3":
                    updateStaffUI();
                    break;
                case "4":
                    deleteStaffUI();
                    break;
                case "5":
                    searchDoctorByRegUI();
                    break;
                case "0":
                    saveFile(); //save before exitting
                    System.out.println("Exited from Staff Management System.");
                    break;
                default:
                    System.out.println("Invalid, Try again.\n");
                    break;
            }
            
        // The loop will keep running forever UNLESS the user types "0"
        } while (!choice.equals("0")); 
    }
    
    public void addStaffUI(){
        String choice;
        do {
            System.out.println("\n====Register new staff=====");
            System.out.println("1. Regular Staff");
            System.out.println("2. Doctor");
            System.out.println("0. Exit");
            System.out.println("");
        
            System.out.print("Choose an option(0-2): ");
            choice = scanner.nextLine();
            
            if (choice.equals("1")) {
                collectEmployeeData("Staff");
            } else if (choice.equals("2")) {
                collectEmployeeData("Doctor");
            } else if (choice.equals("0")) {
                //do nothing
            } else    
                System.out.println("Invalid choice. Try again.");
                
        } while (!choice.equals("0"));
    }
    
    private void collectEmployeeData(String role) {
        System.out.println("\n==== Adding New " + role + " ====");
        
        // --- SHARED DATA ---
        String name = getValidName();
        String phoneNum = getValidPhone();
        String email = getValidEmail();
        
        System.out.print("IC Number OR Passport: "); // No validation 
        String icNumber = scanner.nextLine().trim();
        
        String employmentType = getValidEmploymentType();
        LocalDate hireDate = getValidHireDate();
        
        // --- DOCTOR SPECIFIC DATA & REGISTRATION ---
        if (role.equals("Staff")) {
            Staff newStaff = new Staff(name, phoneNum, email, icNumber, employmentType, hireDate);
            controller.registerStaff(newStaff);
            System.out.println("Success! New staff added.");
            
        } else if (role.equals("Doctor")) {
            // --- Specific Doctor Data ---
            System.out.print("Medical Registration Number: ");
            String regNum = scanner.nextLine().trim();
            
            System.out.print("Specialization: ");
            String specialization = scanner.nextLine().trim();
            
            System.out.print("Availability (e.g., Mon-Fri 10AM-5PM): "); // No validation
            String availability = scanner.nextLine().trim();
                        
            System.out.print("APC License Number: ");
            String licenseNum = scanner.nextLine().trim();
            
            LocalDate licenseExpDate = getValidExpiryDate();
            
            // Build the object using all the collected variables
            Doctor newDoc = new Doctor(name, phoneNum, email, icNumber, employmentType, hireDate, 
                                       regNum, specialization, availability, licenseNum, licenseExpDate);
                                       
            controller.registerStaff(newDoc);
            System.out.println("Success! New doctor added.");
        }
        //saveFile(); //Save after regeistering new employee to update CSV file
    }
    
    public void viewAllStaffUI() {
        System.out.println("\n==== View All Staff ====");
        System.out.println("How would you like to sort the records?");
        System.out.println("1. Sort by Staff ID (Default)");
        System.out.println("2. Sort by Name");
        System.out.print("Choose an option: ");
        
        String choice = scanner.nextLine().trim();
        int sortOption = 1; // Default to ID
        
        if (choice.equals("2")) {
            sortOption = 2;
        } else if (!choice.equals("1")) {
            System.out.println("Invalid choice. Defaulting to Sort by ID.");
        }
        
        // Pass the sort option to the controller and display the formatted table
        String outputTable = controller.viewSortedStaff(sortOption);
        System.out.println("\n" + outputTable);
        
        System.out.println("Press Enter to return.");
        scanner.nextLine();
    }
    
    public void updateStaffUI() {
        String staffId;
        boolean isValidFormat;        
        do {
            System.out.println("====Update Existing Staff Details====");
            System.out.print("Enter Staff ID (e.g., S-001 or D-001) or '0' to cancel: ");
            staffId = scanner.nextLine().trim();
            
            // Escape hatch: Let the user cancel if they don't know the ID
            if (staffId.equals("0")) {
                System.out.println("Exiting update menu...");
                return; // Goes straight back to the main menu
            }
            
            isValidFormat = validateId(staffId);
            if (!isValidFormat) {
                System.out.println("Error: Invalid ID format. Must start with 'S-' or 'D-' followed by 3 digits.");
            }
        } while (!isValidFormat); // Keep looping if validateId returned false
        
        Staff staffToUpdate = controller.searchStaff(staffId);        
        if (staffToUpdate == null) { // Check if exists
            System.out.println("Error: Staff member '" + staffId + "' not found in the system.");
            return; // Exit back to the main menu
        }
        
        String choice;
        do {
            System.out.println("\nWhat details to modify for " + staffToUpdate.getName() + " (" + staffId + ") ?");
            System.out.println("1. Name");
            System.out.println("2. Phone Number");
            System.out.println("3. Email");
            System.out.println("4. Employment Type");
            System.out.println("5. Employment Status");
            System.out.println("6. Hiring Date (dd-MM-yyyy)");
            
            boolean isDoctor = staffToUpdate instanceof Doctor;
            if (isDoctor) {
                System.out.println("7. Schedule Availability (Doctors Only)"); // Fixed duplicate option 6
            }
            
            System.out.println("0. Exit");
            System.out.println("");
            System.out.print("Choose an option: ");
            choice = scanner.nextLine().trim();
            
            // If they want to exit, break immediately
            if (choice.equals("0")) {
                System.out.println("Exiting update menu for " + staffToUpdate.getName() + "...");
                break;
            }
            
            // Basic check so we don't ask for a new value on an invalid option
            if (!choice.matches("[1-7]") || (!isDoctor && choice.equals("7"))) {
                System.out.println("Invalid option. Please try again.");
                continue;
            }

            // Ask for the new value utilizing the getValid...() methods
            String newValue = "";
            switch (choice) {
                case "1":
                    newValue = getValidName();
                    break;
                case "2":
                    newValue = getValidPhone();
                    break;
                case "3":
                    newValue = getValidEmail();
                    break;
                case "4":
                    newValue = getValidEmploymentType();
                    break;
                case "5":
                    System.out.print("Enter Employment Status (e.g., Active, On Leave, Terminated): ");
                    newValue = scanner.nextLine().trim();
                    break;
                case "6":
                    // Get valid LocalDate, then convert back to String for the controller
                    LocalDate newDate = getValidHireDate();
                    newValue = newDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    break;
                case "7":
                    System.out.print("Enter new Schedule Availability (e.g., Mon-Fri 10AM-5PM): ");
                    newValue = scanner.nextLine().trim();
                    break;
            }
            
            // Pass to the Controller to apply the update
            boolean isSuccess = controller.updateStaff(staffId, choice, newValue);
            
            if (isSuccess) {
                System.out.println("Success! Details updated.");
            } else {
                System.out.println("Error: Update failed. Please check your input format.");
            }
            
        } while (!choice.equals("0"));
    }
    
    public void deleteStaffUI() {
        String staffId;
        Staff staffToDelete = null;
        boolean isFound = false;

        do {
            System.out.println("\n====Delete Staff Record====");
            System.out.print("Enter Staff ID to delete (e.g., S-001 or D-001) or '0' to cancel: ");
            staffId = scanner.nextLine().trim();

            if (staffId.equals("0")) {
                System.out.println("Exiting delete menu...");
                return;
            }

            if (!validateId(staffId)) {
                System.out.println("Error: Invalid ID format. Must start with 'S-' or 'D-' followed by 3 digits.");
                continue; // Restart the loop
            }

            staffToDelete = controller.searchStaff(staffId);
            
            if (staffToDelete == null) {
                System.out.println("Error: Staff member '" + staffId + "' not found in the system. Please try again.");
            } else {
                isFound = true; // Staff exists, we can break the loop
            }

        } while (!isFound);

        // 3. Prompt user for confirmation
        System.out.println("\nRecord Found:");
        System.out.println("Name: " + staffToDelete.getName());
        System.out.println("IC Number / Passport: " + staffToDelete.getIcNumber());
        System.out.println("Role: " + staffToDelete.getEmploymentType());
        System.out.println("Status: " + staffToDelete.getStatus());
        System.out.println("-------------------------------------------------");
        System.out.print("Are you SURE you want to permanently delete this record? (Y/N): ");
        String confirm = scanner.nextLine().trim().toUpperCase();

        // 4. Pass to controller if confirmed
        if (confirm.equals("Y")) {
            boolean isDeleted = controller.removeStaff(staffId);
            if (isDeleted) {
                System.out.println("Success: Staff record '" + staffId + "' has been successfully deleted.");
            } else {
                System.out.println("Error: Could not remove the record from the database.");
            }
        } else {
            System.out.println("Deletion cancelled. Returning to main menu.");
        }
    }
    
    public void searchDoctorByRegUI() {
        String regNum;
        Doctor foundDoctor = null;
        boolean isFound = false;

        do {
            System.out.println("\n==== Search Doctor by Medical Reg No ====");
            System.out.print("Enter Registration Number or '0' to cancel: ");
            regNum = scanner.nextLine().trim();

            if (regNum.equals("0")) {
                System.out.println("Returning to main menu...");
                return;
            }

            // Call the controller to perform the optimized search
            foundDoctor = controller.searchDoctorByRegNum(regNum);

            if (foundDoctor == null) {
                System.out.println("Error: No doctor found with Reg No '" + regNum + "'. Please try again.");
            } else {
                isFound = true;
            }

        } while (!isFound);

        // Display the result once found and the loop exits
        System.out.println("\n--- Doctor Record Found ---");
        System.out.println("ID:             " + foundDoctor.getstaffId());
        System.out.println("Name:           " + foundDoctor.getName());
        System.out.println("Reg Number:     " + foundDoctor.getRegNum());
        System.out.println("Specialization: " + foundDoctor.getSpecialization());
        System.out.println("Status:         " + foundDoctor.getStatus());
        System.out.println("---------------------------");
        System.out.println("Press Enter to return.");
        scanner.nextLine();
    }
    
    public void loadFile(){
        controller.loadFile();
    }
    
    public void saveFile(){
        controller.saveFile();
    }
    
    //Validation method for staffID where S-001 or D-001.
    private boolean validateId(String staffId){
        if (staffId == null) { 
            return false;
        }
        return (staffId.matches("^[SD]-\\d{3}$"));
    }    
   
    //Validation methods to be called for String type, email and phone numbers, IC number and so forth.
    private String getValidName() {
        String name;
        while (true) {
            System.out.print("Name: ");
            name = scanner.nextLine().trim();
            if (name.matches("^[A-Za-z\\s.]+$")) {
                return name; // Returns the valid string and exits the method
            }
            System.out.println("Error: Name cannot contain numbers or special characters.");
        }
    }

    private String getValidPhone() {
        String phoneNum;
        while (true) {
            System.out.print("Phone Number (e.g., 012-3456789): ");
            phoneNum = scanner.nextLine().trim();
            if (phoneNum.matches("^01\\d-\\d{7,8}$")) {
                return phoneNum;
            }
            System.out.println("Error: Must follow Malaysian format 01x-xxxxxxx or 01x-xxxxxxxx.");
        }
    }

    private String getValidEmail() {
        String email;
        while (true) {
            System.out.print("Email: ");
            email = scanner.nextLine().trim();
            if (email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                return email;
            }
            System.out.println("Error: Please enter a valid email address.");
        }
    }

    private String getValidEmploymentType() {
        String type;
        while (true) {
            System.out.print("Employment Type (Part-Time, Full-Time, Contract): ");
            type = scanner.nextLine().trim();
            if (type.equalsIgnoreCase("Part-Time") || 
                type.equalsIgnoreCase("Full-Time") || 
                type.equalsIgnoreCase("Contract")) {
                
                // Optional: Capitalize it nicely before returning so your data is uniform
                return type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase(); 
            }
            System.out.println("Error: Must be Part-Time, Full-Time, or Contract.");
        }
    }

    private LocalDate getValidHireDate() {
        String hireDateStr;
        while (true) {
            System.out.print("Enter staff hiring date (dd-MM-yyyy): ");
            hireDateStr = scanner.nextLine().trim();
            
            // Reuses the validation method we built earlier
            if (validateDateStr(hireDateStr)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                return LocalDate.parse(hireDateStr, formatter);
            }
        }
    }
    
    private LocalDate getValidExpiryDate() {
        String dateStr;
        while (true) {
            System.out.print("License Expiry Date (dd-MM-yyyy): ");
            dateStr = scanner.nextLine().trim();
            
            if (validateDateStr(dateStr)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                return LocalDate.parse(dateStr, formatter);
            }
        }
    }
    
    //validation method for date input
    private boolean validateDateStr(String hireDate) {
        // Prevent NullPointerExceptions
        if (hireDate == null || hireDate.trim().isEmpty()) {
            return false;
        }
        
        try {
            // 1. Check date format (Changed to dd-MM-yyyy)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate parsedDate = LocalDate.parse(hireDate, formatter);

            // 2. Check date range >= today
            LocalDate today = LocalDate.now();
            
            // If the date is strictly BEFORE today, it fails validation
            if (parsedDate.isBefore(today)) {
                System.out.println("Error: Hire date cannot be in the past.");
                return false;
            }

            // If it parsed successfully AND is today or in the future, it is valid!
            return true;

        } catch (DateTimeParseException e) {
            // Updated the error message to reflect the new format requirement
            System.out.println("Error: Invalid date format. Please use dd-MM-yyyy (e.g., 12-04-2026).");
            return false;
        }
    }
    
}